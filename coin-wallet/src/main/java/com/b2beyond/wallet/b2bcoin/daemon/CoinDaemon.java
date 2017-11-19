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
public class CoinDaemon implements Daemon {

    private Logger LOGGER = Logger.getLogger(this.getClass());

    private Process process;
    private int processPid;
    private String operatingSystem;
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

//            if (operatingSystem.equalsIgnoreCase(B2BUtil.WINDOWS)) {
//                pb = new ProcessBuilder("cmd", "/c", "start", "/MIN", binariesLocation + daemonExecutable, "--config-file", configLocation + "coin.conf",
//                        "--log-file", logLocation + daemonProperties.getString("log-file-coin"));
//            }

            process = pb.start();
            processPid = B2BUtil.getPid(process, operatingSystem, false);
            LOGGER.debug("Coin Process id retrieved : " + processPid);

            Thread outputThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        InputStream inputStream = process.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream), 1);
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            LOGGER.info(line);
                        }
                        inputStream.close();
                        bufferedReader.close();


                        InputStream errorStream = process.getErrorStream();
                        BufferedReader outBufferedReader = new BufferedReader(new InputStreamReader(errorStream), 1);
                        String outLine;
                        while ((outLine = outBufferedReader.readLine()) != null) {
                            LOGGER.info(outLine);
//                            try {
//                                Thread.sleep(5000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
                        }
                        errorStream.close();
                        outBufferedReader.close();
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

    @Override
    public void stop() {
        ProcessBuilder pb = null;
        if (operatingSystem.equalsIgnoreCase(B2BUtil.MAC)) {
            pb = new ProcessBuilder("kill", "-9", "" + processPid);
        }

//        if (operatingSystem.equalsIgnoreCase(B2BUtil.LINUX)) {
//            pb = new ProcessBuilder("fuser", "-k", walletProperties.getInt("bind-port") + "/tcp");
//        }

        if (operatingSystem.equalsIgnoreCase(B2BUtil.WINDOWS)) {
            LOGGER.info("Windows destroy wallet process ...");
            process.destroy();
            try {
                LOGGER.info("Windows destroy wallet process - wait for :" + process.waitFor());
            } catch (InterruptedException e) {
                // NOOP
            }
            LOGGER.info("Windows destroy wallet process - exit value :" + process.exitValue());
        }

        if (pb != null) {
            try {
                Process process = pb.start();
                try {
                    LOGGER.info("Wait for value : " + process.waitFor());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                LOGGER.info("Killing WALLET daemon exit value : " + process.exitValue());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
