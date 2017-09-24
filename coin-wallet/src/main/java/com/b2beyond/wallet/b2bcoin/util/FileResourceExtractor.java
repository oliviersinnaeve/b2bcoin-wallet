package com.b2beyond.wallet.b2bcoin.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by oliviersinnaeve on 24/09/17.
 */
public class FileResourceExtractor {

    public static void extractFromJar(String resource, String location) throws IOException {
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

}
