package com.b2beyond.wallet.b2bcoin.util;

import java.io.IOException;

public class ProcessUtil {

    public static void stop(String operatingSystem, int processPid, Process process) {
        ProcessBuilder pb = null;
        if (operatingSystem.equalsIgnoreCase(B2BUtil.MAC)) {
            pb = new ProcessBuilder("kill", "-9", "" + processPid);
        }

//        if (operatingSystem.equalsIgnoreCase(B2BUtil.LINUX)) {
//            pb = new ProcessBuilder("fuser", "-k", walletProperties.getInt("bind-port") + "/tcp");
//        }

        if (operatingSystem.equalsIgnoreCase(B2BUtil.WINDOWS)) {
//            LOGGER.info("Windows destroy wallet process ...");
            process.destroy();
            //                LOGGER.info("Windows destroy wallet process - wait for :" + process.waitFor());
            //            LOGGER.info("Windows destroy wallet process - exit value :" + process.exitValue());
        }

        if (pb != null) {
            try {
                Process newProcess = pb.start();
                //                    LOGGER.info("Wait for value : " + process.waitFor());
                //                LOGGER.info("Killing B2BCoin daemon exit value : " + process.exitValue());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
