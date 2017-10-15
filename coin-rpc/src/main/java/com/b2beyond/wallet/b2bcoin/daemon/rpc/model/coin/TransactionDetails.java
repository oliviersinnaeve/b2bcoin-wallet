package com.b2beyond.wallet.b2bcoin.daemon.rpc.model.coin;


class TransactionDetails {

    private long amount_out;
    private long fee;
    private String hash;
    private int mixin;
    private String paymentId;
    private int size;


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

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public int getMixin() {
        return mixin;
    }

    public void setMixin(int mixin) {
        this.mixin = mixin;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
