package com.jifenke.lepluslive.yibao.domain.entities;

import com.jifenke.lepluslive.global.util.MvUtil;

import javax.persistence.*;
import java.util.Date;

/**
 * 退款单
 * Created by zhangwen on 2017/7/12.
 */
@Entity
@Table(name = "YB_LEDGER_REFUND_ORDER")
public class LedgerRefundOrder {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Date dateCreated = new Date();

  private Date dateCompleted;  //退款完成时间

  private String refundOrderSid = MvUtil.getOrderNumber();  //自己的退款单号

  @Column(nullable = false, length = 30)
  private String orderSid; //对应要退款订单的订单号

  @Column(nullable = false, length = 50)
  private String ledgerNo;  //通道子商户号

  private Integer orderFrom = 1;  //退款通道 1=易宝，2=富有，3=微信

  private Integer orderType = 1;  //1=普通订单,2=乐加订单

  @Column(nullable = false, length = 10)
  private String merchantId;  //交易门店ID

  @Column(nullable = false, length = 30)
  private String orderId; //第三方的退款流水号，纯红包退款=" "

  /**
   * 退款完成时间在D日切点前时，清算日期即为D日。
   * 在D日日切点后，清算日期记为D+1日
   */
  @Column(nullable = false, length = 10)
  private String tradeDate; //清算日期（yyyy-MM-dd）

  private Integer state = 0;  //退款状态 0=待退款，1=未开始退款，2=退款成功，3=退款失败，其他为通道返回码

  private Long totalAmount = 0L;  //总退款金额=trueAmount+scoreAmount

  private Long trueAmount = 0L;   //实付部分退款金币（订单实付）（用该值向通道发起退款申请）

  private Long scoreAmount = 0L;  //鼓励金退款金额

  private Integer rateCostSide = 0;  //手续费承担方  0=积分客，1=商户 本期仅支持【积分客承担】模式

  private Long thirdTrueCommission = 0L;  //三方实际手续费(对积分客)=truePay*通道费率

  private Long scoreCommission = 0L;  //鼓励金手续费|鼓励金佣金=对应order.scoreCommission

  private Long transferMoney = 0L; //应收商户的总金额=对应order.transferMoney

  private Long transferMoneyFromTruePay = 0L; //因产生退款，应收商户的资金=对应order.transferMoneyFromTruePay

  private Long transferMoneyFromScore = 0L;   //因产生退款，应收商户的=对应order.transferMoneyFromScore

  private Long realScoreA = 0L; //该退款单向会员实际追回的鼓励金

  private Long realScoreC = 0L; //该退款单向会员实际追回的金币

  /**
   * 0： 追回至余额0为止。
   * 1： 全额追回，无法追回则无法退款。
   */
  private Integer shareRecoverType = 0;  //分润追回策略

  private Long shareBack = 0L;  //应追回的分润总额

  private Long shareRealBack = 0L; //实际追回的分润总额

  private Long shareOrderId;   //如果该订单有分润=分润单ID

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(Date dateCreated) {
    this.dateCreated = dateCreated;
  }

  public Date getDateCompleted() {
    return dateCompleted;
  }

  public void setDateCompleted(Date dateCompleted) {
    this.dateCompleted = dateCompleted;
  }

  public String getRefundOrderSid() {
    return refundOrderSid;
  }

  public void setRefundOrderSid(String refundOrderSid) {
    this.refundOrderSid = refundOrderSid;
  }

  public String getOrderSid() {
    return orderSid;
  }

  public void setOrderSid(String orderSid) {
    this.orderSid = orderSid;
  }

  public Integer getOrderFrom() {
    return orderFrom;
  }

  public void setOrderFrom(Integer orderFrom) {
    this.orderFrom = orderFrom;
  }

  public Integer getOrderType() {
    return orderType;
  }

  public void setOrderType(Integer orderType) {
    this.orderType = orderType;
  }

  public String getMerchantId() {
    return merchantId;
  }

  public void setMerchantId(String merchantId) {
    this.merchantId = merchantId;
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public String getTradeDate() {
    return tradeDate;
  }

  public void setTradeDate(String tradeDate) {
    this.tradeDate = tradeDate;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public Long getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(Long totalAmount) {
    this.totalAmount = totalAmount;
  }

  public Long getTrueAmount() {
    return trueAmount;
  }

  public void setTrueAmount(Long trueAmount) {
    this.trueAmount = trueAmount;
  }

  public Long getScoreAmount() {
    return scoreAmount;
  }

  public void setScoreAmount(Long scoreAmount) {
    this.scoreAmount = scoreAmount;
  }

  public Integer getRateCostSide() {
    return rateCostSide;
  }

  public void setRateCostSide(Integer rateCostSide) {
    this.rateCostSide = rateCostSide;
  }

  public Long getThirdTrueCommission() {
    return thirdTrueCommission;
  }

  public void setThirdTrueCommission(Long thirdTrueCommission) {
    this.thirdTrueCommission = thirdTrueCommission;
  }

  public Long getScoreCommission() {
    return scoreCommission;
  }

  public void setScoreCommission(Long scoreCommission) {
    this.scoreCommission = scoreCommission;
  }

  public Long getTransferMoney() {
    return transferMoney;
  }

  public void setTransferMoney(Long transferMoney) {
    this.transferMoney = transferMoney;
  }

  public Long getTransferMoneyFromTruePay() {
    return transferMoneyFromTruePay;
  }

  public void setTransferMoneyFromTruePay(Long transferMoneyFromTruePay) {
    this.transferMoneyFromTruePay = transferMoneyFromTruePay;
  }

  public Long getTransferMoneyFromScore() {
    return transferMoneyFromScore;
  }

  public void setTransferMoneyFromScore(Long transferMoneyFromScore) {
    this.transferMoneyFromScore = transferMoneyFromScore;
  }

  public Long getRealScoreA() {
    return realScoreA;
  }

  public void setRealScoreA(Long realScoreA) {
    this.realScoreA = realScoreA;
  }

  public Long getRealScoreC() {
    return realScoreC;
  }

  public void setRealScoreC(Long realScoreC) {
    this.realScoreC = realScoreC;
  }

  public Integer getShareRecoverType() {
    return shareRecoverType;
  }

  public void setShareRecoverType(Integer shareRecoverType) {
    this.shareRecoverType = shareRecoverType;
  }

  public Long getShareBack() {
    return shareBack;
  }

  public void setShareBack(Long shareBack) {
    this.shareBack = shareBack;
  }

  public Long getShareRealBack() {
    return shareRealBack;
  }

  public void setShareRealBack(Long shareRealBack) {
    this.shareRealBack = shareRealBack;
  }

  public Long getShareOrderId() {
    return shareOrderId;
  }

  public void setShareOrderId(Long shareOrderId) {
    this.shareOrderId = shareOrderId;
  }

  public String getLedgerNo() {
    return ledgerNo;
  }

  public void setLedgerNo(String ledgerNo) {
    this.ledgerNo = ledgerNo;
  }
}
