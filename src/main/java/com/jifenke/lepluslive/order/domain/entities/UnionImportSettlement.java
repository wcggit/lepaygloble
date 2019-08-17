package com.jifenke.lepluslive.order.domain.entities;

import org.apache.poi.hssf.usermodel.HSSFRow;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * 银联Excel导入资金结算记录
 * Created by zhangwen on 2017/9/8.
 */
@Entity
public class UnionImportSettlement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date createdAt = new Date();

    private String settleDate;//结算日期

    private Long batchNo;//文件批次号=UnionUploadLog.id

    private String merNum;//商户号

    private long merchantUserId;//商户ID

    private String merName;//商户名

    private String pastNon;//历史(往期)未结算余额

    private String pastDefer;//历史暂缓结算余额

    private String tradeAmount;//本期交易资金.交易金额

    private String tradeCharge;//本期交易资金.手续费

    private String tradeTransfer;//本期交易资金.交易结算净额

    private String adjustAmount;//结算调整金额

    private String currentDeferAmount;//本期暂缓金额

    private String currentTransfer;//本期应结算金额

    private String dayAmount;//本期“天天富”申购金额(本期融资-本期申购-本期补申购挂账-补申购成功-补申购失败退回)

    private String currentPay;//本期划付金额(实际划款金额)

    private String currentNon;//本期未结算余额

    private String currentDefer;//本期暂缓结算余额

    public void parseRow(HSSFRow row) {
        this.setSettleDate(row.getCell(0).toString());
        this.setMerNum(row.getCell(1).toString());
        this.setMerName(row.getCell(2).toString());
        this.setPastNon(row.getCell(3).toString());
        this.setPastDefer(row.getCell(4).toString());
        this.setTradeAmount(row.getCell(5).toString());
        this.setTradeCharge(row.getCell(6).toString());
        this.setTradeTransfer(row.getCell(7).toString());
        this.setAdjustAmount(row.getCell(8).toString());
        this.setCurrentDeferAmount(row.getCell(9).toString());
        this.setCurrentTransfer(row.getCell(10).toString());
        this.setDayAmount(row.getCell(11).toString() + "-" + row.getCell(12).toString() + "-" + row.getCell(13).toString() + "-" + row.getCell(14).toString() + "-" + row.getCell(15).toString());
        this.setCurrentPay(row.getCell(16).toString());
        this.setCurrentNon(row.getCell(17).toString());
        this.setCurrentDefer(row.getCell(18).toString());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(String settleDate) {
        this.settleDate = settleDate;
    }

    public String getMerNum() {
        return merNum;
    }

    public void setMerNum(String merNum) {
        this.merNum = merNum;
    }

    public String getMerName() {
        return merName;
    }

    public void setMerName(String merName) {
        this.merName = merName;
    }

    public long getMerchantUserId() {
        return merchantUserId;
    }

    public void setMerchantUserId(long merchantUserId) {
        this.merchantUserId = merchantUserId;
    }

    public String getPastNon() {
        return pastNon;
    }

    public void setPastNon(String pastNon) {
        this.pastNon = pastNon;
    }

    public String getPastDefer() {
        return pastDefer;
    }

    public void setPastDefer(String pastDefer) {
        this.pastDefer = pastDefer;
    }

    public String getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(String tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public String getTradeCharge() {
        return tradeCharge;
    }

    public void setTradeCharge(String tradeCharge) {
        this.tradeCharge = tradeCharge;
    }

    public String getTradeTransfer() {
        return tradeTransfer;
    }

    public void setTradeTransfer(String tradeTransfer) {
        this.tradeTransfer = tradeTransfer;
    }

    public String getAdjustAmount() {
        return adjustAmount;
    }

    public void setAdjustAmount(String adjustAmount) {
        this.adjustAmount = adjustAmount;
    }

    public String getCurrentDeferAmount() {
        return currentDeferAmount;
    }

    public void setCurrentDeferAmount(String currentDeferAmount) {
        this.currentDeferAmount = currentDeferAmount;
    }

    public String getCurrentTransfer() {
        return currentTransfer;
    }

    public void setCurrentTransfer(String currentTransfer) {
        this.currentTransfer = currentTransfer;
    }

    public String getDayAmount() {
        return dayAmount;
    }

    public void setDayAmount(String dayAmount) {
        this.dayAmount = dayAmount;
    }

    public String getCurrentPay() {
        return currentPay;
    }

    public void setCurrentPay(String currentPay) {
        this.currentPay = currentPay;
    }

    public String getCurrentNon() {
        return currentNon;
    }

    public void setCurrentNon(String currentNon) {
        this.currentNon = currentNon;
    }

    public Long getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(Long batchNo) {
        this.batchNo = batchNo;
    }

    public String getCurrentDefer() {
        return currentDefer;
    }

    public void setCurrentDefer(String currentDefer) {
        this.currentDefer = currentDefer;
    }
}
