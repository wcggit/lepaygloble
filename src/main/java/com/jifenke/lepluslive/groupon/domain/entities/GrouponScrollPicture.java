package com.jifenke.lepluslive.groupon.domain.entities;

import javax.persistence.*;

/**
 * Created by wcg on 16/3/11. 轮播图
 */
@Entity
@Table(name = "GROUPON_SCROLL_PICTURE")
public class GrouponScrollPicture {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  private Integer sid;

  private String picture;

  private String description;

  @ManyToOne
  private GrouponProduct grouponProduct;

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

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

  public GrouponProduct getGrouponProduct() {
    return grouponProduct;
  }

  public void setGrouponProduct(GrouponProduct grouponProduct) {
    this.grouponProduct = grouponProduct;
  }
}
