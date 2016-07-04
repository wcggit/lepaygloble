package com.jifenke.lepluslive.weixin.domain.entities;

/**
 * Created by wcg on 16/3/31.
 */
public class AccessToken {


  public AccessToken() {
  }

  public AccessToken(String accessToken, String jsapiTickek) {
    this.accessToken = accessToken;
    this.jsapiTickek = jsapiTickek;
  }

  private String accessToken;

  private String jsapiTickek;

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getJsapiTickek() {
    return jsapiTickek;
  }

  public void setJsapiTickek(String jsapiTickek) {
    this.jsapiTickek = jsapiTickek;
  }
}
