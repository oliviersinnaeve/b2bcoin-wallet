package com.b2beyond.wallet.rpc.model.coin;

/**
 * Created by oliviersinnaeve on 14/10/17.
 */
class Target {

    private String type;

    private TargetData data;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TargetData getData() {
        return data;
    }

    public void setData(TargetData data) {
        this.data = data;
    }
}
