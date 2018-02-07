package com.b2beyond.wallet.rpc.model;

import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;


public class FusionTransactionInput {

    private long threshold;

    private int anonymity = 6;

    private String address;


    public long getThreshold() {
        return threshold;
    }

    public void setThreshold(long threshold) {
        this.threshold = threshold;
    }

    public int getAnonymity() {
        return anonymity;
    }

    public void setAnonymity(int anonymity) {
        this.anonymity = anonymity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getParams() {
        String params = "\"params\":{\n" +
                "    \"anonymity\":" + getAnonymity() + ",\n" +
                "    \"threshold\":" + getThreshold() + ",\n";

        params += "    \"addresses\":[\"" + address + "\"";
        params += "    ],\n";
        params += "   \"destinationAddress\":\"" + getAddress() +"\"\n" + "}";

        return params;
    }

}
