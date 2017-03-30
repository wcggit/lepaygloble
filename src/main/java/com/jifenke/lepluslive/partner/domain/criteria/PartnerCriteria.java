package com.jifenke.lepluslive.partner.domain.criteria;

import com.jifenke.lepluslive.partner.domain.entities.PartnerManager;

import java.util.Date;

/**
 * Created by xf on 17-3-30.
 */
public class PartnerCriteria {
    private Integer offset;
    private String name;
    private String phoneNumer;
    private Date createDate;
    private PartnerManager partnerManager;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumer() {
        return phoneNumer;
    }

    public void setPhoneNumer(String phoneNumer) {
        this.phoneNumer = phoneNumer;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public PartnerManager getPartnerManager() {
        return partnerManager;
    }

    public void setPartnerManager(PartnerManager partnerManager) {
        this.partnerManager = partnerManager;
    }
}
