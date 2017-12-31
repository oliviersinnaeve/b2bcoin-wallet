package com.b2beyond.wallet.rpc.model;


import java.util.ArrayList;
import java.util.List;

public class Addresses {

    private List<String> addresses = new ArrayList<>();

    public List<String> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Addresses) {
            Addresses addresses = (Addresses)obj;

            boolean containsAll = true;
            for (String address: addresses.getAddresses()) {
                if (!getAddresses().contains(address)) {
                    containsAll = false;
                }
            }

            return containsAll;
        }

        return false;
    }
}
