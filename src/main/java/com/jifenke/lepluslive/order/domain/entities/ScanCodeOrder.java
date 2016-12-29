package com.jifenke.lepluslive.order.domain.entities;

import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.weixin.domain.entities.Category;
import org.hibernate.annotations.GenericGenerator;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * 富友线下扫码订单 Created by zhangwen on 16/11/25.
 */
@Entity
@Table(name = "SCAN_CODE_ORDER")
public class ScanCodeOrder {

  @Id
  @GeneratedValue(generator = "system-uuid")
  @GenericGenerator(name = "system-uuid", strategy = "uuid")
  private String id;

  private String orderSid = MvUtil.getOrderNumber();

  private String orderCode;  //第三方渠道流水号

  private Date createdDate;

  private Date completeDate;

  private Date refundDate;  //退款时间

  @ManyToOne
  private LeJiaUser leJiaUser;

  @ManyToOne
  private Merchant merchant;

  @NotNull
  private Long merchantUserId;  //门店对应的商户ID

  @NotNull
  private Integer channel = 0;  //支付渠道  0=微信|1=支付宝

  private Integer source = 0;   //支付来源  0=WAP|1=APP

  @NotNull
  private Integer payment = 0;  //付款方式  0=纯现金|1=纯红包|2=混合

  @NotNull
  private Integer state = 0; //支付状态  0=未支付|1=已支付|2=已退款

  private String merchantNum;  //该订单使用的富友商户号

  private String merchantRate;  //商户号当时的佣金费率

  @NotNull
  @ManyToOne
  private Category orderType;//订单类型
  @NotNull
  private Long totalPrice = 0L;

  @NotNull
  private Long truePay = 0L;

  @NotNull
  private Long trueScore = 0L; //实际使用红包

  private Long commission = 0L; //乐加总佣金=truePayCommission+scoreCommission

  private Long truePayCommission = 0L; //实付部分佣金

  private Long scoreCommission = 0L; //红包部分佣金

  private Long wxCommission = 0L; //微信手续费(分润用)=totalPrice*0.6%

  private Long wxTrueCommission = 0L;  //微信实际手续费(对积分客)=truePay*0.35%

  private Long rebate = 0L; //返利红包

  private Long scoreB = 0L; //发放积分

  private Long share = 0L;  //待分润金额(对于导流订单和会员订单)=commission-rebate-wxCommission>=0

  private Long transferMoney = 0L; //商户实际入账(包括现金和红包)=transferMoneyFromTruePay+transferMoneyFromScore

  private Long transferMoneyFromTruePay = 0L; //每笔订单中现金支付转给商户的金额

  private Long transferMoneyFromScore = 0L;   //每笔订单中红包支付转给商户的金额

  private Integer messageState = 0; //发送模版消息状态

  @Column(nullable = false, length = 10)
  private String lePayCode; //四位数支付码

  private Long ljProfit = 0L;//每笔订单的额外收入=最多发放红包-实际发放红包

  public Long getLjProfit() {
    return ljProfit;
  }

  public void setLjProfit(Long ljProfit) {
    this.ljProfit = ljProfit;
  }

  public Long getTransferMoneyFromTruePay() {
    return transferMoneyFromTruePay;
  }

  public void setTransferMoneyFromTruePay(Long transferMoneyFromTruePay) {
    this.transferMoneyFromTruePay = transferMoneyFromTruePay;
  }

  public String getOrderCode() {
    return orderCode;
  }

  public void setOrderCode(String orderCode) {
    this.orderCode = orderCode;
  }

  public String getMerchantNum() {
    return merchantNum;
  }

  public void setMerchantNum(String merchantNum) {
    this.merchantNum = merchantNum;
  }

  public String getMerchantRate() {
    return merchantRate;
  }

  public void setMerchantRate(String merchantRate) {
    this.merchantRate = merchantRate;
  }

  public Long getShare() {
    return share;
  }

  public void setShare(Long share) {
    this.share = share;
  }

  public Integer getMessageState() {
    return messageState;
  }

  public void setMessageState(Integer messageState) {
    this.messageState = messageState;
  }

  public Integer getSource() {
    return source;
  }

  public void setSource(Integer source) {
    this.source = source;
  }

  public Category getOrderType() {
    return orderType;
  }

  public void setOrderType(Category orderType) {
    this.orderType = orderType;
  }

  public Long getTransferMoney() {
    return transferMoney;
  }

  public void setTransferMoney(Long transferMoney) {
    this.transferMoney = transferMoney;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
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

  public Merchant getMerchant() {
    return merchant;
  }

  public void setMerchant(Merchant merchant) {
    this.merchant = merchant;
  }

  public Date getRefundDate() {
    return refundDate;
  }

  public void setRefundDate(Date refundDate) {
    this.refundDate = refundDate;
  }

  public Long getMerchantUserId() {
    return merchantUserId;
  }

  public void setMerchantUserId(Long merchantUserId) {
    this.merchantUserId = merchantUserId;
  }

  public Integer getChannel() {
    return channel;
  }

  public void setChannel(Integer channel) {
    this.channel = channel;
  }

  public Integer getPayment() {
    return payment;
  }

  public void setPayment(Integer payment) {
    this.payment = payment;
  }

  public Long getWxTrueCommission() {
    return wxTrueCommission;
  }

  public void setWxTrueCommission(Long wxTrueCommission) {
    this.wxTrueCommission = wxTrueCommission;
  }

  public Long getTransferMoneyFromScore() {
    return transferMoneyFromScore;
  }

  public void setTransferMoneyFromScore(Long transferMoneyFromScore) {
    this.transferMoneyFromScore = transferMoneyFromScore;
  }

  public String getLePayCode() {
    return lePayCode;
  }

  public void setLePayCode(String lePayCode) {
    this.lePayCode = lePayCode;
  }

  public Long getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(Long totalPrice) {
    this.totalPrice = totalPrice;
  }

  public Long getTruePay() {
    return truePay;
  }

  public void setTruePay(Long truePay) {
    this.truePay = truePay;
  }

  public Long getCommission() {
    return commission;
  }

  public void setCommission(Long commission) {
    this.commission = commission;
  }

  public Long getTruePayCommission() {
    return truePayCommission;
  }

  public void setTruePayCommission(Long truePayCommission) {
    this.truePayCommission = truePayCommission;
  }

  public Long getScoreCommission() {
    return scoreCommission;
  }

  public void setScoreCommission(Long scoreCommission) {
    this.scoreCommission = scoreCommission;
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
}
