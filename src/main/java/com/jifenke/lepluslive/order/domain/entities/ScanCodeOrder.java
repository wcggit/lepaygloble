package com.jifenke.lepluslive.order.domain.entities;

import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 富友线下扫码订单 Created by zhangwen on 16/11/25.
 */
@Entity
@Table(name = "SCAN_CODE_ORDER")
public class ScanCodeOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String orderSid = MvUtil.getOrderNumber();

    private String orderCode;  //第三方渠道流水号

    private String settleDate;  //第三方支付完成时间(入账时间[第二天结算]：易宝格式：yyyy-MM-dd,富友：yyyyMMdd)

    private Date createdDate;

    private Date completeDate;

    private Date refundDate;  //退款时间

    @ManyToOne
    private LeJiaUser leJiaUser;

    @ManyToOne
    private Merchant merchant;

    @NotNull
    private Integer state = 0; //支付状态  0=未支付|1=已支付|2=已退款

    @NotNull
    private Long orderType;//订单类型 对应category.id

    @NotNull
    private Long totalPrice = 0L;

    @NotNull
    private Long truePay = 0L;

    @NotNull
    private Long trueScore = 0L; //实际使用鼓励金

    private Long commission = 0L; //乐加总佣金=truePayCommission+scoreCommission

    private Long truePayCommission = 0L; //实付部分佣金

    private Long scoreCommission = 0L; //红包部分佣金

    private Long rebate = 0L; //返鼓励金

    private Long scoreC = 0L; //返金币数 = commission*40%

    private Long share = 0L;  //待分润金额(对于导流订单和会员订单)=commission*60%

    private Long transferMoney = 0L; //商户实际入账(包括现金和红包)=transferMoneyFromTruePay+transferMoneyFromScore

    private Long transferMoneyFromTruePay = 0L; //每笔订单中现金支付转给商户的金额

    private Long transferMoneyFromScore = 0L;   //每笔订单中红包支付转给商户的金额

    private Integer messageState = 0; //发送模版消息状态

    @Column(nullable = false, length = 10)
    private String lePayCode; //四位数支付码

    @OneToOne
    private ScanCodeOrderExt scanCodeOrderExt;

    private Long sumPrice; //totalprice+优惠金额

    @Version
    private Long version;

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(Long sumPrice) {
        this.sumPrice = sumPrice;
    }

    public Long getTransferMoneyFromTruePay() {
        return transferMoneyFromTruePay;
    }

    public void setTransferMoneyFromTruePay(Long transferMoneyFromTruePay) {
        this.transferMoneyFromTruePay = transferMoneyFromTruePay;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }


    public Long getShare() {
        return share;
    }

    public void setShare(Long share) {
        this.share = share;
    }

    public Integer getMessageState() {
        return messageState;
    }

    public void setMessageState(Integer messageState) {
        this.messageState = messageState;
    }


    public Long getOrderType() {
        return orderType;
    }

    public void setOrderType(Long orderType) {
        this.orderType = orderType;
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

    public Long getScoreC() {
        return scoreC;
    }

    public void setScoreC(Long scoreC) {
        this.scoreC = scoreC;
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

    public Long getLjCommission() {
        return this.getCommission();
    }

    public Long getWxCommission() {
        return scanCodeOrderExt.getThirdCommission();
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

    public Date getRefundDate() {
        return refundDate;
    }

    public void setRefundDate(Date refundDate) {
        this.refundDate = refundDate;
    }

    public Long getTransferMoneyFromScore() {
        return transferMoneyFromScore;
    }

    public void setTransferMoneyFromScore(Long transferMoneyFromScore) {
        this.transferMoneyFromScore = transferMoneyFromScore;
    }

    public String getLePayCode() {
        return lePayCode;
    }

    public void setLePayCode(String lePayCode) {
        this.lePayCode = lePayCode;
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

    public Long getTrueScore() {
        return trueScore;
    }

    public void setTrueScore(Long trueScore) {
        this.trueScore = trueScore;
    }

    public Long getScoreB() {
        return null;
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

    public String getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(String settleDate) {
        this.settleDate = settleDate;
    }

    public ScanCodeOrderExt getScanCodeOrderExt() {
        return scanCodeOrderExt;
    }

    public void setScanCodeOrderExt(ScanCodeOrderExt scanCodeOrderExt) {
        this.scanCodeOrderExt = scanCodeOrderExt;
    }

    public void setWxCommission(long wxCommission) {
        scanCodeOrderExt.setThirdCommission(wxCommission);
    }

    public void setWxTrueCommission(long wxTrueCommission) {
        scanCodeOrderExt.setThirdTrueCommission(wxTrueCommission);
    }

}
