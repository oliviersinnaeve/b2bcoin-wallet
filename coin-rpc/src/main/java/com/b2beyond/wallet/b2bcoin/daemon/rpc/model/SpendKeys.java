package com.b2beyond.wallet.b2bcoin.daemon.rpc.model;

/**
 * Created by oliviersinnaeve on 30/11/17.
 */
public class SpendKeys {

    private String spendSecretKey;

    private String spendPublicKey;


    public String getSpendSecretKey() {
        return spendSecretKey;
    }

    public void setSpendSecretKey(String spendSecretKey) {
        this.spendSecretKey = spendSecretKey;
    }

    public String getSpendPublicKey() {
        return spendPublicKey;
    }

    public void setSpendPublicKey(String spendPublicKey) {
        this.spendPublicKey = spendPublicKey;
    }
}
