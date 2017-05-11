package com.jifenke.lepluslive.withdraw.domain.entities;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerManager;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by xf on 2016/9/18.
 */
@Entity
@Table(name = "WeiXin_WITHDRAW_BILL")
public class WeiXinWithdrawBill {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String withdrawBillSid;

    @ManyToOne
    private Partner partner;

    @ManyToOne
    private WeiXinUser weiXinUser; //微信接口对应的用户

    private Date createdDate;

    private Date completeDate;

    private Integer state;//0是申请中 1是提现完成   2已驳回

    private Long totalPrice; //totalPrice = onlineWallet+offlineWallet

    private Long onlineWallet; //总提现金额中线上钱包提现金额

    private Long offlineWallet; //总提现金额中线下钱包提现金额

    private String note;//备注

    public WeiXinUser getWeiXinUser() {
        return weiXinUser;
    }

    public void setWeiXinUser(WeiXinUser weiXinUser) {
        this.weiXinUser = weiXinUser;
    }

    public Long getOnlineWallet() {
        return onlineWallet;
    }

    public void setOnlineWallet(Long onlineWallet) {
        this.onlineWallet = onlineWallet;
    }

    public Long getOfflineWallet() {
        return offlineWallet;
    }

    public void setOfflineWallet(Long offlineWallet) {
        this.offlineWallet = offlineWallet;
    }

    public Integer getState() {
        return state;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }


    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getWithdrawBillSid() {
        return withdrawBillSid;
    }

    public void setWithdrawBillSid(String withdrawBillSid) {
        this.withdrawBillSid = withdrawBillSid;
    }

}
