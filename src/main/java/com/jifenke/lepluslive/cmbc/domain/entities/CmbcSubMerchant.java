package com.jifenke.lepluslive.cmbc.domain.entities;

import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author ZM.Wang
 * 民生子商户
 */

@Entity
public class CmbcSubMerchant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date dateCreated;

    private Date dateUpdate;

    @OneToOne(fetch = FetchType.LAZY)
    @NotNull
    private MerchantUser merchantUser;

    private String subMerchantNo;

    private Integer cmbcSignupType; //1：个人    2：企业

    private Long wxRate;

    private Long aliRate;

    private String accNo;

    private String accName;

    private Integer accType;   //1：对私账户，2：对公账户

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public MerchantUser getMerchantUser() {
        return merchantUser;
    }

    public void setMerchantUser(MerchantUser merchantUser) {
        this.merchantUser = merchantUser;
    }

    public String getSubMerchantNo() {
        return subMerchantNo;
    }

    public void setSubMerchantNo(String subMerchantNo) {
        this.subMerchantNo = subMerchantNo;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public Integer getCmbcSignupType() {
        return cmbcSignupType;
    }

    public void setCmbcSignupType(Integer cmbcSignupType) {
        this.cmbcSignupType = cmbcSignupType;
    }

    public Long getWxRate() {
        return wxRate;
    }

    public void setWxRate(Long wxRate) {
        this.wxRate = wxRate;
    }

    public Long getAliRate() {
        return aliRate;
    }

    public void setAliRate(Long aliRate) {
        this.aliRate = aliRate;
    }

    public Integer getAccType() {
        return accType;
    }

    public void setAccType(Integer accType) {
        this.accType = accType;
    }
}
