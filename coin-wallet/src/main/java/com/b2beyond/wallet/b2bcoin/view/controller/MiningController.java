package com.b2beyond.wallet.b2bcoin.view.controller;

import java.io.BufferedReader;

/**
 * Created by oliviersinnaeve on 12/09/17.
 */
public interface MiningController {
    void startMining(String address, String numberOfProcessors);

    void stopMining();

    boolean isMining();

    BufferedReader getMiningOutput();
}
