package com.b2beyond.wallet.rpc.model;

import org.apache.commons.lang.StringUtils;

/**
 * Created by oliviersinnaeve on 04/02/18.
 */
public class FusionEstimateInput {

    private long threshold;

    private String address;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getThreshold() {
        return threshold;
    }

    public void setThreshold(long threshold) {
        this.threshold = threshold;
    }

    public String getParams() {
        String params = "\"params\":{\n" +
                "    \"threshold\":" + getThreshold() + ",\n";
        params += "  \"addresses\":[\"" + address + "\"";
        params += "  ]\n";
        params +=  "}";
        return params;
    }

}
