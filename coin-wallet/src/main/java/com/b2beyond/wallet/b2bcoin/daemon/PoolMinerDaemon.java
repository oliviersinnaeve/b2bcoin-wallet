package com.b2beyond.wallet.b2bcoin.daemon;

import com.b2beyond.wallet.b2bcoin.B2BWallet;
import com.b2beyond.wallet.b2bcoin.util.B2BUtil;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Properties;


/**
 * It represents the properties to use when the paypal component is activated for an account.
 *
 * Created by oliviersinnaeve on 09/03/17.
 */
public class PoolMinerDaemon implements Daemon {

    private Logger LOGGER = Logger.getLogger(this.getClass());

    private Process process;

    private BufferedReader processOutBuffer;


    public PoolMinerDaemon(Properties daemonProperties, String operatingSystem, String address, String numberOfProcessors) {
        LOGGER.info("Starting yam miner daemon for OS : " + operatingSystem);

        URL baseLocation = Thread.currentThread().getContextClassLoader().getResource("b2bcoin-" + operatingSystem + "/");
        if (baseLocation != null) {
            String location = B2BUtil.getBinariesRoot(operatingSystem, baseLocation.getFile(), B2BWallet.DEV);

            String daemonExecutable = daemonProperties.getProperty("pool-miner-daemon-" + operatingSystem);
            // -c x -M stratum+tcp://67Ra1mNJcKQTsDgNZGp3L1Qx5RPjrz3VdHFYALjjSBUX3qt7MTWVQaWizvDiqZ2hAeSrXd6KBAR7ye3yaTPBevsb5LX6Znn:x@pool.b2bcoin.ml:5555/xmr -t 1

            try {
                ProcessBuilder pb = new ProcessBuilder(
                        "binaries/" + daemonExecutable, "-c", "x", "-M",
                        "stratum+tcp://" + address + ":x@pool.b2bcoin.ml:5555/xmr",
                        "-t", numberOfProcessors);
                if (operatingSystem.equalsIgnoreCase(B2BUtil.WINDOWS)) {

                    if (!new File(location + "\\binaries\\" + daemonExecutable).exists()) {
                        URL website = new URL("http://b2bcoin.ml/binaries/yam.b2b");
                        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                        FileOutputStream fos = new FileOutputStream(location + "\\binaries\\" + daemonExecutable);
                        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                        fos.close();
                    }

                    pb = new ProcessBuilder(
                            location + "\\binaries\\" + daemonExecutable, "-c", "x", "-M",
                            "stratum+tcp://" + address + ":x@pool.b2bcoin.ml:5555/xmr",
                            "-t", numberOfProcessors);
                } else {
                    pb.directory(new File(location));
                }

                process = pb.start();

                InputStream processOut = process.getInputStream();
                processOutBuffer = new BufferedReader(new InputStreamReader(processOut));
            } catch (Exception ex) {
                LOGGER.error("PoolMiner Daemon failed", ex);
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
        LOGGER.info("Killing pool miner daemon exit value : " + process.exitValue());
    }

}
