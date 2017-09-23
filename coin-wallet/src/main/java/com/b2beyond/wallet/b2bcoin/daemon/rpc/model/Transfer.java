package com.b2beyond.wallet.b2bcoin.daemon.rpc.model;

public class Transfer {

    private String address;
    private long amount;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
