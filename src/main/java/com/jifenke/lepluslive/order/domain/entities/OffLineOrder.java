package com.jifenke.lepluslive.order.domain.entities;

import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

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

    private int basicType;      // 0=普通订单  1=乐加订单

    private Integer payType = 0; // 0代表微信 1 代表支付宝

    private Long totalPrice = 0L;

    private Long truePay = 0L;

    private Long ljCommission = 0L; //乐加佣金

    private Long trueScore = 0L; //实际使用红包

    private Long wxCommission = 0L; //微信手续费

    private Long rebate = 0L; //返利鼓励金

    private Integer state = 0; //支付状态

    private Long transferMoney; //每笔应该转给商户的金额(包括现金和红包)

    private Long transferMoneyFromTruePay; //每笔订单中现金支付转给商户的金额

    private Integer
        rebateWay;
    //返利方式,如果为0 代表非会员普通订单 则只返b积分 如果为1 导流订单 2 会员普通订单 3会员订单 4 非会员扫纯支付码 5 会员扫纯支付码 6会员订单（普通费率）


    private String lepayCode;

    private Long truePayCommission;//实际支付手续费

    private Long scoreC = 0L; //发放金币

    private BigDecimal commissionScale; // 订单费率

    /**
     * 是否有优惠信息 1=是|0=否
     */
    private Integer discount = 0;


    public Long getScoreC() {
        return scoreC;
    }

    public void setScoreC(Long scoreC) {
        this.scoreC = scoreC;
    }

    public BigDecimal getCommissionScale() {
        return commissionScale;
    }

    public void setCommissionScale(BigDecimal commissionScale) {
        this.commissionScale = commissionScale;
    }

    public Long getTruePayCommission() {
        return truePayCommission;
    }

    public void setTruePayCommission(Long truePayCommission) {
        this.truePayCommission = truePayCommission;
    }

    public Long getTransferMoneyFromTruePay() {
        return transferMoneyFromTruePay;
    }

    public void setTransferMoneyFromTruePay(Long transferMoneyFromTruePay) {
        this.transferMoneyFromTruePay = transferMoneyFromTruePay;
    }

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


    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    private Long shareMoney = 0L; //每笔订单分润金额

    public Long getShareMoney() {
        return shareMoney;
    }

    public void setShareMoney(Long shareMoney) {
        this.shareMoney = shareMoney;
    }

    @Column(length = 50)
    private String desk;//桌号

    public String getDesk() {
        return desk;
    }

    public void setDesk(String desk) {
        this.desk = desk;
    }

    public int getBasicType() {
        return basicType;
    }

    public void setBasicType(int basicType) {
        this.basicType = basicType;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }
}
