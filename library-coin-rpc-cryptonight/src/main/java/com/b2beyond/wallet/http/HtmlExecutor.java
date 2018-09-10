package com.b2beyond.wallet.http;

import com.b2beyond.wallet.rpc.model.Error;
import com.b2beyond.wallet.rpc.exception.KnownJsonRpcException;
import com.google.gson
        .Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class HtmlExecutor<T> {

    private Logger LOGGER = Logger.getLogger(this.getClass());
    public static String CONNECTION_REFUSED = "CONNECTION_REFUSED";

    private String url;

    private int readTimeout = 15000;


    public HtmlExecutor(String url) {
        this.url = url;
        LOGGER.info("JsonRpcExecutor created for baseUrl : '" + url + "'");
    }

    public synchronized T execute() throws KnownJsonRpcException {
//        T result = null;
        HttpsURLConnection httpConnection = null;

        try {
            try {
                SSLContext ctx = SSLContext.getInstance("TLS");
                ctx.init(null, new TrustManager[] { new InvalidCertificateTrustManager() }, null);
                SSLContext.setDefault(ctx);

                final URL urlConnection = new URL(url);
                httpConnection = (HttpsURLConnection) ((urlConnection.openConnection()));
//                httpConnection.setDoOutput(true);
                httpConnection.setRequestProperty("Content-Type", "plain/text");
                httpConnection.setRequestMethod("GET");
                httpConnection.setReadTimeout(readTimeout);
                httpConnection.setHostnameVerifier(new InvalidCertificateHostVerifier());
                httpConnection.connect();

//                OutputStream os = httpConnection.getOutputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String line;
                while((line = in.readLine()) != null && !"".equalsIgnoreCase(line)) {
                    builder.append(line);
                }

                // Parsing the json !! Trying not to return null !!
//                os.close();
                httpConnection.disconnect();
                return (T) builder.toString();
            } catch (IOException e) {
                if (httpConnection != null) {
                    httpConnection.disconnect();
                }

                Error error = new Error();
                error.setCode(CONNECTION_REFUSED);
                throw new KnownJsonRpcException(error);
            }
        } catch (Exception e) {
            LOGGER.error("General - JSon Rcp Executor failed : " + e.getMessage() + " on : " + url);
            if (httpConnection != null) {
                httpConnection.disconnect();
            }

            Error error = new Error();
            error.setCode(CONNECTION_REFUSED);
            throw new KnownJsonRpcException(error);
        }

//        return result;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

}