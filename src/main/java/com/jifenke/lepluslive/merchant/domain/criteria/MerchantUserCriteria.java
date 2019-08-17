package com.jifenke.lepluslive.merchant.domain.criteria;

import com.jifenke.lepluslive.partner.domain.entities.Partner;

/**
 * Created by xf on 17-11-13.
 * 商户查询条件
 */
public class MerchantUserCriteria {

    /**
     * 商户名称
     */
    private String merchantName;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 联系人名称
     */
    private String linkMan;
    /**
     * 手机号
     */
    private String phoneNum;
    /**
     * 创建时间 - 开始时间
     */
    private String startDate;
    /**
     * 创建时间 - 结束时间
     */
    private String endDate;
    /**
     * 当前页码
     */
    private Integer offset;

    /**
     * 绑定合伙人
     */
    private Partner partner;

    private Long partnerId;
    private String partnerSid;

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

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

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    public String getPartnerSid() {
        return partnerSid;
    }

    public void setPartnerSid(String partnerSid) {
        this.partnerSid = partnerSid;
    }
}
