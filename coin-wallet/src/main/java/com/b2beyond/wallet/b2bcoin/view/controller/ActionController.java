package com.b2beyond.wallet.b2bcoin.view.controller;

import com.b2beyond.wallet.b2bcoin.controler.CoinHttpController;
import com.b2beyond.wallet.b2bcoin.controler.CoinRpcController;
import com.b2beyond.wallet.b2bcoin.controler.DaemonController;
import com.b2beyond.wallet.b2bcoin.controler.WalletRpcController;
import com.b2beyond.wallet.b2bcoin.rpc.TransactionItemsRpcPoller;
import com.b2beyond.wallet.b2bcoin.util.B2BUtil;
import com.b2beyond.wallet.rpc.JsonRpcExecutor;
import com.b2beyond.wallet.rpc.RpcPoller;
import com.b2beyond.wallet.rpc.exception.KnownJsonRpcException;
import com.b2beyond.wallet.rpc.model.*;
import com.b2beyond.wallet.rpc.model.coin.BlockWrapper;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;


public class ActionController {

    private Logger LOGGER = Logger.getLogger(this.getClass());

    private PropertiesConfiguration applicationProperties;

    private DaemonController controller;
    private CoinRpcController coinRpcController;
    private CoinHttpController coinHttpController;
    private WalletRpcController walletRpcController;

    private List<RpcPoller> walletRpcPollers;

    private boolean oldChainIsSyncing;
    private boolean walletReset = false;
    private boolean isbeingReset;
    private boolean synced;

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    public ActionController(final DaemonController controller, CoinHttpController coinHttpController, WalletRpcController walletRpcController, CoinRpcController coinRpcController, PropertiesConfiguration applicationProperties) {
        this.controller = controller;
        this.coinHttpController = coinHttpController;
        this.coinRpcController = coinRpcController;
        this.walletRpcController = walletRpcController;

        this.applicationProperties = applicationProperties;
    }

    public void setWalletRpcPollers(List<RpcPoller> walletRpcPollers) {
        this.walletRpcPollers = walletRpcPollers;
    }

    public CoinHttpController getCoinHttpController() {
        return coinHttpController;
    }

    public void stopBackgroundProcessesBeforeReset() {
        coinRpcController.stop();
        walletRpcController.stop();
    }

    public void startBackgroundProcessesAfterReset() {
        coinRpcController.restart();
        walletRpcController.restart();
    }

