package com.jifenke.lepluslive.order.domain.criteria;

/**
 * Created by xf on 17-9-23.
 */
public class UnionSettlementCriteria {
    private Integer offset;          // 页数
    private Integer state;           // 状态
    private String startDate;
    private String endDate;
    private Long merchantUserId;         // 商户ID

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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getMerchantUserId() {
        return merchantUserId;
    }

    public void setMerchantUserId(Long merchantUserId) {
        this.merchantUserId = merchantUserId;
    }
}
