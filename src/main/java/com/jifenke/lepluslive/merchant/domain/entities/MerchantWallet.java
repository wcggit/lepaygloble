package com.jifenke.lepluslive.merchant.domain.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.Date;

/**
 * Created by wcg on 16/5/13.
 */
@Entity
@Table(name = "MERCHANT_WALLET")
public class MerchantWallet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long totalTransferMoney = 0L; //总转账金额

    private Long availableBalance = 0L; //可以佣金余额

    private Long totalMoney = 0L; //获取佣金总额

    private Long totalWithdrawals = 0L;//已经提现总额

    @OneToOne
    private Merchant merchant;

    @Version
    private Long version = 0L;

    private Date lastUpdate;  //最后更新时间

    public Long getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(Long availableBalance) {
        this.availableBalance = availableBalance;
    }

    public Long getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Long totalMoney) {
        this.totalMoney = totalMoney;
    }

    public Long getTotalWithdrawals() {
        return totalWithdrawals;
    }

    public void setTotalWithdrawals(Long totalWithdrawals) {
        this.totalWithdrawals = totalWithdrawals;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getTotalTransferMoney() {
        return totalTransferMoney;
    }

    public void setTotalTransferMoney(Long totalTransferMoney) {
        this.totalTransferMoney = totalTransferMoney;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    // 2.0 版本,扩展,商户 id
    private Long merchantUserId;

    public Long getMerchantUserId() {
        return merchantUserId;
    }

    public void setMerchantUserId(Long merchantUserId) {
        this.merchantUserId = merchantUserId;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
