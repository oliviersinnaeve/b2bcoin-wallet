package com.b2beyond.wallet.b2bcoin.util;

public class ProcessUtil {

    public static void stop(String operatingSystem, Process process, int port) {
        try {
            process.destroy();
            process.waitFor();

            ProcessBuilder pb = null;
            if (operatingSystem.equalsIgnoreCase(B2BUtil.MAC)) {
                pb = new ProcessBuilder("kill", "-9", "$(lsof -t -i :" + port + ")");
            }

            if (pb != null) {
                Process newProcess = pb.start();
                newProcess.waitFor();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
