package com.jifenke.lepluslive.merchant.domain.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 门店扫码支付方式 Created by zhangwen on 16/11/25.
 */
@Entity
@Table(name = "MERCHANT_SCAN_PAY_WAY")
public class MerchantScanPayWay {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Integer type = 1;  //扫码支付方式  0=富友结算|1=乐加结算|2=暂不开通|3=易宝结算

    private Date createDate;

    private Date lastUpdate;  //最后修改时间

    private BigDecimal commission;   //佣金协议，仅仅做展示用，理论和实际一致

    @Column(nullable = false, unique = true)
    private Long merchantId;  //门店ID=Merchant.id

    private Integer openOnLineShare = 0; //0关闭 1开启门店线上分润

    private Integer openOffLineShare = 0; //0关闭 1开启门店线下分润

    public Integer getOpenOnLineShare() {
        return openOnLineShare;
    }

    public void setOpenOnLineShare(Integer openOnLineShare) {
        this.openOnLineShare = openOnLineShare;
    }

    public Integer getOpenOffLineShare() {
        return openOffLineShare;
    }

    public void setOpenOffLineShare(Integer openOffLineShare) {
        this.openOffLineShare = openOffLineShare;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
