package com.jifenke.lepluslive.weixin.domain.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 微信模板消息 Created by zhangwen on 2016/5/10.
 */
@Entity
@Table(name = "WX_TEM_MSG")
public class WxTemMsg {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String templateId;  //对应微信中的templateId
  private String url;//对应微信中的url
  private String first;//对应微信中的first.DATA
  private String remark;  //对应微信中的remark.DATA
  private String color;
  private String keynote1;  //对应微信中的keyword1.DATA
  private String keynote2; //对应微信中的keyword2.DATA
  private String keynote3;//对应微信中的keyword3.DATA
  private String keynote4; //对应微信中的keyword4.DATA

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTemplateId() {
    return templateId;
  }

  public void setTemplateId(String templateId) {
    this.templateId = templateId;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getFirst() {
    return first;
  }

  public void setFirst(String first) {
    this.first = first;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public String getKeynote1() {
    return keynote1;
  }

  public void setKeynote1(String keynote1) {
    this.keynote1 = keynote1;
  }

  public String getKeynote2() {
    return keynote2;
  }

  public void setKeynote2(String keynote2) {
    this.keynote2 = keynote2;
  }

  public String getKeynote3() {
    return keynote3;
  }

  public void setKeynote3(String keynote3) {
    this.keynote3 = keynote3;
  }

  public String getKeynote4() {
    return keynote4;
  }

  public void setKeynote4(String keynote4) {
    this.keynote4 = keynote4;
  }

}
