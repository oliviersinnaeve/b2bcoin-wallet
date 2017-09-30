package com.b2beyond.wallet.b2bcoin.controler;


import com.b2beyond.wallet.b2bcoin.util.B2BUtil;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;


public class PropertiesLoader {

    private static Logger LOGGER = Logger.getLogger(PropertiesLoader.class);

    private PropertiesConfiguration properties = new PropertiesConfiguration();


    public PropertiesLoader(String filename) {
        try {
            String location = B2BUtil.getConfigRoot();

            if (getClass().getClassLoader() != null) {
                if (getClass().getClassLoader().getResource("b2bcoin-" + B2BUtil.getOperatingSystem()) != null) {
                    LOGGER.info("Loading config file : " + location + filename);
                    properties.load(new FileInputStream(location + filename));
                }
            }
        } catch (ConfigurationException | IOException e) {
            LOGGER.error("WalletDaemonProperties", e);
            System.exit(1);
        }
    }

    public PropertiesConfiguration getProperties() {
        return properties;
    }
}
