package com.b2beyond.wallet.b2bcoin;

import com.b2beyond.wallet.b2bcoin.controler.*;
import com.b2beyond.wallet.b2bcoin.daemon.DaemonPortChecker;
import com.b2beyond.wallet.b2bcoin.rpc.SynchronizationRpcPoller;
import com.b2beyond.wallet.b2bcoin.rpc.TransactionItemsRpcPoller;
import com.b2beyond.wallet.b2bcoin.rpc.UnconfirmedTransactionHashesRpcPoller;
import com.b2beyond.wallet.b2bcoin.util.B2BUtil;
import com.b2beyond.wallet.b2bcoin.view.TabContainer;
import com.b2beyond.wallet.b2bcoin.view.controller.ActionController;
import com.b2beyond.wallet.b2bcoin.view.controller.PaymentController;
import com.b2beyond.wallet.b2bcoin.view.view.*;
import com.b2beyond.wallet.b2bcoin.view.view.MenuBar;
import com.b2beyond.wallet.http.HtmlExecutor;
import com.b2beyond.wallet.http.HttpPoller;
import com.b2beyond.wallet.http.NoParamsHttpPoller;
import com.b2beyond.wallet.rpc.NoParamsRpcPoller;
import com.b2beyond.wallet.rpc.RpcPoller;
import com.b2beyond.wallet.rpc.model.Addresses;
import com.b2beyond.wallet.rpc.model.Status;
import com.b2beyond.wallet.rpc.model.coin.BlockCount;
import com.b2beyond.wallet.rpc.model.coin.BlockHeaderWrapper;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.text.DefaultEditorKit;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;


public class B2BWallet extends MainFrame {

    private static Logger LOGGER = Logger.getLogger(B2BWallet.class);

    private static ActionController actionController;

    private static PropertiesConfiguration applicationProperties;
    private static PropertiesConfiguration walletDaemonProperties;
    private static PropertiesConfiguration oldWalletDaemonProperties;
    private static PropertiesConfiguration coinDaemonProperties;

