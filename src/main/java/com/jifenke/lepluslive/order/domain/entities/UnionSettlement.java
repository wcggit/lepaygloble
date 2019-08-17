package com.jifenke.lepluslive.order.domain.entities;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;

import javax.persistence.*;
import java.util.Date;

/**
 * 银商鼓励金结算单
 * Created by zhangwen on 2017/9/12.
 */
@Entity
public class UnionSettlement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date createdAt = new Date();

    private Date completedAt;//转账完成时间

    private int state;   //结算状态 0=待转账|1=已转账|2=已挂账

    @ManyToOne
    private Merchant merchant;//所属门店

    private long merchantUserId;            //商户id
    private String merchantName;            //商户名称

    @Column(nullable = false, length = 10)
    private String tradeDate; //交易日期（对应的订单支付日期）（yyyy-MM-dd）

    private long transferMoney;//应结算金额

    private String bankNumber; //结算卡号或账户

    private String bankName; //开户支行

    @Column(length = 100)
    private String payee;    //收款人或账户主体

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

    public Date getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public long getMerchantUserId() {
        return merchantUserId;
    }

    public void setMerchantUserId(long merchantUserId) {
        this.merchantUserId = merchantUserId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public long getTransferMoney() {
        return transferMoney;
    }

    public void setTransferMoney(long transferMoney) {
        this.transferMoney = transferMoney;
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }
    
}
