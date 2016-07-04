package com.jifenke.lepluslive.global.util;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by wcg on 16/6/6.
 */
public class ImageLoad {

  public static byte[] saveToFile(String destUrl) {
    // FileOutputStream fos = null;
    InputStream stream = null;
    HttpURLConnection httpUrl = null;
    URL url = null;
    int BUFFER_SIZE = 1024;
    byte[] buf = new byte[BUFFER_SIZE];
    int size = 0;
    try {
      url = new URL(destUrl);
      httpUrl = (HttpURLConnection) url.openConnection();
      httpUrl.connect();
//      bis = new BufferedInputStream();
      stream = httpUrl.getInputStream();

      return IOUtils.toByteArray(stream);
//      fos = new FileOutputStream("c:\\haha.gif");
//      while ((size = bis.read(buf)) != -1) {
//        fos.write(buf, 0, size);
//      }
//      fos.flush();
    } catch (Exception e) {

    }
    return null;
  }

  public static InputStream returnStream(String destUrl) {
    // FileOutputStream fos = null;
    InputStream stream = null;
    HttpURLConnection httpUrl = null;
    URL url = null;
    int BUFFER_SIZE = 1024;
    byte[] buf = new byte[BUFFER_SIZE];
    int size = 0;
    try {
      url = new URL(destUrl);
      httpUrl = (HttpURLConnection) url.openConnection();
      httpUrl.connect();
//      bis = new BufferedInputStream();
      stream = httpUrl.getInputStream();

      return stream;
//      fos = new FileOutputStream("c:\\haha.gif");
//      while ((size = bis.read(buf)) != -1) {
//        fos.write(buf, 0, size);
//      }
//      fos.flush();
    } catch (Exception e) {

    }
    return null;
  }

}
