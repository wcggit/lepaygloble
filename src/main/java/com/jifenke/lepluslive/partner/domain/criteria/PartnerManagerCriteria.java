package com.jifenke.lepluslive.partner.domain.criteria;

import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerManager;

import java.util.List;

/**
 * Created by xf on 17-3-22.
 */
public class PartnerManagerCriteria {
    private String startDate;
    private String endDate;
    private Long type;          // 0-锁定会员  1-锁定门店  2-佣金收入
    private PartnerManager partnerManager;
    private List<Partner> partners;     // 城市合伙人下的天使合伙人
    private Partner partner;            // 城市合伙人下指定天使合伙人

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

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public PartnerManager getPartnerManager() {
        return partnerManager;
    }

    public void setPartnerManager(PartnerManager partnerManager) {
        this.partnerManager = partnerManager;
    }

    public List<Partner> getPartners() {
        return partners;
    }

    public void setPartners(List<Partner> partners) {
        this.partners = partners;
    }

    private String partnerName; // 合伙人名称

    private String phoneNumber; // 手机号

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    private Integer limit;       // 页数

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }
}
