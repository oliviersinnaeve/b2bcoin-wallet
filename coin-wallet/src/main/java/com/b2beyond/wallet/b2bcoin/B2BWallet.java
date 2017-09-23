package com.b2beyond.wallet.b2bcoin;

import com.b2beyond.wallet.b2bcoin.controler.CoinDaemonProperties;
import com.b2beyond.wallet.b2bcoin.controler.CoinProperties;
import com.b2beyond.wallet.b2bcoin.controler.CoinRpcController;
import com.b2beyond.wallet.b2bcoin.controler.DaemonController;
import com.b2beyond.wallet.b2bcoin.controler.WalletDaemonProperties;
import com.b2beyond.wallet.b2bcoin.controler.WalletProperties;
import com.b2beyond.wallet.b2bcoin.controler.WalletRpcController;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.NoParamsRpcPoller;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.RpcPoller;
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
import org.apache.log4j.Logger;

import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import java.awt.Color;
import java.awt.SystemColor;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;


public class B2BWallet {

    private Logger LOGGER = Logger.getLogger(this.getClass());

    private ActionController actionController;

    public WalletProperties walletProperties;
    public CoinProperties coinProperties;

    public WalletDaemonProperties walletDaemonProperties;
    public CoinDaemonProperties coinDaemonProperties;

    private SplashWindow loadWindow;
    private int loadingCounter = 1;

    public static BigDecimal DIVIDE_BY = new BigDecimal("1000000000000");
    public static boolean DEV = true;

    public static void main(String[] args) {
        new B2BWallet();
    }

