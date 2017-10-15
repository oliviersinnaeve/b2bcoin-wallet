package com.b2beyond.wallet.b2bcoin.daemon.rpc.model.coin;

import java.util.List;

/**
 * Created by oliviersinnaeve on 14/10/17.
 */
class TransactionBlockWrapper {

    private String extra;
    private int unlock_time;
    private int version;
    private List<TransactionInType> vin;
    private List<TransactionOutType> vout;


    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public int getUnlock_time() {
        return unlock_time;
    }

    public void setUnlock_time(int unlock_time) {
        this.unlock_time = unlock_time;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<TransactionInType> getVin() {
        return vin;
    }

    public void setVin(List<TransactionInType> vin) {
        this.vin = vin;
    }

    public List<TransactionOutType> getVout() {
        return vout;
    }

    public void setVout(List<TransactionOutType> vout) {
        this.vout = vout;
    }

}
