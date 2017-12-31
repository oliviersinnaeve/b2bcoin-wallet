package com.b2beyond.wallet.rpc.model.coin;

/**
 * Created by oliviersinnaeve on 14/10/17.
 */
class TransactionInType {

    private String type;
    private TransactionInTypeValue value;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TransactionInTypeValue getValue() {
        return value;
    }

    public void setValue(TransactionInTypeValue value) {
        this.value = value;
    }

}
