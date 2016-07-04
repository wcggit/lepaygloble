package com.jifenke.lepluslive.global.util;

/**
 * Created by wcg on 16/3/30.
 */
public class DataInfo {
  private String value;
  private String color;

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public DataInfo(String value, String color) {
    this.value = value;
    this.color = color;
  }
}
