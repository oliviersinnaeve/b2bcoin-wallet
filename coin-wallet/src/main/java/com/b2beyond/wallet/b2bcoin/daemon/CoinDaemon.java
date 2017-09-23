package com.b2beyond.wallet.b2bcoin.daemon;

import com.b2beyond.wallet.b2bcoin.B2BWallet;
import com.b2beyond.wallet.b2bcoin.util.B2BUtil;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;


/**
 * It represents the properties to use when the paypal component is activated for an account.
 *
 * Created by oliviersinnaeve on 09/03/17.
 */
public class CoinDaemon implements Daemon {

    private Logger LOGGER = Logger.getLogger(this.getClass());

    private Process process;


    public CoinDaemon(Properties daemonProperties, String operatingSystem) {
        LOGGER.info("Starting coin daemon for OS : " + operatingSystem);

        URL baseLocation = Thread.currentThread().getContextClassLoader().getResource("b2bcoin-" + operatingSystem + "/");
        if (baseLocation != null) {

            String userHome = B2BUtil.getUserHome();
            String location = B2BUtil.getBinariesRoot();
            String configLocation = B2BUtil.getConfigRoot();

            try {
                String daemonExecutable = daemonProperties.getProperty("coin-daemon-" + operatingSystem);

                LOGGER.debug("Coin daemon userHome : " + userHome);
                LOGGER.debug("Coin daemon binaries location : " + location);
                LOGGER.debug("Coin daemon configLocation : " + configLocation);

                ProcessBuilder pb = new ProcessBuilder(location + daemonExecutable, "--config-file", userHome + "configs/b2bcoin.conf",
                        "--log-file", userHome + daemonProperties.getProperty("log-file-coin"));

                process = pb.start();
            } catch (Exception ex) {
                LOGGER.error("Coin daemon failed to load", ex);
            }
        }
    }

    @Override
    public void stop() {
        process.destroy();
        try {
            LOGGER.info("Wait for value : " + process.waitFor());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info("Killing daemon exit value : " + process.exitValue());
    }

}
