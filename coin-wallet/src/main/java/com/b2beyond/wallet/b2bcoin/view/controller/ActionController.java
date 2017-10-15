package com.b2beyond.wallet.b2bcoin.view.controller;

import com.b2beyond.wallet.b2bcoin.controler.CoinRpcController;
import com.b2beyond.wallet.b2bcoin.controler.DaemonController;
import com.b2beyond.wallet.b2bcoin.controler.WalletRpcController;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.JsonRpcExecutor;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.Address;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.coin.BlockWrapper;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.exception.KnownJsonRpcException;
import org.apache.log4j.Logger;


public class ActionController {

    private Logger LOGGER = Logger.getLogger(this.getClass());

    private DaemonController controller;
    private CoinRpcController coinRpcController;
    private WalletRpcController walletRpcController;
    private PoolMiningController miningController;
    private SoloMiningController soloMiningController;


    public ActionController(DaemonController controller, WalletRpcController walletRpcController, CoinRpcController coinRpcController) {
        this.controller = controller;
        this.coinRpcController = coinRpcController;
        this.walletRpcController = walletRpcController;
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

    public CoinRpcController getCoinRpcController() {
        return coinRpcController;
    }

    public WalletRpcController getWalletRpcController() {
        return walletRpcController;
    }
}
