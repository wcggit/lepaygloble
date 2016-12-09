package com.jifenke.lepluslive.merchant.domain.entities;

import javax.persistence.*;

/**
 * Created by xf on 16-10-26.
 * 商户角色权限表
 */
@Entity
@Table(name="MERCHANT_ROLE_AUTHORITY")
public class MerchantRoleAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private MerchantRole merchantRole;
    @ManyToOne
    private MerchantAuthority merchantAuthority;

    public MerchantRole getMerchantRole() {
        return merchantRole;
    }

    public void setMerchantRole(MerchantRole merchantRole) {
        this.merchantRole = merchantRole;
    }

    public MerchantAuthority getMerchantAuthority() {
        return merchantAuthority;
    }

    public void setMerchantAuthority(MerchantAuthority merchantAuthority) {
        this.merchantAuthority = merchantAuthority;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
