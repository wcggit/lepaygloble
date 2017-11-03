package com.jifenke.lepluslive.order.domain.criteria;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;

/**
 * Created by wcg on 16/5/5.
 */
public class OLOrderCriteria {

    private String startDate;

    private String endDate;

    private Integer state;

    private Merchant merchant;

    private String orderSid;

    private Integer rebateWay;

    private Integer offset;

    public Integer getRebateWay() {
        return rebateWay;
    }

    public void setRebateWay(Integer rebateWay) {
        this.rebateWay = rebateWay;
    }

    public String getOrderSid() {
        return orderSid;
    }

    public void setOrderSid(String orderSid) {
        this.orderSid = orderSid;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    private Integer payWay;

    private Integer orderType;

    private Integer payType;  // 0=微信 1=支付宝

    private Integer payment;  ////付款方式  0=纯现金|1=纯红包|2=混合

    public Integer getPayWay() {
        return payWay;
    }

    public void setPayWay(Integer payWay) {
        this.payWay = payWay;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Integer getPayment() {
        return payment;
    }

    public void setPayment(Integer payment) {
        this.payment = payment;
    }
}
