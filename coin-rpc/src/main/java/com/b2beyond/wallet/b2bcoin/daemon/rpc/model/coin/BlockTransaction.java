package com.b2beyond.wallet.b2bcoin.daemon.rpc.model.coin;

public class BlockTransaction {

    private String hash; // hash of the transaction
    private long size; // block size of the transaction
    private long amount_out; // amount of the transaction
    private long fee; // transaction fee


    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getAmount_out() {
        return amount_out;
    }

    public void setAmount_out(long amount_out) {
        this.amount_out = amount_out;
    }

    public long getFee() {
        return fee;
    }

    public void setFee(long fee) {
        this.fee = fee;
    }
}
