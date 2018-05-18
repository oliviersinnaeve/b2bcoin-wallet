package com.b2beyond.wallet.b2bcoin.rpc;

import com.b2beyond.wallet.rpc.model.coin.BlockCount;
import com.b2beyond.wallet.rpc.model.Status;
import com.b2beyond.wallet.rpc.JsonRpcExecutor;
import com.b2beyond.wallet.rpc.NoParamsRpcPoller;
import org.apache.log4j.Logger;

import java.util.Observable;
import java.util.Observer;

public class SynchronizationRpcPoller<T> extends NoParamsRpcPoller<T> implements Observer {

    private Logger LOGGER = Logger.getLogger(this.getClass());

    private long knowBlockCount;
    private boolean blockChainSynchronized;

    public SynchronizationRpcPoller(JsonRpcExecutor<T> executor, long delayInMilliseconds) {
        super(executor, delayInMilliseconds);
        addObserver(this);
    }

    @Override
    public String getParams() {
        LOGGER.debug("Getting EMPTY_PARAMS from SynchronizationRecPoller");
        return JsonRpcExecutor.EMPTY_PARAMS;
    }

    @Override
    public void update(Observable o, Object data) {
        if (data instanceof Status) {
            Status viewData = (Status) data;
            knowBlockCount = viewData.getKnownBlockCount();
        }
        if (data instanceof BlockCount) {
            BlockCount blockCount = (BlockCount) data;
            if (blockCount.getCount() == knowBlockCount) {
                blockChainSynchronized = true;
            }
        }
    }


    public boolean isSynced() {
        return blockChainSynchronized;
    }

}
