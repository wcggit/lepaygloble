package com.jifenke.lepluslive.groupon.domain.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wcg on 2017/6/14. 团购退款申请
 */
@Entity
@Table(name = "GROUPON_REFUND")
public class GrouponRefund {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @OneToOne
  private GrouponOrder grouponOrder;

  private Integer state = 0; //0 退款中 1 退款完成 2 退款驳回

  private Date createDate = new Date();

  private Date completeDate;

  private String description;

  private Integer refundNum = 1; //退款数量

  private String orderId; //通道退款单号

  private Long returnTruePay = 0L;  //实际退款金额

  private Long returnScorea = 0L;  //实际退还鼓励金

  private Long recycleScorea = 0L;  //追回鼓励金

  private Long recycleScorec = 0L;  //追回金币

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getCompleteDate() {
    return completeDate;
  }

  public void setCompleteDate(Date completeDate) {
    this.completeDate = completeDate;
  }

  public GrouponOrder getGrouponOrder() {
    return grouponOrder;
  }

  public void setGrouponOrder(GrouponOrder grouponOrder) {
    this.grouponOrder = grouponOrder;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getRefundNum() {
    return refundNum;
  }

  public void setRefundNum(Integer refundNum) {
    this.refundNum = refundNum;
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public Long getReturnTruePay() {
    return returnTruePay;
  }

  public void setReturnTruePay(Long returnTruePay) {
    this.returnTruePay = returnTruePay;
  }

  public Long getReturnScorea() {
    return returnScorea;
  }

  public void setReturnScorea(Long returnScorea) {
    this.returnScorea = returnScorea;
  }

  public Long getRecycleScorea() {
    return recycleScorea;
  }

  public void setRecycleScorea(Long recycleScorea) {
    this.recycleScorea = recycleScorea;
  }

  public Long getRecycleScorec() {
    return recycleScorec;
  }

  public void setRecycleScorec(Long recycleScorec) {
    this.recycleScorec = recycleScorec;
  }
}
