package com.jifenke.lepluslive.merchant.domain.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by wcg on 16/6/3.
 * 商户轮播图
 */
@Entity
@Table(name = "MERCHANT_SCROLL")
public class MerchantScroll {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String picture;

  private Long sid;

  @ManyToOne
  private Merchant merchant;

  public Long getSid() {
    return sid;
  }

  public void setSid(Long sid) {
    this.sid = sid;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPicture() {
    return picture;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }

  public Merchant getMerchant() {
    return merchant;
  }

  public void setMerchant(Merchant merchant) {
    this.merchant = merchant;
  }
}
