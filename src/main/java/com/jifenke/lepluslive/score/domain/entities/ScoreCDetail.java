package com.jifenke.lepluslive.score.domain.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * 金币记录 Created by wcg on 17/02/17.
 */
@Entity
@Table(name = "SCOREC_DETAIL")
public class ScoreCDetail {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Long number = 0L;
  private String operate;
  private Date dateCreated = new Date();

  @ManyToOne
  private ScoreC scoreC;

  private Integer origin;  //1=线上返还  2=线上消费  3=线下消费  4=线下返还 15002=通道订单退款 15003=充话费消耗 0注册有礼 8分享得金币

  private String orderSid;  //对应的订单号

  public Integer getOrigin() {
    return origin;
  }

  public void setOrigin(Integer origin) {
    this.origin = origin;
  }

  public String getOrderSid() {
    return orderSid;
  }

  public void setOrderSid(String orderSid) {
    this.orderSid = orderSid;
  }

  public ScoreC getScoreC() {
    return scoreC;
  }

  public void setScoreC(ScoreC scoreC) {
    this.scoreC = scoreC;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getNumber() {
    return number;
  }

  public void setNumber(Long number) {
    this.number = number;
  }

  public String getOperate() {
    return operate;
  }

  public void setOperate(String operate) {
    this.operate = operate;
  }

  public Date getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(Date dateCreated) {
    this.dateCreated = dateCreated;
  }
}
