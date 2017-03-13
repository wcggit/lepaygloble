package com.jifenke.lepluslive.global.util;


import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class HttpClientUtil {

  private static Logger log = LoggerFactory.getLogger(HttpClientUtil.class);

  /**
   * HTTP GET
   */
  public static Map<Object, Object> get(String getUrl) {

    CloseableHttpClient client = HttpClients.createDefault();
    HttpGet httpGet = new HttpGet(getUrl);
    httpGet.addHeader("Content-Type", "application/json");
    CloseableHttpResponse response = null;
    try {
      response = client.execute(httpGet);
      HttpEntity entity = response.getEntity();
      ObjectMapper mapper = new ObjectMapper();
      Map<Object, Object>
          map =
          mapper.readValue(new BufferedReader(new InputStreamReader(entity.getContent(), "utf-8")),
                           Map.class);
      EntityUtils.consume(entity);
      response.close();
      return map;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String post(String url, Map<String, String> params, String charset) {
    DefaultHttpClient httpclient = new DefaultHttpClient();
    String body = null;

    log.info("create httppost:" + url);
    HttpPost post = postForm(url, params);
    body = invoke(httpclient, post, charset);

    httpclient.getConnectionManager().shutdown();

    return body;
  }

  public static String get(String url, String charset) {
    DefaultHttpClient httpclient = new DefaultHttpClient();
    String body = null;

    log.info("create httppost:" + url);
    HttpGet get = new HttpGet(url);
    body = invoke(httpclient, get, charset);

    httpclient.getConnectionManager().shutdown();

    return body;
  }

  private static String invoke(DefaultHttpClient httpclient,
                               HttpUriRequest httpost, String charset) {

    HttpResponse response = sendRequest(httpclient, httpost, charset);
    String body = paseResponse(response);

    return body;
  }

  private static String paseResponse(HttpResponse response) {
    log.info("get response from http server..");
    HttpEntity entity = response.getEntity();

    log.info("response status: " + response.getStatusLine());
    String charset = EntityUtils.getContentCharSet(entity);
    log.info(charset);

    String body = null;
    try {
      body = EntityUtils.toString(entity);
      log.info(body);
    } catch (ParseException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return body;
  }

  private static HttpResponse sendRequest(DefaultHttpClient httpclient,
                                          HttpUriRequest httpost, String charset) {
    HttpResponse response = null;
    try {
      if (charset != null) {
        httpost.setHeader("Content-type", "application/x-www-form-urlencoded;charset=" + charset);
      } else {
        httpost.setHeader("Content-type", "application/x-www-form-urlencoded;charset=iso8859-1");
      }
      response = httpclient.execute(httpost);

    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return response;
  }

  /**
   *
   * @param url
   * @param params
   * @return
   */
  private static HttpPost postForm(String url, Map<String, String> params) {

    HttpPost httpost = new HttpPost(url);
    List<NameValuePair> nvps = new ArrayList<NameValuePair>();

    Set<String> keySet = params.keySet();
    for (String key : keySet) {
      nvps.add(new BasicNameValuePair(key, params.get(key)));
      log.info(key + "------" + params.get(key));
    }

    try {
      log.info("set utf-8 form entity to httppost");
      httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }

    return httpost;
  }
}
