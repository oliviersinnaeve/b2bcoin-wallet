package com.b2beyond.wallet.b2bcoin.util;

/**
 * helper class to check the operating system this Java VM runs in
 *
 * please keep the notes below as a pseudo-license
 *
 * http://stackoverflow.com/questions/228477/how-do-i-programmatically-determine-operating-system-in-java
 * compare to http://svn.terracotta.org/svn/tc/dso/tags/2.6.4/code/base/common/src/com/tc/util/runtime/Os.java
 * http://www.docjar.com/html/api/org/apache/commons/lang/SystemUtils.java.html
 */
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import org.apache.log4j.Logger;

import java.awt.Color;
import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;

public final class B2BUtil {

    private static Logger LOGGER = Logger.getLogger(B2BUtil.class);

    public static final String WINDOWS = "windows";
    public static final String MAC = "mac";
    public static final String LINUX = "linux";

    public static final String SEPARATOR = System.getProperty("file.separator");

//    public static Color mainColor = new Color(0, 130, 0);
//    public static Color selectedColor = new Color(0, 88, 0);

    public static Color mainColor = new Color(56, 174, 204);
    public static Color selectedColor = new Color(8, 103, 136);

    public static final DateFormat readFormat = new SimpleDateFormat( "MMM dd, yyyy hh:mm:ss aa");
    public static final DateFormat writeFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");

