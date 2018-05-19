package com.b2beyond.wallet.b2bcoin.view.view.panel;

import java.util.Observable;


public class PanelObservable extends Observable {

    public synchronized void setChanged() {
        super.setChanged();
    }

}
