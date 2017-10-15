package com.b2beyond.wallet.b2bcoin.daemon.rpc.model;

import java.util.List;
import java.util.Map;


public class PaymentInput {

    private List<Address> addresses;

    private Map<String, Long> transfers;

    private long fee;

    private int unlockTime = 10;

    private int anonymity = 10;

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
//                "    \"paymentId\":\"" + input.getPaymentId() + "\",\n";


        int index = 0;
        if(getAddresses() != null && !getAddresses().isEmpty()) {
            params += "    \"addresses\":[\n";

            for (Address key : getAddresses()) {
                String address = key.getAddress();
                params += "        \"" + address + "\"\n";

                if (index < getAddresses().size() - 1) {
                    params += ",";
                }
                index += 1;
            }

//                "        \"27eJo2S9eVo5N2G9zyjkqNBZPR6d2qvVD122vQMGAhcrjZjLu8nsMqk3c4KQ9iMJ4AV4fpBMccmjfJ4cu7uprKLNFX4qWNh\",\n" +
//                "        \"24JtjYsLdSJKNNDCPGdMco5NbMBLqVWZ5ZiW5vzjXQUrLpMs1MRnfTQ3c4KQ9iMJ4AV4fpBMccmjfJ4cu7uprKLNFXHARwn\",\n" +
//                "        \"21fYPCpaM3ochSSyLnhDAhgw1yV5WPb5c1BfyX5eidbMGyEPgnbSgJW3c4KQ9iMJ4AV4fpBMccmjfJ4cu7uprKLNFX8VQMv\"\n" +
            params += "    ],\n";
        }

        params += "    \"transfers\":[\n";

        index = 0;
        for (String key : getTransfers().keySet()) {
            Long amount = getTransfers().get(key);
            params += "        {\n" +
                    "            \"amount\":" + amount + ",\n" +
                    "            \"address\":\"" + key + "\"\n" +
                    "        }\n";

            if (index < getTransfers().size() - 1) {
                params += ",";
            }
            index += 1;
        }

//                "        {\n" +
//                "            \"amount\":123456,\n" +
//                "            \"address\":\"27eJo2S9eVo5N2G9zyjkqNBZPR6d2qvVD122vQMGAhcrjZjLu8nsMqk3c4KQ9iMJ4AV4fpBMccmjfJ4cu7uprKLNFX4qWNh\"\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"amount\":234567,\n" +
//                "            \"address\":\"278g3wNw5W48DeGbjwxkW3XauBip64uYKS9eFveUHBfdRAG3dYHPZvqXy5BWbfuKEtWZ86PJZdRacAgr1x3gtP5nLyGcVt8\"\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"amount\":345678,\n" +
//                "            \"address\":\"2AtjUXGmhP6CmbRxCtBESR4MjSGiWCQUTPCdsDpw72Co2pwzZT7rjnaBNRCSFCEygjNo5oe8mHyXU4Eip8szu4ZnAFyPW1a\"\n" +
//                "        }\n" +
        params += "    ],\n" +
                "    \"changeAddress\":\"" + getAddress() +"\"\n" +
                "}";

        return params;
    }

}
