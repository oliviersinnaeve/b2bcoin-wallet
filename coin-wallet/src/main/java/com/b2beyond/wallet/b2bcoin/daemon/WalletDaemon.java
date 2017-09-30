package com.b2beyond.wallet.b2bcoin.daemon;

import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.BlockCount;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.Status;
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
import java.util.Observable;
import java.util.Observer;
import java.util.Set;


/**
 * It represents the properties to use when the paypal component is activated for an account.
 *
 * Created by oliviersinnaeve on 09/03/17.
 */
public class WalletDaemon implements Daemon {

    private static Logger LOGGER = Logger.getLogger(WalletDaemon.class);

    private WalletDaemonRunnable process;

    public WalletDaemon(PropertiesConfiguration daemonProperties, String operatingSystem, PropertiesConfiguration walletProperties, String container, String password, boolean firstStartup) {
        LOGGER.info("Starting WALLET daemon for OS : " + operatingSystem);

        process = new WalletDaemonRunnable(daemonProperties, operatingSystem, walletProperties, container, password, firstStartup);
        new Thread(process).start();
    }

    @Override
    public void stop() {
        process.stop();
    }
}

class WalletDaemonRunnable implements Daemon, Runnable, Observer {

    private Logger LOGGER = Logger.getLogger(this.getClass());

    private PropertiesConfiguration daemonProperties;
    private String operatingSystem;
    private PropertiesConfiguration walletProperties;
    private String container;
    private String password;
    private boolean firstStartup;

    private boolean syncing;
    private long maxProgress;
    private Process process;
    private int processPid;

    public WalletDaemonRunnable(PropertiesConfiguration daemonProperties, String operatingSystem, PropertiesConfiguration walletProperties, String container, String password, boolean firstStartup) {
        this.daemonProperties = daemonProperties;
        this.operatingSystem = operatingSystem;
        this.walletProperties = walletProperties;
        this.container = container;
        this.password = password;
        this.firstStartup = firstStartup;
    }

    public void run() {
        while (syncing) {
            try {
                Thread.sleep(5000);
                Thread.yield();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        String userHome = B2BUtil.getUserHome();
        String binariesLocation = B2BUtil.getBinariesRoot();
        String configLocation = B2BUtil.getConfigRoot();
        String logLocation = B2BUtil.getLogRoot();

        try {
            String daemonExecutable = daemonProperties.getString("wallet-daemon-" + operatingSystem);

            LOGGER.debug("Wallet daemon userHome : " + userHome);
            LOGGER.debug("Wallet daemon binaries location : " + binariesLocation);
            LOGGER.debug("Wallet daemon configLocation : " + configLocation);

            if (firstStartup) {
                LOGGER.info("First wallet startup - create new wallet or import wallet");
                walletProperties.setProperty("container-file", container);
                walletProperties.setProperty("container-password", password);
                saveProperties(walletProperties, configLocation);

                LOGGER.debug("First wallet startup - Wallet daemon process argument: " + binariesLocation + daemonExecutable + " --config " + configLocation + "coin-wallet.conf" + " --generate-container " +
                        "--log-file " + logLocation + daemonProperties.getString("log-file-wallet") + " --server-root " + userHome);

                ProcessBuilder pb = new ProcessBuilder(binariesLocation + daemonExecutable, "--config", configLocation + "coin-wallet.conf", "--generate-container",
                        "--log-file", "logs/" + daemonProperties.getString("log-file-wallet"), "--server-root", userHome);

                LOGGER.info("First wallet startup - start process");
                Process process = pb.start();

                InputStream processOut = process.getInputStream();
                BufferedReader processOutBuffer = new BufferedReader(new InputStreamReader(processOut));
                String line;
                while ((line = processOutBuffer.readLine()) != null) {
                    LOGGER.info("First wallet startup - WALLET creation output : " + line);
                }

                try {
                    LOGGER.debug("First wallet startup - Wait for first wallet run to finish with exit code : " + process.waitFor());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                process.destroy();
                LOGGER.info("First wallet startup - Wait for value : " + process.waitFor());
                LOGGER.info("First wallet startup - Exit value : " + process.exitValue());
            }


            LOGGER.debug("Wallet daemon process argument: " + binariesLocation + daemonExecutable + " --config " + configLocation + "coin-wallet.conf " +
                    "--log-file " + logLocation + daemonProperties.getString("log-file-wallet") + " -d");

            ProcessBuilder pb = new ProcessBuilder(binariesLocation + daemonExecutable, "--config", configLocation + "coin-wallet.conf",
                    "--log-file", userHome + daemonProperties.getString("log-file-wallet"), "--server-root", userHome, "-d");
            if (operatingSystem.equalsIgnoreCase(B2BUtil.WINDOWS)) {
                pb = new ProcessBuilder(binariesLocation + daemonExecutable, "--config", configLocation + "coin-wallet.conf",
                        "--log-file", logLocation + daemonProperties.getString("log-file-wallet"), "--server-root", userHome);
            }

            process = pb.start();
            processPid = B2BUtil.getPid(process, operatingSystem, true);
            LOGGER.debug("Wallet Process id retrieved : " + processPid);
        } catch (Exception ex) {
            LOGGER.error("Wallet daemon failed : ", ex);
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
    public void stop() {
        ProcessBuilder pb = null;
        if (operatingSystem.equalsIgnoreCase(B2BUtil.LINUX) || operatingSystem.equalsIgnoreCase(B2BUtil.MAC)) {
            pb = new ProcessBuilder("kill", "-9", "" + processPid);
        }

        if (operatingSystem.equalsIgnoreCase(B2BUtil.WINDOWS)) {
            process.destroy();
            try {
                process.waitFor();
            } catch (InterruptedException e) {
                // NOOP
            }
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

    @Override
    public void update(Observable observable, Object data) {
        if (data instanceof Status) {
            Status viewData = (Status) data;
            maxProgress = viewData.getKnownBlockCount();
        }
        if (data instanceof BlockCount) {
            BlockCount blockCount = (BlockCount) data;
            if (maxProgress > 1) {
                syncing = maxProgress > blockCount.getCount();
            }
        }
    }

}