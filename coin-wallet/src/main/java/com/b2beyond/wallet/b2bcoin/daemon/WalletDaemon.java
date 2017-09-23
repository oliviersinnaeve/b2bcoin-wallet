package com.b2beyond.wallet.b2bcoin.daemon;

import com.b2beyond.wallet.b2bcoin.B2BWallet;
import com.b2beyond.wallet.b2bcoin.util.B2BUtil;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Properties;


/**
 * It represents the properties to use when the paypal component is activated for an account.
 *
 * Created by oliviersinnaeve on 09/03/17.
 */
public class WalletDaemon implements Daemon {

    private Logger LOGGER = Logger.getLogger(this.getClass());

    private int processPid;
    private String daemonExecutable;
    private String operatingSystem;


    public WalletDaemon(Properties daemonProperties, String operatingSystem, Properties walletProperties, String container, String password, boolean firstStartup) {
        LOGGER.info("Starting WALLET daemon for OS : " + operatingSystem);
        this.operatingSystem = operatingSystem;

        URL baseLocation = Thread.currentThread().getContextClassLoader().getResource("b2bcoin-" + operatingSystem + "/");
        if (baseLocation != null) {

            String userHome = B2BUtil.getUserHome();
            String location = B2BUtil.getBinariesRoot(operatingSystem, baseLocation.getFile(), B2BWallet.DEV);

            try {
                daemonExecutable = daemonProperties.getProperty("wallet-daemon-" + operatingSystem);

                if (firstStartup) {
                    walletProperties.setProperty("container-file", userHome + container);
                    if (operatingSystem.equalsIgnoreCase(B2BUtil.WINDOWS)) {
                        String prefix = userHome;
                        prefix = prefix.replace("\\", "/");
                        walletProperties.setProperty("container-file", prefix + container);
                    }
                    walletProperties.setProperty("container-password", password);
                    saveProperties(walletProperties, userHome);

                    LOGGER.debug("Wallet daemon process argument: " + "binaries/" + daemonExecutable + " --config " + userHome + "b2bcoin-wallet.conf" + " --generate-container " +
                            "--log-file " + userHome + daemonProperties.getProperty("log-file-wallet"));

                    ProcessBuilder pb = new ProcessBuilder("binaries/" + daemonExecutable, "--config", userHome + "b2bcoin-wallet.conf", "--generate-container",
                            "--log-file", userHome + daemonProperties.getProperty("log-file-wallet"));
                    if (operatingSystem.equalsIgnoreCase(B2BUtil.WINDOWS)) {
                        pb = new ProcessBuilder(location + daemonExecutable, "--config", userHome + "b2bcoin-wallet.conf", "--generate-container",
                                "--log-file", userHome + daemonProperties.getProperty("log-file-wallet"));
                    }

                    pb.directory(new File(location));
//                    }

                    LOGGER.info("First startup, creating wallet");
                    Process process = pb.start();

                    InputStream processOut = process.getInputStream();
                    BufferedReader processOutBuffer = new BufferedReader(new InputStreamReader(processOut));
                    String line;
                    while ((line = processOutBuffer.readLine()) != null) {
                        LOGGER.info("WALLET creation output : " + line);
                    }

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    process.destroy();
                    LOGGER.info("First startup Wait for value : " + process.waitFor());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    LOGGER.info("First startup Exit value : " + process.exitValue());
                }


                LOGGER.debug("Wallet daemon process argument: " + "binaries/" + daemonExecutable + " --config " + userHome + "b2bcoin-wallet.conf " +
                        "--log-file " + userHome + daemonProperties.getProperty("log-file-wallet") + " -d");
                ProcessBuilder pb = new ProcessBuilder("binaries/" + daemonExecutable, "--config", userHome + "b2bcoin-wallet.conf",
                        "--log-file", userHome + daemonProperties.getProperty("log-file-wallet"), "-d");
//                if (operatingSystem.equalsIgnoreCase(B2BUtil.WINDOWS)) {
//                    pb = new ProcessBuilder("cmd", "/c", "start", "", location + "\\binaries\\" + daemonExecutable, "--config", userHome + "b2bcoin-wallet.conf",
//                            "--log-file", userHome + daemonProperties.getProperty("log-file-wallet"));
//                } else {
                    pb.directory(new File(location));
//                }

                Process process = pb.start();
                processPid = B2BUtil.getPid(process, operatingSystem, true);
                LOGGER.debug("Wallet Process id retrieved : " + processPid);
            } catch (Exception ex) {
                LOGGER.error("Wallet daemon failed : ", ex);
            }
        }
    }

    private void saveProperties(Properties walletProperties, String userHome) {
        try{
            PrintWriter writer = new PrintWriter(userHome + "b2bcoin-wallet.conf", "UTF-8");
            for (String property: walletProperties.stringPropertyNames()) {
                writer.println(property + "=" + walletProperties.getProperty(property));
            }
            writer.close();
        } catch (IOException e) {
            // do something
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

}
