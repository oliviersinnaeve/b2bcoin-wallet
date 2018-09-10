package com.b2beyond.wallet.http;

import com.b2beyond.wallet.rpc.JsonRpcExecutor;
import org.apache.log4j.Logger;

public class NoParamsHttpPoller<T> extends HttpPoller<T> {

    private Logger LOGGER = Logger.getLogger(this.getClass());

    public NoParamsHttpPoller(HtmlExecutor<T> executor, long delayInMilliseconds) {
        super(executor, delayInMilliseconds);
    }

    @Override
    public String getParams() {
        LOGGER.debug("Getting EMPTY_PARAMS from NoParamsHttpPoller");
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
