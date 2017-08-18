package com.jifenke.lepluslive.partner.controller.dto;

import com.jifenke.lepluslive.partner.domain.entities.Partner;

/**
 * Created by xf on 17-3-22.
 */
public class PartnerDto {
    private Partner partner;
    private Long bindMerchantNum;         // 锁定门店数量
    private Long bindUserNum;            // 锁定会员数量
    private double offLineCommission;       // 线下佣金收入
    private double onLineCommission;        // 线上佣金收入

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }


    public void setBindUserNum(Long bindUserNum) {
        this.bindUserNum = bindUserNum;
    }

    public double getOffLineCommission() {
        return offLineCommission;
    }

    public void setOffLineCommission(double offLineCommission) {
        this.offLineCommission = offLineCommission;
    }

    public double getOnLineCommission() {
        return onLineCommission;
    }

    public void setOnLineCommission(double onLineCommission) {
        this.onLineCommission = onLineCommission;
    }

    public Long getBindMerchantNum() {
        return bindMerchantNum;
    }

    public void setBindMerchantNum(Long bindMerchantNum) {
        this.bindMerchantNum = bindMerchantNum;
    }

    public Long getBindUserNum() {
        return bindUserNum;
    }
}

