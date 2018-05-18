package com.b2beyond.wallet.rpc.model.coin;

/**
 * Created by oliviersinnaeve on 14/10/17.
 */
class TransactionBlock {

    private long cumul_size;
    private String hash;
    private int height;
    private long timestamp;
    private int tx_count;


    public long getCumul_size() {
        return cumul_size;
    }

    public void setCumul_size(long cumul_size) {
        this.cumul_size = cumul_size;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getTx_count() {
        return tx_count;
    }

    public void setTx_count(int tx_count) {
        this.tx_count = tx_count;
    }
}
