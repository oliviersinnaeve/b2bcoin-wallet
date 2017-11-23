package com.b2beyond.wallet.b2bcoin.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;


/**
 * Created by oliviersinnaeve on 23/11/17.
 */
public class DateUtilTest {

    @Test
    public void testParse() throws Exception {
        String dateString = "19 nov. 2017 04:24:26";

        Date date = DateUtil.parse(dateString);
        Assert.assertNotNull(date);
    }

}