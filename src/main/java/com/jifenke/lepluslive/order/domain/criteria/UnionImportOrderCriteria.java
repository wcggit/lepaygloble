package com.jifenke.lepluslive.order.domain.criteria;

/**
 * Created by xf on 17-9-23.
 */
public class UnionImportOrderCriteria {

    private Integer offset;           // 页数
    private Integer payWay;           // 支付方式
    private String settleDate;
    private String merNum;            // 商户号

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getPayWay() {
        return payWay;
    }

    public void setPayWay(Integer payWay) {
        this.payWay = payWay;
    }

    public String getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(String settleDate) {
        this.settleDate = settleDate;
    }

    public String getMerNum() {
        return merNum;
    }

    public void setMerNum(String merNum) {
        this.merNum = merNum;
    }
}
