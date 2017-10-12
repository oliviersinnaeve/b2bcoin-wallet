package com.b2beyond.wallet.b2bcoin;

import com.b2beyond.wallet.b2bcoin.controler.CoinRpcController;
import com.b2beyond.wallet.b2bcoin.controler.DaemonController;
import com.b2beyond.wallet.b2bcoin.controler.PropertiesLoader;
import com.b2beyond.wallet.b2bcoin.controler.WalletRpcController;
import com.b2beyond.wallet.b2bcoin.daemon.DaemonPortChecker;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.NoParamsRpcPoller;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.RpcPoller;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.SynchronizationRpcPoller;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.TransactionItemsRpcPoller;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.UnconfirmedTransactionHashesRpcPoller;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.Addresses;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.BlockCount;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.Status;
import com.b2beyond.wallet.b2bcoin.util.B2BUtil;
import com.b2beyond.wallet.b2bcoin.view.TabContainer;
import com.b2beyond.wallet.b2bcoin.view.controller.ActionController;
import com.b2beyond.wallet.b2bcoin.view.controller.AddressesController;
import com.b2beyond.wallet.b2bcoin.view.controller.PaymentController;
import com.b2beyond.wallet.b2bcoin.view.controller.PoolMiningController;
import com.b2beyond.wallet.b2bcoin.view.controller.SoloMiningController;
import com.b2beyond.wallet.b2bcoin.view.view.AddressesTabView;
import com.b2beyond.wallet.b2bcoin.view.view.CreatePaymentTabView;
import com.b2beyond.wallet.b2bcoin.view.view.MainFrame;
import com.b2beyond.wallet.b2bcoin.view.view.MenuBar;
import com.b2beyond.wallet.b2bcoin.view.view.MiningTabView;
import com.b2beyond.wallet.b2bcoin.view.view.PaymentTabView;
import com.b2beyond.wallet.b2bcoin.view.view.SplashWindow;
import com.b2beyond.wallet.b2bcoin.view.view.StatusTabView;
import com.b2beyond.wallet.b2bcoin.view.view.TransactionsTabView;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import java.awt.Color;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;


public class B2BWallet extends MainFrame {

    private static Logger LOGGER = Logger.getLogger(B2BWallet.class);

    private static ActionController actionController;

    public static PropertiesConfiguration applicationProperties;
    public static PropertiesConfiguration walletDaemonProperties;
    public static PropertiesConfiguration coinDaemonProperties;

    private static SplashWindow loadWindow;
    private static int loadingCounter = 1;

    public static void main(String[] args) {
        System.setProperty("user.home.forknote", "b2bcoin");

        B2BUtil.copyConfigsOnFirstRun();

        loadingFrame();
        loadWindow.setProgress(loadingCounter++);
        applicationProperties = new PropertiesLoader("application.config").getProperties();
        loadWindow.setProgress(loadingCounter++);
        walletDaemonProperties = new PropertiesLoader("coin-wallet.conf").getProperties();
        loadWindow.setProgress(loadingCounter++);
        coinDaemonProperties = new PropertiesLoader("coin.conf").getProperties();
        loadWindow.setProgress(loadingCounter++);

        LOGGER.debug(walletDaemonProperties.getInt("p2p-bind-port"));
        int daemonPort = walletDaemonProperties.getInt("p2p-bind-port");
        int daemonRpcPort = walletDaemonProperties.getInt("rpc-bind-port");
        int walletRpcPort = walletDaemonProperties.getInt("bind-port");

        LOGGER.info("Checking ports : '" + walletRpcPort + "' : '" + daemonPort + "' : '" + daemonRpcPort + "'");

        if (!B2BUtil.availableForConnection(walletRpcPort)
                || !B2BUtil.availableForConnection(daemonPort)
                || !B2BUtil.availableForConnection(daemonRpcPort)) {
            int dialogResult = JOptionPane.showConfirmDialog (
                    null,
                    "A " + System.getProperty("user.home.forknote") + " dameon and/or wallet daemon is running.\nWould you like to continue with them ?",
                    "Press yas to start the wallet with current daemons", JOptionPane.YES_NO_OPTION);
            if(dialogResult == JOptionPane.NO_OPTION){
                System.exit(1);
            }
        }

        LOGGER.info("Properties loaded, wallet can get started");
        String daemonExecutable = applicationProperties.getString("coin-daemon-" + B2BUtil.getOperatingSystem());
        String walletExecutable = applicationProperties.getString("wallet-daemon-" + B2BUtil.getOperatingSystem());
        String poolMinerExecutable = applicationProperties.getString("pool-miner-daemon-" + B2BUtil.getOperatingSystem());
        B2BUtil.copyDaemonsOnFirstRun(daemonExecutable, walletExecutable, poolMinerExecutable);

        DaemonController coinDaemon = new DaemonController(
                applicationProperties,
                walletDaemonProperties,
                B2BUtil.getOperatingSystem());
        WalletRpcController walletRpcController = new WalletRpcController(applicationProperties.getString("wallet-daemon-base-url"));
        CoinRpcController coinRpcController = new CoinRpcController(applicationProperties.getString("coin-daemon-base-url"));
        actionController = new ActionController(coinDaemon, walletRpcController, coinRpcController);
        loadWindow.setProgress(loadingCounter++);

        new B2BWallet(applicationProperties, actionController);
    }

