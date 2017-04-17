package com.jifenke.lepluslive.order.domain.entities;

import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;

import java.util.Date;

import javax.persistence.*;

/**
 * Created by wcg on 16/5/5.
 */
@Entity
@Table(name = "FINANCIAL_STATISTIC")
public class FinancialStatistic {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String statisticId = MvUtil.getOrderNumber();

    private Date balanceDate; //结算日期

    private Date transferDate; //转账日期

    @ManyToOne
    private Merchant merchant;

    private Long transferPrice;

<<<<<<< HEAD
    private Integer state = 0; //状态2 表示挂帐
=======
  private Integer state = 0; //状态2 表示挂帐
>>>>>>> 9d0a76cf7863542b79adb69eceb808dd5a9af7e0


    private Long transferFromTruePay; //扫码支付转账金额

    public Long getTransferFromTruePay() {
        return transferFromTruePay;
    }

    public void setTransferFromTruePay(Long transferFromTruePay) {
        this.transferFromTruePay = transferFromTruePay;
    }

    private Long appTransfer;//app总转账

    private Long appTransFromTruePay;//app微信转账 金额

    private Long posTransfer; //pos 转账金额

    private Long posTransFromTruePay;//pos银行转账

    @Version
    private Long version = 0L;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public Long getTransferPrice() {
        return transferPrice;
    }

    public void setTransferPrice(Long transferPrice) {
        this.transferPrice = transferPrice;
    }

    public Date getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(Date transferDate) {
        this.transferDate = transferDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatisticId() {
        return statisticId;
    }

    public void setStatisticId(String statisticId) {
        this.statisticId = statisticId;
    }

    public Date getBalanceDate() {
        return balanceDate;
    }

    public void setBalanceDate(Date balanceDate) {
        this.balanceDate = balanceDate;
    }

    public Long getAppTransfer() {
        return appTransfer;
    }

    public void setAppTransfer(Long appTransfer) {
        this.appTransfer = appTransfer;
    }

    public Long getAppTransFromTruePay() {
        return appTransFromTruePay;
    }

    public void setAppTransFromTruePay(Long appTransFromTruePay) {
        this.appTransFromTruePay = appTransFromTruePay;
    }

    public Long getPosTransfer() {
        return posTransfer;
    }

    public void setPosTransfer(Long posTransfer) {
        this.posTransfer = posTransfer;
    }

    public Long getPosTransFromTruePay() {
        return posTransFromTruePay;
    }

    public void setPosTransFromTruePay(Long posTransFromTruePay) {
        this.posTransFromTruePay = posTransFromTruePay;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
