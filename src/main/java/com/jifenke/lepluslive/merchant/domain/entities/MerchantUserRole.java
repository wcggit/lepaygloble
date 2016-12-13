package com.jifenke.lepluslive.merchant.domain.entities;

import javax.persistence.*;

/**
 * Created by xf on 16-10-26.
 * 商户资源角色表
 */
@Entity
@Table(name = "MERCHANT_USER_ROLE")
public class MerchantUserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private MerchantUser merchantUser;
    @ManyToOne
    private MerchantRole merchantRole;

    private Integer resourceType;                     //  资源类型  0-门店资源  1-页面资源 2-操作

    @ManyToOne
    private Merchant merchant;

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

    public MerchantUser getMerchantUser() {
        return merchantUser;
    }

    public void setMerchantUser(MerchantUser merchantUser) {
        this.merchantUser = merchantUser;
    }

    public MerchantRole getMerchantRole() {
        return merchantRole;
    }

    public void setMerchantRole(MerchantRole merchantRole) {
        this.merchantRole = merchantRole;
    }

    public Integer getResourceType() {
        return resourceType;
    }

    public void setResourceType(Integer resourceType) {
        this.resourceType = resourceType;
    }

}
