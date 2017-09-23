package com.b2beyond.wallet.b2bcoin.controler;


import com.b2beyond.wallet.b2bcoin.util.B2BUtil;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

public class CoinDaemonProperties {

    private Logger LOGGER = Logger.getLogger(this.getClass());

    private Properties properties = new Properties();

    public CoinDaemonProperties() {
        try {
            LOGGER.info("Loading coin daemon config for OS : " + B2BUtil.getOperatingSystemType());
            LOGGER.info("Loading coin daemon properties from root " + Thread.currentThread().getContextClassLoader()
                    .getResource(""));
            LOGGER.info("Loading coin daemon properties from root " + Thread.currentThread().getContextClassLoader()
                    .getResource("b2bcoin-" + B2BUtil.getOperatingSystemType() + "/configs/b2bcoin.conf").getFile());
            properties.load(getClass().getClassLoader().getResourceAsStream(
                    "b2bcoin-" + B2BUtil.getOperatingSystemType() + "/configs/b2bcoin.conf"));
        } catch (IOException e) {
            LOGGER.error("CoinDaemonProperties", e);
            System.exit(1);
        }
    }

    public Properties getProperties() {
        return properties;
    }
}
