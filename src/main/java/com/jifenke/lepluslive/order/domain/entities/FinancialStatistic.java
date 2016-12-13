package com.jifenke.lepluslive.order.domain.entities;

import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by wcg on 16/5/5.
 */
@Entity
@Table(name = "FINANCIAL_STATISTIC")
public class FinancialStatistic {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String statisticId = MvUtil.getOrderNumber();

  private Date balanceDate; //结算日期

  private Date transferDate; //转账日期

  @ManyToOne
  private Merchant merchant;

  private Long transferPrice;

  private Integer state = 0; //状态2 表示挂帐

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public Merchant getMerchant() {
    return merchant;
  }

  public void setMerchant(Merchant merchant) {
    this.merchant = merchant;
  }

  public Long getTransferPrice() {
    return transferPrice;
  }

  public void setTransferPrice(Long transferPrice) {
    this.transferPrice = transferPrice;
  }

  public Date getTransferDate() {
    return transferDate;
  }

  public void setTransferDate(Date transferDate) {
    this.transferDate = transferDate;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getStatisticId() {
    return statisticId;
  }

  public void setStatisticId(String statisticId) {
    this.statisticId = statisticId;
  }

  public Date getBalanceDate() {
    return balanceDate;
  }

  public void setBalanceDate(Date balanceDate) {
    this.balanceDate = balanceDate;
  }
}
