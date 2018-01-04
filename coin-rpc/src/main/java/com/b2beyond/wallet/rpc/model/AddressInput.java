package com.b2beyond.wallet.rpc.model;


import org.apache.commons.lang.StringUtils;

public class AddressInput {

    private String publicSpendKey;
    private String secretSpendKey;

    public String getPublicSpendKey() {
        return publicSpendKey;
    }

    public void setPublicSpendKey(String publicSpendKey) {
        this.publicSpendKey = publicSpendKey;
    }

    public String getSecretSpendKey() {
        return secretSpendKey;
    }

    public void setSecretSpendKey(String secretSpendKey) {
        this.secretSpendKey = secretSpendKey;
    }

    public String getParams() {
        String params = "\"params\":{";
        if (StringUtils.isNotBlank(getPublicSpendKey())) {
            params += "\"publicSpendKey\": \"" + getPublicSpendKey() + "\"";
        }

        if (StringUtils.isNotBlank(getSecretSpendKey())) {
            params +=  "\"secretSpendKey\": \"" + getSecretSpendKey() + "\"";
        }

        params += "}";

        return params;
    }

}
