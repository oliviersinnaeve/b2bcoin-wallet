package com.b2beyond.wallet.b2bcoin.utils;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;


public class QRCodeGenerator {

    public static byte[] generateQRCode(String value) {

        int size = 250;
        String fileType = "png";

        try {

            Map<EncodeHintType, Object> hintMap = new EnumMap<>(EncodeHintType.class);
            hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            // Now with zxing version 3.2.1 you could change border size (white border size to just 1)
            hintMap.put(EncodeHintType.MARGIN, 1); /* default = 4 */
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix byteMatrix = qrCodeWriter.encode(value, BarcodeFormat.QR_CODE, size,
                    size, hintMap);
            int crunchWidth = byteMatrix.getWidth();
            int crunchHeight = byteMatrix.getHeight();
            BufferedImage image = new BufferedImage(crunchWidth, crunchHeight,
                    BufferedImage.TYPE_INT_RGB);
            image.createGraphics();

            Graphics2D graphics = (Graphics2D) image.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, crunchWidth, crunchHeight);
            graphics.setColor(Color.BLACK);

            for (int i = 0; i < crunchWidth; i++) {
                for (int j = 0; j < crunchWidth; j++) {
                    if (byteMatrix.get(i, j)) {
                        graphics.fillRect(i, j, 1, 1);
                    }
                }
            }

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ImageIO.write(image, fileType, output);

            return output.toByteArray();
        } catch (IOException | WriterException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
