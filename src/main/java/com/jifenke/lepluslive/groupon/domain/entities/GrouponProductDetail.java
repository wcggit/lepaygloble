package com.jifenke.lepluslive.groupon.domain.entities;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * Created by wcg on 16/3/17. 图文详情
 */
@Entity
@Table(name = "GROUPON_PRODUCT_DETAIL")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GrouponProductDetail {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  private String picture;

  private String description;

  private Integer sid;

  @ManyToOne
  private GrouponProduct grouponProduct;

  public Integer getSid() {
    return sid;
  }

  public void setSid(Integer sid) {
    this.sid = sid;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getPicture() {
    return picture;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public GrouponProduct getGrouponProduct() {
    return grouponProduct;
  }

  public void setGrouponProduct(GrouponProduct grouponProduct) {
    this.grouponProduct = grouponProduct;
  }
}
