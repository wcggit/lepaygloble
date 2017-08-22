package com.jifenke.lepluslive.yibao.domain.criteria;

/**
 * 易宝门店结算单 - 查询条件
 * Created by xf on 17-7-13.
 */
public class StoreSettlementCriteria {

    private Integer offset;                // 页数
    private String startDate;              // 清算日期 - 起始时间
    private String endDate;                // 清算日期 - 截止时间
    private String merchantName;           // 门店名称
    private Long merchantId;               // 门店ID
    private String ledgerNo;               // 易宝商户号
    private String ledgerSid;              // 通道结算单号

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

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getLedgerNo() {
        return ledgerNo;
    }

    public void setLedgerNo(String ledgerNo) {
        this.ledgerNo = ledgerNo;
    }

    public String getLedgerSid() {
        return ledgerSid;
    }

    public void setLedgerSid(String ledgerSid) {
        this.ledgerSid = ledgerSid;
    }


    private String tradeDate;

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }
}
