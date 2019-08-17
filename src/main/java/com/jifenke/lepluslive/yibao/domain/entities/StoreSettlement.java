package com.jifenke.lepluslive.yibao.domain.entities;

import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 门店结算单(易宝)
 * Created by zhangwen on 2017/7/12.
 */
@Entity
@Table(name = "YB_STORE_SETTLEMENT")
public class StoreSettlement {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @NotNull
  private Merchant merchant;

  private Date dateCreated = new Date();

  //0=待查询，1=打款成功，2=已退回，3=已扣款未打款，4=打款中，5=无结算记录，-1=打款失败，-2=银行返回打款失败
  private Integer state = 0;   //结算状态

  @Column(nullable = false, length = 10)
  private String tradeDate; //清算日期（对应的交易记录是哪一天完成的）（yyyy-MM-dd）

  @Column(nullable = false, length = 30)
  private String orderSid = MvUtil.getOrderNumber(7); //结算单号(唯一)

  @Column(length = 30)
  private String ledgerSid;  //通道结算单号=LedgerSettlement.orderSid

  @Column(nullable = false, length = 50)
  private String ledgerNo;  //易宝的子商户号

  private Long wxTruePayTransfer = 0L;   //微信实付部分应入账

  private Long aliTruePayTransfer = 0L;  //支付宝实付部分应入账

  private Long scoreTransfer = 0L;  //鼓励金支付部分应入账

  private Integer refundNumber = 0;   //清算日期为该日的退款单笔数 仅统计退款成功的

  private Long refundExpend = 0L;  //清算日期为该日的成功退款单的【应收商户总额】累加

  //=wxTruePayTransfer+aliTruePayTransfer+scoreTransfer-refundExpend
  private Long actualTransfer = 0L;   //应转账金额

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getDateCreated() {
    return dateCreated;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public String getLedgerSid() {
    return ledgerSid;
  }

  public void setLedgerSid(String ledgerSid) {
    this.ledgerSid = ledgerSid;
  }

  public void setDateCreated(Date dateCreated) {
    this.dateCreated = dateCreated;
  }

  public String getOrderSid() {
    return orderSid;
  }

  public void setOrderSid(String orderSid) {
    this.orderSid = orderSid;
  }

  public String getTradeDate() {
    return tradeDate;
  }

  public void setTradeDate(String tradeDate) {
    this.tradeDate = tradeDate;
  }

  public Merchant getMerchant() {
    return merchant;
  }

  public void setMerchant(Merchant merchant) {
    this.merchant = merchant;
  }

  public String getLedgerNo() {
    return ledgerNo;
  }

  public void setLedgerNo(String ledgerNo) {
    this.ledgerNo = ledgerNo;
  }

  public Long getWxTruePayTransfer() {
    return wxTruePayTransfer;
  }

  public void setWxTruePayTransfer(Long wxTruePayTransfer) {
    this.wxTruePayTransfer = wxTruePayTransfer;
  }

  public Long getAliTruePayTransfer() {
    return aliTruePayTransfer;
  }

  public void setAliTruePayTransfer(Long aliTruePayTransfer) {
    this.aliTruePayTransfer = aliTruePayTransfer;
  }

  public Long getScoreTransfer() {
    return scoreTransfer;
  }

  public void setScoreTransfer(Long scoreTransfer) {
    this.scoreTransfer = scoreTransfer;
  }

  public Integer getRefundNumber() {
    return refundNumber;
  }

  public void setRefundNumber(Integer refundNumber) {
    this.refundNumber = refundNumber;
  }

  public Long getRefundExpend() {
    return refundExpend;
  }

  public void setRefundExpend(Long refundExpend) {
    this.refundExpend = refundExpend;
  }

  public Long getActualTransfer() {
    return actualTransfer;
  }

  public void setActualTransfer(Long actualTransfer) {
    this.actualTransfer = actualTransfer;
  }
}
