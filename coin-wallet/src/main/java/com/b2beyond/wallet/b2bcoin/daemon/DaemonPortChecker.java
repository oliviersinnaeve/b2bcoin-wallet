package com.b2beyond.wallet.b2bcoin.daemon;

import com.b2beyond.wallet.b2bcoin.B2BWallet;
import org.apache.log4j.Logger;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.net.Socket;


public class DaemonPortChecker implements Runnable {

    private static Logger LOGGER = Logger.getLogger(DaemonPortChecker.class);


    @Override
    public void run() {
        int walletTries = 10;
        int coinTries = 10;
        while (availableForConnection(9090)) {
            LOGGER.info("Still Loading the wallet daemon ...");
            if (walletTries == 0) {
                JOptionPane.showMessageDialog(null,
                        "We tried to start the wallet rpc server on port 9090, it could not be started.\n" +
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
        while (availableForConnection(39155)) {
            LOGGER.info("Still Loading the coin daemon ...");
            if (coinTries == 0) {
                JOptionPane.showMessageDialog(null,
                        "We tried to start the coin daemon on port 39155, it could not be started.\n" +
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

//        while (availableForConnection(39156)) {
//            LOGGER.info("Still Loading the wallet daemon and coin daemon ...");
//            if (tries == 0) {
//                JOptionPane.showMessageDialog(null,
//                        "We tried to start the coin rpc server on port 39156, it could not be started.\n" +
//                                "We will shutdown the application, it is not usable anyway.",
//                        "Fatal error",
//                        JOptionPane.ERROR_MESSAGE);
//                System.exit(1);
//            }
//            try {
//                Thread.sleep(2000);
//                tries -= 1;
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }

    private boolean availableForConnection(int port) {
        try (Socket ignored = new Socket("localhost", port)) {
            return false;
        } catch (IOException ignored) {
            return true;
        }
    }
}
