package com.b2beyond.wallet.b2bcoin.daemon.rpc.model;

/**
 * Created by oliviersinnaeve on 15/10/17.
 */
public class Error {

    private String code;
    private String message;

    private ErrorData data;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorData getData() {
        return data;
    }

    public void setData(ErrorData data) {
        this.data = data;
    }

}
