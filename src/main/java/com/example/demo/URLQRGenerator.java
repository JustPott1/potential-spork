package com.example.demo;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.nio.file.Files;
import java.nio.file.Path;

public class URLQRGenerator extends QRGenerator {
    @Override
    public void generate(String text, Path output) throws Exception {
        Files.createDirectories(output.getParent());
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 350, 350);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", output);
    }
}
