package com.b2beyond.wallet.b2bcoin.util;

import com.b2beyond.wallet.b2bcoin.B2BWallet;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class CoinUtil {

    public static long getLongForText(String text) {
        BigDecimal decimal = new BigDecimal(text);
        decimal = decimal.multiply(B2BWallet.DIVIDE_BY);

        return decimal.longValue();
    }

    public static String getTextForLong(Long amount) {
        BigDecimal decimal = new BigDecimal(amount);
        decimal = decimal.setScale(12, RoundingMode.UP);
        decimal = decimal.divide(B2BWallet.DIVIDE_BY, RoundingMode.UP);
        return decimal.toPlainString();
    }
}
