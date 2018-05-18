package com.b2beyond.wallet.rpc;

import org.apache.log4j.Logger;

public class NoParamsRpcPoller<T> extends RpcPoller<T> {

    private Logger LOGGER = Logger.getLogger(this.getClass());

    public NoParamsRpcPoller(JsonRpcExecutor<T> executor, long delayInMilliseconds) {
        super(executor, delayInMilliseconds);
    }

    @Override
    public String getParams() {
        LOGGER.debug("Getting EMPTY_PARAMS from NoParamsRpcPoller");
        return JsonRpcExecutor.EMPTY_PARAMS;
    }

    @Override
    public void updateOnSucceed(T nothing) { }

    @Override
    public boolean isExecuted() {
        return false;
    }

    @Override
    public boolean isActive() {
        return true;
    }
}
