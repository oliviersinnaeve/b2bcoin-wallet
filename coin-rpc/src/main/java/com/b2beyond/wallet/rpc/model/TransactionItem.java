package com.b2beyond.wallet.rpc.model;

import java.util.ArrayList;
import java.util.List;


public class TransactionItem {

    private String blockHash;
    private List<Transaction> transactions = new ArrayList<>();


    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
