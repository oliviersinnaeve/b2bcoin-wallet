package com.b2beyond.wallet.rpc.model;

import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;


public class PaymentInput {

    private List<Address> addresses;

    private Map<String, Long> transfers;

    private long fee;

    private int unlockTime = 10;

    private int anonymity = 6;

    private String paymentId;

    private String address;

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public Map<String, Long> getTransfers() {
        return transfers;
    }

    public void setTransfers(Map<String, Long> transfers) {
        this.transfers = transfers;
    }

    public long getFee() {
        return fee;
    }

    public void setFee(long fee) {
        this.fee = fee;
    }

    public int getUnlockTime() {
        return unlockTime;
    }

    public void setUnlockTime(int unlockTime) {
        this.unlockTime = unlockTime;
    }

    public int getAnonymity() {
        return anonymity;
    }

    public void setAnonymity(int anonymity) {
        this.anonymity = anonymity;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
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
                "    \"fee\":" + getFee() + ",\n" +
                "    \"unlockTime\":" + getUnlockTime() + ",\n";

        if (StringUtils.isNotBlank(getPaymentId())) {
            params += "    \"paymentId\":\"" + getPaymentId() + "\",\n";
        }

        params += "    \"addresses\":[\"" + address + "\"";
        params += "    ],\n";


        int index = 0;
        int totalAmountOfTransfers = 0;

        params += "    \"transfers\":[\n";
        for (String key : getTransfers().keySet()) {
            Long amount = getTransfers().get(key);
            params += "        {\n" +
                    "            \"amount\":" + amount + ",\n" +
                    "            \"address\":\"" + key + "\"\n" +
                    "        }\n";
        }

        if (index < totalAmountOfTransfers - 1) {
            params += ",";
        }
        index += 1;


        params += "    ],\n" +
            "    \"changeAddress\":\"" + getAddress() +"\"\n" +
            "}";

        return params;
    }

}
