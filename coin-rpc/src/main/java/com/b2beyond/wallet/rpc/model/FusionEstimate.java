package com.b2beyond.wallet.rpc.model;


public class FusionEstimate {

    private long totalOutputCount;
    private long fusionReadyCount;


    public long getTotalOutputCount() {
        return totalOutputCount;
    }

    public void setTotalOutputCount(long totalOutputCount) {
        this.totalOutputCount = totalOutputCount;
    }

    public long getFusionReadyCount() {
        return fusionReadyCount;
    }

    public void setFusionReadyCount(long fusionReadyCount) {
        this.fusionReadyCount = fusionReadyCount;
    }
}
