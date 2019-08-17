package com.jifenke.lepluslive.partner.domain.criteria;

/**
 * Created by xf on 17-11-16.
 */
public class PartnerWalletLogCriteria {
    /**
     * 合伙人ID
     */
    private Long partnerId;
    private String partnerSid;
    /**
     * 变更来源
     */
    private Integer type;
    /**
     * 变更开始时间
     */
    private String startDate;
    /**
     * 变更截止时间
     */
    private String endDate;

    /***
     *   当前页数
     */
    private Integer offset;

    /***
     *   每页展示数据数
     */
    private Integer limit = 10;


    /***
     *   每页展示数据数
     *   0=线上   1=线下
     */
    private Integer lineType;

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getLineType() {
        return lineType;
    }

    public void setLineType(Integer lineType) {
        this.lineType = lineType;
    }

    public String getPartnerSid() {
        return partnerSid;
    }

    public void setPartnerSid(String partnerSid) {
        this.partnerSid = partnerSid;
    }
}
