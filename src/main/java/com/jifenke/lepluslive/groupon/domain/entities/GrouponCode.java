package com.jifenke.lepluslive.groupon.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wcg on 2017/6/14. 团购码
 */
@Entity
@Table(name = "GROUPON_CODE")
public class GrouponCode {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne
  @JsonIgnore
  private GrouponOrder grouponOrder;

  private String sid = MvUtil.getRandomNumber(12);

  private Integer state = -1; //-1 未付款 0 未使用  1 已使用 2退款中 3 已退款  4过期

  @ManyToOne
  private GrouponProduct grouponProduct;

  @ManyToOne
  private LeJiaUser leJiaUser;

  private Date expiredDate; //过期时间

  private Date startDate = new Date(); //起始日期

  private Long shareToLockMerchant = 0L;

  private Long shareToLockPartner = 0L;

  private Long shareToTradePartner = 0L;

  private Long shareToLockPartnerManager = 0L;

  private Long shareToTradePartnerManager = 0L;

  private Long totalPrice = 0L;

  private Long commission = 0L;

  private Long trasnferMoney = 0L;

  private Integer codeType = 0;// 0 普通费率 1 高迁费率

  @ManyToOne
  private Merchant merchant; //核销对应门店，未核销为空

  private Date createDate = new Date();  // 创建时间

  private Date checkDate; //核销时间

  private String merchantUser; //核销员

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public GrouponOrder getGrouponOrder() {
    return grouponOrder;
  }

  public void setGrouponOrder(GrouponOrder grouponOrder) {
    this.grouponOrder = grouponOrder;
  }

  public String getSid() {
    return sid;
  }

  public void setSid(String sid) {
    this.sid = sid;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public GrouponProduct getGrouponProduct() {
    return grouponProduct;
  }

  public void setGrouponProduct(GrouponProduct grouponProduct) {
    this.grouponProduct = grouponProduct;
  }

  public LeJiaUser getLeJiaUser() {
    return leJiaUser;
  }

  public void setLeJiaUser(LeJiaUser leJiaUser) {
    this.leJiaUser = leJiaUser;
  }

  public Date getExpiredDate() {
    return expiredDate;
  }

  public void setExpiredDate(Date expiredDate) {
    this.expiredDate = expiredDate;
  }

  public Long getShareToLockMerchant() {
    return shareToLockMerchant;
  }

  public void setShareToLockMerchant(Long shareToLockMerchant) {
    this.shareToLockMerchant = shareToLockMerchant;
  }

  public Long getShareToLockPartner() {
    return shareToLockPartner;
  }

  public void setShareToLockPartner(Long shareToLockPartner) {
    this.shareToLockPartner = shareToLockPartner;
  }

  public Long getShareToTradePartner() {
    return shareToTradePartner;
  }

  public void setShareToTradePartner(Long shareToTradePartner) {
    this.shareToTradePartner = shareToTradePartner;
  }

  public Long getShareToLockPartnerManager() {
    return shareToLockPartnerManager;
  }

  public void setShareToLockPartnerManager(Long shareToLockPartnerManager) {
    this.shareToLockPartnerManager = shareToLockPartnerManager;
  }

  public Long getShareToTradePartnerManager() {
    return shareToTradePartnerManager;
  }

  public void setShareToTradePartnerManager(Long shareToTradePartnerManager) {
    this.shareToTradePartnerManager = shareToTradePartnerManager;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Long getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(Long totalPrice) {
    this.totalPrice = totalPrice;
  }

  public Long getCommission() {
    return commission;
  }

  public void setCommission(Long commission) {
    this.commission = commission;
  }

  public Long getTrasnferMoney() {
    return trasnferMoney;
  }

  public void setTrasnferMoney(Long trasnferMoney) {
    this.trasnferMoney = trasnferMoney;
  }

  public Integer getCodeType() {
    return codeType;
  }

  public void setCodeType(Integer codeType) {
    this.codeType = codeType;
  }

  public Merchant getMerchant() {
    return merchant;
  }

  public void setMerchant(Merchant merchant) {
    this.merchant = merchant;
  }

  public Date getCheckDate() {
    return checkDate;
  }

  public void setCheckDate(Date checkDate) {
    this.checkDate = checkDate;
  }

  public String getMerchantUser() {
    return merchantUser;
  }

  public void setMerchantUser(String merchantUser) {
    this.merchantUser = merchantUser;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }
}
