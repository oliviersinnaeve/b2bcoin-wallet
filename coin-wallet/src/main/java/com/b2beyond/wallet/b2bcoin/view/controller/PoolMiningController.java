package com.b2beyond.wallet.b2bcoin.view.controller;

import com.b2beyond.wallet.b2bcoin.daemon.PoolMinerDaemon;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.util.Properties;


public class PoolMiningController implements MiningController {

    private Logger LOGGER = Logger.getLogger(this.getClass());


    private PropertiesConfiguration applicationProperties;

    private String operatingSystem;

    private PoolMinerDaemon poolMinerDaemon;
    private boolean mining;

    private BufferedReader miningOutput;


    public PoolMiningController(PropertiesConfiguration applicationProperties, String operatingSystem) {
        this.applicationProperties = applicationProperties;
        this.operatingSystem = operatingSystem;
    }

    @Override
    public void startMining(String pool, String port, String address, String numberOfProcessors) {
        if (this.poolMinerDaemon == null) {
            LOGGER.info("Starting pool miner ...");
            this.poolMinerDaemon = new PoolMinerDaemon(this.applicationProperties, this.operatingSystem, pool, port, address, numberOfProcessors);
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
