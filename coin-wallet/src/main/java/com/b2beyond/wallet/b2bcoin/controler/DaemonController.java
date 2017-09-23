package com.b2beyond.wallet.b2bcoin.controler;

import com.b2beyond.wallet.b2bcoin.controler.panel.NewWalletPanel;
import com.b2beyond.wallet.b2bcoin.daemon.CoinDaemon;
import com.b2beyond.wallet.b2bcoin.daemon.Daemon;
import com.b2beyond.wallet.b2bcoin.daemon.WalletDaemon;
import com.b2beyond.wallet.b2bcoin.util.B2BUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;


public class DaemonController {


    private Logger LOGGER = Logger.getLogger(this.getClass());

    private Daemon coinDaemon;
    private Daemon walletDaemon;

    private WalletDaemonProperties walletProperties;

    private Properties coinProperties;
    private String operatingSystem;
    private String userHome;
    private String password;
    private String container;


    public DaemonController(Properties coinProperties, WalletDaemonProperties walletProperties, String operatingSystem) {
        LOGGER.info("Loading Daemon controller");
        this.coinProperties = coinProperties;
        this.walletProperties = walletProperties;
        this.operatingSystem = operatingSystem;
        boolean firstStartup = false;

        userHome = B2BUtil.getUserHome();
        container = walletProperties.getProperties().getProperty("container-file");
        password = walletProperties.getProperties().getProperty("container-password");

        File userHomeFiles = new File(userHome);
        userHomeFiles.mkdirs();

        if (new File(userHome + "b2bcoin-wallet.conf").exists()) {
            try {
                walletProperties.getProperties().load(new FileInputStream(userHome + "b2bcoin-wallet.conf"));
                container = walletProperties.getProperties().getProperty("container-file");
                password = walletProperties.getProperties().getProperty("container-password");
            } catch (IOException e) {
                LOGGER.info("No wallet has been loaded ever");
            }
        }
            if (StringUtils.isBlank(container) || StringUtils.isBlank(password) || !new File(userHome + container).exists()) {

                final NewWalletPanel newWalletPallet = new NewWalletPanel();

                String daemonExecutable = coinProperties.getProperty("coin-daemon-" + operatingSystem);
                String walletExecutable = coinProperties.getProperty("wallet-daemon-" + operatingSystem);
                B2BUtil.copyDaemonsOnFirstRun(daemonExecutable, walletExecutable);

                boolean valid = false;
                while (!valid) {
                    URL splashScreenLocation = Thread.currentThread().getContextClassLoader().getResource("splash.png");
                    JOptionPane.showMessageDialog(null, newWalletPallet, "Create or Import",
                            JOptionPane.INFORMATION_MESSAGE, new ImageIcon(splashScreenLocation));

                    newWalletPallet.getWalletNameField().setEnabled(true);
                    valid = StringUtils.isNotBlank(newWalletPallet.getWalletNameField().getText()) && StringUtils.isNotBlank(newWalletPallet.getPasswordField().getText());
                }

                container = newWalletPallet.getWalletNameField().getText();
                password = newWalletPallet.getPasswordField().getText();

                firstStartup = true;
            }


        coinDaemon = new CoinDaemon(coinProperties, operatingSystem);
        walletDaemon =  new WalletDaemon(coinProperties, operatingSystem, walletProperties.getProperties(), container, password, firstStartup);
    }

    public void stop() {
        walletDaemon.stop();
        coinDaemon.stop();

        try {
            String timestamp = new SimpleDateFormat("dd-MM-yyyy-hh-mm").format(new Date());
            LOGGER.info("Backing up for container : " + container + " : " + timestamp);
            File userHomeBackupFiles = new File(userHome + "/backup");
            userHomeBackupFiles.mkdirs();
            walletProperties.getProperties().store(new FileOutputStream(userHome + B2BUtil.SEPARATOR + "backup" + B2BUtil.SEPARATOR + "b2bcoin-wallet-" + timestamp +".conf"), "");

            try {
                String[] splitContainer = container.split("/");
                String containerName = splitContainer[splitContainer.length - 1];

                if (splitContainer.length == 1) {
                    Files.copy(
                            Paths.get(userHome + container),
                            new FileOutputStream(userHome + B2BUtil.SEPARATOR + "backup" + B2BUtil.SEPARATOR + containerName + "." + timestamp + ".bckp"));
                } else {
                    Files.copy(
                            Paths.get(container),
                            new FileOutputStream(userHome + B2BUtil.SEPARATOR + "backup" + B2BUtil.SEPARATOR + containerName + "." + timestamp + ".bckp"));
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        LOGGER.info("STOPPING DAEMON !!!!!!");
    }

    public void restartWallet() {
        walletDaemon.stop();
        walletDaemon =  new WalletDaemon(coinProperties, operatingSystem, walletProperties.getProperties(), container, password, false);
    }
}
