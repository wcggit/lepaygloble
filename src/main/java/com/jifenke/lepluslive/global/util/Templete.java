package com.jifenke.lepluslive.global.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by wcg on 16/3/30.
 */
public class Templete {

  private String touser;
  private String template_id;
  private String url;
  private Data data;

  public String getTouser() {
    return touser;
  }

  public void setTouser(String touser) {
    this.touser = touser;
  }

  public String getTemplate_id() {
    return template_id;
  }

  public void setTemplate_id(String template_id) {
    this.template_id = template_id;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Data getData() {
    return data;
  }

  public void setData(Data data) {
    this.data = data;
  }

}
