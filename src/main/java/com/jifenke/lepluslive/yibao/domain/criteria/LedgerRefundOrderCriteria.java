package com.jifenke.lepluslive.yibao.domain.criteria;


/**
 * 退款单 - 查询条件
 * Created by xf on 17-7-14.
 */
public class LedgerRefundOrderCriteria {
    private Integer offset;        // 页数
    private String completeStart;  // 退款完成时间 - 开始
    private String completeEnd;    // 退款完成时间 - 结束
    private String merchantId;     // 交易门店ID
    private Integer orderType;     // 订单类型
    private Integer state;         //退款状态 0=待退款，1=未开始退款，2=退款成功，3=退款失败，其他为通道返回码
    private String refundOrderSid; //己方退款单号

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public String getCompleteStart() {
        return completeStart;
    }

    public void setCompleteStart(String completeStart) {
        this.completeStart = completeStart;
    }

    public String getCompleteEnd() {
        return completeEnd;
    }

    public void setCompleteEnd(String completeEnd) {
        this.completeEnd = completeEnd;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
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

    public String getRefundOrderSid() {
        return refundOrderSid;
    }

    public void setRefundOrderSid(String refundOrderSid) {
        this.refundOrderSid = refundOrderSid;
    }

    private String tradeDate;

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }
}
