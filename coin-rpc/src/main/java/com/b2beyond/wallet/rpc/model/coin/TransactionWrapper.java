package com.b2beyond.wallet.rpc.model.coin;


public class TransactionWrapper {

    private TransactionBlock block;

    private String status;

    private TransactionBlockWrapper tx;

    private TransactionDetails txDetails;


    public TransactionBlock getBlock() {
        return block;
    }

    public void setBlock(TransactionBlock block) {
        this.block = block;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public TransactionBlockWrapper getTx() {
        return tx;
    }

    public void setTx(TransactionBlockWrapper tx) {
        this.tx = tx;
    }

    public TransactionDetails getTxDetails() {
        return txDetails;
    }

    public void setTxDetails(TransactionDetails txDetails) {
        this.txDetails = txDetails;
    }
}
