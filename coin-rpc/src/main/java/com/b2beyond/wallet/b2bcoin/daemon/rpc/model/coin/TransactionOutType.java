package com.b2beyond.wallet.b2bcoin.daemon.rpc.model.coin;

/**
 * Created by oliviersinnaeve on 14/10/17.
 */
class TransactionOutType {

    private long amount;
    private Target target;


    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }
}
