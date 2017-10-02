package com.b2beyond.wallet.b2bcoin.daemon;

import com.b2beyond.wallet.b2bcoin.util.B2BUtil;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.net.Socket;


public class DaemonPortChecker implements Runnable {

    private static Logger LOGGER = Logger.getLogger(DaemonPortChecker.class);

    private int daemonPort;
    private int walletRpcPort;

    public DaemonPortChecker(PropertiesConfiguration walletDaemonProperties) {
        this.daemonPort = walletDaemonProperties.getInt("p2p-bind-port");
        this.walletRpcPort = walletDaemonProperties.getInt("bind-port");
    }

    @Override
    public void run() {
        int walletTries = 10;
        int coinTries = 10;
        while (B2BUtil.availableForConnection(walletRpcPort)) {
            LOGGER.info("Still Loading the wallet daemon ...");
            if (walletTries == 0) {
                JOptionPane.showMessageDialog(null,
                        "We tried to start the wallet rpc server on port " + walletRpcPort + ", it could not be started.\n" +
                                "We will shutdown the application, it is not usable anyway.",
                        "Fatal error",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
            try {
                Thread.sleep(2000);
                walletTries -= 1;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while (B2BUtil.availableForConnection(daemonPort)) {
            LOGGER.info("Still Loading the coin daemon ...");
            if (coinTries == 0) {
                JOptionPane.showMessageDialog(null,
                        "We tried to start the coin daemon on port " + daemonPort + ", it could not be started.\n" +
                                "We will shutdown the application, it is not usable anyway.",
                        "Fatal error",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
            try {
                Thread.sleep(2000);
                coinTries -= 1;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
