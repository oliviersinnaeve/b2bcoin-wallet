package com.b2beyond.wallet.b2bcoin.daemon.rpc.model;

public class Address {

    private String address;


    public Address() {}

    public Address(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
