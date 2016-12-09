package com.jifenke.lepluslive.order.domain.entities;

import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wcg on 16/8/2.
 */
@Entity
@Table(name = "POS_ORDER")
public class PosOrder {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String orderSid;

  private Date createdDate;

  private Date completeDate;

  @ManyToOne
  private LeJiaUser leJiaUser;

  @ManyToOne
  private MerchantPos merchantPos;

  @ManyToOne
  private Merchant merchant;

  private Long truePayCommission;//实际支付手续费

  private Integer rebateWay; //1 代表非会员消费 2 代表会员订单 3 导流订单 4 会员普通订单

  private Long ljCommission = 0L; //乐加佣金

  private Long trueScore = 0L; //实际使用红包 （订单总金额-实际支付）

  private Long wxCommission = 0L; //三方手续费

  private Long rebate = 0L; //返利红包

  private Long scoreB = 0L; //发放积分

  private Integer state = 0; //支付状态

  private Long transferMoney; //每笔应该转给商户的金额  (商户实际到账 = 第三方渠道到账 + 乐加支付)
  private Long transferByBank; //第三方支付转给商户的金额

  private Long totalPrice;   //订单总金额

  private Long truePay = 0L; //实际支付 (订单总金额-实际使用红包)

  private Integer tradeFlag; //0支付宝、3POS刷卡、4微信、5纯积分（会员登录后不能用现金交易） 6现金支付

  private Integer paidType;  //1,非会员消费   2只用货币完成交易、3混用了货币积分完成交易、4只用积分完成了交易

  private String cardNo; //如果刷卡支付 则为卡号

  private Integer cardType; //卡的类型

  public Long getTruePayCommission() {
    return truePayCommission;
  }

  public void setTruePayCommission(Long truePayCommission) {
    this.truePayCommission = truePayCommission;
  }

  public String getCardNo() {
    return cardNo;
  }

  public void setCardNo(String cardNo) {
    this.cardNo = cardNo;
  }

  public Integer getCardType() {
    return cardType;
  }

  public void setCardType(Integer cardType) {
    this.cardType = cardType;
  }

  public Long getTransferByBank() {
    return transferByBank;
  }

  public void setTransferByBank(Long transferByBank) {
    this.transferByBank = transferByBank;
  }

  public Long getTruePay() {
    return truePay;
  }

  public void setTruePay(Long truePay) {
    this.truePay = truePay;
  }

  public Long getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(Long totalPrice) {
    this.totalPrice = totalPrice;
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

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public Date getCompleteDate() {
    return completeDate;
  }

  public void setCompleteDate(Date completeDate) {
    this.completeDate = completeDate;
  }

  public LeJiaUser getLeJiaUser() {
    return leJiaUser;
  }

  public void setLeJiaUser(LeJiaUser leJiaUser) {
    this.leJiaUser = leJiaUser;
  }

  public MerchantPos getMerchantPos() {
    return merchantPos;
  }

  public void setMerchantPos(MerchantPos merchantPos) {
    this.merchantPos = merchantPos;
  }

  public Long getLjCommission() {
    return ljCommission;
  }

  public void setLjCommission(Long ljCommission) {
    this.ljCommission = ljCommission;
  }

  public Long getTrueScore() {
    return trueScore;
  }

  public void setTrueScore(Long trueScore) {
    this.trueScore = trueScore;
  }

  public Long getWxCommission() {
    return wxCommission;
  }

  public void setWxCommission(Long wxCommission) {
    this.wxCommission = wxCommission;
  }

  public Long getRebate() {
    return rebate;
  }

  public void setRebate(Long rebate) {
    this.rebate = rebate;
  }

  public Long getScoreB() {
    return scoreB;
  }

  public void setScoreB(Long scoreB) {
    this.scoreB = scoreB;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public Long getTransferMoney() {
    return transferMoney;
  }

  public void setTransferMoney(Long transferMoney) {
    this.transferMoney = transferMoney;
  }

  public Integer getTradeFlag() {
    return tradeFlag;
  }

  public void setTradeFlag(Integer tradeFlag) {
    this.tradeFlag = tradeFlag;
  }

  public Integer getPaidType() {
    return paidType;
  }

  public void setPaidType(Integer paidType) {
    this.paidType = paidType;
  }

  public Merchant getMerchant() {
    return merchant;
  }

  public void setMerchant(Merchant merchant) {
    this.merchant = merchant;
  }

  public Integer getRebateWay() {
    return rebateWay;
  }

  public void setRebateWay(Integer rebateWay) {
    this.rebateWay = rebateWay;
  }
}
