package com.b2beyond.wallet.rpc.exception;

import com.b2beyond.wallet.rpc.model.Error;

public class KnownJsonRpcException extends Throwable {

    private Error error;

    public KnownJsonRpcException(Error error) {
        this.error = error;
    }

    public Error getError() {
        return error;
    }
}
