package com.b2beyond.wallet.b2bcoin.rpc;


import com.b2beyond.wallet.rpc.model.Addresses;
import com.b2beyond.wallet.rpc.model.Status;
import com.b2beyond.wallet.rpc.model.Transaction;
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

    @Override
    public void update(Observable o, Object data) {
        if (data instanceof Addresses) {
            this.addresses = (Addresses)data;
        }
        if (data instanceof Status) {
            Status status = (Status)data;
            knowBlockCount = status.getBlockCount();
            if (knowBlockCount != status.getKnownBlockCount() - 1) {
               setExecuted(false);
            }
        }
    }


    @Override
    public boolean isActive() {
        return firstBlockCount < knowBlockCount;
    }

    public void reset() {
        firstBlockCount = 1;
    }

    @Override
    public void updateOnSucceed(TransactionItems data) {
//        if (data.getItems().get(data.getItems().size() - 1).getTransactions().size() > 1) {
//            for (Transaction item : data.getItems().get(data.getItems().size() - 1).getTransactions()) {
//                if (item.getBlockIndex() > firstBlockCount) {
//                    firstBlockCount = item.getBlockIndex();
//                }
//            }
//        } else {
//          firstBlockCount += data.getItems().size();
//        }
        firstBlockCount = knowBlockCount;
    }
}
