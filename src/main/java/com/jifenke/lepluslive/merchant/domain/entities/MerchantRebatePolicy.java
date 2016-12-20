package com.jifenke.lepluslive.merchant.domain.entities;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by wcg on 2016/11/7.商户发放红包积分策略
 */
@Entity
@Table(name = "MERCHANT_REBATE_POLICY")
public class MerchantRebatePolicy {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Long merchantId;

  private Integer rebateFlag; //是否开启会员订单scoreBRebate     0-不开启（按比例）  1-开启（全部） 2-不开启

  private BigDecimal importScoreBScale;//导流订单发放积分策略    【导流订单积分】

  private BigDecimal userScoreBScale;//会员订单按比例发放积分策略返积分比  【会员订单-比例-积分】

  private BigDecimal userScoreBScaleB;//会员订单全额发放积分策略返积分比   【会员订单-全额-积分】

  private BigDecimal userScoreAScale;//会员订单按比例发放积分策略返红包比  【会员订单-比例-红包】

  private Integer stageOne;//0%~20%

  private Integer stageTwo;//20%~40%

  private Integer stageThree;//40%·60%

  private Integer stageFour;//60%~80%


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getMerchantId() {
    return merchantId;
  }

  public void setMerchantId(Long merchantId) {
    this.merchantId = merchantId;
  }

  public Integer getRebateFlag() {
    return rebateFlag;
  }

  public void setRebateFlag(Integer rebateFlag) {
    this.rebateFlag = rebateFlag;
  }

  public BigDecimal getImportScoreBScale() {
    return importScoreBScale;
  }

  public void setImportScoreBScale(BigDecimal importScoreBScale) {
    this.importScoreBScale = importScoreBScale;
  }

  public BigDecimal getUserScoreBScale() {
    return userScoreBScale;
  }

  public void setUserScoreBScale(BigDecimal userScoreBScale) {
    this.userScoreBScale = userScoreBScale;
  }

  public BigDecimal getUserScoreBScaleB() {
    return userScoreBScaleB;
  }

  public void setUserScoreBScaleB(BigDecimal userScoreBScaleB) {
    this.userScoreBScaleB = userScoreBScaleB;
  }

  public BigDecimal getUserScoreAScale() {
    return userScoreAScale;
  }

  public void setUserScoreAScale(BigDecimal userScoreAScale) {
    this.userScoreAScale = userScoreAScale;
  }

  public Integer getStageOne() {
    return stageOne;
  }

  public void setStageOne(Integer stageOne) {
    this.stageOne = stageOne;
  }

  public Integer getStageTwo() {
    return stageTwo;
  }

  public void setStageTwo(Integer stageTwo) {
    this.stageTwo = stageTwo;
  }

  public Integer getStageThree() {
    return stageThree;
  }

  public void setStageThree(Integer stageThree) {
    this.stageThree = stageThree;
  }

  public Integer getStageFour() {
    return stageFour;
  }

  public void setStageFour(Integer stageFour) {
    this.stageFour = stageFour;
  }
}
