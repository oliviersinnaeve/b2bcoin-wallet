package com.b2beyond.wallet.b2bcoin.controler;

import com.b2beyond.wallet.b2bcoin.controler.panel.NewWalletPanel;
import com.b2beyond.wallet.b2bcoin.daemon.CoinDaemon;
import com.b2beyond.wallet.b2bcoin.daemon.Daemon;
import com.b2beyond.wallet.b2bcoin.daemon.WalletDaemon;
import com.b2beyond.wallet.b2bcoin.util.B2BUtil;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
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


public class DaemonController {


    private Logger LOGGER = Logger.getLogger(this.getClass());

    private Daemon coinDaemon;
    private Daemon walletDaemon;

    private PropertiesConfiguration walletProperties;
    private PropertiesConfiguration applicationProperties;

    private String operatingSystem;
    private String userHome;
    private String password;
    private String container;


    public DaemonController(PropertiesConfiguration applicationProperties, PropertiesConfiguration walletProperties, String operatingSystem) {
        LOGGER.info("Loading Daemon controller");
        this.applicationProperties = applicationProperties;
        this.walletProperties = walletProperties;
        this.operatingSystem = operatingSystem;
        boolean firstStartup = false;

        String configLocation = B2BUtil.getConfigRoot();
        userHome = B2BUtil.getUserHome();
        container = walletProperties.getString("container-file");
        password = walletProperties.getString("container-password");

        File userHomeFiles = new File(userHome);
        userHomeFiles.mkdirs();

        if (new File(configLocation + "coin-wallet.conf").exists()) {
            try {
                walletProperties.load(new FileInputStream(configLocation + "coin-wallet.conf"));
                container = walletProperties.getString("container-file");
                password = walletProperties.getString("container-password");
            } catch (ConfigurationException | IOException e) {
                LOGGER.info("No wallet has been loaded ever");
            }
        }

        if (StringUtils.isBlank(container) || StringUtils.isBlank(password) || !new File(userHome + container).exists()) {

            // TODO: rethink this, This is not the correct place, else we would not need to include the wallet panel in the
            // backend part ...
            final NewWalletPanel newWalletPallet = new NewWalletPanel();

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


        coinDaemon = new CoinDaemon(applicationProperties, operatingSystem);
        walletDaemon =  new WalletDaemon(applicationProperties, operatingSystem, walletProperties, container, password, firstStartup);
    }

    public void stop() {
        walletDaemon.stop();
        coinDaemon.stop();

        try {
            String timestamp = new SimpleDateFormat("dd-MM-yyyy-hh-mm").format(new Date());
            LOGGER.info("Backing up for container : " + container + " : " + timestamp);
            File userHomeBackupFiles = new File(userHome + "/backup/" + timestamp);
            userHomeBackupFiles.mkdirs();
            walletProperties.save(new FileOutputStream(userHomeBackupFiles + B2BUtil.SEPARATOR + "b2bcoin-wallet-" + timestamp + ".conf"));

            try {
                String[] splitContainer = container.split("/");
                String containerName = splitContainer[splitContainer.length - 1];

                if (splitContainer.length == 1) {
                    Files.copy(
                            Paths.get(userHome + container),
                            new FileOutputStream(userHomeBackupFiles + B2BUtil.SEPARATOR + containerName + "." + timestamp + ".bckp"));
                } else {
                    Files.copy(
                            Paths.get(container),
                            new FileOutputStream(userHomeBackupFiles + B2BUtil.SEPARATOR + containerName + "." + timestamp + ".bckp"));
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

    public void restartWalletDaemonIfStopped() {
        if (B2BUtil.availableForConnection(walletProperties.getInt("bind-port"))){
            ((WalletDaemon)walletDaemon).start();
        }
    }

}
