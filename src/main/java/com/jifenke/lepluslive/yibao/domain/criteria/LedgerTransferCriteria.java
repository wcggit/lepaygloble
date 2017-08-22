package com.jifenke.lepluslive.yibao.domain.criteria;

/**
 * 转账记录 - 查询条件
 * Created by xf on 17-7-14.
 */
public class LedgerTransferCriteria {

    private Integer offset;        // 页数
    private String ledgerSid;      // 通道结算单号
    private String orderSid;       // 转账单号
    private String ledgerNo;       // 易宝子商户号
    private String state;          // 转账状态 0=待转账，1=转账成功，其他为易宝错误码

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public String getLedgerSid() {
        return ledgerSid;
    }

    public void setLedgerSid(String ledgerSid) {
        this.ledgerSid = ledgerSid;
    }

    public String getOrderSid() {
        return orderSid;
    }

    public void setOrderSid(String orderSid) {
        this.orderSid = orderSid;
    }

    public String getLedgerNo() {
        return ledgerNo;
    }

    public void setLedgerNo(String ledgerNo) {
        this.ledgerNo = ledgerNo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
