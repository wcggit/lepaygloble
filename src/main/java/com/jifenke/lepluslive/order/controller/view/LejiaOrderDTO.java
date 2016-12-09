package com.jifenke.lepluslive.order.controller.view;

import java.util.Date;

/**
 * Created by xf on 16-12-8.
 * 每日账单表格显示数据
 */
public class LejiaOrderDTO {
    private String startDate;  // 结束时间
    private Long totalTransfer; // 商户入账总金额;
    private Long wxTransfer;    // 微信扫码牌;
    private Long posCardTransfer;   // pos 刷卡入账
    private Long posMobileTransfer; // pos 移动入账
    private Long scoreTransfer;     // 红包支付入账

    public Long getTotalTransfer() {
        return totalTransfer;
    }

    public void setTotalTransfer(Long totalTransfer) {
        this.totalTransfer = totalTransfer;
    }

    public Long getWxTransfer() {
        return wxTransfer;
    }

    public void setWxTransfer(Long wxTransfer) {
        this.wxTransfer = wxTransfer;
    }

    public Long getPosCardTransfer() {
        return posCardTransfer;
    }

    public void setPosCardTransfer(Long posCardTransfer) {
        this.posCardTransfer = posCardTransfer;
    }

    public Long getPosMobileTransfer() {
        return posMobileTransfer;
    }

    public void setPosMobileTransfer(Long posMobileTransfer) {
        this.posMobileTransfer = posMobileTransfer;
    }

    public Long getScoreTransfer() {
        return scoreTransfer;
    }

    public void setScoreTransfer(Long scoreTransfer) {
        this.scoreTransfer = scoreTransfer;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
