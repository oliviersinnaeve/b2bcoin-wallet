package com.b2beyond.wallet.b2bcoin.view.controller;

import com.b2beyond.wallet.b2bcoin.daemon.PoolMinerDaemon;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.util.Properties;


public class PoolMiningController implements MiningController {

    private Logger LOGGER = Logger.getLogger(this.getClass());


    private Properties daemonProperties;

    private String operatingSystem;

    private PoolMinerDaemon poolMinerDaemon;
    private boolean mining;

    private BufferedReader miningOutput;


    public PoolMiningController(Properties daemonProperties, String operatingSystem) {
        this.daemonProperties = daemonProperties;
        this.operatingSystem = operatingSystem;
    }

    @Override
    public void startMining(String address, String numberOfProcessors) {
        if (this.poolMinerDaemon == null) {
            LOGGER.info("Starting pool miner ...");
            this.poolMinerDaemon = new PoolMinerDaemon(this.daemonProperties, this.operatingSystem, address, numberOfProcessors);
            setMining(true);
            miningOutput = this.poolMinerDaemon.getProcessOutBuffer();
            LOGGER.info("Pool miner started !!");
        }
    }

    @Override
    public void stopMining() {
        if (poolMinerDaemon != null) {
            LOGGER.info("Stopping pool miner ...");
            poolMinerDaemon.stop();
            setMining(false);
            poolMinerDaemon = null;
            LOGGER.info("Pool miner stopped !!");
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
