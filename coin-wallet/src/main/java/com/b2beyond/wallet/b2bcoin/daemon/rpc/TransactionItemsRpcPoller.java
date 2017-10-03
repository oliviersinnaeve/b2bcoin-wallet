package com.b2beyond.wallet.b2bcoin.daemon.rpc;


import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.Addresses;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.Status;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.TransactionItems;

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
                    "    \"blockCount\":" + BLOCKS_TO_FETCH + "," +
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
            params = "\"params\":{}";
        }

        return params;
    }

    @Override
    public void update(Observable o, Object data) {
        if (data instanceof Addresses) {
            this.addresses = (Addresses)data;
        }
        if (data instanceof Status) {
            Status status = (Status)data;
            if (isExecuted()) {
                knowBlockCount = status.getKnownBlockCount();
                setExecuted(false);
            }
        }
    }


    @Override
    public boolean isActive() {
        return firstBlockCount < knowBlockCount - 1;
    }

    @Override
    public void updateOnSucceed(TransactionItems data) {
        firstBlockCount += data.getItems().size();
    }
}
