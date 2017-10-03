package com.b2beyond.wallet.b2bcoin.daemon.rpc;


import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.Addresses;
import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.UnconfirmedTransactionHashes;

import java.util.Observable;
import java.util.Observer;


public class UnconfirmedTransactionHashesRpcPoller extends RpcPoller<UnconfirmedTransactionHashes> implements Observer {

    private Addresses addresses = new Addresses();


    public UnconfirmedTransactionHashesRpcPoller(JsonRpcExecutor<UnconfirmedTransactionHashes> executor, long delayInMilliseconds) {
        super(executor, delayInMilliseconds);
    }

    @Override
    public String getParams() {
        String params;

        if (this.addresses.getAddresses().size() > 0) {
            params = "\"params\":{\n" +
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
    public void updateOnSucceed(UnconfirmedTransactionHashes data) {
    }
}
