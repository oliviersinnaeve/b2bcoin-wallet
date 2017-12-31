package com.b2beyond.wallet.rpc.model;

public class Address {

    private String address;

    private String standard_address;

    private String payment_id;

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

    public String getStandard_address() {
        return standard_address;
    }

    public void setStandard_address(String standard_address) {
        this.standard_address = standard_address;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }
}
