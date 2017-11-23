package com.jifenke.lepluslive.order.domain.entities;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerManager;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wcg on 16/6/22.
 */
@Entity
@Table(name = "OFF_LINE_ORDER_SHARE")
public class OffLineOrderShare {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private OffLineOrder offLineOrder;

    @OneToOne(fetch = FetchType.LAZY)
    private PosOrder posOrder;

    @OneToOne(fetch = FetchType.LAZY)
    private UnionPosOrder unionPosOrder;

    @OneToOne(fetch = FetchType.LAZY)
    private ScanCodeOrder scanCodeOrder;

    private Integer type; //1 代表线下订单分润 2代表中慧pos订单分润 3 银联pos  4:通道订单

    private String orderSid;   //对应的分润来源的订单号

    private Long userId;//对应的leJiaUser.id

    private Long shareMoney;

    private Long toTradePartner = 0L;

    private Long toTradePartnerManager = 0L;

    private Long toLockMerchant = 0L;

    private Long toLockPartner = 0L;

    private Long toLockPartnerManager = 0L;

    private Long toLePlusLife = 0L;

    private Date createDate = new Date();

    public UnionPosOrder getUnionPosOrder() {
        return unionPosOrder;
    }

    public void setUnionPosOrder(UnionPosOrder unionPosOrder) {
        this.unionPosOrder = unionPosOrder;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    private Partner tradePartner;

    @ManyToOne(fetch = FetchType.LAZY)
    private PartnerManager tradePartnerManager;

    @ManyToOne(fetch = FetchType.LAZY)
    private Merchant tradeMerchant;

    @ManyToOne(fetch = FetchType.LAZY)
    private Merchant lockMerchant;

    @ManyToOne(fetch = FetchType.LAZY)
    private Partner lockPartner;

    @ManyToOne(fetch = FetchType.LAZY)
    private PartnerManager lockPartnerManager;

    public PosOrder getPosOrder() {
        return posOrder;
    }

    public void setPosOrder(PosOrder posOrder) {
        this.posOrder = posOrder;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public ScanCodeOrder getScanCodeOrder() {
        return scanCodeOrder;
    }

    public void setScanCodeOrder(ScanCodeOrder scanCodeOrder) {
        this.scanCodeOrder = scanCodeOrder;
    }

    public Merchant getTradeMerchant() {
        return tradeMerchant;
    }

    public void setTradeMerchant(Merchant tradeMerchant) {
        this.tradeMerchant = tradeMerchant;
    }

    public Long getShareMoney() {
        return shareMoney;
    }

    public void setShareMoney(Long shareMoney) {
        this.shareMoney = shareMoney;
    }

    public OffLineOrder getOffLineOrder() {
        return offLineOrder;
    }

    public void setOffLineOrder(OffLineOrder offLineOrder) {
        this.offLineOrder = offLineOrder;
    }

    public Partner getTradePartner() {
        return tradePartner;
    }

    public void setTradePartner(Partner tradePartner) {
        this.tradePartner = tradePartner;
    }

    public PartnerManager getTradePartnerManager() {
        return tradePartnerManager;
    }

    public void setTradePartnerManager(PartnerManager tradePartnerManager) {
        this.tradePartnerManager = tradePartnerManager;
    }

    public Merchant getLockMerchant() {
        return lockMerchant;
    }

    public void setLockMerchant(Merchant lockMerchant) {
        this.lockMerchant = lockMerchant;
    }

    public Partner getLockPartner() {
        return lockPartner;
    }

    public void setLockPartner(Partner lockPartner) {
        this.lockPartner = lockPartner;
    }

    public PartnerManager getLockPartnerManager() {
        return lockPartnerManager;
    }

    public void setLockPartnerManager(PartnerManager lockPartnerManager) {
        this.lockPartnerManager = lockPartnerManager;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getToLePlusLife() {
        return toLePlusLife;
    }

    public void setToLePlusLife(Long toLePlusLife) {
        this.toLePlusLife = toLePlusLife;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
