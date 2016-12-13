package com.jifenke.lepluslive.merchant.domain.criteria;

import com.jifenke.lepluslive.order.domain.entities.OffLineOrder;

import org.springframework.data.domain.Page;

/**
 * 扫码支付
 * Created by tqy on 16/12/12.
 */
public class CodeOrderCriteria {

    /**
     * 订单数据
     */
    private Page<OffLineOrder> page;

    /**
     * 门店ID
     */
    private Object [] storeIds;

    /**
     * 订单类型
     */
    private Integer rebateWay; //1 代表非会员消费 2 代表会员订单 3 导流订单 4 会员普通订单

    /**
     * 支付方式
     */
    private Integer payWay; //1微信,2红包

    /**
     * 扫码订单编号
     */
    private String orderSid;

    /**
     * 开始时间
     */
    private String startDate;

    /**
     * 结束时间
     */
    private String endDate;

    private Double totalPrice;
    private Double truePay;
    private Double trueScore;
    private Double transferMoneyFromTruePay;
    private Double transferMoneyHB;

    private Integer state = 0; //支付状态
    /**
     * 当前第几页
     */
    private Integer currentPage;
    private Integer pageSize = 10;



    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getOrderSid() {
        return orderSid;
    }

    public void setOrderSid(String orderSid) {
        this.orderSid = orderSid;
    }

    public Page<OffLineOrder> getPage() {
        return page;
    }

    public void setPage(Page<OffLineOrder> page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPayWay() {
        return payWay;
    }

    public void setPayWay(Integer payWay) {
        this.payWay = payWay;
    }

    public Integer getRebateWay() {
        return rebateWay;
    }

    public void setRebateWay(Integer rebateWay) {
        this.rebateWay = rebateWay;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Object[] getStoreIds() {
        return storeIds;
    }

    public void setStoreIds(Object[] storeIds) {
        this.storeIds = storeIds;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getTransferMoneyFromTruePay() {
        return transferMoneyFromTruePay;
    }

    public void setTransferMoneyFromTruePay(Double transferMoneyFromTruePay) {
        this.transferMoneyFromTruePay = transferMoneyFromTruePay;
    }

    public Double getTransferMoneyHB() {
        return transferMoneyHB;
    }

    public void setTransferMoneyHB(Double transferMoneyHB) {
        this.transferMoneyHB = transferMoneyHB;
    }

    public Double getTruePay() {
        return truePay;
    }

    public void setTruePay(Double truePay) {
        this.truePay = truePay;
    }

    public Double getTrueScore() {
        return trueScore;
    }

    public void setTrueScore(Double trueScore) {
        this.trueScore = trueScore;
    }

}