    public B2BWallet(PropertiesConfiguration applicationProperties, final ActionController actionController) {
        super(applicationProperties, actionController);

        UIManager.put("Table.foreground",
                new ColorUIResource(Color.black));
        UIManager.put("Table.background",
                new ColorUIResource(157, 217, 210));

        if (LOGGER.isDebugEnabled()) {
            Properties p = System.getProperties();
            Enumeration keys = p.keys();
            while (keys.hasMoreElements()) {
                String key = (String)keys.nextElement();
                String value = (String)p.get(key);
                LOGGER.info(key + ": " + value);
            }
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                actionController.exit();
            }
        });

        new Thread(new DaemonPortChecker(walletDaemonProperties)).start();

        LOGGER.info("Starting controllers ...");
        AddressesController addressesController = new AddressesController(
                actionController.getWalletRpcController().getCreateAddressExecutor(),
                actionController.getWalletRpcController().getBalanceExecutor());
        PaymentController paymentController = new PaymentController(
                actionController.getWalletRpcController().getPaymentExecutor());
        PoolMiningController miningController = new PoolMiningController(
                applicationProperties,
                B2BUtil.getOperatingSystem());
        SoloMiningController soloMiningController = new SoloMiningController(
                applicationProperties,
                walletDaemonProperties,
                B2BUtil.getOperatingSystem());
        loadWindow.setProgress(loadingCounter++);
        LOGGER.info("Controllers started.");

        LOGGER.info("Creating tab view instances ...");
        final StatusTabView statusTabView = new StatusTabView(actionController, actionController.getWalletRpcController().getResetExecutor(), actionController.getWalletRpcController().getTransactionExecutor());
        final TransactionsTabView transactionsTabView = new TransactionsTabView(actionController.getWalletRpcController().getTransactionExecutor());
        final AddressesTabView addressesTabView = new AddressesTabView(addressesController);
        final PaymentTabView paymentTabView = new PaymentTabView();
        final CreatePaymentTabView createPaymentTabView = new CreatePaymentTabView(paymentController);
        final MiningTabView miningTabView = new MiningTabView(miningController, applicationProperties);
        final MiningTabView soloMiningTabView = new MiningTabView(soloMiningController, applicationProperties);

        loadWindow.setProgress(loadingCounter++);
        actionController.setMiningController(miningController);
        actionController.setSoloMiningController(soloMiningController);

        MenuBar menuBar = new MenuBar(actionController);
        setJMenuBar(menuBar);
        loadWindow.setProgress(loadingCounter++);

        List<TabContainer> containers = new ArrayList<>();
        URL splashScreenLocation = Thread.currentThread().getContextClassLoader().getResource("stats-icon.png");
        containers.add(new TabContainer<>(0, "Overview", statusTabView, true, new ImageIcon(splashScreenLocation)));
        containers.add(new TabContainer<>(1, "Addresses", addressesTabView, true));
        containers.add(new TabContainer<>(2, "Transactions", transactionsTabView, true));
        containers.add(new TabContainer<>(3, "Payments", paymentTabView, true));
        containers.add(new TabContainer<>(4, "Create payments", createPaymentTabView, true));
        containers.add(new TabContainer<>(5, "Pool Mining", miningTabView, true));
        containers.add(new TabContainer<>(6, "Solo Mining", soloMiningTabView, false));
        loadWindow.setProgress(loadingCounter++);

        setContainers(containers);

        LOGGER.info("Generating frame ...");
        loadWindow.setProgress(loadingCounter++);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Start polling and updating the views
        LOGGER.info("Starting the rpc pollers ...");
        final RpcPoller<Status> statusPoller = new NoParamsRpcPoller<>(actionController.getWalletRpcController().getStatusExecutor(), 10000);
        final SynchronizationRpcPoller<BlockCount> syncPoller = new SynchronizationRpcPoller<>(actionController.getCoinRpcController().getBlockCountExecutor(), 10000);

        actionController.getCoinRpcController().addPollers(syncPoller);
        actionController.getWalletRpcController().addPollers(statusPoller);

        statusPoller.addObserver(syncPoller);
        statusPoller.addObserver(this);
        syncPoller.addObserver(this);
        loadWindow.setProgress(loadingCounter++);

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean synced = false;
                while (!synced) {
                    synced = syncPoller.isSynced();
                    if (synced) {
                        RpcPoller<Addresses> addressesPoller = new NoParamsRpcPoller<>(actionController.getWalletRpcController().getAddressesExecutor(), 60000);
                        TransactionItemsRpcPoller transactionsPoller = new TransactionItemsRpcPoller(actionController.getWalletRpcController().getTransactionsExecutor(), 5000);
                        UnconfirmedTransactionHashesRpcPoller unconfirmedTransactionHashesPoller = new UnconfirmedTransactionHashesRpcPoller(actionController.getWalletRpcController().getUnconfirmedTransactionHashesExecutor(), 60000);

                        actionController.getWalletRpcController().addPollers(addressesPoller);
                        actionController.getWalletRpcController().addPollers(transactionsPoller);
                        actionController.getWalletRpcController().addPollers(unconfirmedTransactionHashesPoller);

                        // Add observers
                        transactionsPoller.addObserver(transactionsTabView);
                        transactionsPoller.addObserver(statusTabView);
                        transactionsPoller.addObserver(paymentTabView);

                        unconfirmedTransactionHashesPoller.addObserver(statusTabView);
                        unconfirmedTransactionHashesPoller.addObserver(transactionsTabView);

                        statusPoller.addObserver(transactionsPoller);
                        statusPoller.addObserver(statusTabView);

                        addressesPoller.addObserver(addressesTabView);
                        addressesPoller.addObserver(miningTabView);
                        addressesPoller.addObserver(soloMiningTabView);
                        addressesPoller.addObserver(paymentTabView);
                        addressesPoller.addObserver(createPaymentTabView);
                        addressesPoller.addObserver(transactionsTabView);
                        addressesPoller.addObserver(transactionsPoller);
                        addressesPoller.addObserver(unconfirmedTransactionHashesPoller);
                    } else {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                LOGGER.info("Rpc pollers started.");

            }
        }).start();

        loadWindow.setProgress(loadingCounter++);
        loadWindow.setScreenVisible(false);
        //make sure the JFrame is visible
        this.setVisible(true);
    }

    private static void loadingFrame() {
        URL splashScreenLocation = Thread.currentThread().getContextClassLoader().getResource("splash.png");
        if (splashScreenLocation != null) {
            loadWindow = new SplashWindow(new ImageIcon(splashScreenLocation));
        }
        loadWindow.setLocationRelativeTo(null);
        loadWindow.setProgressMax(10);
        loadWindow.setScreenVisible(true);
    }

}
