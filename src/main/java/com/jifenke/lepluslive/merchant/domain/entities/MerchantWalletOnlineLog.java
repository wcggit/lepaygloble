package com.jifenke.lepluslive.merchant.domain.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * 商户线上钱包记录 Created by zhangwen on 16/11/05.
 */
@Entity
@Table(name = "MERCHANT_WALLET_ONLINE_LOG")
public class MerchantWalletOnlineLog {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String orderSid;

  private Long merchantId;

  private Long beforeChangeMoney; //商户钱包改变前金额

  private Long afterChangeMoney; //改变后的金额

  private Long changeMoney;   //线上钱包改变金额 理论=beforeChangeMoney-afterChangeMoney

  private Long type; //如果为1代表app线上订单分润  2代表公众号线上订单分润

  private Date createDate = new Date();

  public Long getType() {
    return type;
  }

  public void setType(Long type) {
    this.type = type;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getOrderSid() {
    return orderSid;
  }

  public void setOrderSid(String orderSid) {
    this.orderSid = orderSid;
  }

  public Long getMerchantId() {
    return merchantId;
  }

  public void setMerchantId(Long merchantId) {
    this.merchantId = merchantId;
  }

  public Long getBeforeChangeMoney() {
    return beforeChangeMoney;
  }

  public void setBeforeChangeMoney(Long beforeChangeMoney) {
    this.beforeChangeMoney = beforeChangeMoney;
  }

  public Long getAfterChangeMoney() {
    return afterChangeMoney;
  }

  public Long getChangeMoney() {
    return changeMoney;
  }

  public void setChangeMoney(Long changeMoney) {
    this.changeMoney = changeMoney;
  }

  public void setAfterChangeMoney(Long afterChangeMoney) {
    this.afterChangeMoney = afterChangeMoney;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }
}
