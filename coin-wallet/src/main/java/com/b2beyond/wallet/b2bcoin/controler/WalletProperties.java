package com.b2beyond.wallet.b2bcoin.controler;


import org.apache.log4j.Logger;

import java.util.Properties;

public class WalletProperties {

    private Logger LOGGER = Logger.getLogger(this.getClass());

    private Properties walletProperties = new Properties();

    public WalletProperties() {
        try {
            LOGGER.info("Loading wallet properties from root - class : " + getClass());
            LOGGER.info("Loading wallet properties from root - class loader" + getClass().getClassLoader());
            if (getClass().getClassLoader() != null) {
                if (getClass().getClassLoader().getResource("wallet-gui.config") != null) {
                    LOGGER.info("Loading wallet properties from root " + getClass().getClassLoader().getResource("wallet-gui.config").getFile());
                }
                walletProperties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("wallet-gui.config"));
            }
        } catch (Exception e) {
            LOGGER.error("WalletProperties", e);
            System.exit(1);
        }

        LOGGER.info("Loading wallet properties from root : LOADED");
    }

    public int getMinWidth() {
        return Integer.valueOf(walletProperties.getProperty("min-width"));
    }

    public int getMinHeight() {
        return Integer.valueOf(walletProperties.getProperty("min-height"));
    }

    public Properties getProperties() {
        return walletProperties;
    }
}
