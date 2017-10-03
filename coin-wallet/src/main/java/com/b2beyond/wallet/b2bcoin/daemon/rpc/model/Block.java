package com.b2beyond.wallet.b2bcoin.daemon.rpc.model;

/**
 * Created by oliviersinnaeve on 03/10/17.
 */
public class Block {

    private long alreadyGeneratedCoins;
    private long alreadyGeneratedTransactions;
    private long baseReward;
    private long difficulty;
    private String hash;
    private long height;

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

}
