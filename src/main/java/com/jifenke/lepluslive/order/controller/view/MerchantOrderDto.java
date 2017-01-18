package com.jifenke.lepluslive.order.controller.view;

/**
 * Created by xf on 16-12-14.
 * 门店账单数据封装类
 */
public class MerchantOrderDto {
    private String merchantName;       //  商户名
    private Double totalTransfer;      //  总入账
    private Double offTransferFromTruePay;  // 微信入账
    private Double posCardTransfer;    //   Pos 刷卡入账
    private Double mobileTransfer;     //   Pos 移动入账 =  微信 + 支付宝
    private Double mobileWxTransfer;   //   Pos 移动 - 微信入账
    private Double mobileAliTransfer;  //   Pos 移动 - 支付宝入账
    private Double totalScore;         //   红包入账 = 扫码 + pos
    private Double offScore;           //   红包入账 - 扫码
    private Double posScore;           //   红包入账 - pos 支付

    private Long merchantId;            //  商户 id

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Double getTotalTransfer() {
        return totalTransfer;
    }

    public void setTotalTransfer(Double totalTransfer) {
        this.totalTransfer = totalTransfer;
    }

    public Double getOffTransferFromTruePay() {
        return offTransferFromTruePay;
    }

    public void setOffTransferFromTruePay(Double offTransferFromTruePay) {
        this.offTransferFromTruePay = offTransferFromTruePay;
    }

    public Double getPosCardTransfer() {
        return posCardTransfer;
    }

    public void setPosCardTransfer(Double posCardTransfer) {
        this.posCardTransfer = posCardTransfer;
    }

    public Double getMobileWxTransfer() {
        return mobileWxTransfer;
    }

    public void setMobileWxTransfer(Double mobileWxTransfer) {
        this.mobileWxTransfer = mobileWxTransfer;
    }

    public Double getMobileAliTransfer() {
        return mobileAliTransfer;
    }

    public void setMobileAliTransfer(Double mobileAliTransfer) {
        this.mobileAliTransfer = mobileAliTransfer;
    }

    public Double getOffScore() {
        return offScore;
    }

    public void setOffScore(Double offScore) {
        this.offScore = offScore;
    }

    public Double getPosScore() {
        return posScore;
    }

    public void setPosScore(Double posScore) {
        this.posScore = posScore;
    }

    public Double getMobileTransfer() {
        return mobileTransfer;
    }

    public void setMobileTransfer(Double mobileTransfer) {
        this.mobileTransfer = mobileTransfer;
    }

    public Double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Double totalScore) {
        this.totalScore = totalScore;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }
}
