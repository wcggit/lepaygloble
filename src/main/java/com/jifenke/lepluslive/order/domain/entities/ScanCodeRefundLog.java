package com.jifenke.lepluslive.order.domain.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 富友扫码订单退款记录 Created by zhangwen on 2016/11/27.
 */
@Entity
@Table(name = "SCAN_CODE_REFUND_LOG")
public class ScanCodeRefundLog {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Date createDate = new Date();

  private String resultCode; //错误代码, 000000成功,其他详细参见错误列表

  private String resultMsg;  //成功=SUCCESS|失败=失败原因

  private String orderType;  //订单类型:ALIPAY , WECHAT

  private String orderSid; //对应的自己的订单号码

  private String refundOrderSid;  //对应的自己的退款单号

  private String refundSid; //商户退款单号，第三方的退款单号

  private String fySettleDt;  //富友清算日

  private String transactionId;  //渠道交易流水号

  private String refundId;      //渠道退款流水号

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public String getResultCode() {
    return resultCode;
  }

  public void setResultCode(String resultCode) {
    this.resultCode = resultCode;
  }

  public String getResultMsg() {
    return resultMsg;
  }

  public void setResultMsg(String resultMsg) {
    this.resultMsg = resultMsg;
  }

  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  public String getRefundId() {
    return refundId;
  }

  public void setRefundId(String refundId) {
    this.refundId = refundId;
  }

  public String getOrderType() {
    return orderType;
  }

  public void setOrderType(String orderType) {
    this.orderType = orderType;
  }

  public String getOrderSid() {
    return orderSid;
  }

  public void setOrderSid(String orderSid) {
    this.orderSid = orderSid;
  }

  public String getRefundSid() {
    return refundSid;
  }

  public void setRefundSid(String refundSid) {
    this.refundSid = refundSid;
  }

  public String getFySettleDt() {
    return fySettleDt;
  }

  public void setFySettleDt(String fySettleDt) {
    this.fySettleDt = fySettleDt;
  }

  public String getRefundOrderSid() {
    return refundOrderSid;
  }

  public void setRefundOrderSid(String refundOrderSid) {
    this.refundOrderSid = refundOrderSid;
  }
}
