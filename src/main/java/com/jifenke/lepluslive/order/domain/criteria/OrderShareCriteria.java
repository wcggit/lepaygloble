package com.jifenke.lepluslive.order.domain.criteria;

import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerManager;

/**
 * Created by wcg on 16/7/8.
 */
public class OrderShareCriteria {

    private String startDate;

    private String endDate;

    private Integer offset;

    private Merchant merchant;

    private Merchant tradeMerchant;

    private Merchant lockMerchant;

    private Partner lockPartner;

    private Partner tradePartner;

    public Merchant getMerchant() {
        return merchant;
    }

    private LeJiaUser lejiaUser;

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

    private Partner partner;

    private PartnerManager partnerManager;

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public PartnerManager getPartnerManager() {
        return partnerManager;
    }

    public void setPartnerManager(PartnerManager partnerManager) {
        this.partnerManager = partnerManager;
    }

    public Merchant getTradeMerchant() {
        return tradeMerchant;
    }

    public void setTradeMerchant(Merchant tradeMerchant) {
        this.tradeMerchant = tradeMerchant;
    }

    public Merchant getLockMerchant() {
        return lockMerchant;
    }

    public void setLockMerchant(Merchant lockMerchant) {
        this.lockMerchant = lockMerchant;
    }

    public Partner getLockPartner() {
        return lockPartner;
    }

    public void setLockPartner(Partner lockPartner) {
        this.lockPartner = lockPartner;
    }

    public Partner getTradePartner() {
        return tradePartner;
    }

    public void setTradePartner(Partner tradePartner) {
        this.tradePartner = tradePartner;
    }

    public LeJiaUser getLejiaUser() {
        return lejiaUser;
    }

    public void setLejiaUser(LeJiaUser lejiaUser) {
        this.lejiaUser = lejiaUser;
    }
}
