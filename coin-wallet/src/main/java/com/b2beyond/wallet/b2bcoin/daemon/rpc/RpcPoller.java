package com.b2beyond.wallet.b2bcoin.daemon.rpc;

import java.util.Observable;


public abstract class RpcPoller<T> extends Observable implements Runnable {

    private JsonRpcExecutor<T> executor;

    private long delayInMilliseconds;

    private boolean running;

    private T currentValue;

    public RpcPoller(JsonRpcExecutor<T> executor, long delayInMilliseconds) {
        this.executor = executor;
        this.delayInMilliseconds = delayInMilliseconds;

        this.running = true;
    }

    @Override
    public void run() {
        while (running) {
            if (getParams() != null) {
                executor.setParams(getParams());

                T value = executor.execute();
                if (value != null) {
                    currentValue = value;
                    updateOnSucceed(value);
                    setChanged();
                    notifyObservers(value);
                }
            } else {
                System.out.println("Null parameters on RpcPoller");
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

}
