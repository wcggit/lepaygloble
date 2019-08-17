package com.jifenke.lepluslive.cmbc.domain.entities;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author ZM.Wang
 * 民生子商户和门店关系
 */
@Entity
public class CmbcSubMerNexus {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date dateCreated;

    @NotNull
    @OneToOne
    private Merchant merchant;

    @ManyToOne
    @NotNull
    private CmbcSubMerchant cmbcSubMerchant;

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

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public CmbcSubMerchant getCmbcSubMerchant() {
        return cmbcSubMerchant;
    }

    public void setCmbcSubMerchant(CmbcSubMerchant cmbcSubMerchant) {
        this.cmbcSubMerchant = cmbcSubMerchant;
    }
}
