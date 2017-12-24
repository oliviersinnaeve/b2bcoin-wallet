package com.b2beyond.wallet.b2bcoin.daemon.rpc.model.coin;


public class BlockWrapper {

    private String currencyName;

    private Block block;


    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }
}
