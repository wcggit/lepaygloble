package com.jifenke.lepluslive.order.domain.criteria;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;

/**
 * Created by wcg on 16/7/8.
 */
public class OrderShareCriteria {

    private String startDate;

    private String endDate;

    private Integer offset;

    private Merchant merchant;


    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
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
}
