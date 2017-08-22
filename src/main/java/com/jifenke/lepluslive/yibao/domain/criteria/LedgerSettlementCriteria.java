package com.jifenke.lepluslive.yibao.domain.criteria;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;

/**
 * 易宝通道结算单 - 查询条件
 * Created by xf on 17-7-13.
 */
public class LedgerSettlementCriteria {

    private Integer offset;                // 页数
    private String orderSid;               // 通道结算单号
    private String merchantUserId;         // 乐加商户编号
    private String ledgerNo;               // 易宝商户编号
    private Integer transferState;         // 转账状态 0=待转账，1=转账成功，2=转账失败
    private Integer state;                 // 结算状态 0=待查询，1=打款成功，2=已退回，3=无结算记录，4=已扣款未打款，5=打款中，-1=打款失败，-2=银行返回打款失败
    private String startDate;              // 清算日期 - 起始时间
    private String endDate;                // 清算日期 - 截止时间

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
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

    public Integer getTransferState() {
        return transferState;
    }

    public void setTransferState(Integer transferState) {
        this.transferState = transferState;
    }

    public String getMerchantUserId() {
        return merchantUserId;
    }

    public void setMerchantUserId(String merchantUserId) {
        this.merchantUserId = merchantUserId;
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

    private Merchant merchant;

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }
}
