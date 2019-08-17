package com.jifenke.lepluslive.order.domain.entities;

import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;

import javax.persistence.*;
import java.util.Date;

/**
 * 银联商务 Created by zhangwen on 16/10/10.
 */
@Entity
@Table(name = "UNION_POS_ORDER")
public class UnionPosOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String orderSid;

    private String orderCode; //银联商务销账流水号，用于对账及撤销

    private String settleDate;  //清算日期  YYYYMMDD

    private Date createdDate;

    private Date completeDate;

    private Date cancelDate; //冲正或撤销时间

    @ManyToOne
    private LeJiaUser leJiaUser;

    @ManyToOne
    private Merchant merchant;

    private Integer orderType = 0; //订单类型 对应category.id

    private Integer basicType = 0; //实际订单类型 0=普通订单|1=乐加订单

    private Long commission = 0L;  //总佣金

    private Long truePayCommission = 0L; //实际支付(银商)佣金

    private Long scoreCommission = 0L; //乐加佣金(红包部分)

    private Long wxCommission = 0L; //三方手续费

    private Long ysCharge = 0L; //银商实际收取的手续费

    private Long rebate = 0L; //返鼓励金

    private Long scoreC = 0L; //发放金币

    private Integer state = 0; //支付状态 1=已支付

    private Integer orderState = 0; //订单状态 0=未支付|1=已支付(未对账)|2=已支付(已对账)|3=未支付(已冲正)|4=未支付(已撤销)

    private Long transferMoney = 0L; //每笔应该转给商户的金额=transferByBank+transferByScore

    private Long transferMoneyFromTruePay = 0L; //银商转给商户的金额

    private Long transferMoneyFromScore = 0L; //鼓励金部分转给商户的金额

    private Long totalPrice = 0L;  //订单总额=truePay+trueScore

    private Long truePay = 0L; //实际支付

    private Long trueScore = 0L; //实际使用鼓励金

    private Integer paidType;  //1纯通道（刷卡|纯微信|纯支付宝）   2纯红包  3通道+红包

    private Integer channel = 0;  //0=刷卡|1=微信|2=支付宝

    private String account;  //操作账户名

    private Long share = 0L;  //待分润金额

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderSid() {
        return orderSid;
    }

    public void setOrderSid(String orderSid) {
        this.orderSid = orderSid;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(String settleDate) {
        this.settleDate = settleDate;
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

    public Date getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(Date cancelDate) {
        this.cancelDate = cancelDate;
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

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getBasicType() {
        return basicType;
    }

    public void setBasicType(Integer basicType) {
        this.basicType = basicType;
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

    public Long getWxCommission() {
        return wxCommission;
    }

    public void setWxCommission(Long wxCommission) {
        this.wxCommission = wxCommission;
    }

    public Long getYsCharge() {
        return ysCharge;
    }

    public void setYsCharge(Long ysCharge) {
        this.ysCharge = ysCharge;
    }

    public Long getRebate() {
        return rebate;
    }

    public void setRebate(Long rebate) {
        this.rebate = rebate;
    }

    public Long getScoreC() {
        return scoreC;
    }

    public void setScoreC(Long scoreC) {
        this.scoreC = scoreC;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getOrderState() {
        return orderState;
    }

    public void setOrderState(Integer orderState) {
        this.orderState = orderState;
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

    public Long getTrueScore() {
        return trueScore;
    }

    public void setTrueScore(Long trueScore) {
        this.trueScore = trueScore;
    }

    public Integer getPaidType() {
        return paidType;
    }

    public void setPaidType(Integer paidType) {
        this.paidType = paidType;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Long getShare() {
        return share;
    }

    public void setShare(Long share) {
        this.share = share;
    }
}
