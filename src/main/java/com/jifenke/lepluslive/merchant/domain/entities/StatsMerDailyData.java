package com.jifenke.lepluslive.merchant.domain.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * 商户每日交易等信息统计
 *
 * @author zhangwen at 2017-11-06 10:30
 **/
@Entity
public class StatsMerDailyData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * 创建时间
     */
    private Date createdAt = new Date();
    /**
     * 交易日期 yyyy-MM-dd
     */
    private String tradeDate;
    /**
     * 门店ID
     */
    private long merchantId;
    /**
     * 今日锁定会员数
     */
    private long lockNum;
    /**
     * 今日扫码订单总数
     */
    private long orderNum;
    /**
     * 今日扫码总流水
     */
    private long orderAmount;
    /**
     * 今日普通订单数量
     */
    private long comOrderNum;
    /**
     * 今日普通订单流水
     */
    private long comOrderAmount;
    /**
     * 今日乐加订单数量
     */
    private long leOrderNum;
    /**
     * 今日乐加订单流水
     */
    private long leOrderAmount;
    /**
     * 今日乐加订单收取佣金
     */
    private long leOrderComm;
    /**
     * 今日收取鼓励金
     */
    private long useScore;

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

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(long merchantId) {
        this.merchantId = merchantId;
    }

    public long getLockNum() {
        return lockNum;
    }

    public void setLockNum(long lockNum) {
        this.lockNum = lockNum;
    }

    public long getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(long orderNum) {
        this.orderNum = orderNum;
    }

    public long getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(long orderAmount) {
        this.orderAmount = orderAmount;
    }

    public long getComOrderNum() {
        return comOrderNum;
    }

    public void setComOrderNum(long comOrderNum) {
        this.comOrderNum = comOrderNum;
    }

    public long getComOrderAmount() {
        return comOrderAmount;
    }

    public void setComOrderAmount(long comOrderAmount) {
        this.comOrderAmount = comOrderAmount;
    }

    public long getLeOrderNum() {
        return leOrderNum;
    }

    public void setLeOrderNum(long leOrderNum) {
        this.leOrderNum = leOrderNum;
    }

    public long getLeOrderAmount() {
        return leOrderAmount;
    }

    public void setLeOrderAmount(long leOrderAmount) {
        this.leOrderAmount = leOrderAmount;
    }

    public long getLeOrderComm() {
        return leOrderComm;
    }

    public void setLeOrderComm(long leOrderComm) {
        this.leOrderComm = leOrderComm;
    }

    public long getUseScore() {
        return useScore;
    }

    public void setUseScore(long useScore) {
        this.useScore = useScore;
    }

    @Override
    public String toString() {
        return "StatsMerDailyData{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", tradeDate='" + tradeDate + '\'' +
                ", merchantId=" + merchantId +
                ", lockNum=" + lockNum +
                ", orderNum=" + orderNum +
                ", orderAmount=" + orderAmount +
                ", comOrderNum=" + comOrderNum +
                ", comOrderAmount=" + comOrderAmount +
                ", leOrderNum=" + leOrderNum +
                ", leOrderAmount=" + leOrderAmount +
                ", leOrderComm=" + leOrderComm +
                ", useScore=" + useScore +
                '}';
    }
}
