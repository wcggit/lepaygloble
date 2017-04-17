package com.jifenke.lepluslive.order.domain.criteria;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;

import java.util.List;

/**
 * Created by wcg on 16/5/9.
 */
public class FinancialCriteria {

    private String startDate;

    private String endDate;

    private Merchant merchant;

    private Integer offset;

    private Integer state;          // 0－转账中　１－已到账　　２－已挂账

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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    private List<Merchant> merchantList;

    public List<Merchant> getMerchantList() {
        return merchantList;
    }

    public void setMerchantList(List<Merchant> merchantList) {
        this.merchantList = merchantList;
    }
}
