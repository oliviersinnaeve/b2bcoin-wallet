package com.b2beyond.wallet.b2bcoin.util;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class CoinUtil {

    public static BigDecimal DIVIDE_BY = new BigDecimal("1000000000000");

    public static long getLongForText(String text) {
        BigDecimal decimal = new BigDecimal(text);
        decimal = decimal.multiply(DIVIDE_BY);

        return decimal.longValue();
    }

    public static String getTextForLong(Long amount) {
        BigDecimal decimal = new BigDecimal(amount);
        decimal = decimal.setScale(12, RoundingMode.UP);
        decimal = decimal.divide(DIVIDE_BY, RoundingMode.UP);
        return decimal.toPlainString();
    }
}
