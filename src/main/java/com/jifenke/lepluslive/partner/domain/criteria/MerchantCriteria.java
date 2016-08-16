package com.jifenke.lepluslive.partner.domain.criteria;

import com.jifenke.lepluslive.partner.domain.entities.Partner;

/**
 * Created by wcg on 16/8/16.
 */
public class MerchantCriteria {

    private String startDate; //商户创建时间

    private String endDate;

    private Integer offset;

    private Partner partner;

    private Integer partnerShip;

    private String merchantName;

    private Integer userBindState;


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

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public Integer getPartnerShip() {
        return partnerShip;
    }

    public void setPartnerShip(Integer partnerShip) {
        this.partnerShip = partnerShip;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Integer getUserBindState() {
        return userBindState;
    }

    public void setUserBindState(Integer userBindState) {
        this.userBindState = userBindState;
    }
}
