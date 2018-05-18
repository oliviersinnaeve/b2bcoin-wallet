package com.b2beyond.wallet.rpc.model;

import java.util.List;


public class Transaction {

    private String transactionHash; // hash of the transaction
    private long blockIndex; // number of the block that contains a transaction
    private long timestamp; // timestamp of the transaction
    private boolean isBase; // shows if the transaction is a coinbase transaction or not
    private long unlockTime; // height of the block when transaction is going to be available for spending
    private long amount; // amount of the transaction
    private long fee; // transaction fee
    private String extra; // string
    private String paymentId; // payment_id of the transaction (optional)
    private List<Transfer> transfers;


    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public long getBlockIndex() {
        return blockIndex;
    }

    public void setBlockIndex(long blockIndex) {
        this.blockIndex = blockIndex;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isBase() {
        return isBase;
    }

    public void setIsBase(boolean isBase) {
        this.isBase = isBase;
    }

    public long getUnlockTime() {
        return unlockTime;
    }

    public void setUnlockTime(long unlockTime) {
        this.unlockTime = unlockTime;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getFee() {
        return fee;
    }

    public void setFee(long fee) {
        this.fee = fee;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public List<Transfer> getTransfers() {
        return transfers;
    }

    public void setTransfers(List<Transfer> transfers) {
        this.transfers = transfers;
    }
}
