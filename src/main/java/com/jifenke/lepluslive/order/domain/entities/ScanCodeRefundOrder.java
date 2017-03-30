package com.jifenke.lepluslive.order.domain.entities;

import com.jifenke.lepluslive.global.util.MvUtil;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 富友扫码退款单 Created by zhangwen on 2016/12/22.
 */
@Entity
@Table(name = "SCAN_CODE_REFUND_ORDER")
public class ScanCodeRefundOrder {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Date createDate = new Date();

  private String refundOrderSid = MvUtil.getOrderNumber();  //自己的退款单号

  private String merchantNum;    //商户号, 富友分配的商户号

  @OneToOne
  private ScanCodeOrder scanCodeOrder; //对应的富友订单

  private Long orderShareId;   //如果该订单有分润=OffLineOrderShare.id

  private String refundSid; //商户退款单号，第三方的退款单号

  private Date completeDate;  //退款完成时间

  private Integer state = 0;  //退款状态  0=未完成|1=已退款

  private Long realRebate = 0L; //该退款单向会员实际追回的红包

  private Long realScoreB = 0L; //该退款单向会员实际追回的积分=会员积分变更值

  private Long toTradePartner = 0L;   //实际追回

  private Long toTradePartnerManager = 0L;//实际追回

  private Long toLockMerchant = 0L;//实际追回

  private Long toLockPartner = 0L;//实际追回

  private Long toLockPartnerManager = 0L;//实际追回

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

  public String getRefundOrderSid() {
    return refundOrderSid;
  }

  public void setRefundOrderSid(String refundOrderSid) {
    this.refundOrderSid = refundOrderSid;
  }

  public String getMerchantNum() {
    return merchantNum;
  }

  public void setMerchantNum(String merchantNum) {
    this.merchantNum = merchantNum;
  }

  public ScanCodeOrder getScanCodeOrder() {
    return scanCodeOrder;
  }

  public void setScanCodeOrder(ScanCodeOrder scanCodeOrder) {
    this.scanCodeOrder = scanCodeOrder;
  }

  public Long getOrderShareId() {
    return orderShareId;
  }

  public void setOrderShareId(Long orderShareId) {
    this.orderShareId = orderShareId;
  }

  public String getRefundSid() {
    return refundSid;
  }

  public void setRefundSid(String refundSid) {
    this.refundSid = refundSid;
  }

  public Date getCompleteDate() {
    return completeDate;
  }

  public void setCompleteDate(Date completeDate) {
    this.completeDate = completeDate;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public Long getRealRebate() {
    return realRebate;
  }

  public void setRealRebate(Long realRebate) {
    this.realRebate = realRebate;
  }

  public Long getRealScoreB() {
    return realScoreB;
  }

  public void setRealScoreB(Long realScoreB) {
    this.realScoreB = realScoreB;
  }

  public Long getToTradePartner() {
    return toTradePartner;
  }

  public void setToTradePartner(Long toTradePartner) {
    this.toTradePartner = toTradePartner;
  }

  public Long getToTradePartnerManager() {
    return toTradePartnerManager;
  }

  public void setToTradePartnerManager(Long toTradePartnerManager) {
    this.toTradePartnerManager = toTradePartnerManager;
  }

  public Long getToLockMerchant() {
    return toLockMerchant;
  }

  public void setToLockMerchant(Long toLockMerchant) {
    this.toLockMerchant = toLockMerchant;
  }

  public Long getToLockPartner() {
    return toLockPartner;
  }

  public void setToLockPartner(Long toLockPartner) {
    this.toLockPartner = toLockPartner;
  }

  public Long getToLockPartnerManager() {
    return toLockPartnerManager;
  }

  public void setToLockPartnerManager(Long toLockPartnerManager) {
    this.toLockPartnerManager = toLockPartnerManager;
  }
}
