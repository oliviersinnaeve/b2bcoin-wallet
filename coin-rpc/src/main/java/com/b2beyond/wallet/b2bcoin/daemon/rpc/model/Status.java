package com.b2beyond.wallet.b2bcoin.daemon.rpc.model;


public class Status {

    private int peerCount;
    private long blockCount;
    private String lastBlockHash;
    private long knownBlockCount;

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
        return lastBlockHash;
    }

    public void setLastBlockHash(String lastBlockHash) {
        this.lastBlockHash = lastBlockHash;
    }

    public long getKnownBlockCount() {
        return knownBlockCount;
    }

    public void setKnownBlockCount(long knownBlockCount) {
        this.knownBlockCount = knownBlockCount;
    }
}
