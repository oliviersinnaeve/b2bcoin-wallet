package com.b2beyond.wallet.b2bcoin.daemon.rpc.model.coin;


public class BlockCount {

    private long count;

    private String status;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
