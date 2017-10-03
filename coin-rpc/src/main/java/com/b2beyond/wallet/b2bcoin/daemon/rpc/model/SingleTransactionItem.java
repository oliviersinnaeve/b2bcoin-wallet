package com.b2beyond.wallet.b2bcoin.daemon.rpc.model;

public class SingleTransactionItem {

    private Transaction transaction;

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
