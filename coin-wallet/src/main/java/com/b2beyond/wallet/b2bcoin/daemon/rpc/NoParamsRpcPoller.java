package com.b2beyond.wallet.b2bcoin.daemon.rpc;

public class NoParamsRpcPoller<T> extends RpcPoller<T> {


    public NoParamsRpcPoller(JsonRpcExecutor<T> executor, long delayInMilliseconds) {
        super(executor, delayInMilliseconds);
    }

    @Override
    public String getParams() {
        return "\"params\": {}";
    }

    @Override
    public void updateOnSucceed(T nothing) { }

}
