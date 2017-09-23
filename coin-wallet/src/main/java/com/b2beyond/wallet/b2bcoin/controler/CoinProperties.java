package com.b2beyond.wallet.b2bcoin.controler;


import org.apache.log4j.Logger;

import java.util.Properties;

public class CoinProperties {

    private Logger LOGGER = Logger.getLogger(this.getClass());

    private Properties coinProperties = new Properties();

    public CoinProperties() {
        try {
            LOGGER.info("Loading coin properties from root - class : " + getClass());
            LOGGER.info("Loading coin properties from root - class loader" + getClass().getClassLoader());
            if (getClass().getClassLoader() != null) {
                if (getClass().getClassLoader().getResource("coin-daemon.config") != null) {
                    LOGGER.info("Loading coin properties from root " + getClass().getClassLoader().getResource("coin-daemon.config").getFile());
                }
                coinProperties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("coin-daemon.config"));
            }
        } catch (Exception e) {
            LOGGER.error("CoinProperties", e);
            System.exit(1);
        }

        LOGGER.info("Loading coin properties from root : LOADED");
    }

    public Properties getCoinProperties() {
        return coinProperties;
    }
}
