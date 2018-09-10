package com.b2beyond.wallet.b2bcoin.controler;

import com.b2beyond.wallet.http.HtmlExecutor;
import com.b2beyond.wallet.http.HttpPoller;
import com.b2beyond.wallet.rpc.CoinController;
import com.b2beyond.wallet.rpc.JsonRpcExecutor;
import com.b2beyond.wallet.rpc.RpcPoller;
import com.b2beyond.wallet.rpc.model.coin.BlockCount;
import com.b2beyond.wallet.rpc.model.coin.BlockHeaderWrapper;
import com.b2beyond.wallet.rpc.model.coin.BlockWrapper;
import com.b2beyond.wallet.rpc.model.coin.TransactionWrapper;

import java.util.ArrayList;
import java.util.List;


public class CoinHttpController {

    private HtmlExecutor<Long> heightExecutor;


    private List<HttpPoller<?>> pollers = new ArrayList<>();

    public CoinHttpController(String url) {
        heightExecutor = new HtmlExecutor<>(url);
    }

    public void addPollers(HttpPoller poller) {
        pollers.add(poller);
        new Thread(poller).start();
    }

    public void stop() {
        for (HttpPoller poller : pollers) {
            poller.stop();
        }
    }

    public void restart() {
        for (HttpPoller poller : pollers) {
            poller.start();
            new Thread(poller).start();
        }
    }

    public HtmlExecutor<Long> getHeightExecutor() {
        return heightExecutor;
    }

}
