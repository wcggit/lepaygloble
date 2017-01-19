package com.jifenke.lepluslive.withdraw.domain.entities;

import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerManager;

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
@Table(name = "WITHDRAW_BILL")
public class WithdrawBill {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String withdrawBillSid;

    private String payee;

    @ManyToOne
    private Merchant merchant;

    @ManyToOne
    private Partner partner;

    @ManyToOne
    private PartnerManager partnerManager;

    private Integer billType;//0是合伙人管理员  1是合伙人 2是商户

    private Date createdDate;

    private Date completeDate;

    private String bankNumber;

    private String bankName;

    private Integer state;//0是申请中 1是提现完成   2已驳回

    private Long totalPrice;

    private String note;//备注

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

    public PartnerManager getPartnerManager() {
        return partnerManager;
    }

    public void setPartnerManager(PartnerManager partnerManager) {
        this.partnerManager = partnerManager;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
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

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
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

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    // 2.0 版本
    private Long merchantUserId;

    public Long getMerchantUserId() {
        return merchantUserId;
    }

    public void setMerchantUserId(Long merchantUserId) {
        this.merchantUserId = merchantUserId;
    }
}
