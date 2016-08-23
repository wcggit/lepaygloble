package com.jifenke.lepluslive.barcode.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import com.jifenke.lepluslive.barcode.BarcodeConfig;

import org.krysalis.barcode4j.impl.AbstractBarcodeBean;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.swing.*;

@Service
public class BarcodeService {

  public byte[] barcode(String barcode, BarcodeConfig.Barcode barcodeConfig) throws IOException {
    AbstractBarcodeBean codeBean = new Code128Bean();

    final int dpi = barcodeConfig.getDpi();

    codeBean.setHeight(barcodeConfig.getHeight());
    codeBean.setBarHeight(barcodeConfig.getBarHeight());
    codeBean.setFontSize(barcodeConfig.getFontSize());
    codeBean.setModuleWidth(barcodeConfig.getModuleWidth());

    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    BitmapCanvasProvider
        canvasProvider =
        new BitmapCanvasProvider(byteArrayOutputStream, "image/x-png", dpi,
                                 BufferedImage.TYPE_BYTE_BINARY, false, 0);
    codeBean.generateBarcode(canvasProvider, barcode);
    canvasProvider.finish();

    return byteArrayOutputStream.toByteArray();
  }

  public BufferedImage barcode(String barcode) throws IOException {
    byte[] bytes = barcode(barcode, BarcodeConfig.Barcode.defaultConfig());

    try {
      return ImageIO.read(new ByteArrayInputStream(bytes));
    } catch (IOException e) {
      // ignored
    }

    return null;
  }

  public String barcodeAsBase64(String code) throws IOException, InterruptedException {
    final ByteArrayOutputStream os = new ByteArrayOutputStream();

    BufferedImage bufferedImage = barcode(code);

    ImageIO.write(bufferedImage, "png", Base64.getEncoder().wrap(os));
    return os.toString(StandardCharsets.UTF_8.name());
  }

  public byte[] qrCode(String code, BarcodeConfig.QRCode qrCodeConfig)
      throws InterruptedException, IOException {
    Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();

    hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
    hints.put(EncodeHintType.MARGIN, 1);
    try {

      BitMatrix byteMatrix = new MultiFormatWriter().encode(code,
                                                            BarcodeFormat.QR_CODE, 850, 850, hints);

      // Make the BufferedImage that are to hold the QRCode
      int matrixWidth = byteMatrix.getWidth();

      BufferedImage image = new BufferedImage(matrixWidth, matrixWidth, BufferedImage.TYPE_INT_RGB);
      image.createGraphics();

      Graphics2D graphics = (Graphics2D) image.getGraphics();
      graphics.setColor(Color.WHITE);
      graphics.fillRect(0, 0, matrixWidth, matrixWidth);

      // Paint and save the image using the ByteMatrix

      graphics.setColor(Color.BLACK);

      for (int i = 0; i < matrixWidth; i++) {
        for (int j = 0; j < matrixWidth; j++) {
          if (byteMatrix.get(i, j) == true) {
            graphics.fillRect(i, j, 1, 1);
          }
        }
      }
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      ImageIO.write(image, "png", out);
      return out.toByteArray();
    } catch (WriterException e) {

      e.printStackTrace();

    }
    return null;
    //return QRCode.from(code).withSize(qrCodeConfig.getWidth(), qrCodeConfig.getHeight()).to(qrCodeConfig.getImageType()).stream().toByteArray();
  }

  public BufferedImage qrCode(String code) throws InterruptedException, IOException {
    byte[] bytes = qrCode(code, BarcodeConfig.QRCode.defaultConfig());

    try {
      return ImageIO.read(new ByteArrayInputStream(bytes));
    } catch (IOException e) {
      // ignored
    }

    return null;
  }

  public String qrCodeAsBase64(String code) throws IOException, InterruptedException {
    final ByteArrayOutputStream os = new ByteArrayOutputStream();

    BufferedImage bufferedImage = qrCode(code);

    ImageIO.write(bufferedImage, "png", Base64.getEncoder().wrap(os));
    return os.toString(StandardCharsets.UTF_8.name());
  }



}