    public static final DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));

    // cached result of OS detection
    protected static String detectedOS;

    static {
        df.setMaximumFractionDigits(12);
    }

    /**
     * detect the operating system from the os.name System property and cache
     * the result
     *
     * @returns - the operating system detected
     */
    public static String getOperatingSystem() {
        if (detectedOS == null) {
            String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
            if (OS.contains("mac") || OS.contains("darwin")) {
                detectedOS = MAC;
            } else if (OS.contains("win")) {
                detectedOS = WINDOWS;
            } else if (OS.contains("nux")) {
                detectedOS = LINUX;
            } else {
                detectedOS = WINDOWS;
            }
        }
        return detectedOS;
    }

    public static void copyConfigsOnFirstRun() {
        try {
            new File(getConfigRoot()).mkdirs();

            if (!Paths.get(getConfigRoot() + "coin.conf").toFile().exists()) {
                LOGGER.trace("Exporting the coin daemon config");
                FileResourceExtractor.extractFromJar(
                        "configs/coin.conf",
                        getConfigRoot() + "coin.conf");
            }
            if (Paths.get(getUserHome() + "coin-wallet.conf").toFile().exists()) {
                LOGGER.trace("delete the previous coin wallet");
                Paths.get(getUserHome() + "coin-wallet.conf").toFile().delete();
            }
            if (!Paths.get(getConfigRoot() + "coin-wallet.conf").toFile().exists()) {
                LOGGER.trace("Exporting the coin wallet config");
                FileResourceExtractor.extractFromJar(
                        "configs/coin-wallet.conf",
                        getConfigRoot() + "coin-wallet.conf");
            }
            if (!Paths.get(getConfigRoot() + "application.config").toFile().exists()) {
                LOGGER.trace("Exporting the coin daemon config");
                FileResourceExtractor.extractFromJar(
                        "configs/application.config",
                        getConfigRoot() + "application.config");
            }
        } catch (Exception e) {
            LOGGER.error("Failed to copy file", e);
        }
    }

    public static void copyDaemonsOnFirstRun(String daemonExecutable, String walletExecutable, String poolMinerExecutable) {
        try {
            new File(getBinariesRoot()).mkdirs();

            String os = getOperatingSystem();

            if ("32".equals(System.getProperty("sun.arch.data.model")) && getOperatingSystem().equalsIgnoreCase(WINDOWS)) {
                os += "-32";
            }

            LOGGER.debug("Exporting binaries for os : " + os);

            if (!Paths.get(getBinariesRoot() + daemonExecutable).toFile().exists()) {
                LOGGER.trace("Exporting the coin daemon");
                FileResourceExtractor.extractFromJar(
                        "b2bcoin-" + os + "/binaries/" + daemonExecutable,
                        getBinariesRoot() + daemonExecutable);
            }
            if (!Paths.get(getBinariesRoot() + walletExecutable).toFile().exists()) {
                LOGGER.trace("Exporting the wallet daemon");
                FileResourceExtractor.extractFromJar(
                        "b2bcoin-" + os + "/binaries/" + walletExecutable,
                        getBinariesRoot() + walletExecutable);
            }

            if (getOperatingSystem().equalsIgnoreCase(LINUX) || getOperatingSystem().equalsIgnoreCase(MAC)) {
                if (!Paths.get(getBinariesRoot() + poolMinerExecutable).toFile().exists()) {
                    LOGGER.trace("Exporting the wallet daemon");
                    FileResourceExtractor.extractFromJar(
                            "b2bcoin-" + os + "/binaries/" + poolMinerExecutable,
                            getBinariesRoot() + poolMinerExecutable);
                }

                Process p = Runtime.getRuntime().exec("chmod 755 " + getBinariesRoot() + daemonExecutable);
                p.waitFor();
                p = Runtime.getRuntime().exec("chmod 755 " + getBinariesRoot() + walletExecutable);
                p.waitFor();
                p = Runtime.getRuntime().exec("chmod 755 " + getBinariesRoot() + poolMinerExecutable);
                p.waitFor();
            }
        } catch (Exception e) {
            LOGGER.error("Failed to copy file", e);
        }
    }

    public static String getBinariesRoot() {
        String location = getUserHome() + "binaries" + SEPARATOR;
        LOGGER.info("Loading daemon from OS / Location : " + getOperatingSystem() + " :: " + location);
        return location;
    }

    public static String getConfigRoot() {
        String location = getUserHome() + "configs" + SEPARATOR;
        LOGGER.info("Loading daemon from OS / Location : " + getOperatingSystem() + " :: " + location);
        return location;
    }

    public static String getLogRoot() {
        String location = getUserHome() + "logs" + SEPARATOR;
        LOGGER.info("Loading daemon from OS / Location : " + getOperatingSystem() + " :: " + location);
        return location;
    }

    public static String getUserHome() {
        String userHome = System.getProperty("user.home") + SEPARATOR + "b2bcoin" + SEPARATOR;
        if (getOperatingSystem().equalsIgnoreCase(WINDOWS)) {
            userHome = System.getProperty("user.home") + SEPARATOR + "b2bcoin" + SEPARATOR;
        }
        LOGGER.info("Starting daemon for OS : '" + getOperatingSystem() + "' with user home : " + userHome);
        return userHome;
    }

    public static int getPid(Process process, String operatingSystem, boolean spawned) {
        if (B2BUtil.WINDOWS.equalsIgnoreCase(operatingSystem)) {
            LOGGER.info("Windows process class name : " + process.getClass().getName());
            /* determine the pid on windows plattforms */
            if (process.getClass().getName().equals("java.lang.Win32Process") ||
                    process.getClass().getName().equals("java.lang.ProcessImpl")) {
                try {
                    Field f = process.getClass().getDeclaredField("handle");
                    f.setAccessible(true);
                    long processHandle = f.getLong(process);

                    Kernel32 kernel = Kernel32.INSTANCE;
                    WinNT.HANDLE handle = new WinNT.HANDLE();
                    handle.setPointer(Pointer.createConstant(processHandle));
                    return kernel.GetProcessId(handle);
                } catch (Throwable e) {
                    LOGGER.error("Failed to get process pid !", e);
                }
            }
        } else {
            /* get the PID on unix/linux systems */
            LOGGER.info("Linux/Mac process class name : " + process.getClass().getName());
            if (process.getClass().getName().equals("java.lang.UNIXProcess")) {
                try {
                    Field f = process.getClass().getDeclaredField("pid");
                    f.setAccessible(true);
                    if (B2BUtil.MAC.equalsIgnoreCase(operatingSystem)) {
                        if (spawned) {
                            return f.getInt(process) + 2;
                        } else {
                            return f.getInt(process);
                        }
                    } else {
                        return f.getInt(process);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }
}