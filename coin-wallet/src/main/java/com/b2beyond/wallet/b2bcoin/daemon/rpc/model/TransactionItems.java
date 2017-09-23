package com.b2beyond.wallet.b2bcoin.daemon.rpc.model;

import java.util.ArrayList;
import java.util.List;


public class TransactionItems {

    private List<TransactionItem> items = new ArrayList<>();


    public List<TransactionItem> getItems() {
        return items;
    }

    public void setItems(List<TransactionItem> items) {
        this.items = items;
    }
}
