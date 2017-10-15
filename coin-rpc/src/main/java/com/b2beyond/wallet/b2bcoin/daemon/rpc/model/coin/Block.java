package com.b2beyond.wallet.b2bcoin.daemon.rpc.model.coin;

import java.util.List;

/**
 * Created by oliviersinnaeve on 03/10/17.
 */
public class Block {

    private long alreadyGeneratedCoins;
    private long alreadyGeneratedTransactions;
    private long baseReward;
    private long blockSize;
    private int depth;
    private long difficulty;
    private String hash;
    private long height;
    private int major_version;
    private int minor_version;
    private long nonce;
    private boolean orphan_status;
    private int penalty;
    private String prev_hash;
    private long reward;
    private int sizeMedian;
    private long timestamp;
    private long totalFeeAmount;
    private List<BlockTransaction> transactions;
    private long transactionsCumulativeSize;


    public long getAlreadyGeneratedCoins() {
        return alreadyGeneratedCoins;
    }

    public void setAlreadyGeneratedCoins(long alreadyGeneratedCoins) {
        this.alreadyGeneratedCoins = alreadyGeneratedCoins;
    }

    public long getAlreadyGeneratedTransactions() {
        return alreadyGeneratedTransactions;
    }

    public void setAlreadyGeneratedTransactions(long alreadyGeneratedTransactions) {
        this.alreadyGeneratedTransactions = alreadyGeneratedTransactions;
    }

    public long getBaseReward() {
        return baseReward;
    }

    public void setBaseReward(long baseReward) {
        this.baseReward = baseReward;
    }

    public long getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(long blockSize) {
        this.blockSize = blockSize;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public long getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(long difficulty) {
        this.difficulty = difficulty;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public int getMajor_version() {
        return major_version;
    }

    public void setMajor_version(int major_version) {
        this.major_version = major_version;
    }

    public int getMinor_version() {
        return minor_version;
    }

    public void setMinor_version(int minor_version) {
        this.minor_version = minor_version;
    }

    public long getNonce() {
        return nonce;
    }

    public void setNonce(long nonce) {
        this.nonce = nonce;
    }

    public boolean isOrphan_status() {
        return orphan_status;
    }

    public void setOrphan_status(boolean orphan_status) {
        this.orphan_status = orphan_status;
    }

    public int getPenalty() {
        return penalty;
    }

    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }

    public String getPrev_hash() {
        return prev_hash;
    }

    public void setPrev_hash(String prev_hash) {
        this.prev_hash = prev_hash;
    }

    public long getReward() {
        return reward;
    }

    public void setReward(long reward) {
        this.reward = reward;
    }

    public int getSizeMedian() {
        return sizeMedian;
    }

    public void setSizeMedian(int sizeMedian) {
        this.sizeMedian = sizeMedian;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTotalFeeAmount() {
        return totalFeeAmount;
    }

    public void setTotalFeeAmount(long totalFeeAmount) {
        this.totalFeeAmount = totalFeeAmount;
    }

    public List<BlockTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<BlockTransaction> transactions) {
        this.transactions = transactions;
    }

    public long getTransactionsCumulativeSize() {
        return transactionsCumulativeSize;
    }

    public void setTransactionsCumulativeSize(long transactionsCumulativeSize) {
        this.transactionsCumulativeSize = transactionsCumulativeSize;
    }
}
