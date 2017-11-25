package com.b2beyond.wallet.b2bcoin.view.model;


public class JComboboxItem {

    private String key;
    private String value;

    public JComboboxItem(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public JComboboxItem(int key, int value) {
        this.key = "" + key;
        this.value = "" + value;
    }

    @Override
    public String toString() {
        return key;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}