package com.b2beyond.wallet.b2bcoin.daemon;

import com.b2beyond.wallet.b2bcoin.util.B2BUtil;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;


/**
 * It represents the properties to use when the paypal component is activated for an account.
 *
 * Created by oliviersinnaeve on 09/03/17.
 */
public class WalletDaemon extends AbstractDaemon {

    private static Logger LOGGER = Logger.getLogger(WalletDaemon.class);

    private PropertiesConfiguration walletProperties;

    public WalletDaemon(PropertiesConfiguration applicationProperties, String operatingSystem, final PropertiesConfiguration walletProperties, final PropertiesConfiguration oldWalletProperties, String container, String password, boolean firstStartup) {
        LOGGER.info("Starting WALLET daemon for OS : " + operatingSystem);
        this.operatingSystem = operatingSystem;
        this.walletProperties = walletProperties;

        final String userHome = B2BUtil.getUserHome();
        final String binariesLocation = B2BUtil.getBinariesRoot();
        final String configLocation = B2BUtil.getConfigRoot();
        final String logLocation = B2BUtil.getLogRoot();

        try {
            String daemonExecutable = applicationProperties.getString("wallet-daemon-" + operatingSystem);

            LOGGER.debug("Wallet daemon userHome : " + userHome);
            LOGGER.debug("Wallet daemon binaries location : " + binariesLocation);
            LOGGER.debug("Wallet daemon configLocation : " + configLocation);

            // Password needs to be set every time so it does not get remembered
            walletProperties.setProperty("container-password", password);
            walletProperties.setProperty("container-file", container);
            oldWalletProperties.setProperty("container-password", password);
            oldWalletProperties.setProperty("container-file", container);
            saveProperties(walletProperties, configLocation, "coin-wallet.conf");
            saveProperties(oldWalletProperties, configLocation, "coin-wallet-old.conf");

            if (firstStartup) {
                LOGGER.info("First wallet startup - create new wallet or import wallet");
                LOGGER.debug("First wallet startup - Wallet daemon process argument: " + binariesLocation + daemonExecutable + " --config " + configLocation + "coin-wallet.conf" + " --generate-container " +
                        "--log-file " + logLocation + walletProperties.getString("log-file-wallet") + " --server-root " + userHome);

                ProcessBuilder pb = new ProcessBuilder(binariesLocation + daemonExecutable, "--config", configLocation + "coin-wallet.conf", "--generate-container",
                        "--log-file", userHome + applicationProperties.getString("log-file-wallet"), "--server-root", userHome);

                LOGGER.info("First wallet startup - start process");
                Process process = pb.start();
                processPid = B2BUtil.getPid(process, operatingSystem, false);

                InputStream processOut = process.getInputStream();
                BufferedReader processOutBuffer = new BufferedReader(new InputStreamReader(processOut));
                String line;
                while ((line = processOutBuffer.readLine()) != null) {
                    LOGGER.info("First wallet startup - WALLET creation output : " + line);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    LOGGER.debug("First wallet startup - Wait for first wallet run to finish with exit code : " + process.waitFor());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                process.destroy();
                LOGGER.info("First wallet startup - Wait for value : " + process.waitFor());
                LOGGER.info("First wallet startup - Exit value : " + process.exitValue());

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            LOGGER.debug("Wallet daemon process argument: " + binariesLocation + daemonExecutable + " --config " + configLocation + "coin-wallet.conf " +
                    "--log-file " + logLocation + applicationProperties.getString("log-file-wallet"));

            ProcessBuilder pb = new ProcessBuilder(binariesLocation + daemonExecutable, "--config", configLocation + "coin-wallet.conf",
                    "--log-file", userHome + applicationProperties.getString("log-file-wallet"), "--server-root", userHome);
            if (operatingSystem.equalsIgnoreCase(B2BUtil.WINDOWS)) {
                pb = new ProcessBuilder(binariesLocation + daemonExecutable, "--config", configLocation + "coin-wallet.conf",
                        "--log-file", userHome + applicationProperties.getString("log-file-wallet"), "--server-root", userHome);
            }

            process = pb.start();
            processPid = B2BUtil.getPid(process, operatingSystem, true);
            LOGGER.debug("Wallet Process id retrieved : " + processPid);

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

//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Thread.sleep(15000);
//                        LOGGER.info("Reset password");
//                        walletProperties.setProperty("container-password", "");
//                        saveProperties(walletProperties, configLocation, "coin-wallet.conf");
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
        } catch (Exception ex) {
            LOGGER.error("Wallet daemon failed : ", ex);
        }

    }

    private void saveProperties(PropertiesConfiguration walletProperties, String configLocation, String filename) {
        try{
            PrintWriter writer = new PrintWriter(configLocation + filename, "UTF-8");
            Iterator<String> propertyNames = walletProperties.getKeys();
            while (propertyNames.hasNext()) {
                String property = propertyNames.next();
                String[] values = walletProperties.getStringArray(property);

                Set<String> set = new LinkedHashSet<>(Arrays.asList(values));
                values = set.toArray(new String[values.length]);

                for (String value : values) {
                    if (value != null && !"null".equalsIgnoreCase(value)) {
                        writer.println(property + "=" + value);
                    }
                }
            }
            writer.close();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            // do something
        }
    }

    @Override
    public int getPort() {
        return 9090;
    }

}
