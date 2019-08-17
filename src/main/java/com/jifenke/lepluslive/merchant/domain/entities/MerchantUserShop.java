package com.jifenke.lepluslive.merchant.domain.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * 账号对应的门店列表 Created by zhangwen on 2017/2/7.
 */
@Entity
@Table(name = "MERCHANT_USER_SHOP")
public class MerchantUserShop {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne
  private MerchantUser merchantUser;

  @ManyToOne
  private Merchant merchant;

  private Date createDate = new Date();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public MerchantUser getMerchantUser() {
    return merchantUser;
  }

  public void setMerchantUser(MerchantUser merchantUser) {
    this.merchantUser = merchantUser;
  }

  public Merchant getMerchant() {
    return merchant;
  }

  public void setMerchant(Merchant merchant) {
    this.merchant = merchant;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }
}