    public Address createAddress() {
        try {
            return walletRpcController.getCreateAddressExecutor().execute(JsonRpcExecutor.EMPTY_PARAMS);
        } catch (KnownJsonRpcException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Address importAddress(AddressInput input) {
        try {
            return walletRpcController.getCreateAddressExecutor().execute(input.getParams());
        } catch (KnownJsonRpcException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BlockWrapper getBlockWrapper(String hash) {
        try {
            return coinRpcController.getBlockWrapperExecutor().execute("\"params\": {\"hash\": \"" + hash + "\"}");
        } catch (KnownJsonRpcException e) {
//            restartCoinDaemon();
        }
        return null;
    }

    public void exit() {
        LOGGER.info("ActionController.exit was called");
        // Save the wallet
//        try {
//            walletRpcController.getSaveExecutor().execute(JsonRpcExecutor.EMPTY_PARAMS);
//        } catch (KnownJsonRpcException e) {
//            e.printStackTrace();
//        }

        LOGGER.info("ActionController.exit was called");
        coinRpcController.stop();
        walletRpcController.stop();
        controller.stopWallet();
        controller.stopDaemon();
    }

    private void startOldDaemon() {
        controller.startOldDaemn();
    }

    public void restartCoinDaemon() {
        if (oldChainIsSyncing) {
            controller.startOldDaemn();
        } else {
            controller.restartDaemon();
        }
    }

    public void restartWalletDaemon() {
        if (oldChainIsSyncing) {
            controller.startOldDaemn();
        } else {
            controller.restartDaemon();
        }
    }

    private void stopCoinDaemon() {
        controller.stopDaemon();
    }

    public CoinRpcController getCoinRpcController() {
        return coinRpcController;
    }

    public WalletRpcController getWalletRpcController() {
        return walletRpcController;
    }

    public void startWallet() {
        if (B2BUtil.availableForConnection(9090)) {
            controller.startWallet();

            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (RpcPoller poller : walletRpcPollers) {
                getCoinRpcController().addPollers(poller);
            }
        }
    }

    public void startOldWallet() {
        if (B2BUtil.availableForConnection(9080)) {
            controller.startOldWallet();

            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (RpcPoller poller : walletRpcPollers) {
                getCoinRpcController().addPollers(poller);
            }
        }
    }

    public void stopWalletd() {
        if (!isbeingReset) {
            controller.stopWalletd();
            controller.stopOldWalletd();
        }
    }

    public void resetWallet() {
        if (!isbeingReset) {
            isbeingReset = true;
            try {
                walletRpcController.getResetExecutor().execute(JsonRpcExecutor.EMPTY_PARAMS);
            } catch (KnownJsonRpcException e) {
                e.printStackTrace();
            }
        }
    }

    public void resetWalletAndBlockChain() {
        try {
            isbeingReset = false;
            stopWalletd();
            startOldWallet();
            resetBlockChain(true);
            walletRpcController.getResetExecutor().execute(JsonRpcExecutor.EMPTY_PARAMS);

            for (RpcPoller poller : walletRpcPollers) {
                if (poller instanceof TransactionItemsRpcPoller) {
                    ((TransactionItemsRpcPoller) poller).reset();
                }
            }
        } catch (KnownJsonRpcException e) {
            e.printStackTrace();
        }
    }

    public void resetBlockChain(boolean old) {
        LOGGER.info("Delete block chain");
        LOGGER.debug("Command : " + B2BUtil.getDeleteBlockChainHomeCommand());

        oldChainIsSyncing = old;

        // STOP THE DAEMON
        stopCoinDaemon();

        // DELETE THE BLOCKCHAIN
        Process process;
        try {
            process = Runtime.getRuntime().exec(B2BUtil.getDeleteBlockChainHomeCommand());
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        try {
            LOGGER.info("Windows sleep : 5 seconds");
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        if (old) {
            startOldDaemon();
        } else {
            restartCoinDaemon();

            try {
                LOGGER.info("Wait a minute before resetting the wallet ...");
                Thread.sleep(60000 * 2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public SpendKeys getSpendKeys(String address) {
        try {
            return walletRpcController.getSpendKeysExecutor().execute("\"params\": {\"address\": \"" + address + "\"}");
        } catch (KnownJsonRpcException e) {
            e.printStackTrace();
        }


        return null;
    }

    public ViewSecretKey getPublicKey(String address) {
        try {
            return walletRpcController.getViewSecretKeyExecutor().execute("\"params\": {\"address\": \"" + address + "\"}");
        } catch (KnownJsonRpcException e) {
            e.printStackTrace();
        }


        return null;
    }

    public AddressBalance getBalance(String address) {
        LOGGER.info("Get address balance : " + address);

        try {
            return this.walletRpcController.getBalanceExecutor().execute("\"params\": {" +
                    "\"address\": \"" + address + "\"" +
                    "}");
        } catch (KnownJsonRpcException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Success deleteAddress(String address) {
        try {
            return this.walletRpcController.getDeleteAddressExecutor().execute("\"params\": {" +
                    "\"address\": \"" + address + "\"" +
                    "}");
        } catch (KnownJsonRpcException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isOldDaemonSynced(int value) {
        int stepOne = applicationProperties.getInt("step-one");
        return stepOne == value;
    }

    public boolean isOldWaletSynced(int value) {
        int stepOne = applicationProperties.getInt("step-one");
        return stepOne == value + 1;
    }

    public void setOldBlockchainSynced() {
        oldChainIsSyncing = false;
    }

}
