package com.jifenke.lepluslive.withdraw.domain.criteria;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerManager;


/**
 * Created by xf on 16-10-25.
 */
public class WithdrawCriteria {
    private Integer offset;
    private String withDrawStartDate;   // 提现开始时间-开始
    private String withDrawEndDate;     // 提现开始时间-结束
    private String completeDateStartDate;   // 到帐时间-开始
    private String completeDateEndDate;     // 到帐时间-结束
    private Integer state;              // 提现状态
    private Integer billType;           //  0是合伙人管理员  1是合伙人 2是商户
    private Partner partner;            //  合伙人
    private Merchant merchant;          //  商户
    private PartnerManager partnerManager;  // 城市合伙人
    private MerchantUser merchantUser;

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public String getWithDrawStartDate() {
        return withDrawStartDate;
    }

    public void setWithDrawStartDate(String withDrawStartDate) {
        this.withDrawStartDate = withDrawStartDate;
    }

    public String getWithDrawEndDate() {
        return withDrawEndDate;
    }

    public void setWithDrawEndDate(String withDrawEndDate) {
        this.withDrawEndDate = withDrawEndDate;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public String getCompleteDateStartDate() {
        return completeDateStartDate;
    }

    public void setCompleteDateStartDate(String completeDateStartDate) {
        this.completeDateStartDate = completeDateStartDate;
    }

    public String getCompleteDateEndDate() {
        return completeDateEndDate;
    }

    public void setCompleteDateEndDate(String completeDateEndDate) {
        this.completeDateEndDate = completeDateEndDate;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public PartnerManager getPartnerManager() {
        return partnerManager;
    }

    public void setPartnerManager(PartnerManager partnerManager) {
        this.partnerManager = partnerManager;
    }

    public MerchantUser getMerchantUser() {
        return merchantUser;
    }

    public void setMerchantUser(MerchantUser merchantUser) {
        this.merchantUser = merchantUser;
    }
}
