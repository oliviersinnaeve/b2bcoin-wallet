package com.b2beyond.wallet.http;

import com.b2beyond.wallet.rpc.exception.KnownJsonRpcException;
import com.b2beyond.wallet.rpc.model.Error;
import org.apache.log4j.Logger;

import java.util.Observable;


public abstract class HttpPoller<T> extends Observable implements Runnable {

    private Logger LOGGER = Logger.getLogger(this.getClass());

    private HtmlExecutor<T> executor;

    private long delayInMilliseconds;

    private boolean running;

    private boolean executed = true;

    public HttpPoller(HtmlExecutor<T> executor, long delayInMilliseconds) {
        this.executor = executor;
        this.delayInMilliseconds = delayInMilliseconds;

        this.running = true;
    }

    @Override
    public void run() {
        while (running) {
            if (getParams() != null && isActive()) {
                if (!isExecuted()) {
                    executed = true;

                    T value = null;
                    try {
                        value = executor.execute();
                    } catch (KnownJsonRpcException e) {
                        Error error = e.getError();
                        setChanged();
                        notifyObservers(error);
                    }
                    if (value != null) {
                        updateOnSucceed(value);
                        setChanged();
                        notifyObservers(value);
                    }
                } else {
                    LOGGER.debug("Does not need execution ...");
                }
            } else {
                LOGGER.error("Null parameters on RpcPoller");
            }

            try {
                Thread.sleep(delayInMilliseconds);
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
        }
    }

    public void stop() {
        this.running = false;
    }

    public abstract String getParams();

    public abstract void updateOnSucceed(T data);

    public void start() {
        this.running = true;
    }

    public boolean isExecuted() {
        return executed;
    }

    public void setExecuted(boolean executed) {
        this.executed = executed;
    }

    /**
     * Defines wheter the rpc call should execute or not !!
     *
     * @return boolean true if the executor should execute
     */
    public abstract boolean isActive();


}
