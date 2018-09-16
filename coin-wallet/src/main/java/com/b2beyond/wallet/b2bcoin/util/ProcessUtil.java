package com.b2beyond.wallet.b2bcoin.util;

import java.io.IOException;

public class ProcessUtil {

    public static void stop(String operatingSystem, int processPid, Process process, int port) {

        process.destroy();

        ProcessBuilder pb = null;
        if (operatingSystem.equalsIgnoreCase(B2BUtil.MAC)) {
            pb = new ProcessBuilder("kill", "-9", "$(lsof -t -i :" + port + ")");
        }

        if (pb != null) {
            try {
                Process newProcess = pb.start();
                newProcess.waitFor();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (operatingSystem.equalsIgnoreCase(B2BUtil.MAC)) {
            pb = new ProcessBuilder("kill", "-9", "" + processPid);
        }

        if (pb != null) {
            try {
                Process newProcess = pb.start();
                newProcess.waitFor();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
