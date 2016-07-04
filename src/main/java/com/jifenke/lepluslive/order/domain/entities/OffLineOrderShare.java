package com.jifenke.lepluslive.order.domain.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by wcg on 16/6/22.
 */
@Entity
@Table(name = "OFF_LINE_ORDER_SHARE")
public class OffLineOrderShare {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String orderSid;

  private Long toTradePartner = 0L;

  private Long toTradePartnerManager = 0L;

  private Long toLockMerchant = 0L;

  private Long toLockPartner = 0L;

  private Long toLockPartnerManager = 0L;

  private Long toLePlusLife = 0L;

  private Date createDate = new Date();

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Long getToLePlusLife() {
    return toLePlusLife;
  }

  public void setToLePlusLife(Long toLePlusLife) {
    this.toLePlusLife = toLePlusLife;
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

  public Long getToTradePartner() {
    return toTradePartner;
  }

  public void setToTradePartner(Long toTradePartner) {
    this.toTradePartner = toTradePartner;
  }

  public Long getToTradePartnerManager() {
    return toTradePartnerManager;
  }

  public void setToTradePartnerManager(Long toTradePartnerManager) {
    this.toTradePartnerManager = toTradePartnerManager;
  }

  public Long getToLockMerchant() {
    return toLockMerchant;
  }

  public void setToLockMerchant(Long toLockMerchant) {
    this.toLockMerchant = toLockMerchant;
  }

  public Long getToLockPartner() {
    return toLockPartner;
  }

  public void setToLockPartner(Long toLockPartner) {
    this.toLockPartner = toLockPartner;
  }

  public Long getToLockPartnerManager() {
    return toLockPartnerManager;
  }

  public void setToLockPartnerManager(Long toLockPartnerManager) {
    this.toLockPartnerManager = toLockPartnerManager;
  }
}
