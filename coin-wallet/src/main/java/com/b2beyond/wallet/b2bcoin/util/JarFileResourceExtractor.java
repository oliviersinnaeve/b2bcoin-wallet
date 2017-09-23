package com.b2beyond.wallet.b2bcoin.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

/**
 * Created by oliviersinnaeve on 24/09/17.
 */
public class JarFileResourceExtractor {

    public static void extract(String resource, String location) throws URISyntaxException, IOException {
        InputStream ddlStream = B2BUtil.class.getClassLoader().getResourceAsStream(resource);

        FileOutputStream fos = new FileOutputStream(location);
        byte[] buf = new byte[2048];
        int r;
        while(-1 != (r = ddlStream.read(buf))) {
            fos.write(buf, 0, r);
        }
        fos.close();
    }

}
