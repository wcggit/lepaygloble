package com.jifenke.lepluslive.order.domain.entities;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wcg on 16/8/2.
 */
@Entity
@Table(name = "MERCHANT_POS")
public class MerchantPos {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne
  private Merchant merchant;

  private String posId;

  private String sshKey;

  private String psamCard;

  private BigDecimal debitCardCommission; //借记卡非会员佣金比

  private BigDecimal ljCommission; //会员刷卡消费佣金比 或者纯红包消费佣金比

  private BigDecimal wxCommission; // 微信非会员佣金比

  private BigDecimal aliCommission; //阿里非会员佣金比

  private BigDecimal creditCardCommission; //贷记卡非会员佣金比

  private Long ceil; //借记卡非会员封顶手续费

  private BigDecimal wxUserCommission;//微信会员佣金比

  private BigDecimal aliUserCommission; //支付宝会员佣金比

  private BigDecimal scoreARebate;//导流订单返红包比

  private BigDecimal scoreBRebate; //导流订单返积分比

  private BigDecimal userScoreARebate; //会员订单返红包比

  private BigDecimal userScoreBRebate; //会员订单返积分比

  public BigDecimal getScoreARebate() {
    return scoreARebate;
  }

  public void setScoreARebate(BigDecimal scoreARebate) {
    this.scoreARebate = scoreARebate;
  }

  public BigDecimal getScoreBRebate() {
    return scoreBRebate;
  }

  public void setScoreBRebate(BigDecimal scoreBRebate) {
    this.scoreBRebate = scoreBRebate;
  }

  public BigDecimal getUserScoreARebate() {
    return userScoreARebate;
  }

  public void setUserScoreARebate(BigDecimal userScoreARebate) {
    this.userScoreARebate = userScoreARebate;
  }

  public BigDecimal getUserScoreBRebate() {
    return userScoreBRebate;
  }

  public void setUserScoreBRebate(BigDecimal userScoreBRebate) {
    this.userScoreBRebate = userScoreBRebate;
  }

  public BigDecimal getWxUserCommission() {
    return wxUserCommission;
  }

  public void setWxUserCommission(BigDecimal wxUserCommission) {
    this.wxUserCommission = wxUserCommission;
  }

  public BigDecimal getAliUserCommission() {
    return aliUserCommission;
  }

  public void setAliUserCommission(BigDecimal aliUserCommission) {
    this.aliUserCommission = aliUserCommission;
  }

  public BigDecimal getLjCommission() {
    return ljCommission;
  }

  public void setLjCommission(BigDecimal ljCommission) {
    this.ljCommission = ljCommission;
  }

  public String getPsamCard() {
    return psamCard;
  }

  public void setPsamCard(String psamCard) {
    this.psamCard = psamCard;
  }


  public BigDecimal getWxCommission() {
    return wxCommission;
  }

  public void setWxCommission(BigDecimal wxCommission) {
    this.wxCommission = wxCommission;
  }

  public BigDecimal getAliCommission() {
    return aliCommission;
  }

  public void setAliCommission(BigDecimal aliCommission) {
    this.aliCommission = aliCommission;
  }

  public Long getCeil() {
    return ceil;
  }

  public void setCeil(Long ceil) {
    this.ceil = ceil;
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

  public String getPosId() {
    return posId;
  }

  public void setPosId(String posId) {
    this.posId = posId;
  }

  public String getSshKey() {
    return sshKey;
  }

  public void setSshKey(String sshKey) {
    this.sshKey = sshKey;
  }


  public BigDecimal getDebitCardCommission() {
    return debitCardCommission;
  }

  public void setDebitCardCommission(BigDecimal debitCardCommission) {
    this.debitCardCommission = debitCardCommission;
  }

  public BigDecimal getCreditCardCommission() {
    return creditCardCommission;
  }

  public void setCreditCardCommission(BigDecimal creditCardCommission) {
    this.creditCardCommission = creditCardCommission;
  }

  private Date createdDate;                  // 创建时间

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }
}
