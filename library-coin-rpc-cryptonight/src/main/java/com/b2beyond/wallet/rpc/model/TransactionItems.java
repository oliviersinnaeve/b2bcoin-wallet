package com.b2beyond.wallet.rpc.model;

import java.util.ArrayList;
import java.util.List;


public class TransactionItems {

    private String address;

    private String currencyName;

    private List<TransactionItem> items = new ArrayList<>();


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public List<TransactionItem> getItems() {
        return items;
    }

    public void setItems(List<TransactionItem> items) {
        this.items = items;
    }
}
