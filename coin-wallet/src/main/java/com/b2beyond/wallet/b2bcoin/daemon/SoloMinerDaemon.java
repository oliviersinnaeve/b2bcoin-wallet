package com.b2beyond.wallet.b2bcoin.daemon;

import com.b2beyond.wallet.b2bcoin.B2BWallet;
import com.b2beyond.wallet.b2bcoin.controler.WalletDaemonProperties;
import com.b2beyond.wallet.b2bcoin.util.B2BUtil;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;


/**
 * It represents the properties to use when the paypal component is activated for an account.
 *
 * Created by oliviersinnaeve on 09/03/17.
 */
public class SoloMinerDaemon implements Daemon {

    private Logger LOGGER = Logger.getLogger(this.getClass());

    private Process process;

    private BufferedReader processOutBuffer;


    public SoloMinerDaemon(Properties daemonProperties, WalletDaemonProperties walletProperties, String operatingSystem, String address, String numberOfProcessors) {
        LOGGER.info("Starting solo simplewallet miner daemon for OS : " + operatingSystem);

        URL baseLocation = Thread.currentThread().getContextClassLoader().getResource("b2bcoin-" + operatingSystem + "/");

        if (baseLocation != null) {
            String userHome = B2BUtil.getUserHome();
            String location = B2BUtil.getBinariesRoot(operatingSystem, baseLocation.getFile(), B2BWallet.DEV);

            String daemonExecutable = daemonProperties.getProperty("solo-miner-daemon-" + operatingSystem);
            String container = walletProperties.getProperties().getProperty("container-file");
            String password = walletProperties.getProperties().getProperty("container-password");
            // ./simplewallet --config-file configs/b2bcoin-wallet.conf --wallet-file MinerWallet.wallet --password AlexiRenzo2 --command start_mining 1

            try {
                ProcessBuilder pb = new ProcessBuilder(
                        "binaries/" + daemonExecutable, "-config-file", userHome + "b2bcoin-wallet.conf", "--wallet-file", container, "--wallet-password", password, "--command", "start_mining", numberOfProcessors);
                if (operatingSystem.equalsIgnoreCase(B2BUtil.WINDOWS)) {
                    pb = new ProcessBuilder(
                            location + "\\binaries\\" + daemonExecutable,
                            "-config-file", userHome + "b2bcoin-wallet.conf",
                            "--wallet-file", container, "--wallet-password", password,
                            "--command", "start_mining", numberOfProcessors);
                } else {
                    pb.directory(new File(location));
                }

                process = pb.start();

                InputStream processOut = process.getInputStream();
                processOutBuffer = new BufferedReader(new InputStreamReader(processOut));
            } catch (Exception ex) {
                LOGGER.error("Solo Miner Daemon failed", ex);
            }
        }
    }

    public BufferedReader getProcessOutBuffer() {
        return processOutBuffer;
    }

    @Override
    public void stop() {
        // STOP coinDaemons
        process.destroy();

        try {
            LOGGER.info("Wait for value : " + process.waitFor());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Integer processValue = null;
        while (processValue == null) {
            try {
                processValue = process.exitValue();
            } catch (IllegalThreadStateException e) {
                LOGGER.info(e.getMessage());
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                LOGGER.error("jonRcpExecutor failed", e);
            }
        }
        LOGGER.info("Killing solo miner daemon exit value : " + process.exitValue());
    }

}
