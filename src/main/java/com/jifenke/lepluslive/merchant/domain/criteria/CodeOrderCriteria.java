package com.jifenke.lepluslive.merchant.domain.criteria;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.order.domain.entities.OffLineOrder;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 扫码支付
 * Created by tqy on 16/12/12.
 */
public class CodeOrderCriteria {

    /**
     * 订单数据
     */
    private Page<OffLineOrder> page;//此字段不用了

    private List<Object[]> listCodeOrder;//订单数据

    public List<Object[]> getListCodeOrder() {
        return listCodeOrder;
    }

    public void setListCodeOrder(List<Object[]> listCodeOrder) {
        this.listCodeOrder = listCodeOrder;
    }

    /**
     * 门店ID
     */
    private Object[] storeIds;

    /**
     * 订单类型
     */
    private String rebateWay; //1 代表非会员消费 2 代表会员订单 3 导流订单 4 会员普通订单

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

    private Integer currentPage;//当前第几页
    private Integer pageSize = 10;//每页size
    private Integer totalPages;//总页数
    private Integer totalCount;//总记录数


    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

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

    public String getRebateWay() {
        return rebateWay;
    }

    public void setRebateWay(String rebateWay) {
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

    private Long merchantId;            // 门店ID

    private Integer payment;            // 金额类型

    private Integer payType;            // 支付方式  0=微信  1=支付宝

    private Integer offset;             // 页码

    private Integer orderType;          // 订单类型

    private String payWayName;          // 支付名称

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getPayment() {
        return payment;
    }

    public void setPayment(Integer payment) {
        this.payment = payment;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getPayWayName() {
        return payWayName;
    }

    public void setPayWayName(String payWayName) {
        this.payWayName = payWayName;
    }

    private Merchant merchant;

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }
}
