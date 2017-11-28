package com.b2beyond.wallet.b2bcoin.util;

import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;


public class CoinUtil {

    private static Logger LOGGER = Logger.getLogger(CoinUtil.class);

    public static BigDecimal DIVIDE_BY = new BigDecimal("1000000000000");

    public static long getLongForText(String text) {
        text = text.replace(",", ".");
        LOGGER.info("getLongForText : Converting " + text + " to long");
        BigDecimal decimal = new BigDecimal(text);
        decimal = decimal.multiply(DIVIDE_BY);
        LOGGER.info("getLongForText : Converted " + text + " to long : " + decimal.longValue());
        return decimal.longValue();
    }

    public static String getTextForLong(Long amount) {
        NumberFormat amountFormat = NumberFormat.getNumberInstance();
        amountFormat.setGroupingUsed(false);
        amountFormat.setMinimumFractionDigits(12);
        amountFormat.setMaximumFractionDigits(12);

        String text = amount.toString().replace(",", ".");
        LOGGER.info("getTextForLong : Converting " + text + " to long");
        BigDecimal decimal = new BigDecimal(amount.toString());
        decimal = decimal.setScale(12, RoundingMode.UP);
        decimal = decimal.divide(DIVIDE_BY, RoundingMode.UP);
        LOGGER.info("getTextForLong : Converted " + text + " to long : " + decimal.toPlainString());
        return amountFormat.format(decimal.doubleValue());
    }

}
