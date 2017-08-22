package com.jifenke.lepluslive.yibao.domain.criteria;

/**
 * 结算详情 - 查询条件
 * Created by xf on 17-8-21.
 */
public class SettlementDetialCriteria {
    private Long merchantId;    //  门店ID
    private String tradeDate;   //  交易日期
    private Integer orderType;  //  订单类型
    private Integer offset;     //  页数
    private Integer payType;    //  支付方式

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }
}
