package com.demo.payment.service;

import com.demo.payment.domain.PaymentStatus;
import com.demo.payment.domain.entity.TxOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RequiredArgsConstructor
public class InMemoryPaymentProcessor {
    private static final int MAX_RETRIES = 3;
    private static final long[] RETRY_DELAYS = {5, 15, 30}; // seconds

    private final InMemoryPaymentQueue paymentQueue;
    private final TxOrderService txOrderService;
    private final AccountService accountService;
    private final PaymentExecuteNotify paymentExecuteNotify;

    // Track transactions being processed to prevent concurrent processing
    private final Map<String, Boolean> processingLocks = new ConcurrentHashMap<>();

    @Scheduled(fixedDelay = 100) // Poll queue every 100ms
    public void processPayments() {
        InMemoryPaymentQueue.PaymentTask task = paymentQueue.poll();
        if (task == null) {
            return;
        }

        TxOrder txOrder = task.getTxOrder();
        String txId = txOrder.getTransactionId();

        // Skip if already being processed
        if (processingLocks.putIfAbsent(txId, true) != null) {
            log.debug("Transaction already being processed: {}", txId);
            return;
        }

        try {
            processPayment(txOrder, task.getRetryCount());
        } catch (Exception ex) {
            log.error("Unexpected error processing payment: {}", txId, ex);
        } finally {
            processingLocks.remove(txId);
        }
    }

    @Transactional
    public void processPayment(TxOrder txOrder, int retryCount) {
        String txId = txOrder.getTransactionId();
        try {
            // Update status to processing
            txOrderService.updatePaymentStatus(txOrder, PaymentStatus.PROCESSING);

            // Execute payment
            accountService.transferFunds(txOrder);

            // Update status to success
            txOrderService.updatePaymentStatus(txOrder, PaymentStatus.SUCCESS);

            paymentExecuteNotify.onSuccess(txOrder);

            log.info("Payment processed successfully: {}", txId);

        } catch (Exception ex) {
            log.error("Error processing payment: {}", txId, ex);
            handlePaymentFailure(txOrder, retryCount, ex);
        }
    }

    private void handlePaymentFailure(TxOrder txOrder, int retryCount, Exception ex) {
        String txId = txOrder.getTransactionId();

        try {

            if (retryCount < MAX_RETRIES) {
                // Schedule retry
                int nextRetry = retryCount + 1;
                long delaySeconds = RETRY_DELAYS[retryCount];

                log.info("Scheduling retry {} for transaction: {} with delay: {} seconds",
                        nextRetry, txId, delaySeconds);

                // Schedule retry with delay
                scheduleRetry(txOrder, nextRetry, delaySeconds);

            } else {
                log.error("Max retries exceeded for transaction: {}", txId);

                // Mark as failed
                txOrder.setPaymentStatus(PaymentStatus.FAILED);
                txOrder.setDescription(ex.getMessage().substring(0, Math.min(ex.getMessage().length(), 200)));
                txOrderService.save(txOrder);

                // Notify about failure
                paymentExecuteNotify.onFailure(txOrder, ex);
            }
        } catch (Exception e) {
            log.error("Error handling payment failure for transaction: {}", txId, e);
        }
    }

    private void scheduleRetry(TxOrder txOrder, int retryCount, long delaySeconds) {
        // Simple thread-based delay implementation
        // In production, consider using Spring's TaskScheduler instead
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(delaySeconds);
                paymentQueue.enqueue(txOrder, retryCount);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}
