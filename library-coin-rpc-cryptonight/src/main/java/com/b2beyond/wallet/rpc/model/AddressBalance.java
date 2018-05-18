package com.b2beyond.wallet.rpc.model;

public class AddressBalance {

    private String address;

    private long availableBalance;
    private long lockedAmount;

    public AddressBalance() { }

    public AddressBalance(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(long availableBalance) {
        this.availableBalance = availableBalance;
    }

    public long getLockedAmount() {
        return lockedAmount;
    }

    public void setLockedAmount(long lockedAmount) {
        this.lockedAmount = lockedAmount;
    }
}
