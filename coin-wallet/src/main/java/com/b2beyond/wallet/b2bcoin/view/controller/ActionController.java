package com.b2beyond.wallet.b2bcoin.view.controller;

import com.b2beyond.wallet.b2bcoin.controler.CoinRpcController;
import com.b2beyond.wallet.b2bcoin.controler.DaemonController;
import com.b2beyond.wallet.b2bcoin.controler.WalletRpcController;
import com.b2beyond.wallet.rpc.JsonRpcExecutor;
import com.b2beyond.wallet.rpc.model.Address;
import com.b2beyond.wallet.rpc.model.AddressBalance;
import com.b2beyond.wallet.rpc.model.SpendKeys;
import com.b2beyond.wallet.rpc.model.Success;
import com.b2beyond.wallet.rpc.model.coin.BlockWrapper;
import com.b2beyond.wallet.rpc.exception.KnownJsonRpcException;
import com.b2beyond.wallet.b2bcoin.util.B2BUtil;
import org.apache.log4j.Logger;


public class ActionController {

    private Logger LOGGER = Logger.getLogger(this.getClass());

    private DaemonController controller;
    private CoinRpcController coinRpcController;
    private WalletRpcController walletRpcController;
    private PoolMiningController miningController;
    private SoloMiningController soloMiningController;


    public ActionController(final DaemonController controller, WalletRpcController walletRpcController, CoinRpcController coinRpcController) {
        this.controller = controller;
        this.coinRpcController = coinRpcController;
        this.walletRpcController = walletRpcController;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    if (B2BUtil.availableForConnection(controller.getDaemonPort())) {
                        controller.restartDaemon();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void setMiningController(PoolMiningController miningController) {
        this.miningController = miningController;
    }

    public void setSoloMiningController(SoloMiningController soloMiningController) {
        this.soloMiningController = soloMiningController;
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

    public BlockWrapper getBlockWrapper(String hash) {
        try {
            return coinRpcController.getBlockWrapperExecutor().execute("\"params\": {\"hash\": \"" + hash + "\"}");
        } catch (KnownJsonRpcException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void exit() {
        LOGGER.info("ActionController.exit was called");
        miningController.stopMining();
        soloMiningController.stopMining();
        coinRpcController.stop();
        // Save the wallet
        try {
            walletRpcController.getSaveExecutor().execute(JsonRpcExecutor.EMPTY_PARAMS);
        } catch (KnownJsonRpcException e) {
            e.printStackTrace();
        }
        walletRpcController.stop();
        controller.stop();
    }

    public void restartCoinDaemon() {
        controller.restartDaemon();
    }

    public CoinRpcController getCoinRpcController() {
        return coinRpcController;
    }

    public WalletRpcController getWalletRpcController() {
        return walletRpcController;
    }

    public void resetWallet() {
        try {
            walletRpcController.getResetExecutor().execute(JsonRpcExecutor.EMPTY_PARAMS);
        } catch (KnownJsonRpcException e) {
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
