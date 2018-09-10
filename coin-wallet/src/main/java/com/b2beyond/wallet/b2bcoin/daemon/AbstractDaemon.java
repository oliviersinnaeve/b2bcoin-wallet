package com.b2beyond.wallet.b2bcoin.daemon;

import com.b2beyond.wallet.b2bcoin.util.ProcessUtil;

public abstract class AbstractDaemon implements Daemon {

    protected String operatingSystem;
    protected int processPid;
    protected Process process;

    @Override
    public void stop() {
        ProcessUtil.stop(operatingSystem, processPid, process);
    }
}
