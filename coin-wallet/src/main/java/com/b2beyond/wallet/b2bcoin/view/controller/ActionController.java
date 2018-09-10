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
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;


public class ActionController {

    private Logger LOGGER = Logger.getLogger(this.getClass());

    private DaemonController controller;
    private CoinRpcController coinRpcController;
    private CoinHttpController coinHttpController;
    private WalletRpcController walletRpcController;

    private List<RpcPoller> walletRpcPollers;

    private boolean synced;

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    public ActionController(final DaemonController controller, CoinHttpController coinHttpController, WalletRpcController walletRpcController, CoinRpcController coinRpcController) {
        this.controller = controller;
        this.coinHttpController = coinHttpController;
        this.coinRpcController = coinRpcController;
        this.walletRpcController = walletRpcController;
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
            restartCoinDaemon();
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

    public void restartCoinDaemon() {
        controller.restartDaemon();
    }

//    public void startCoinDaemon() {
//        controller.startDaemon();
//    }

    public void stopCoinDaemon() {
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
            // Add pollers if wallet rpc port is available
            for (RpcPoller poller : walletRpcPollers) {
                getCoinRpcController().addPollers(poller);
            }
        }
//        }
    }

    public void stopWallet() {
        controller.stopWallet();
    }

    public void resetWallet() {
        try {
            //controller.stopDaemon();
            walletRpcController.getResetExecutor().execute(JsonRpcExecutor.EMPTY_PARAMS);

            for (RpcPoller poller : walletRpcPollers) {
                if (poller instanceof TransactionItemsRpcPoller) {
                    ((TransactionItemsRpcPoller) poller).reset();
                }
            }

            //controller.startDaemon();
        } catch (KnownJsonRpcException e) {
            e.printStackTrace();
        }
    }

    public void resetBlockChain() {
        LOGGER.info("Delete block chain");
        LOGGER.debug("Command : " + B2BUtil.getDeleteBlockChainHomeCommand());

        stopCoinDaemon();
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

        restartCoinDaemon();

        try {
            LOGGER.info("Wait a minute before resetting the wallet ...");
            Thread.sleep(60000);
            resetWallet();
        } catch (InterruptedException e) {
            e.printStackTrace();
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

}
