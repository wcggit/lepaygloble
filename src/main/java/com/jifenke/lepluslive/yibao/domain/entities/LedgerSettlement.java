package com.jifenke.lepluslive.yibao.domain.entities;

import com.jifenke.lepluslive.global.util.MvUtil;

import javax.persistence.*;
import java.util.Date;

/**
 * 通道结算单
 * Created by zhangwen on 2017/7/12.
 */
@Entity
@Table(name = "YB_LEDGER_SETTLEMENT")
public class LedgerSettlement {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Date dateCreated = new Date();

  private Date dateUpdated = new Date();  //最近更新时间

  //【打款成功】【无结算记录】和【已退回】为终态
  //0=待查询，1=打款成功，2=已退回，3=已扣款未打款，4=打款中，-1=打款失败，-2=银行返回打款失败
  private Integer state = 0;   //结算状态

  @Column(nullable = false, length = 10)
  private String tradeDate; //结算日期（对应的给商户转账是哪一天完成的）（yyyy-MM-dd）

  @Column(length = 30)
  private String startEndDate;  //结算起止时间

  @Column(nullable = false, length = 30)
  private String orderSid = MvUtil.getOrderNumber(7); //通道结算单号(唯一)

  @Column(nullable = false, length = 50)
  private String ledgerNo;  //易宝的子商户号

  @Column(nullable = false)
  private Long merchantUserId;  //对应的商户ID

  private Long totalTransfer = 0L;  //交易应入账金额=使用该账户门店结算单之和

  /**
   * 实际转账金额=结算起止时间内的转账成功金额
   */
  private Long actualTransfer = 0L;   //实际转账金额

  private Long settlementTrueAmount = 0L;  //查询后易宝返回的实际结算金额（理应=settlementAmount）

  @Column(length = 50)
  private String batchNo;  //通道返回的结算批次号

  @Column(length = 30)
  private String accountName; //开户名

  @Column(length = 30)
  private String bankAccountNumber;  //出款用的银行卡号【必须为储蓄卡】

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

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public String getTradeDate() {
    return tradeDate;
  }

  public void setTradeDate(String tradeDate) {
    this.tradeDate = tradeDate;
  }

  public String getOrderSid() {
    return orderSid;
  }

  public void setOrderSid(String orderSid) {
    this.orderSid = orderSid;
  }

  public String getLedgerNo() {
    return ledgerNo;
  }

  public void setLedgerNo(String ledgerNo) {
    this.ledgerNo = ledgerNo;
  }

  public Long getMerchantUserId() {
    return merchantUserId;
  }

  public void setMerchantUserId(Long merchantUserId) {
    this.merchantUserId = merchantUserId;
  }

  public String getStartEndDate() {
    return startEndDate;
  }

  public void setStartEndDate(String startEndDate) {
    this.startEndDate = startEndDate;
  }

  public String getBatchNo() {
    return batchNo;
  }

  public void setBatchNo(String batchNo) {
    this.batchNo = batchNo;
  }

  public Long getTotalTransfer() {
    return totalTransfer;
  }

  public void setTotalTransfer(Long totalTransfer) {
    this.totalTransfer = totalTransfer;
  }

  public Long getActualTransfer() {
    return actualTransfer;
  }

  public void setActualTransfer(Long actualTransfer) {
    this.actualTransfer = actualTransfer;
  }

  public Long getSettlementTrueAmount() {
    return settlementTrueAmount;
  }

  public void setSettlementTrueAmount(Long settlementTrueAmount) {
    this.settlementTrueAmount = settlementTrueAmount;
  }

  public String getAccountName() {
    return accountName;
  }

  public void setAccountName(String accountName) {
    this.accountName = accountName;
  }

  public String getBankAccountNumber() {
    return bankAccountNumber;
  }

  public void setBankAccountNumber(String bankAccountNumber) {
    this.bankAccountNumber = bankAccountNumber;
  }

  public Date getDateUpdated() {
    return dateUpdated;
  }

  public void setDateUpdated(Date dateUpdated) {
    this.dateUpdated = dateUpdated;
  }
}
