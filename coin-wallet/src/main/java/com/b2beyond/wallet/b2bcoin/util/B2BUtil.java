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
import com.sun.jna.platform.FileUtils;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import org.apache.log4j.Logger;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

    public static Color mainColor = new Color(164, 205, 255);
    public static Color selectedColor = new Color(64, 64, 64);

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

    public static void copyDaemonsOnFirstRun(String daemonExecutable, String walletExecutable) {
        try {
            new File(getBinariesRoot()).mkdirs();
            new File(getConfigRoot()).mkdirs();

            if (!Paths.get(getBinariesRoot() + daemonExecutable).toFile().exists()) {
                LOGGER.trace("Exporting the coin daemon");
                JarFileResourceExtractor.extract(
                        "b2bcoin-" + getOperatingSystem() + "/binaries/" + daemonExecutable,
                        getBinariesRoot() + daemonExecutable);
            }
            if (!Paths.get(getBinariesRoot() + walletExecutable).toFile().exists()) {
                LOGGER.trace("Exporting the wallet daemon");
                JarFileResourceExtractor.extract(
                        "b2bcoin-" + getOperatingSystem() + "/binaries/" + walletExecutable,
                        getBinariesRoot() + walletExecutable);
            }
            if (!Paths.get(getConfigRoot() + "b2bcoin.conf").toFile().exists()) {
                LOGGER.trace("Exporting the coin daemon config");
                JarFileResourceExtractor.extract(
                        "b2bcoin-" + getOperatingSystem() + "/configs/b2bcoin.conf",
                        getConfigRoot() + "b2bcoin.conf");
            }

            if (getOperatingSystem().equalsIgnoreCase(LINUX) || getOperatingSystem().equalsIgnoreCase(MAC)) {
                Process p = Runtime.getRuntime().exec("chmod 755 " + getBinariesRoot() + daemonExecutable);
                p.waitFor();
                p = Runtime.getRuntime().exec("chmod 755 " + getBinariesRoot() + walletExecutable);
                p.waitFor();
            }
        } catch (Exception e) {
            LOGGER.error("Failed to copy file", e);
        }
    }

    public static String getBinariesRoot() {
        String location = getUserHome() + "binaries/";

        if (getOperatingSystem().equalsIgnoreCase(WINDOWS)) {
            location = getUserHome() + "binaries\\";
        }

        LOGGER.info("Loading daemon from OS / Location : " + getOperatingSystem() + " :: " + location);
        return location;
    }

    public static String getConfigRoot() {
        String location = getUserHome() + "configs/";

        if (getOperatingSystem().equalsIgnoreCase(WINDOWS)) {
            location = getUserHome() + "configs\\";
        }

        LOGGER.info("Loading daemon from OS / Location : " + getOperatingSystem() + " :: " + location);
        return location;
    }

    public static String getUserHome() {
        String userHome = System.getProperty("user.home") + "/b2bcoin/";
        if (getOperatingSystem().equalsIgnoreCase(WINDOWS)) {
            userHome = System.getProperty("user.home") + "\\b2bcoin\\";
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