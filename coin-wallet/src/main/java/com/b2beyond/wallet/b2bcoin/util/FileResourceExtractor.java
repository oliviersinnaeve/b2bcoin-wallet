package com.b2beyond.wallet.b2bcoin.util;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * Created by oliviersinnaeve on 24/09/17.
 */
public class FileResourceExtractor {

    private static Logger LOGGER = Logger.getLogger(FileResourceExtractor.class);

    public static void extractFromJar(String resource, String location) throws IOException {
        LOGGER.debug("Extract resource from jar : " + resource);
        InputStream ddlStream = B2BUtil.class.getClassLoader().getResourceAsStream(resource);

        FileOutputStream fos = new FileOutputStream(location);
        byte[] buf = new byte[2048];
        int r;
        while(-1 != (r = ddlStream.read(buf))) {
            fos.write(buf, 0, r);
        }
        fos.close();
        ddlStream.close();
    }

    public static void copyFromFileSystem(String resource, String location) throws IOException {
        LOGGER.debug("Copy resource from filesystem : " + resource);
        InputStream ddlStream = new FileInputStream(resource);

        FileOutputStream fos = new FileOutputStream(location);
        byte[] buf = new byte[2048];
        int r;
        while(-1 != (r = ddlStream.read(buf))) {
            fos.write(buf, 0, r);
        }
        fos.close();
        ddlStream.close();
    }

    public static void copyFromURL(String url, String location) {
        try {
            URL website = new URL(url);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(location);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException("Unable to download file from location : " + url);
        }
    }

}
