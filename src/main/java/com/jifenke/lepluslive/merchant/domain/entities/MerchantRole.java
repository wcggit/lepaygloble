package com.jifenke.lepluslive.merchant.domain.entities;

import javax.persistence.*;

/**
 * Created by xf on 16-10-26.
 */
@Entity
@Table(name = "MERCHANT_ROLE")
public class MerchantRole {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String roleName;                        // 角色名称
    private Integer roleType;                       // 角色类型     0-系统角色  1-自定义角色
    @ManyToOne
    private MerchantUser merchantUser;              // 创建人

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }

    public MerchantUser getMerchantUser() {
        return merchantUser;
    }

    public void setMerchantUser(MerchantUser merchantUser) {
        this.merchantUser = merchantUser;
    }

}
