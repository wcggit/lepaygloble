package com.jifenke.lepluslive.partner.controller.dto;

/**
 * Created by xf on 17-3-22.
 */
public class PartnerManagerDto {
    private String date;            //  日期
    private Long count;             //  会员/门店 数量
    private double commission;      //  佣金

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }
}
