package com.b2beyond.wallet.b2bcoin.rpc;


import com.b2beyond.wallet.rpc.model.Addresses;
import com.b2beyond.wallet.rpc.model.Status;
import com.b2beyond.wallet.rpc.model.TransactionItems;
import com.b2beyond.wallet.rpc.JsonRpcExecutor;
import com.b2beyond.wallet.rpc.RpcPoller;

import java.util.Observable;
import java.util.Observer;


public class TransactionItemsRpcPoller extends RpcPoller<TransactionItems> implements Observer {

    private long firstBlockCount = 0;
    private long knowBlockCount = 0;
    private static final long BLOCKS_TO_FETCH = 10000;


    private Addresses addresses = new Addresses();


    public TransactionItemsRpcPoller(JsonRpcExecutor<TransactionItems> executor, long delayInMilliseconds) {
        super(executor, delayInMilliseconds);
    }

    @Override
    public String getParams() {
        String params;

        if (this.addresses.getAddresses().size() > 0) {
            params = "\"params\":{\n" +
                    "    \"firstBlockIndex\":" + firstBlockCount + ",\n" +
                    "    \"blockCount\":" + knowBlockCount + "," +
                    "    \"addresses\":[\n";

            int index = 0;
            for (String key : addresses.getAddresses()) {
                params += "        \"" + key + "\"";

                if (index < addresses.getAddresses().size() - 1) {
                    params += ",\n";
                }
                index++;
            }
            params += "    ]}";
        } else {
            params = JsonRpcExecutor.EMPTY_PARAMS;
        }

        return params;
    }

    public void reset() {
        this.firstBlockCount = 0;
    }

    @Override
    public void update(Observable o, Object data) {
        if (data instanceof Addresses) {
            this.addresses = (Addresses)data;
        }
        if (data instanceof Status) {
            Status status = (Status)data;
            if (knowBlockCount != status.getBlockCount()) {
                knowBlockCount = status.getBlockCount();
            }
        }
    }

    @Override
    public boolean isExecuted() {
        return false;
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void updateOnSucceed(TransactionItems data) {
        firstBlockCount = knowBlockCount;
    }
}
