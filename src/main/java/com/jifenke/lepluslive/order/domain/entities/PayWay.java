package com.jifenke.lepluslive.order.domain.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by wcg on 16/5/5.
 */
@Entity
@Table(name = "PAY_WAY")
public class PayWay {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;


  private String payWay;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPayWay() {
    return payWay;
  }

  public void setPayWay(String payWay) {
    this.payWay = payWay;
  }

  public PayWay(Long id) {
    this.id = id;
  }
  public PayWay() {
  }
}
