package com.jifenke.lepluslive.merchant.domain.entities;



import javax.persistence.*;

/**
 * Created by xf on 16-11-18.
 */
@Entity
@Table(name="MERCHANT_USER_RESOURCE")
public class MerchantUserResource {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private MerchantUser merchantUser;

    @ManyToOne
    private LejiaResource leJiaResource;

    private Integer resourceType;

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

    public LejiaResource getLeJiaResource() {
        return leJiaResource;
    }

    public void setLeJiaResource(LejiaResource leJiaResource) {
        this.leJiaResource = leJiaResource;
    }

    public Integer getResourceType() {
        return resourceType;
    }

    public void setResourceType(Integer resourceType) {
        this.resourceType = resourceType;
    }
}
