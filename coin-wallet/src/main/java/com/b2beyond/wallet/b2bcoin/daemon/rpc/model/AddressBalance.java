package com.b2beyond.wallet.b2bcoin.daemon.rpc.model;

public class AddressBalance {

    private String address;

    private long availableBalance;
    private long lockedAmount;


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
