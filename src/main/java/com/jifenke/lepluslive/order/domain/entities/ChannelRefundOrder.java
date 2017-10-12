package com.jifenke.lepluslive.order.domain.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * 通道退款单
 */
@Entity
public class ChannelRefundOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private Date dateCreated;
    private Date dateCompleted;             //退款完成时间
    private String refundOrderSid;          //退款请求号

    @Column(nullable = false, length = 30)
    private String orderSid;                //对应要退款订单的订单号

    @Column(nullable = false, length = 50)
    private String ledgerNo;                //支付时使用的通道子商户号

    @Column(nullable = false, length = 50)
    private String realMerNum;              //该订单所属门店实际对应的通道子商户号
    private int orderFrom;                  //退款通道 1=易宝，2=民生
    private int orderType;                  //0=普通订单,1=乐加订单

    @Column(nullable = false, length = 10)
    private String merchantId;              //交易门店SID

    @Column(length = 30)
    private String orderId;                 //第三方的退款流水号，纯红包退款=" "

    @Column(nullable = false, length = 10)
    private String tradeDate;               //清算日期（yyyy-MM-dd） 退款完成时间在D日切点前时，清算日期即为D日。在D日日切点后，清算日期记为D+1日
    private int state;                      //退款状态 0=待退款，1=未开始退款，2=退款成功，3=退款失败，其他为通道返回码
    private long totalAmount;               //总退款金额=trueAmount+scoreAmount
    private long trueAmount;                //实付部分退款金币（订单实付）（用该值向通道发起退款申请）
    private long scoreAmount;               //鼓励金退款金额
    private int rateCostSide;               //手续费承担方  0=积分客，1=商户，2=通道方
    private long thirdTrueCommission;       //三方实际手续费(对积分客)=truePay*通道费率
    private long scoreCommission;           //鼓励金手续费|鼓励金佣金=对应order.scoreCommission
    private long transferMoney;             //应收商户的总金额=对应order.transferMoney
    private long transferMoneyFromTruePay;  //因产生退款，应收商户的资金=对应order.transferMoneyFromTruePay
    private long transferMoneyFromScore;    //因产生退款，应收商户的=对应order.transferMoneyFromScore
    private long realScoreA;                //追回会员本笔交易获得的鼓励金
    private long realScoreC;                //追回会员本笔交易获得的金币
    private int shareRecoverType;           //分润追回策略, 0：追回至余额0为止。1:全额追回，无法追回则无法退款。
    private long shareBack;                 //应追回的分润总额
    private Long shareRealBack;             //实际追回的分润总额
    private Long shareOrderId;              //如果该订单有分润=分润单ID
    private int payType;                    //0代表微信 1 代表支付宝
    private String merchantRate;            //商户号当时的佣金费率
    //set
    public void setDateCompleted(Date dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public void setRefundOrderSid(String refundOrderSid) {
        this.refundOrderSid = refundOrderSid;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setRateCostSide(int rateCostSide) {
        this.rateCostSide = rateCostSide;
    }

    public void setShareRealBack(Long shareRealBack) {
        this.shareRealBack = shareRealBack;
    }

    //get
    public long getId() {
        return id;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public Date getDateCompleted() {
        return dateCompleted;
    }

    public String getRefundOrderSid() {
        return refundOrderSid;
    }

    public String getOrderSid() {
        return orderSid;
    }

    public String getLedgerNo() {
        return ledgerNo;
    }

    public String getRealMerNum() {
        return realMerNum;
    }

    public int getOrderFrom() {
        return orderFrom;
    }

    public int getOrderType() {
        return orderType;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public int getState() {
        return state;
    }

    public long getTotalAmount() {
        return totalAmount;
    }

    public long getTrueAmount() {
        return trueAmount;
    }

    public long getScoreAmount() {
        return scoreAmount;
    }

    public int getRateCostSide() {
        return rateCostSide;
    }

    public long getThirdTrueCommission() {
        return thirdTrueCommission;
    }

    public long getScoreCommission() {
        return scoreCommission;
    }

    public long getTransferMoney() {
        return transferMoney;
    }

    public long getTransferMoneyFromTruePay() {
        return transferMoneyFromTruePay;
    }

    public long getTransferMoneyFromScore() {
        return transferMoneyFromScore;
    }

    public long getRealScoreA() {
        return realScoreA;
    }

    public long getRealScoreC() {
        return realScoreC;
    }

    public int getShareRecoverType() {
        return shareRecoverType;
    }

    public long getShareBack() {
        return shareBack;
    }

    public Long getShareRealBack() {
        return shareRealBack;
    }

    public Long getShareOrderId() {
        return shareOrderId;
    }

    public int getPayType() {
        return payType;
    }

    public String getMerchantRate() {
        return merchantRate;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setOrderSid(String orderSid) {
        this.orderSid = orderSid;
    }

    public void setLedgerNo(String ledgerNo) {
        this.ledgerNo = ledgerNo;
    }

    public void setRealMerNum(String realMerNum) {
        this.realMerNum = realMerNum;
    }

    public void setOrderFrom(int orderFrom) {
        this.orderFrom = orderFrom;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setTrueAmount(long trueAmount) {
        this.trueAmount = trueAmount;
    }

    public void setScoreAmount(long scoreAmount) {
        this.scoreAmount = scoreAmount;
    }

    public void setThirdTrueCommission(long thirdTrueCommission) {
        this.thirdTrueCommission = thirdTrueCommission;
    }

    public void setScoreCommission(long scoreCommission) {
        this.scoreCommission = scoreCommission;
    }

    public void setTransferMoney(long transferMoney) {
        this.transferMoney = transferMoney;
    }

    public void setTransferMoneyFromTruePay(long transferMoneyFromTruePay) {
        this.transferMoneyFromTruePay = transferMoneyFromTruePay;
    }

    public void setTransferMoneyFromScore(long transferMoneyFromScore) {
        this.transferMoneyFromScore = transferMoneyFromScore;
    }

    public void setRealScoreA(long realScoreA) {
        this.realScoreA = realScoreA;
    }

    public void setRealScoreC(long realScoreC) {
        this.realScoreC = realScoreC;
    }

    public void setShareRecoverType(int shareRecoverType) {
        this.shareRecoverType = shareRecoverType;
    }

    public void setShareBack(long shareBack) {
        this.shareBack = shareBack;
    }

    public void setShareOrderId(Long shareOrderId) {
        this.shareOrderId = shareOrderId;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public void setMerchantRate(String merchantRate) {
        this.merchantRate = merchantRate;
    }
}
