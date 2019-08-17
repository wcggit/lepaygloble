package com.jifenke.lepluslive.groupon.domain.criteria;

/**
 * 团购订单 - 查询条件
 *
 * @author XF
 * @date 2017/6/19
 */
public class GrouponOrderCriteria {
    private String orderSid;                     // 订单SID
    private String productSid;              // 团购SID
    private String productName;             // 团购名称
    private Integer orderState;             // 团购状态  0下架 1 上架
    private Integer offset;                 // 页数
    private Integer orderType;                   // 页数
    private Integer state;                  // 支付状态
    private String startDate;
    private String endDate;

    public String getProductSid() {
        return productSid;
    }

    public void setProductSid(String productSid) {
        this.productSid = productSid;
    }

    public Integer getOrderState() {
        return orderState;
    }

    public void setOrderState(Integer orderState) {
        this.orderState = orderState;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOrderSid() {
        return orderSid;
    }

    public void setOrderSid(String orderSid) {
        this.orderSid = orderSid;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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
}