    private static SplashWindow loadWindow;
    private static int loadingCounter = 1;

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo lnf :
                    UIManager.getInstalledLookAndFeels()) {
                System.out.println(lnf.getName());
                if ("metal".equalsIgnoreCase(lnf.getName())) {
                    UIManager.setLookAndFeel(lnf.getClassName());

                    InputMap im = (InputMap) UIManager.get("TextField.focusInputMap");
                    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.META_DOWN_MASK), DefaultEditorKit.copyAction);
                    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.META_DOWN_MASK), DefaultEditorKit.pasteAction);
                    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.META_DOWN_MASK), DefaultEditorKit.cutAction);
                    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.META_DOWN_MASK), DefaultEditorKit.selectAllAction);

                    UIManager.put("Table.foreground",
                            new ColorUIResource(Color.black));
                    UIManager.put("Table.background",
                            new ColorUIResource(B2BUtil.mainColor));

                    UIManager.put("OptionPane.border",
                            new EmptyBorder(10, 10, 10, 10));
                    break;
                }
            }
        } catch (Exception e) { /* Lazy handling this >.> */ }


        System.setProperty("user.home.forknote", "b2bcoin");

        B2BUtil.copyConfigsOnRun();

        applicationProperties = new PropertiesLoader("application.properties").getProperties();
        loadingFrame(applicationProperties.getString("version"));
        loadWindow.setProgress(loadingCounter++);
        walletDaemonProperties = new PropertiesLoader("coin-wallet.conf").getProperties();
        loadWindow.setProgress(loadingCounter++);
        oldWalletDaemonProperties = new PropertiesLoader("coin-wallet-old.conf").getProperties();
        loadWindow.setProgress(loadingCounter++);
        coinDaemonProperties = new PropertiesLoader("coin.conf").getProperties();
        loadWindow.setProgress(loadingCounter++);

        LOGGER.debug(coinDaemonProperties.getInt("p2p-bind-port"));
        int daemonPort = coinDaemonProperties.getInt("p2p-bind-port");
        int daemonRpcPort = coinDaemonProperties.getInt("rpc-bind-port");
        int walletRpcPort = walletDaemonProperties.getInt("bind-port");

        LOGGER.info("Checking ports : '" + walletRpcPort + "' : '" + daemonPort + "' : '" + daemonRpcPort + "'");

        if (!B2BUtil.availableForConnection(walletRpcPort)
                || !B2BUtil.availableForConnection(daemonPort)
                || !B2BUtil.availableForConnection(daemonRpcPort)) {
            int dialogResult = JOptionPane.showConfirmDialog(
                    null,
                    "A " + System.getProperty("user.home.forknote") + " dameon and/or wallet daemon is running.\nWould you like to continue with them ?",
                    "Press yas to start the wallet with current daemons", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.NO_OPTION) {
                System.exit(1);
            }
        }

        LOGGER.info("Properties loaded, wallet can get started");
        String daemonExecutable = applicationProperties.getString("coin-daemon-" + B2BUtil.getOperatingSystem());
        String walletExecutable = applicationProperties.getString("wallet-daemon-" + B2BUtil.getOperatingSystem());
        String poolMinerExecutable = applicationProperties.getString("pool-miner-daemon-" + B2BUtil.getOperatingSystem());
        B2BUtil.copyDaemonsOnRun(daemonExecutable, walletExecutable, poolMinerExecutable);

        DaemonController coinDaemon = new DaemonController(
                applicationProperties,
                coinDaemonProperties,
                walletDaemonProperties,
                oldWalletDaemonProperties,
                B2BUtil.getOperatingSystem());
        WalletRpcController walletRpcController = new WalletRpcController(applicationProperties.getString("wallet-daemon-base-url"));
        CoinRpcController coinRpcController = new CoinRpcController(applicationProperties.getString("coin-daemon-base-url"));
        CoinHttpController coinHttpController = new CoinHttpController(applicationProperties.getString("wallet-height-url"));
        actionController = new ActionController(coinDaemon, coinHttpController, walletRpcController, coinRpcController, applicationProperties);
        loadWindow.setProgress(loadingCounter++);

        new B2BWallet(applicationProperties, actionController);
    }

    private B2BWallet(PropertiesConfiguration applicationProperties, final ActionController actionController) {
        super(applicationProperties, actionController);

        if (LOGGER.isDebugEnabled()) {
            Properties p = System.getProperties();
            Enumeration keys = p.keys();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                String value = (String) p.get(key);
                LOGGER.info(key + ": " + value);
            }
        }

