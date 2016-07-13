package com.jifenke.lepluslive.order.domain.entities;

import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Created by wcg on 16/5/5.
 */
@Entity
@Table(name = "OFF_LINE_ORDER")
public class OffLineOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String orderSid = MvUtil.getOrderNumber();

    private Date createdDate;

    private Date completeDate;


    @ManyToOne
    private LeJiaUser leJiaUser;

    @ManyToOne
    private Merchant merchant;

    @ManyToOne
    private PayWay payWay;

    private Long totalPrice = 0L;

    private Long truePay = 0L;

    private Long ljCommission = 0L; //乐加佣金

    private Long trueScore = 0L;

    private Long wxCommission = 0L; //微信手续费

    private Long rebate = 0L; //返利红包

    private Long scoreB = 0L; //发放积分

    private Integer state = 0;

    private Long transferMoney; //每笔应该转给商户的金额

    private Integer rebateWay; //返利方式,如果为0 代表非会员订单或者会员在非签约商家消费 则只返b积分 如果为1 代表会员订单

    private String lepayCode = MvUtil.getLePayCode();


    public String getLepayCode() {
        return lepayCode;
    }

    public void setLepayCode(String lepayCode) {
        this.lepayCode = lepayCode;
    }

    public Integer getRebateWay() {
        return rebateWay;
    }

    public void setRebateWay(Integer rebateWay) {
        this.rebateWay = rebateWay;
    }

    public Long getTransferMoney() {
        return transferMoney;
    }

    public void setTransferMoney(Long transferMoney) {
        this.transferMoney = transferMoney;
    }

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

    public PayWay getPayWay() {
        return payWay;
    }

    public void setPayWay(PayWay payWay) {
        this.payWay = payWay;
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

    public Long getLjCommission() {
        return ljCommission;
    }

    public void setLjCommission(Long ljCommission) {
        this.ljCommission = ljCommission;
    }

    public Long getTrueScore() {
        return trueScore;
    }

    public void setTrueScore(Long trueScore) {
        this.trueScore = trueScore;
    }

    public Long getWxCommission() {
        return wxCommission;
    }

    public void setWxCommission(Long wxCommission) {
        this.wxCommission = wxCommission;
    }

    public Long getRebate() {
        return rebate;
    }

    public void setRebate(Long rebate) {
        this.rebate = rebate;
    }

    public Long getScoreB() {
        return scoreB;
    }

    public void setScoreB(Long scoreB) {
        this.scoreB = scoreB;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
