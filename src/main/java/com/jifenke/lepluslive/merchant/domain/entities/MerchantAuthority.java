package com.jifenke.lepluslive.merchant.domain.entities;


import javax.persistence.*;

/**
 * Created by xf on 16-10-26.
 */
@Entity
@Table(name="MERCHANT_AUTHORITY")
public class MerchantAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String autoName;            // 权限名称 (操作)
    private Integer autoType;           // 权限类型 -》 资源类型   0-MERCHANT

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAutoName() {
        return autoName;
    }

    public void setAutoName(String autoName) {
        this.autoName = autoName;
    }

    public Integer getAutoType() {
        return autoType;
    }

    public void setAutoType(Integer autoType) {
        this.autoType = autoType;
    }

    //  重写构造方法
    public MerchantAuthority() {

    }
    public MerchantAuthority(Long id) {
        this.id = id;
    }


    //  重写 equals,方便使用contains判断是否拥有权限

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MerchantAuthority authority = (MerchantAuthority) o;

        return id != null ? id.equals(authority.id) : authority.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
