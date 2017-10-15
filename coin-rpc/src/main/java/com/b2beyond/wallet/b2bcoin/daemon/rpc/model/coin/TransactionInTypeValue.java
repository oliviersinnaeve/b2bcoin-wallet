package com.b2beyond.wallet.b2bcoin.daemon.rpc.model.coin;

/**
 * Created by oliviersinnaeve on 14/10/17.
 */
class TransactionInTypeValue {

    private long amount;
    private String k_image;
    private int[] key_offsets;


    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getK_image() {
        return k_image;
    }

    public void setK_image(String k_image) {
        this.k_image = k_image;
    }

    public int[] getKey_offsets() {
        return key_offsets;
    }

    public void setKey_offsets(int[] key_offsets) {
        this.key_offsets = key_offsets;
    }
}
