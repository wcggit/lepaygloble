package com.jifenke.lepluslive.merchant.domain.entities;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Created by wcg on 16/6/3. 佣金比例表
 */
@Entity
@Table(name = "COMMISSION_RATE")
public class CommissionRate {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @OneToOne
  private Merchant merchant;

  private BigDecimal ljCommission=new BigDecimal(0);

  private BigDecimal rebateScoreARate=new BigDecimal(0);

  private BigDecimal rebateScoreBRate=new BigDecimal(0);

  private BigDecimal shareMerchantRate=new BigDecimal(0);

  private BigDecimal sharePartnerRate=new BigDecimal(0);


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

  public BigDecimal getLjCommission() {
    return ljCommission;
  }

  public void setLjCommission(BigDecimal ljCommission) {
    this.ljCommission = ljCommission;
  }

  public BigDecimal getRebateScoreARate() {
    return rebateScoreARate;
  }

  public void setRebateScoreARate(BigDecimal rebateScoreARate) {
    this.rebateScoreARate = rebateScoreARate;
  }

  public BigDecimal getRebateScoreBRate() {
    return rebateScoreBRate;
  }

  public void setRebateScoreBRate(BigDecimal rebateScoreBRate) {
    this.rebateScoreBRate = rebateScoreBRate;
  }

  public BigDecimal getShareMerchantRate() {
    return shareMerchantRate;
  }

  public void setShareMerchantRate(BigDecimal shareMerchantRate) {
    this.shareMerchantRate = shareMerchantRate;
  }

  public BigDecimal getSharePartnerRate() {
    return sharePartnerRate;
  }

  public void setSharePartnerRate(BigDecimal sharePartnerRate) {
    this.sharePartnerRate = sharePartnerRate;
  }
}
