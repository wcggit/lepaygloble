package com.jifenke.lepluslive.cmbc.domain.criteria;

/**
 * Created by xf on 17-9-23.
 */
public class CmbcSettlementCriteria {
    private Integer offset;          // 页数
    private Integer state;           // 状态
    private String startDate;
    private String endDate;
    private Long merchantId;         // 商户ID
    private String cmbcMerchantNo;   // 子商户号

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

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCmbcMerchantNo() {
        return cmbcMerchantNo;
    }

    public void setCmbcMerchantNo(String cmbcMerchantNo) {
        this.cmbcMerchantNo = cmbcMerchantNo;
    }
}
