package com.jifenke.lepluslive.order.domain.entities;

import javax.persistence.*;

/**
 * 订单来源和支付方式
 * Created by wcg on 16/5/5.
 */
@Entity
@Table(name = "PAY_ORIGIN")
public class PayOrigin {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Integer payFrom;   //支付来源 1=app 2=公众号 3=线下扫码

  private Integer payType;   //支付方式  0=未选择 1=微信 2=微信+A积分 3=微信+B积分

  private String detail;  //描述

  public String getDetail() {
    return detail;
  }

  public void setDetail(String detail) {
    this.detail = detail;
  }

  public Integer getPayFrom() {

    return payFrom;
  }

  public Integer getPayType() {
    return payType;
  }

  public void setPayType(Integer payType) {
    this.payType = payType;
  }

  public void setPayFrom(Integer payFrom) {
    this.payFrom = payFrom;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public PayOrigin(Long id) {
    this.id = id;
  }

  public PayOrigin() {
  }
}
