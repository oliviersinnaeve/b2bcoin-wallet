package com.b2beyond.wallet.rpc.model;


public class Status {

    private int peerCount;
    private long blockCount;
    private String lastBlockHash;
    private long knownBlockCount;

    private String top_block_hash;
    private int height;


    public int getPeerCount() {
        return peerCount;
    }

    public void setPeerCount(int peerCount) {
        this.peerCount = peerCount;
    }

    public long getBlockCount() {
        return blockCount;
    }

    public void setBlockCount(long blockCount) {
        this.blockCount = blockCount;
    }

    public String getLastBlockHash() {
        if (lastBlockHash != null) {
            return lastBlockHash;
        } else {
            return top_block_hash;
        }
    }

    public void setLastBlockHash(String lastBlockHash) {
        this.lastBlockHash = lastBlockHash;
    }

    public void setTop_block_hash(String top_block_hash) {
        this.top_block_hash = top_block_hash;
    }

    public long getKnownBlockCount() {
        if (knownBlockCount > 0) {
            return knownBlockCount;
        } else {
            return height;
        }
    }

    public void setKnownBlockCount(long knownBlockCount) {
        this.knownBlockCount = knownBlockCount;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
