package com.b2beyond.wallet.b2bcoin.daemon;

import com.b2beyond.wallet.b2bcoin.util.B2BUtil;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * It represents the properties to use when the paypal component is activated for an account.
 *
 * Created by oliviersinnaeve on 09/03/17.
 */
public class CoinDaemon extends AbstractDaemon {

    private Logger LOGGER = Logger.getLogger(this.getClass());
    private String daemonExecutable;


    public CoinDaemon(PropertiesConfiguration daemonProperties, String operatingSystem) {
        LOGGER.info("Starting coin daemon for OS : " + operatingSystem);

        this.operatingSystem = operatingSystem;

        String userHome = B2BUtil.getUserHome();
        String binariesLocation = B2BUtil.getBinariesRoot();
        String configLocation = B2BUtil.getConfigRoot();
        String logLocation = B2BUtil.getLogRoot();

        try {
            daemonExecutable = daemonProperties.getString("coin-daemon-" + operatingSystem);

            LOGGER.debug("Coin daemon userHome : " + userHome);
            LOGGER.debug("Coin daemon binaries location : " + binariesLocation);
            LOGGER.debug("Coin daemon configLocation : " + configLocation);
            LOGGER.debug("Coin daemon logs : " + logLocation);

            ProcessBuilder pb = new ProcessBuilder(binariesLocation + daemonExecutable, "--config-file", configLocation + "coin.conf",
                    "--log-file", logLocation + daemonProperties.getString("log-file-coin"));

            process = pb.start();
            processPid = B2BUtil.getPid(process, operatingSystem, false);
            LOGGER.debug("Coin Process id retrieved : " + processPid);

            Thread outputThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        InputStream inputStream = process.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream), 1);

                        InputStream errorStream = process.getErrorStream();
                        BufferedReader outBufferedReader = new BufferedReader(new InputStreamReader(errorStream), 1);

                        String line;
                        while (true) {

                            while ((line = bufferedReader.readLine()) != null) {
                                System.out.println(line);
                            }
                            while ((line = outBufferedReader.readLine()) != null) {
                                System.out.println(line);
                            }

                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            outputThread.start();


        } catch (Exception ex) {
            LOGGER.error("Coin daemon failed to load", ex);
        }
    }

}
