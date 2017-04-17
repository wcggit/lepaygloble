package com.jifenke.lepluslive.merchant.domain.entities;



import javax.persistence.*;

/**
 * Created by xf on 16-11-1.
 */
@Entity
@Table(name="LE_JIA_RESOURCE")
public class LejiaResource {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long resourceId;                        //  资源 id
    private Integer resourceType;                   //  资源类型： 0-merchant

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public Integer getResourceType() {
        return resourceType;
    }

    public void setResourceType(Integer resourceType) {
        this.resourceType = resourceType;
    }
}
