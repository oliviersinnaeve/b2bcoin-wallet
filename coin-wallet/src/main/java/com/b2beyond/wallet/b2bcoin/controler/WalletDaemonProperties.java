package com.b2beyond.wallet.b2bcoin.controler;


import com.b2beyond.wallet.b2bcoin.util.B2BUtil;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

public class WalletDaemonProperties {

    private Logger LOGGER = Logger.getLogger(this.getClass());

    private Properties properties = new Properties();

    public WalletDaemonProperties() {
        try {
            LOGGER.info("Loading wallet daemon config for OS : " + B2BUtil.getOperatingSystemType());

            LOGGER.info("Loading wallet daemon properties from root - class : " + getClass());
            LOGGER.info("Loading wallet daemon properties from root - class loader" + getClass().getClassLoader());
            if (getClass().getClassLoader() != null) {
                if (getClass().getClassLoader().getResource("b2bcoin-" + B2BUtil.getOperatingSystemType()) != null) {
                    LOGGER.info("Loading wallet daemon properties from root " + getClass().getClassLoader()
                            .getResource("b2bcoin-" + B2BUtil.getOperatingSystemType()));
                }
            }

//            LOGGER.info("Loading wallet daemon properties from root " + Thread.currentThread().getContextClassLoader()
//                    .getResource("b2bcoin-" + OsChecker.getOperatingSystemType() + "/configs/b2bcoin-wallet.conf").getFile());
            properties.load(getClass().getClassLoader().getResourceAsStream(
                    "b2bcoin-" + B2BUtil.getOperatingSystemType() + "/configs/b2bcoin-wallet.conf"));
        } catch (IOException e) {
            LOGGER.error("WalletDaemonProperties", e);
            System.exit(1);
        }
    }

    public Properties getProperties() {
        return properties;
    }
}
