package com.b2beyond.wallet.b2bcoin.daemon.rpc.model.exception;

import com.b2beyond.wallet.b2bcoin.daemon.rpc.model.Error;


public class KnownJsonRpcException extends Throwable {

    private Error error;

    public KnownJsonRpcException(Error error) {
        this.error = error;
    }

    public Error getError() {
        return error;
    }
}
