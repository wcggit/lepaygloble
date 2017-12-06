package com.jifenke.lepluslive.groupon.domain.entities;

import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.Date;

/**
 * Created by wcg on 2017/6/14. 团购商品
 */
@Entity
@Table(name = "GROUPON_PRODUCT")
public class GrouponProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private Long sellVolume = 0L; //销量

    private String description; //简介

    private String explainPicture; //说明图 说明团购商品内容 （详情明细）

    private String displayPicture; //展示图 (列表图)

    private Integer reservation = 0; //是否需要预约 0 不需要预约 1,2,3..需要提前1,2,3..天预约

    private Integer refundType = 0; //0 随时退款 1 不可退款

    private Integer phoneType = 0;  //0 商户电话 1 门店电话

    private String sid;

    private String instruction; //购买须知图

    private Long originPrice = 0L; //原始价格

    private Long normalPrice = 0L; //普通团购价格

    private Long ljPrice = 0L; //乐加会员团购价格

    private Long normalStorage = 0L; //普通库存

    private Long ljStorage = 0L;//乐加库存

    private Long ljCommission = 0L; //佣金

    private Long charge = 0L; //普通手续费 具体值 非费率

    private Long rebateScorea = 0L; //返鼓励金

    private Long rebateScorec = 0L; //返金币

    private Long shareToLockMerchant = 0L;

    private Long shareToLockPartner = 0L;

    private Long shareToTradePartner = 0L;

    private Long shareToLockPartnerManager = 0L;

    private Long shareToTradePartnerManager = 0L;

    @ManyToOne
    private MerchantUser merchantUser; //对应一个商户

    private Integer validityType; // 有效期类型 0 相对日期 1 绝对日期

    private String validity; // 如果是0 则是数字 如果是1 则显示 2017-09-19 01:14:31 ~ 2017-10-19 01:14:31

    private Integer state = 0; // 0下架 1 上架

    @Min(0)
    private int payNumType;

    @Min(0)
    private int payNumMax;

    public MerchantUser getMerchantUser() {
        return merchantUser;
    }

    public void setMerchantUser(MerchantUser merchantUser) {
        this.merchantUser = merchantUser;
    }

    public Integer getValidityType() {
        return validityType;
    }

    public void setValidityType(Integer validityType) {
        this.validityType = validityType;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayPicture() {
        return displayPicture;
    }

    public void setDisplayPicture(String displayPicture) {
        this.displayPicture = displayPicture;
    }

    public Integer getReservation() {
        return reservation;
    }

    public void setReservation(Integer reservation) {
        this.reservation = reservation;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public Long getOriginPrice() {
        return originPrice;
    }

    public void setOriginPrice(Long originPrice) {
        this.originPrice = originPrice;
    }

    public Long getNormalPrice() {
        return normalPrice;
    }

    public void setNormalPrice(Long normalPrice) {
        this.normalPrice = normalPrice;
    }

    public Long getLjPrice() {
        return ljPrice;
    }

    public void setLjPrice(Long ljPrice) {
        this.ljPrice = ljPrice;
    }

    public Long getNormalStorage() {
        return normalStorage;
    }

    public void setNormalStorage(Long normalStorage) {
        this.normalStorage = normalStorage;
    }

    public Long getLjStorage() {
        return ljStorage;
    }

    public void setLjStorage(Long ljStorage) {
        this.ljStorage = ljStorage;
    }

    public Long getLjCommission() {
        return ljCommission;
    }

    public void setLjCommission(Long ljCommission) {
        this.ljCommission = ljCommission;
    }

    public Long getRebateScorea() {
        return rebateScorea;
    }

    public void setRebateScorea(Long rebateScorea) {
        this.rebateScorea = rebateScorea;
    }

    public Long getRebateScorec() {
        return rebateScorec;
    }

    public void setRebateScorec(Long rebateScorec) {
        this.rebateScorec = rebateScorec;
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

    public Long getSellVolume() {
        return sellVolume;
    }

    public void setSellVolume(Long sellVolume) {
        this.sellVolume = sellVolume;
    }

    public String getExplainPicture() {
        return explainPicture;
    }

    public void setExplainPicture(String explainPicture) {
        this.explainPicture = explainPicture;
    }

    public Integer getRefundType() {
        return refundType;
    }

    public void setRefundType(Integer refundType) {
        this.refundType = refundType;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public Long getCharge() {
        return charge;
    }

    public void setCharge(Long charge) {
        this.charge = charge;
    }

    private Date createDate;          //  创建时间

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(Integer phoneType) {
        this.phoneType = phoneType;
    }

    public int getPayNumType() {
        return payNumType;
    }

    public void setPayNumType(int payNumType) {
        this.payNumType = payNumType;
    }

    public int getPayNumMax() {
        return payNumMax;
    }

    public void setPayNumMax(int payNumMax) {
        this.payNumMax = payNumMax;
    }
}
