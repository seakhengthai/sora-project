package com.demo.payment.utils;

import lombok.extern.slf4j.Slf4j;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuple4;
import reactor.util.function.Tuples;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
public class EquivalentAmountUtils {
    public static List<Tuple4<String, String, Double, Double>> LIST_EXCHANGE_RATE = List.of(
            Tuples.of("USD", "KHR", 4000.0, 4100.0), // ccy1, ccy2, buy_rate, sell_rate
            Tuples.of("USD", "THB", 30.0, 31.0),
            Tuples.of("THB", "KHR", 117.0, 118.0)
    );

    public static Tuple2<BigDecimal, Double> getEquivalentAmount(BigDecimal amountToBeConvert, String fromCcy, String toCcy, String txnCcy) {
        Tuple3<String, String, String> rateOperator = getRateIndicator(fromCcy, toCcy, txnCcy);
        double exchangeRate = getExchangeRate(fromCcy, toCcy, txnCcy);
        String operator = rateOperator.getT3();
        String ccy2 = rateOperator.getT2();

        BigDecimal equivalentAmountConvert = BigDecimal.valueOf(0.0);
        if (operator.equals("/")) {
            equivalentAmountConvert = amountToBeConvert.divide(BigDecimal.valueOf(exchangeRate), 10, RoundingMode.HALF_UP);
        } else if (operator.equals("*")) {
            equivalentAmountConvert = amountToBeConvert.multiply(BigDecimal.valueOf(exchangeRate));
        }
        if ("KHR".equals(ccy2)) {
            equivalentAmountConvert = equivalentAmountConvert
                    .divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP) // Round to nearest hundred
                    .multiply(BigDecimal.valueOf(100));
        } else {
            equivalentAmountConvert = equivalentAmountConvert.setScale(2, RoundingMode.CEILING);
        }
        log.debug("The original amount: [{}] and after converted : [{}]", amountToBeConvert, equivalentAmountConvert);
        return Tuples.of(equivalentAmountConvert, exchangeRate);
    }

    public static Tuple3<String, String, String> getRateIndicator(String fromCcy, String toCcy, String txnCcy){
        String ccy1 = fromCcy;
        String ccy2 = toCcy;
        String operator;
        if (toCcy.equals(txnCcy)) {
            ccy1 = toCcy;
            ccy2 = fromCcy;
        }
        if ("KHR".equals(ccy1) && List.of("USD", "THB").contains(ccy2)){
            operator = "/";
        }else if ("USD".equals(ccy1) && List.of("KHR", "THB").contains(ccy2)){
            operator = "*";
        } else if ("THB".equals(ccy1) && "KHR".equals(ccy2)){
            operator = "*";
        }else if ("THB".equals(ccy1) && "USD".equals(ccy2)){
            operator = "/";
        } else{
            throw new RuntimeException("Currency is not supported. " + ccy1 + "_" + ccy2);
        }
        return Tuples.of(ccy1, ccy2, operator);
    }

    public static Double getExchangeRate(String fromCcy, String toCcy, String txnCcy) {
        String ccy1 = fromCcy;
        String ccy2 = toCcy;
        if (toCcy.equals(txnCcy)) {
            ccy1 = toCcy;
            ccy2 = fromCcy;
        }

        for(Tuple4<String, String, Double, Double> item: LIST_EXCHANGE_RATE) {
            String exchangeRate1 = item.getT1() + "_" + item.getT2();
            String exchangeRate2 = item.getT2() + "_" + item.getT1();
            String fromToCcy = ccy1 + "_" + ccy2;
            if (fromToCcy.equals(exchangeRate1)
                    || fromToCcy.equals(exchangeRate2)) {
                double exchangeRate;
                if (isCrossSender(fromCcy, txnCcy)) {
                    exchangeRate = fromToCcy.equals(exchangeRate1) ? item.getT4() : item.getT3();
                } else {
                    exchangeRate = fromToCcy.equals(exchangeRate1) ? item.getT3() : item.getT4();
                }
                return exchangeRate;
            }
        }
        throw new RuntimeException("Currency is not supported. " + ccy1 + "_" + ccy2);
    }

    public static boolean isCrossSender(String fromCcy, String txnCcy){
        return !fromCcy.equals(txnCcy);
    }

    public static boolean isCrossCurrency(String fromCcy, String toCcy, String txnCcy) {
        return !fromCcy.equals(txnCcy) || !toCcy.equals(txnCcy);
    }

    public static void main(String[] args) {
        String fromCcy = "USD";
        String toCcy = "KHR";
        String txnCcy = "USD";
        BigDecimal amount = BigDecimal.valueOf(3423.63);
        Tuple2<BigDecimal, Double> values = EquivalentAmountUtils.getEquivalentAmount(
                amount, fromCcy, toCcy, txnCcy
        );
        System.err.println(values.getT1() + ", " + values.getT2());
    }
}
