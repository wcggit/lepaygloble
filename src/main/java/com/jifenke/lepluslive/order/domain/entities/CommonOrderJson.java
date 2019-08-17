package com.jifenke.lepluslive.order.domain.entities;

import org.apache.poi.hssf.usermodel.HSSFRow;

/**
 * 通用订单记录参数json对应表
 * Created by zhangwen on 2017/9/4.
 */
public class CommonOrderJson {

    private String settleDate;//清算日期

    private String tradeDate;//交易日期

    private String tradeTime;//交易时间

    private String terminalNum;//终端号

    private String tradeAmount;//交易金额，对应营销联盟.原单金额

    private String settleAmount;//清算金额

    private String charge;//手续费

    private String serialNum;//流水号

    private String tradeType;//交易类型

    private String cardNum;//卡号

    private String cardName;//发卡行

    private String cardType;//卡类型

    private String merNum;//商户号

    public void rowToJson(HSSFRow row) {
        this.setSettleDate(row.getCell(0).toString());
        this.setTradeDate(row.getCell(1).toString());
        this.setTradeTime(row.getCell(2).toString());
    }

    public String getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(String settleDate) {
        this.settleDate = settleDate;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }

    public String getTerminalNum() {
        return terminalNum;
    }

    public void setTerminalNum(String terminalNum) {
        this.terminalNum = terminalNum;
    }

    public String getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(String tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public String getSettleAmount() {
        return settleAmount;
    }

    public void setSettleAmount(String settleAmount) {
        this.settleAmount = settleAmount;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getMerNum() {
        return merNum;
    }

    public void setMerNum(String merNum) {
        this.merNum = merNum;
    }
}
