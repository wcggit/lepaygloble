package com.jifenke.lepluslive.order.domain.entities;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerManager;

import javax.persistence.*;
import java.util.Date;

/**
 * 线上订单分润 Created by zhangwen on 16/11/05.
 */
@Entity
@Table(name = "ON_LINE_ORDER_SHARE")
public class OnLineOrderShare {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer type = 0;   //分润单来源   1=商城|2=电影票|3=团购

    private String orderSid;   //对应的分润来源的订单号

    private Long userId;//对应leJiaUser.id

    //=toLockMerchant + toLockPartner + toLockPartnerManager + toTradePartner + toTradePartnerManager
    private Long shareMoney = 0L;

    private Date createDate = new Date();

    @ManyToOne
    private Merchant lockMerchant; //锁定商户

    private Long toLockMerchant = 0L;

    @ManyToOne
    private Partner lockPartner;  //锁定天使合伙人

    private Long toLockPartner = 0L;

    @ManyToOne
    private PartnerManager lockPartnerManager; //锁定城市合伙人

    private Long toLockPartnerManager = 0L;

    private Long toTradePartner = 0L;

    @ManyToOne
    private Partner tradePartner;

    private Long toTradePartnerManager = 0L;

    @ManyToOne
    private PartnerManager tradePartnerManager;

    private Long toLePlusLife = 0L; //给积分客的

    public Long getShareMoney() {
        return shareMoney;
    }

    public void setShareMoney(Long shareMoney) {
        this.shareMoney = shareMoney;
    }

    public Merchant getLockMerchant() {
        return lockMerchant;
    }

    public void setLockMerchant(Merchant lockMerchant) {
        this.lockMerchant = lockMerchant;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Partner getLockPartner() {
        return lockPartner;
    }

    public void setLockPartner(Partner lockPartner) {
        this.lockPartner = lockPartner;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public PartnerManager getLockPartnerManager() {
        return lockPartnerManager;
    }

    public void setLockPartnerManager(PartnerManager lockPartnerManager) {
        this.lockPartnerManager = lockPartnerManager;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getOrderSid() {
        return orderSid;
    }

    public void setOrderSid(String orderSid) {
        this.orderSid = orderSid;
    }

    public Long getToLePlusLife() {
        return toLePlusLife;
    }

    public void setToLePlusLife(Long toLePlusLife) {
        this.toLePlusLife = toLePlusLife;
    }

    public Long getToTradePartner() {
        return toTradePartner;
    }

    public void setToTradePartner(Long toTradePartner) {
        this.toTradePartner = toTradePartner;
    }

    public Partner getTradePartner() {
        return tradePartner;
    }

    public void setTradePartner(Partner tradePartner) {
        this.tradePartner = tradePartner;
    }

    public Long getToTradePartnerManager() {
        return toTradePartnerManager;
    }

    public void setToTradePartnerManager(Long toTradePartnerManager) {
        this.toTradePartnerManager = toTradePartnerManager;
    }

    public PartnerManager getTradePartnerManager() {
        return tradePartnerManager;
    }

    public void setTradePartnerManager(
        PartnerManager tradePartnerManager) {
        this.tradePartnerManager = tradePartnerManager;
    }
}
