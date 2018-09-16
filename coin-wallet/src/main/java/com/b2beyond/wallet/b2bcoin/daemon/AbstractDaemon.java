package com.b2beyond.wallet.b2bcoin.daemon;

import com.b2beyond.wallet.b2bcoin.util.ProcessUtil;

public abstract class AbstractDaemon implements Daemon {

    protected String operatingSystem;
    protected int processPid;
    protected Process process;

    @Override
    public void stop() {
        System.out.println("Killing process with id : " + processPid);
        ProcessUtil.stop(operatingSystem, processPid, process, getPort());
        // Mac hack
//        System.out.println("Killing process with id : " + (processPid + 1));
//        ProcessUtil.stop(operatingSystem, processPid + 1, process);
//
//        System.out.println("Killing process with id : " + (processPid + 2));
//        ProcessUtil.stop(operatingSystem, processPid + 2, process);
//
//        System.out.println("Killing process with id : " + (processPid + 3));
//        ProcessUtil.stop(operatingSystem, processPid + 3, process);
    }

    public abstract int getPort();

}
