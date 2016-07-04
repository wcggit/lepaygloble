package com.jifenke.lepluslive.merchant.controller.dto;

/**
 * Created by wcg on 16/6/29.
 */
public class MerchantDto {

    private Long transferingMoney; //转移中的金额

    private Long totalTransferMoney; //累计转账

    private Long availableCommission; //可以佣金

    private Long totalCommission; //总佣金

    public MerchantDto() {
    }

    public MerchantDto(Long transferingMoney, Long totalTransferMoney,
                       Long availableCommission, Long totalCommission) {
        this.transferingMoney = transferingMoney;
        this.totalTransferMoney = totalTransferMoney;
        this.availableCommission = availableCommission;
        this.totalCommission = totalCommission;
    }

    public Long getTransferingMoney() {
        return transferingMoney;
    }

    public void setTransferingMoney(Long transferingMoney) {
        this.transferingMoney = transferingMoney;
    }

    public Long getTotalTransferMoney() {
        return totalTransferMoney;
    }

    public void setTotalTransferMoney(Long totalTransferMoney) {
        this.totalTransferMoney = totalTransferMoney;
    }

    public Long getAvailableCommission() {
        return availableCommission;
    }

    public void setAvailableCommission(Long availableCommission) {
        this.availableCommission = availableCommission;
    }

    public Long getTotalCommission() {
        return totalCommission;
    }

    public void setTotalCommission(Long totalCommission) {
        this.totalCommission = totalCommission;
    }
}
