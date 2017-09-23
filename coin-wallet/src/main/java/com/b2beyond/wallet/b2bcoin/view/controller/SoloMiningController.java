package com.b2beyond.wallet.b2bcoin.view.controller;

import com.b2beyond.wallet.b2bcoin.controler.WalletDaemonProperties;
import com.b2beyond.wallet.b2bcoin.daemon.SoloMinerDaemon;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.util.Properties;


public class SoloMiningController implements MiningController {

    private Logger LOGGER = Logger.getLogger(this.getClass());


    private Properties daemonProperties;
    private WalletDaemonProperties walletDaemonProperties;

    private String operatingSystem;

    private SoloMinerDaemon poolMinerDaemon;
    private boolean mining;

    private BufferedReader miningOutput;


    public SoloMiningController(Properties daemonProperties, WalletDaemonProperties walletDaemonProperties, String operatingSystem) {
        this.daemonProperties = daemonProperties;
        this.walletDaemonProperties = walletDaemonProperties;
        this.operatingSystem = operatingSystem;
    }

    @Override
    public void startMining(String address, String numberOfProcessors) {
        if (this.poolMinerDaemon == null) {
            LOGGER.info("Starting solo miner ...");
            this.poolMinerDaemon = new SoloMinerDaemon(this.daemonProperties, this.walletDaemonProperties, this.operatingSystem, address, numberOfProcessors);
            setMining(true);
            miningOutput = this.poolMinerDaemon.getProcessOutBuffer();
            LOGGER.info("Solo miner started !!");
        }
    }

    @Override
    public void stopMining() {
        if (poolMinerDaemon != null) {
            LOGGER.info("Stopping solo miner ...");
            poolMinerDaemon.stop();
            setMining(false);
            poolMinerDaemon = null;
            LOGGER.info("Solo miner stopped !!");
        }
    }

    @Override
    public boolean isMining() {
        return mining;
    }

    private void setMining(boolean mining) {
        this.mining = mining;
    }

    @Override
    public BufferedReader getMiningOutput() {
        return miningOutput;
    }
}
