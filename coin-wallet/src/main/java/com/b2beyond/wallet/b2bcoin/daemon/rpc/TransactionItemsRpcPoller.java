package com.b2beyond.wallet.b2bcoin.daemon.rpc;


import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.Addresses;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.Status;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.TransactionItems;

import java.util.Observable;
import java.util.Observer;


public class TransactionItemsRpcPoller extends RpcPoller<TransactionItems> implements Observer {

    private String params;
    private long firstBlockCount = 1;
    private static final long blockCount = 10000;

    private Addresses addresses = new Addresses();


    public TransactionItemsRpcPoller(JsonRpcExecutor<TransactionItems> executor, long delayInMilliseconds) {
        super(executor, delayInMilliseconds);
    }

    @Override
    public String getParams() {
        if (this.addresses.getAddresses().size() > 0) {
            params = "\"params\":{\n" +
                    "    \"firstBlockIndex\":" + firstBlockCount + ",\n" +
                    "    \"blockCount\":" + blockCount + "," +
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
//            if (status.getKnownBlockCount() > firstBlockCount + 1) {
//                start();
//            } else {
//                stop();
//            }
        }
    }

    @Override
    public void updateOnSucceed(TransactionItems data) {
        firstBlockCount += data.getItems().size();
    }
}
