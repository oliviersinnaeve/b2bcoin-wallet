package com.b2beyond.wallet.b2bcoin.daemon;

import com.b2beyond.wallet.b2bcoin.util.B2BUtil;
import com.b2beyond.wallet.b2bcoin.view.controller.ActionController;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.net.Socket;


public class DaemonPortChecker implements Runnable {

    private static Logger LOGGER = Logger.getLogger(DaemonPortChecker.class);

    private ActionController actionController;

    private boolean firstStart = true;

    private int daemonPort;
    private int daemonRpcPort;

    public DaemonPortChecker(ActionController actionController, PropertiesConfiguration walletDaemonProperties) {
        this.actionController = actionController;

        this.daemonPort = walletDaemonProperties.getInt("p2p-bind-port");
        this.daemonRpcPort = walletDaemonProperties.getInt("daemon-port");
    }

    @Override
    public void run() {
        while (true) {
            if (firstStart) {
                LOGGER.info("Loading the coin daemon ...");
                try {
                    Thread.sleep(60000 * 2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                firstStart = false;
            }

            if (B2BUtil.availableForConnection(daemonPort) && B2BUtil.availableForConnection(daemonRpcPort)) {
                if (B2BUtil.availableForConnection(daemonPort) && B2BUtil.availableForConnection(daemonRpcPort)) {
                    actionController.restartCoinDaemon();
                    try {
                        Thread.sleep(60000 * 2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
