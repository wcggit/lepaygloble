package com.jifenke.lepluslive.partner.domain.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by wcg on 16/9/28.
 */
@Entity
@Table(name = "PARTNER_SCORE_LOG")//代表合伙人积分红包变化的日志
public class PartnerScoreLog {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Long partnerId;

  private String sid; //代表订单号 会员号 等..

  private Integer type; // 0代表 积分变更 1 代表红包变更

  private Integer scoreAOrigin; //红包变更来源  0 注册送红包 1 送邀请会员

  private Integer scoreBOrigin;//积分变更来源 0 注册送积分 1 送邀请会员

  private String description;//描述

  private Long number; //变更值

  private Date createDate;

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPartnerId() {
    return partnerId;
  }

  public void setPartnerId(Long partnerId) {
    this.partnerId = partnerId;
  }

  public String getSid() {
    return sid;
  }

  public void setSid(String sid) {
    this.sid = sid;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public Integer getScoreAOrigin() {
    return scoreAOrigin;
  }

  public void setScoreAOrigin(Integer scoreAOrigin) {
    this.scoreAOrigin = scoreAOrigin;
  }

  public Integer getScoreBOrigin() {
    return scoreBOrigin;
  }

  public void setScoreBOrigin(Integer scoreBOrigin) {
    this.scoreBOrigin = scoreBOrigin;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Long getNumber() {
    return number;
  }

  public void setNumber(Long number) {
    this.number = number;
  }
}
