package com.demo.payment.service;

import com.demo.payment.domain.entity.TxOrder;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class InMemoryPaymentQueue {
    private final BlockingQueue<PaymentTask> queue = new LinkedBlockingQueue<>();

    public void enqueue(TxOrder txOrder) {
        queue.offer(new PaymentTask(txOrder, 0));
    }

    public void enqueue(TxOrder txOrder, int retryCount) {
        queue.offer(new PaymentTask(txOrder, retryCount));
    }

    public PaymentTask dequeue() throws InterruptedException {
        return queue.take();
    }

    public PaymentTask poll() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }


    public int size() {
        return queue.size();
    }

    @Getter
    public static class PaymentTask {
        private final TxOrder txOrder;
        private final int retryCount;

        public PaymentTask(TxOrder txOrder, int retryCount) {
            this.txOrder = txOrder;
            this.retryCount = retryCount;
        }

    }
}
