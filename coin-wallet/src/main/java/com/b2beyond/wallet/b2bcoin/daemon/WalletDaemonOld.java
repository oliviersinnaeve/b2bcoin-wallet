package com.b2beyond.wallet.b2bcoin.daemon;

import com.b2beyond.wallet.b2bcoin.util.B2BUtil;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;


/**
 * It represents the properties to use when the paypal component is activated for an account.
 *
 * Created by oliviersinnaeve on 09/03/17.
 */
public class WalletDaemonOld extends AbstractDaemon {

    private static Logger LOGGER = Logger.getLogger(WalletDaemonOld.class);

    private PropertiesConfiguration walletProperties;

    public WalletDaemonOld(PropertiesConfiguration applicationProperties, String operatingSystem, final PropertiesConfiguration walletProperties, String container, String password) {
        LOGGER.info("Starting OLD WALLET daemon for OS : " + operatingSystem);
        this.operatingSystem = operatingSystem;
        this.walletProperties = walletProperties;

        final String userHome = B2BUtil.getUserHome();
        final String binariesLocation = B2BUtil.getBinariesRoot();
        final String configLocation = B2BUtil.getConfigRoot();
        final String logLocation = B2BUtil.getLogRoot();

        try {
            String daemonExecutable = applicationProperties.getString("wallet-daemon-old-" + operatingSystem);

            LOGGER.debug("Wallet daemon userHome : " + userHome);
            LOGGER.debug("Wallet daemon binaries location : " + binariesLocation);
            LOGGER.debug("Wallet daemon configLocation : " + configLocation);

            // Password needs to be set every time so it does not get remembered
            walletProperties.setProperty("container-password", password);
            walletProperties.setProperty("container-file", container);
            saveProperties(walletProperties, configLocation);

            LOGGER.debug("Wallet daemon process argument: " + binariesLocation + daemonExecutable + " --config " + configLocation + "coin-wallet.conf " +
                    "--log-file " + logLocation + applicationProperties.getString("log-file-wallet-old"));

            ProcessBuilder pb = new ProcessBuilder(binariesLocation + daemonExecutable, "--config", configLocation + "coin-wallet.conf",
                    "--log-file", userHome + applicationProperties.getString("log-file-wallet-old"), "--server-root", userHome);
            if (operatingSystem.equalsIgnoreCase(B2BUtil.WINDOWS)) {
                pb = new ProcessBuilder(binariesLocation + daemonExecutable, "--config", configLocation + "coin-wallet.conf",
                        "--log-file", userHome + applicationProperties.getString("log-file-wallet-old"), "--server-root", userHome);
            }

            process = pb.start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        InputStream inputStream = process.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream), 1);
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            System.out.println(line);
                        }
                        inputStream.close();
                        bufferedReader.close();


                        InputStream errorStream = process.getErrorStream();
                        BufferedReader outBufferedReader = new BufferedReader(new InputStreamReader(errorStream), 1);
                        String outLine;
                        while ((outLine = outBufferedReader.readLine()) != null) {
                            System.out.println(outLine);
                        }
                        errorStream.close();
                        outBufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception ex) {
            LOGGER.error("Old Wallet daemon failed : ", ex);
        }
    }

    private void saveProperties(PropertiesConfiguration walletProperties, String configLocation) {
        try{
            PrintWriter writer = new PrintWriter(configLocation + "coin-wallet.conf", "UTF-8");
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
