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

    private int processPid;
    private String daemonExecutable;
    private String operatingSystem;


    public CoinDaemon(Properties daemonProperties, String operatingSystem) {
        LOGGER.info("Starting coin daemon for OS : " + operatingSystem);
        this.operatingSystem = operatingSystem;

        URL baseLocation = Thread.currentThread().getContextClassLoader().getResource("b2bcoin-" + operatingSystem + "/");
        if (baseLocation != null) {

            String userHome = B2BUtil.getUserHome();
            String location = B2BUtil.getBinariesRoot(operatingSystem, baseLocation.getFile(), B2BWallet.DEV);

            try {
                daemonExecutable = daemonProperties.getProperty("coin-daemon-" + operatingSystem);
                ProcessBuilder pb = new ProcessBuilder("binaries/" + daemonExecutable, "--config-file", "configs/b2bcoin.conf",
                        "--log-file", userHome + daemonProperties.getProperty("log-file-coin"));
                if (operatingSystem.equalsIgnoreCase(B2BUtil.WINDOWS)) {
                    pb = new ProcessBuilder("cmd", "/c", "start", "", location + "\\binaries\\" + daemonExecutable, "--config-file", "\"" + location + "\\configs\\b2bcoin.conf\"",
                            "--log-file", userHome + daemonProperties.getProperty("log-file-coin"));
                } else {
                    pb.directory(new File(location));
                }

                Process process = pb.start();
                processPid = B2BUtil.getPid(process, operatingSystem, false);
                LOGGER.debug("Coin Process id retrieved : " + processPid);
            } catch (Exception ex) {
                LOGGER.error("Coin daemon failed to load", ex);
            }
        }
    }

    @Override
    public void stop() {
        ProcessBuilder pb = null;
        if (operatingSystem.equalsIgnoreCase(B2BUtil.LINUX) || operatingSystem.equalsIgnoreCase(B2BUtil.MAC)) {
            pb = new ProcessBuilder("kill", "-9", "" + processPid);
        }

        if (operatingSystem.equalsIgnoreCase(B2BUtil.WINDOWS)) {
            pb = new ProcessBuilder("cmd", "/c", "taskkill", "/IM", daemonExecutable);
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

//    @Override
//    public void stop() {
//        // STOP coinDaemons
//        process.destroy();
//
//        try {
//            LOGGER.info("Wait for value : " + process.waitFor());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        Integer processValue = null;
//        while (processValue == null) {
//            try {
//                processValue = process.exitValue();
//            } catch (IllegalThreadStateException e) {
//                LOGGER.info(e.getMessage());
//            }
//
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        LOGGER.info("Killing COIN daemon exit value : " + process.exitValue());
//    }

}
