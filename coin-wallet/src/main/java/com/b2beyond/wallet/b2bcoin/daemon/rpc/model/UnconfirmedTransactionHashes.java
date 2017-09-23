package com.b2beyond.wallet.b2bcoin.daemon.rpc.model;

import java.util.List;

/**
 * Created by oliviersinnaeve on 12/09/17.
 */
public class UnconfirmedTransactionHashes {

    private List<String> transactionHashes;

    public List<String> getTransactionHashes() {
        return transactionHashes;
    }

    public void setTransactionHashes(List<String> transactionHashes) {
        this.transactionHashes = transactionHashes;
    }
}
