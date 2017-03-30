package com.jifenke.lepluslive.order.controller.view;

import java.util.Date;
import java.util.List;

/**
 * Created by xf on 16-12-8.
 * 每日账单表格显示数据
 */
public class LejiaOrderDTO {
    private List<String> dates;                             //  统计时间
    private List<Double> totalTransfer; // 商户入账总金额;
    private List<Double> wxTransfer;    // 微信扫码牌;
    private List<Double> posCardTransfer;   // pos 刷卡入账
    private List<Double> posMobileTransfer; // pos 移动入账
    private List<Double> scoreTransfer;     // 红包支付入账

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    public List<Double> getTotalTransfer() {
        return totalTransfer;
    }

    public void setTotalTransfer(List<Double> totalTransfer) {
        this.totalTransfer = totalTransfer;
    }

    public List<Double> getWxTransfer() {
        return wxTransfer;
    }

    public void setWxTransfer(List<Double> wxTransfer) {
        this.wxTransfer = wxTransfer;
    }

    public List<Double> getPosCardTransfer() {
        return posCardTransfer;
    }

    public void setPosCardTransfer(List<Double> posCardTransfer) {
        this.posCardTransfer = posCardTransfer;
    }

    public List<Double> getPosMobileTransfer() {
        return posMobileTransfer;
    }

    public void setPosMobileTransfer(List<Double> posMobileTransfer) {
        this.posMobileTransfer = posMobileTransfer;
    }

    public List<Double> getScoreTransfer() {
        return scoreTransfer;
    }

    public void setScoreTransfer(List<Double> scoreTransfer) {
        this.scoreTransfer = scoreTransfer;
    }

    private List<Double> wxMobileTransfer;              // 微信移动支付
    private List<Double> aliMobileTransfer;             // 支付宝移动支付

    public List<Double> getWxMobileTransfer() {
        return wxMobileTransfer;
    }

    public void setWxMobileTransfer(List<Double> wxMobileTransfer) {
        this.wxMobileTransfer = wxMobileTransfer;
    }

    public List<Double> getAliMobileTransfer() {
        return aliMobileTransfer;
    }

    public void setAliMobileTransfer(List<Double> aliMobileTransfer) {
        this.aliMobileTransfer = aliMobileTransfer;
    }

    private List<Double> posScores;                    // POS积分
    private List<Double> offScores;                    // 扫码积分

    public List<Double> getPosScores() {
        return posScores;
    }

    public void setPosScores(List<Double> posScores) {
        this.posScores = posScores;
    }

    public List<Double> getOffScores() {
        return offScores;
    }

    public void setOffScores(List<Double> offScores) {
        this.offScores = offScores;
    }
}