//        Runtime.getRuntime().addShutdownHook(new Thread() {
//            @Override
//            public void run() {
//                actionController.exit();
//            }
//        });

        new Thread(new DaemonPortChecker(actionController, walletDaemonProperties)).start();

        LOGGER.info("Starting controllers ...");
        PaymentController paymentController = new PaymentController(actionController);
        loadWindow.setProgress(loadingCounter++);
        LOGGER.info("Controllers started.");

        LOGGER.info("Creating tab view instances ...");
        final StatusTabView statusTabView = new StatusTabView(actionController);
        final TransactionsTabView transactionsTabView = new TransactionsTabView(actionController.getWalletRpcController().getTransactionExecutor());
        final AddressesTabView addressesTabView = new AddressesTabView(actionController);
        final PaymentTabView paymentTabView = new PaymentTabView();
        final CreatePaymentTabView createPaymentTabView = new CreatePaymentTabView(paymentController);

        loadWindow.setProgress(loadingCounter++);

        MenuBar menuBar = new MenuBar(this, walletDaemonProperties, applicationProperties, actionController);
        setJMenuBar(menuBar);
        loadWindow.setProgress(loadingCounter++);
        menuBar.panelObservable.addObserver(this);

        List<TabContainer> containers = new ArrayList<>();
        URL splashScreenLocation = Thread.currentThread().getContextClassLoader().getResource("stats-icon.png");
        containers.add(new TabContainer<>(0, "Overview", statusTabView, true, new ImageIcon(splashScreenLocation)));
        containers.add(new TabContainer<>(1, "Addresses", addressesTabView, true));
        containers.add(new TabContainer<>(2, "Transactions", transactionsTabView, true));
        containers.add(new TabContainer<>(3, "Payments", paymentTabView, true));
        containers.add(new TabContainer<>(4, "Create payments", createPaymentTabView, true));
        loadWindow.setProgress(loadingCounter++);

        setContainers(containers);

        LOGGER.info("Generating frame ...");
        loadWindow.setProgress(loadingCounter++);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Start http height call
        LOGGER.info("Starting http poller ...");
        final HttpPoller<Long> heightHttpPoller = new NoParamsHttpPoller<Long>(actionController.getCoinHttpController().getHeightExecutor(), 5000);
        heightHttpPoller.addObserver(this);
        actionController.getCoinHttpController().addPollers(heightHttpPoller);


        // Start polling and updating the views
        LOGGER.info("Starting the rpc pollers ...");
        final RpcPoller<Status> statusPoller = new NoParamsRpcPoller<>(actionController.getWalletRpcController().getStatusExecutor(), 15000);
        final SynchronizationRpcPoller<BlockCount> syncPoller = new SynchronizationRpcPoller<>(actionController.getCoinRpcController().getBlockCountExecutor(), 15000);
        final SynchronizationRpcPoller<BlockHeaderWrapper> blockHeaderPoller = new SynchronizationRpcPoller<>(actionController.getCoinRpcController().getBlockHeaderWrapperExecutor(), 15000);

        statusPoller.addObserver(syncPoller);
        statusPoller.addObserver(this);
        syncPoller.addObserver(this);
        blockHeaderPoller.addObserver(this);
        actionController.getCoinRpcController().addPollers(syncPoller);
        actionController.getCoinRpcController().addPollers(blockHeaderPoller);
        loadWindow.setProgress(loadingCounter++);

        RpcPoller<Addresses> addressesPoller = new NoParamsRpcPoller<>(actionController.getWalletRpcController().getAddressesExecutor(), 120000);
        TransactionItemsRpcPoller transactionsPoller = new TransactionItemsRpcPoller(actionController.getWalletRpcController().getTransactionsExecutor(), 120000);
        UnconfirmedTransactionHashesRpcPoller unconfirmedTransactionHashesPoller = new UnconfirmedTransactionHashesRpcPoller(actionController.getWalletRpcController().getUnconfirmedTransactionHashesExecutor(), 120000);


        walletRpcPollers.add(statusPoller);
        walletRpcPollers.add(addressesPoller);
        walletRpcPollers.add(transactionsPoller);
        walletRpcPollers.add(unconfirmedTransactionHashesPoller);

        actionController.setWalletRpcPollers(walletRpcPollers);

        // Add observers
        transactionsPoller.addObserver(transactionsTabView);
        transactionsPoller.addObserver(statusTabView);
        transactionsPoller.addObserver(paymentTabView);

        unconfirmedTransactionHashesPoller.addObserver(statusTabView);
        unconfirmedTransactionHashesPoller.addObserver(transactionsTabView);

        statusPoller.addObserver(transactionsPoller);
        statusPoller.addObserver(statusTabView);

        addressesPoller.addObserver(statusTabView);
        addressesPoller.addObserver(addressesTabView);
        addressesPoller.addObserver(paymentTabView);
        addressesPoller.addObserver(createPaymentTabView);
        addressesPoller.addObserver(transactionsTabView);
        addressesPoller.addObserver(transactionsPoller);
        addressesPoller.addObserver(unconfirmedTransactionHashesPoller);

        LOGGER.info("Rpc pollers started.");

        loadWindow.setProgress(loadingCounter++);
        loadWindow.setScreenVisible(false);
        //make sure the JFrame is visible
        this.setVisible(true);
    }

    private static void loadingFrame(String version) {
        URL splashScreenLocation = Thread.currentThread().getContextClassLoader().getResource("splash.png");
        if (splashScreenLocation != null) {
            loadWindow = new SplashWindow(new ImageIcon(splashScreenLocation), version);
        }
        loadWindow.setLocationRelativeTo(null);
        loadWindow.setProgressMax(10);
        loadWindow.setScreenVisible(true);
    }

}
