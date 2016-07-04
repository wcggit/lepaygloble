package com.jifenke.lepluslive.merchant.domain.entities;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 商家推荐 Created by zhangwen on 16/6/6.
 */
@Entity
@Table(name = "MERCHANT_RECOMMEND")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MerchantRecommend {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @ManyToOne
  private Merchant merchant;

  private Integer sid;   //序号

  private Integer state = 1;  //0=删除 1=有效

  private Date createDate = new Date();  //创建时间

  private Date lastUpDate;  //最近更新时间

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Merchant getMerchant() {
    return merchant;
  }

  public void setMerchant(Merchant merchant) {
    this.merchant = merchant;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Date getLastUpDate() {
    return lastUpDate;
  }

  public void setLastUpDate(Date lastUpDate) {
    this.lastUpDate = lastUpDate;
  }

  public Integer getSid() {
    return sid;
  }

  public void setSid(Integer sid) {
    this.sid = sid;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }
}
