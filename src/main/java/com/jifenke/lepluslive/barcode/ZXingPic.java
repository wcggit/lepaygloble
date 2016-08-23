package com.jifenke.lepluslive.barcode;//package com.jifenke.lepluslive.barcode;
//
///**
//* Created by wcg on 16/6/2.
//*/
//
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.OutputStream;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.imageio.ImageIO;
//import javax.swing.*;
//
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.Binarizer;
//import com.google.zxing.BinaryBitmap;
//import com.google.zxing.EncodeHintType;
//import com.google.zxing.LuminanceSource;
//import com.google.zxing.MultiFormatReader;
//import com.google.zxing.MultiFormatWriter;
//import com.google.zxing.Result;
//import com.google.zxing.WriterException;
//import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
//import com.google.zxing.common.BitMatrix;
//import com.google.zxing.common.HybridBinarizer;
//import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
//
//import com.jifenke.lepluslive.barcode.service.BarcodeService;
//import com.sun.image.codec.jpeg.JPEGCodec;
//import com.sun.image.codec.jpeg.JPEGEncodeParam;
//import com.sun.image.codec.jpeg.JPEGImageEncoder;
//
//import org.apache.commons.io.FileUtils;
//
//public class ZXingPic {
//
//  public static void main(String[] args) throws WriterException {
//    String content = "【优秀员工】恭喜您，中奖了！！！领取方式，请拨打电话：15998099997*咨询。";
//    String filePath = "/Users/wcg/Downloads/qrCode.png";
//
//    // if(args.length != 2)
//    // {
//    // System.out.println("没有内容,图片生成失败!");
//    // System.exit(0);
//    // }
//
//    try {
//      File file = new File(filePath);
//      if (file.exists()) {
//        file = new File("/Users/wcg/Downloads/", new Date().getTime() + ".jpg");
//      }
//
//      ZXingPic zp = new ZXingPic();
//
////      BufferedImage
////          bim =
////          zp.getQR_CODEBufferedImage(content, BarcodeFormat.QR_CODE, 250, 250,
////                                     zp.getDecodeHintType());
//
//     // ImageIO.write(bim, "jpeg", file);
//
//      byte[] bytes = new BarcodeService().qrCode("1", BarcodeConfig.QRCode.defaultConfig());
//
//      FileUtils.writeByteArrayToFile(file, bytes);
//
//      zp.addLogo_QRCode( new File(filePath),file, new LogoConfig());
//
//      zp.createStringMark("/Users/wcg/Downloads/newPic.jpg","功夫鸡排++",Color.white,100,"/Users/wcg/Downloads/newPic.jpg");
//
//    //  Thread.sleep(5000);
//     // zp.parseQR_CODEImage(new File("D:/newPic.png"));
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//
//  /**
//   * 给二维码图片添加Logo
//   */
//  public void addLogo_QRCode(File qrPic, File logoPic, LogoConfig logoConfig) {
//    try {
//      if (!qrPic.isFile() || !logoPic.isFile()) {
//        System.out.print("file not find !");
//        System.exit(0);
//      }
//
//      /**
//       * 读取二维码图片，并构建绘图对象
//       */
//      BufferedImage image = ImageIO.read(qrPic);
//      Graphics2D g = image.createGraphics();
//
//      /**
//       * 读取Logo图片
//       */
//      BufferedImage logo = ImageIO.read(logoPic);
//
//      int widthLogo = logo.getWidth(), heightLogo = logo.getHeight();
//
//      // 计算图片放置位置
//      int x = (image.getWidth() - widthLogo) / 2;
//      int y = (image.getHeight() - logo.getHeight()) / 2;
//        //开始绘制图片
//      g.drawImage(logo, x, y, widthLogo, heightLogo, null);
//      g.drawRoundRect(x, y, widthLogo, heightLogo, 20, 20);
//      g.setStroke(new BasicStroke(1.0f));
//      g.setColor(logoConfig.getBorderColor());
//      g.drawRect(x, y, widthLogo,heightLogo);
//
//      g.dispose();
//
//      ImageIO.write(image, "jpeg", new File("/Users/wcg/Downloads/newPic.jpg"));
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//
//  /**
//   * 二维码的解析
//   */
//  public void parseQR_CODEImage(File file) {
//    try {
//      MultiFormatReader formatReader = new MultiFormatReader();
//
//      // File file = new File(filePath);
//      if (!file.exists()) {
//        return;
//      }
//
//      BufferedImage image = ImageIO.read(file);
//
//      LuminanceSource source = new BufferedImageLuminanceSource(image);
//      Binarizer binarizer = new HybridBinarizer(source);
//      BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
//
//      Map hints = new HashMap();
//      hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
//
//      Result result = formatReader.decode(binaryBitmap, hints);
//
//      System.out.println("result = " + result.toString());
//      System.out.println("resultFormat = " + result.getBarcodeFormat());
//      System.out.println("resultText = " + result.getText());
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//
//  /**
//   * 将二维码生成为文件
//   */
//  public void decodeQR_CODE2ImageFile(BitMatrix bm, String imageFormat, File file) {
//    try {
//      if (null == file || file.getName().trim().isEmpty()) {
//        throw new IllegalArgumentException("文件异常，或扩展名有问题！");
//      }
//
//      BufferedImage bi = fileToBufferedImage(bm);
//      ImageIO.write(bi, "jpeg", file);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//
//  /**
//   * 将二维码生成为输出流
//   */
//  public void decodeQR_CODE2OutputStream(BitMatrix bm, String imageFormat, OutputStream os) {
//    try {
//      BufferedImage image = fileToBufferedImage(bm);
//      ImageIO.write(image, imageFormat, os);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//
//  /**
//   * 构建初始化二维码
//   */
//  public BufferedImage fileToBufferedImage(BitMatrix bm) {
//    BufferedImage image = null;
//    try {
//      int w = bm.getWidth(), h = bm.getHeight();
//      image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
//
//      for (int x = 0; x < w; x++) {
//        for (int y = 0; y < h; y++) {
//          image.setRGB(x, y, bm.get(x, y) ? 0xFF000000 : 0xFFCCDDEE);
//        }
//      }
//
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//    return image;
//  }
//
//  /**
//   * 生成二维码bufferedImage图片
//   *
//   * @param content       编码内容
//   * @param barcodeFormat 编码类型
//   * @param width         图片宽度
//   * @param height        图片高度
//   * @param hints         设置参数
//   */
//  public BufferedImage getQR_CODEBufferedImage(String content, BarcodeFormat barcodeFormat,
//                                               int width, int height,
//                                               Map<EncodeHintType, ?> hints) {
//    MultiFormatWriter multiFormatWriter = null;
//    BitMatrix bm = null;
//    BufferedImage image = null;
//    try {
//      multiFormatWriter = new MultiFormatWriter();
//
//      // 参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
//      bm = multiFormatWriter.encode(content, barcodeFormat, width, height, hints);
//
//      int w = bm.getWidth();
//      int h = bm.getHeight();
//      image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
//
//      // 开始利用二维码数据创建Bitmap图片，分别设为黑（0xFFFFFFFF）白（0xFF000000）两色
//      for (int x = 0; x < w; x++) {
//        for (int y = 0; y < h; y++) {
//          image.setRGB(x, y, bm.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
//        }
//      }
//    } catch (WriterException e) {
//      e.printStackTrace();
//    }
//    return image;
//  }
//
//  /**
//   * 设置二维码的格式参数
//   */
//  public Map<EncodeHintType, Object> getDecodeHintType() {
//    // 用于设置QR二维码参数
//    Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
//    // 设置QR二维码的纠错级别（H为最高级别）具体级别信息
//    hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
//    // 设置编码方式
//    hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
//    hints.put(EncodeHintType.MAX_SIZE, 350);
//    hints.put(EncodeHintType.MIN_SIZE, 100);
//    hints.put(EncodeHintType.MARGIN, 0);
//    return hints;
//  }
//
//  //给jpg添加文字
//  public  boolean createStringMark(String filePath,String markContent,Color markContentColor,float qualNum ,String outPath)
//  {
//    ImageIcon imgIcon=new ImageIcon(filePath);
//    Image theImg =imgIcon.getImage();
//    int width=theImg.getWidth(null)==-1?200:theImg.getWidth(null);
//    int height= theImg.getHeight(null)==-1?200:theImg.getHeight(null);
//    System.out.println(width);
//    System.out.println(height);
//    System.out.println(theImg);
//    BufferedImage bimage = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
//    Graphics2D g=bimage.createGraphics();
//    g.setColor(markContentColor);
//    g.setBackground(Color.red);
//    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//    g.drawImage(theImg, 0, 0, null );
//    g.setFont(new Font(null, 12, 88)); //字体、字型、字号
//    g.drawString(markContent, (width - g.getFontMetrics().stringWidth(markContent)) / 2,1700); //画文字
//    g.dispose();
//    try
//    {
//      FileOutputStream out=new FileOutputStream(outPath); //先用一个特定的输出文件名
//      JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//      JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bimage);
//      param.setQuality(qualNum, true);
//      encoder.encode(bimage, param);
//      out.close();
//    }
//    catch(Exception e)
//    { return false; }
//    return true;
//  }
//}
//
//
//
//class LogoConfig {
//
//  // logo默认边框颜色
//  public static final Color DEFAULT_BORDERCOLOR = Color.WHITE;
//  // logo默认边框宽度
//  public static final int DEFAULT_BORDER = 2;
//  // logo大小默认为照片的1/5
//  public static final int DEFAULT_LOGOPART = 5;
//
//  private final int border = DEFAULT_BORDER;
//  private final Color borderColor;
//  private final int logoPart;
//
//  /**
//   * Creates a default config with on color {@link #BLACK} and off color {@link #WHITE}, generating
//   * normal black-on-white barcodes.
//   */
//  public LogoConfig() {
//    this(DEFAULT_BORDERCOLOR, DEFAULT_LOGOPART);
//  }
//
//  public LogoConfig(Color borderColor, int logoPart) {
//    this.borderColor = borderColor;
//    this.logoPart = logoPart;
//  }
//
//  public Color getBorderColor() {
//    return borderColor;
//  }
//
//  public int getBorder() {
//    return border;
//  }
//
//  public int getLogoPart() {
//    return logoPart;
//  }
//
//
//}
