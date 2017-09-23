package com.b2beyond.wallet.b2bcoin.controler;

import com.b2beyond.wallet.b2bcoin.daemon.rpc.JsonRpcExecutor;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.RpcPoller;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.BlockCount;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class CoinRpcController {

    private JsonRpcExecutor<BlockCount> blockCountExecutor;

    private List<RpcPoller<?>> pollers = new ArrayList<>();

    public CoinRpcController(CoinProperties coinProperties) {
        Properties daemonProperties = coinProperties.getCoinProperties();
        String baseUrl = daemonProperties.getProperty("coin-daemon-base-url");

        blockCountExecutor = new JsonRpcExecutor<>(baseUrl + "/json_rpc", "getblockcount", BlockCount.class);
    }

    public void addPollers(RpcPoller poller) {
        pollers.add(poller);
        new Thread(poller).start();
    }

    public void stop() {
        for (RpcPoller poller : pollers) {
            poller.stop();
        }
    }

    public void restart() {
        for (RpcPoller poller : pollers) {
            poller.setRunning();
            new Thread(poller).start();
        }
    }

    public JsonRpcExecutor<BlockCount> getBlockCountExecutor() {
        return blockCountExecutor;
    }

}
