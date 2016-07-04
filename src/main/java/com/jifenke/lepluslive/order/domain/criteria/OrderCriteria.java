package com.jifenke.lepluslive.order.domain.criteria;

/**
 * Created by wcg on 16/4/28.
 */
public class OrderCriteria {

  private Integer state;

  private String orderSid;

  public String getOrderSid() {
    return orderSid;
  }

  public void setOrderSid(String orderSid) {
    this.orderSid = orderSid;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }
}