    public B2BWallet() {
        UIManager.put("Table.foreground",
                new ColorUIResource(Color.black));
        UIManager.put("Table.background",
                new ColorUIResource(SystemColor.controlShadow));

        if (LOGGER.isDebugEnabled()) {
            Properties p = System.getProperties();
            Enumeration keys = p.keys();
            while (keys.hasMoreElements()) {
                String key = (String)keys.nextElement();
                String value = (String)p.get(key);
                LOGGER.info(key + ": " + value);
            }
        }
        loadingFrame();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                actionController.exit();
            }
        });

        walletProperties = new WalletProperties();
        loadWindow.setProgress(loadingCounter++);

        coinProperties = new CoinProperties();
        loadWindow.setProgress(loadingCounter++);

        walletDaemonProperties = new WalletDaemonProperties();
        loadWindow.setProgress(loadingCounter++);

        coinDaemonProperties = new CoinDaemonProperties();
        loadWindow.setProgress(loadingCounter++);
        LOGGER.info("Properties loaded, wallet can get started");

        DaemonController coinDaemon = new DaemonController(
                coinProperties.getCoinProperties(),
                walletDaemonProperties,
                B2BUtil.getOperatingSystemType());
        WalletRpcController walletRpcController = new WalletRpcController(coinProperties);
        CoinRpcController coinRpcController = new CoinRpcController(coinProperties);
        actionController = new ActionController(coinDaemon, walletRpcController, coinRpcController);
        loadWindow.setProgress(loadingCounter++);


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (availableForConnection(9090) && availableForConnection(39155) && availableForConnection(39156)) {
                    LOGGER.info("Still Loading the wallet daemon ...");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                loadWindow.setProgress(loadingCounter++);
            }
        }).start();

        //viewSecretKey = walletRpcController.getViewSecretKeyExecutor().execute().getViewSecretKey();


        LOGGER.info("Starting controllers ...");
        AddressesController addressesController = new AddressesController(
                walletRpcController.getCreateAddressExecutor(),
                walletRpcController.getBalanceExecutor());
        PaymentController paymentController = new PaymentController(
                walletRpcController.getPaymentExecutor());
        PoolMiningController miningController = new PoolMiningController(
                coinProperties.getCoinProperties(),
                B2BUtil.getOperatingSystemType());
        SoloMiningController soloMiningController = new SoloMiningController(
                coinProperties.getCoinProperties(),
                walletDaemonProperties,
                B2BUtil.getOperatingSystemType());
        LOGGER.info("Controllers started.");

        LOGGER.info("Creating tab view instances ...");
        StatusTabView statusTabView = new StatusTabView(actionController, walletRpcController.getResetExecutor(), walletRpcController.getTransactionExecutor());
        TransactionsTabView transactionsTabView = new TransactionsTabView(walletRpcController.getTransactionExecutor());
        AddressesTabView addressesTabView = new AddressesTabView(addressesController);
        PaymentTabView paymentTabView = new PaymentTabView();
        CreatePaymentTabView createPaymentTabView = new CreatePaymentTabView(paymentController);
        MiningTabView miningTabView = new MiningTabView(miningController);
        MiningTabView soloMiningTabView = new MiningTabView(soloMiningController);

        loadWindow.setProgress(loadingCounter++);
        actionController.setMiningController(miningController);
        actionController.setSoloMiningController(soloMiningController);

        MenuBar menuBar = new MenuBar(actionController);
        List<TabContainer> containers = new ArrayList<>();
        containers.add(new TabContainer<>(0, "Overview", statusTabView, true, new ImageIcon("/Users/oliviersinnaeve/Pictures/stats-icon.png")));
        containers.add(new TabContainer<>(1, "Addresses", addressesTabView, true));
        containers.add(new TabContainer<>(2, "Transactions", transactionsTabView, true));
        containers.add(new TabContainer<>(3, "Payments", paymentTabView, true));
        containers.add(new TabContainer<>(4, "Create payments", createPaymentTabView, true));
        containers.add(new TabContainer<>(5, "Pool Mining", miningTabView, true));
        containers.add(new TabContainer<>(6, "Solo Mining", soloMiningTabView, false));

        LOGGER.info("Generating frame ...");
        MainFrame guiFrame = new MainFrame(menuBar, containers, walletProperties);
        loadWindow.setProgress(loadingCounter++);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Start polling and updating the views
        LOGGER.info("Starting the rpc pollers ...");
        RpcPoller<Status> statusPoller = new NoParamsRpcPoller<>(walletRpcController.getStatusExecutor(), 5000);

        // TODO take 1 and 2 into account !!!


        // TODO 1 -  Status block count / available blocks need to do the trigger,
        // TODO 1 -  not the polling,that is to memory and cpu intensive !!!!!!!

        // TODO 1 -  MOVE TO ONE POLLER - STATUS, based on that one trigger other observers that know of the status type.
        // TODO 1 -  On adding/create address call observers as well !!

        // TODO 1 -  POINT MADE, change architecture !!


        // TODO 2 - squeeze in un confirmed transactions into the transaction panel !!!

        RpcPoller<Addresses> addressesPoller = new NoParamsRpcPoller<>(walletRpcController.getAddressesExecutor(), 5000);
        RpcPoller<BlockCount> syncPoller = new NoParamsRpcPoller<>(coinRpcController.getBlockCountExecutor(), 5000);
        TransactionItemsRpcPoller transactionsPoller = new TransactionItemsRpcPoller(walletRpcController.getTransactionsExecutor(), 5000);
        UnconfirmedTransactionHashesRpcPoller unconfirmedTransactionHashesPoller = new UnconfirmedTransactionHashesRpcPoller(walletRpcController.getUnconfirmedTransactionHashesExecutor(), 5000);
        loadWindow.setProgress(loadingCounter++);


        walletRpcController.addPollers(statusPoller);
        walletRpcController.addPollers(addressesPoller);
        walletRpcController.addPollers(transactionsPoller);
        walletRpcController.addPollers(unconfirmedTransactionHashesPoller);
        coinRpcController.addPollers(syncPoller);
        loadWindow.setProgress(loadingCounter++);

        // Add observers
        transactionsPoller.addObserver(transactionsTabView);
        transactionsPoller.addObserver(statusTabView);
        transactionsPoller.addObserver(paymentTabView);

        unconfirmedTransactionHashesPoller.addObserver(statusTabView);
        unconfirmedTransactionHashesPoller.addObserver(transactionsTabView);

        statusPoller.addObserver(guiFrame);
        statusPoller.addObserver(statusTabView);

        addressesPoller.addObserver(addressesTabView);
        addressesPoller.addObserver(miningTabView);
        addressesPoller.addObserver(soloMiningTabView);
        addressesPoller.addObserver(paymentTabView);
        addressesPoller.addObserver(createPaymentTabView);
        addressesPoller.addObserver(transactionsTabView);
        addressesPoller.addObserver(transactionsPoller);
        addressesPoller.addObserver(unconfirmedTransactionHashesPoller);

        syncPoller.addObserver(guiFrame);
        loadWindow.setProgress(loadingCounter++);

        LOGGER.info("Rpc pollers started.");

        loadWindow.setScreenVisible(false);
        //make sure the JFrame is visible
        guiFrame.setVisible(true);
    }

    private void loadingFrame() {
        URL splashScreenLocation = Thread.currentThread().getContextClassLoader().getResource("splash.png");
        if (splashScreenLocation != null) {
            loadWindow = new SplashWindow(new ImageIcon(splashScreenLocation));
        }
        loadWindow.setLocationRelativeTo(null);
        loadWindow.setProgressMax(10);
        loadWindow.setScreenVisible(true);
    }

    private boolean availableForConnection(int port) {
        try (Socket ignored = new Socket("localhost", port)) {
            return false;
        } catch (IOException ignored) {
            return true;
        }
    }

}
