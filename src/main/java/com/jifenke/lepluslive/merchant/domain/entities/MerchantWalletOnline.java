package com.jifenke.lepluslive.merchant.domain.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * 商户线上订单钱包 Created by zhangwen on 16/11/05.
 */
@Entity
@Table(name = "MERCHANT_WALLET_ONLINE")
public class MerchantWalletOnline {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Long availableBalance = 0L; //可用佣金余额

  private Long totalMoney = 0L; //获得佣金总额

  private Long totalWithdrawals = 0L;//已经提现总额

  private Date createDate = new Date();

  private Date lastUpdate;  //最后更新时间

  @OneToOne
  private Merchant merchant;

  @Version
  private Long version = 0L;

  public Long getAvailableBalance() {
    return availableBalance;
  }

  public void setAvailableBalance(Long availableBalance) {
    this.availableBalance = availableBalance;
  }

  public Long getTotalMoney() {
    return totalMoney;
  }

  public void setTotalMoney(Long totalMoney) {
    this.totalMoney = totalMoney;
  }

  public Long getTotalWithdrawals() {
    return totalWithdrawals;
  }

  public void setTotalWithdrawals(Long totalWithdrawals) {
    this.totalWithdrawals = totalWithdrawals;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public Date getLastUpdate() {
    return lastUpdate;
  }

  public void setLastUpdate(Date lastUpdate) {
    this.lastUpdate = lastUpdate;
  }
}
