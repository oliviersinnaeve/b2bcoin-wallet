package com.b2beyond.wallet.b2bcoin.controler;

import com.b2beyond.wallet.b2bcoin.controler.panel.NewWalletPanel;
import com.b2beyond.wallet.b2bcoin.controler.panel.PasswordPanel;
import com.b2beyond.wallet.b2bcoin.daemon.*;
import com.b2beyond.wallet.b2bcoin.util.B2BUtil;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DaemonController {


    private Logger LOGGER = Logger.getLogger(this.getClass());

    private Daemon coinDaemon;
    private Daemon coinDaemonold;
    private WalletDaemon walletDaemon;
    private WalletDaemonOld walletDaemonOld;

    private PropertiesConfiguration applicationProperties;
    private PropertiesConfiguration walletProperties;
    private PropertiesConfiguration oldWalletProperties;
    private PropertiesConfiguration coinProperties;

    private String operatingSystem;

    private String userHome;
    private String password;
    private String container;

    private boolean firstStartup;


    public DaemonController(PropertiesConfiguration applicationProperties, PropertiesConfiguration coinProperties, PropertiesConfiguration walletProperties, PropertiesConfiguration oldWalletProperties, String operatingSystem) {
        LOGGER.info("Loading Daemon controller");
        this.applicationProperties = applicationProperties;
        this.walletProperties = walletProperties;
        this.oldWalletProperties = oldWalletProperties;
        this.coinProperties = coinProperties;
        this.operatingSystem = operatingSystem;
        firstStartup = false;
        URL splashScreenLocation = Thread.currentThread().getContextClassLoader().getResource("splash.png");

        // start the daemon already !!
        // We use remote dameon now ...
        coinDaemon = new CoinDaemon(applicationProperties, operatingSystem);

        String configLocation = B2BUtil.getConfigRoot();
        userHome = B2BUtil.getUserHome();
        container = walletProperties.getString("container-file");

        File userHomeFiles = new File(userHome);
        userHomeFiles.mkdirs();

        if (new File(configLocation + "coin-wallet.conf").exists()) {
            try {
                walletProperties.load(new FileInputStream(configLocation + "coin-wallet.conf"));
                container = walletProperties.getString("container-file");

                if (!StringUtils.isBlank(container) && new File(userHome + container).exists()) {
                    PasswordPanel passwordPanel = new PasswordPanel(container);

                    JOptionPane.showMessageDialog(null, passwordPanel, "Enter wallet password",
                            JOptionPane.INFORMATION_MESSAGE, B2BUtil.getIcon());

                    password = passwordPanel.getPasswordField().getText();
                }
            } catch (ConfigurationException | IOException e) {
                LOGGER.info("No wallet has been loaded ever");
            }
        }

        if (StringUtils.isBlank(container) || !new File(userHome + container).exists()) {

            // TODO: rethink this, This is not the correct place, else we would not need to include the wallet panel in the
            // backend part ...
            final NewWalletPanel newWalletPallet = new NewWalletPanel();

            boolean valid = false;
            while (!valid) {
                JOptionPane.showMessageDialog(null, newWalletPallet, "Create or Import",
                        JOptionPane.INFORMATION_MESSAGE, new ImageIcon(splashScreenLocation));

                newWalletPallet.getWalletNameField().setEnabled(true);
                valid = StringUtils.isNotBlank(newWalletPallet.getWalletNameField().getText())
                        && StringUtils.isNotBlank(newWalletPallet.getPasswordField().getText())
                        && StringUtils.isNotBlank(newWalletPallet.getPasswordField2().getText())
                        && StringUtils.equals(
                                newWalletPallet.getPasswordField().getText(),
                                newWalletPallet.getPasswordField2().getText());

                newWalletPallet.enablePasswordError(!StringUtils.equals(
                        newWalletPallet.getPasswordField().getText(),
                        newWalletPallet.getPasswordField2().getText()));
            }

            container = newWalletPallet.getWalletNameField().getText();
            password = newWalletPallet.getPasswordField().getText();

            firstStartup = true;
        }


//        walletDaemon = new WalletDaemon(applicationProperties, operatingSystem, walletProperties, oldWalletProperties, container, password, firstStartup);
    }

    public void startOldDaemn() {
        coinDaemonold = new CoinDaemonOld(applicationProperties, operatingSystem);
    }

    public void restartDaemon() {

        if (coinDaemonold != null) {
            coinDaemonold.stop();
        }
        if (coinDaemon != null) {
            coinDaemon.stop();
        }

        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        coinDaemon = new CoinDaemon(applicationProperties, operatingSystem);
    }

    public void stopDaemon() {
        if (coinDaemon != null) {
            coinDaemon.stop();

            int daemonPort = getDaemonPort();
            int daemonRpcPort = coinProperties.getInt("rpc-bind-port");

            LOGGER.info("Checking ports : '" + daemonPort + "' : '" + daemonRpcPort + "'");

            while (!B2BUtil.availableForConnection(daemonPort)
                    || !B2BUtil.availableForConnection(daemonRpcPort)) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            coinDaemon = null;
        }
    }

    public void startWallet() {
        int walletRpc = getWalletRpcPort();

        if (B2BUtil.availableForConnection(walletRpc) || walletDaemon == null) {
            walletDaemon = new WalletDaemon(applicationProperties, operatingSystem, walletProperties, oldWalletProperties, container, password, firstStartup);
        }
    }

    public void startOldWallet() {
        int walletRpc = getWalletRpcPort();

        if (B2BUtil.availableForConnection(walletRpc) || walletDaemon == null) {
            walletDaemonOld = new WalletDaemonOld(applicationProperties, operatingSystem, walletProperties, container, password);
        }
    }

    public void stopWalletd() {
        if (walletDaemon != null) {
            walletDaemon.stop();
            walletDaemon = null;
        }
    }

    public void stopOldWalletd() {
        if (walletDaemonOld != null) {
            walletDaemonOld.stop();
            walletDaemonOld = null;
        }
    }

    public void stopWallet() {
        if (walletDaemon != null) {
            walletDaemon.stop();
        }
        if (coinDaemon != null) {
            coinDaemon.stop();
        }
        if (coinDaemonold != null) {
            coinDaemonold.stop();
        }

        String timestamp = new SimpleDateFormat("dd-MM-yyyy-hh-mm").format(new Date());
        LOGGER.info("Backing up for container : " + container + " : " + timestamp);
        File userHomeBackupFiles = new File(userHome + "/backup/" + timestamp);
        userHomeBackupFiles.mkdirs();
        try {
            walletProperties.save(new FileOutputStream(userHomeBackupFiles + B2BUtil.SEPARATOR + "coin-wallet-" + timestamp + ".conf"));
        } catch (ConfigurationException | FileNotFoundException e) {
            e.printStackTrace();
        }

        B2BUtil.backupWallet(userHomeBackupFiles + B2BUtil.SEPARATOR, container, timestamp);
    }

    private int getDaemonPort() {
        return walletProperties.getInt("p2p-bind-port");
    }

    private int getWalletRpcPort() {
        return walletProperties.getInt("bind-port");
    }

//    public boolean isWalletStarted() {
//        return walletDaemon.isStarted();
//    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        LOGGER.info("STOPPING DAEMON !!!!!!");
    }

}
