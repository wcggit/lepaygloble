package com.jifenke.lepluslive.groupon.domain.entities;


import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by wcg on 2017/6/14.
 */
@Entity
@Table(name = "GROUPON_ORDER")
public class GrouponOrder {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String orderSid;

  private String orderId; //3方订单号

  private Date createDate = new Date();

  private Date completeDate;

  @ManyToOne
  private LeJiaUser leJiaUser;

  private Long totalPrice = 0L;

  private Long truePay = 0L;

  private Long scorea = 0L; //使用鼓励金

  private Long state = 0L;  //0 未付款 1 已完成

  private Integer orderState = 0;  // 0=待使用  1=已使用  2=退款

  private Long rebateScorea = 0L;

  private Long rebateScorec = 0L;

  private Long orderType = 0L; // 订单类型 0 普通订单 1 乐加订单

  @ManyToOne
  private GrouponProduct grouponProduct;

  private Integer payOrigin = 0; //0 公众号 1 app

  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "grouponOrder")
  private List<GrouponCode> grouponCodes; //一个订单可能对应多个团购码

  //以下为冗余字段
  private Date expiredDate; //过期时间

  private Integer buyNum = 1;  //购买数量

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getOrderSid() {
    return orderSid;
  }

  public void setOrderSid(String orderSid) {
    this.orderSid = orderSid;
  }

  public LeJiaUser getLeJiaUser() {
    return leJiaUser;
  }

  public void setLeJiaUser(LeJiaUser leJiaUser) {
    this.leJiaUser = leJiaUser;
  }

  public Long getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(Long totalPrice) {
    this.totalPrice = totalPrice;
  }

  public Long getTruePay() {
    return truePay;
  }

  public void setTruePay(Long truePay) {
    this.truePay = truePay;
  }

  public Long getScorea() {
    return scorea;
  }

  public void setScorea(Long scorea) {
    this.scorea = scorea;
  }

  public Long getRebateScorea() {
    return rebateScorea;
  }

  public void setRebateScorea(Long rebateScorea) {
    this.rebateScorea = rebateScorea;
  }

  public Long getRebateScorec() {
    return rebateScorec;
  }

  public void setRebateScorec(Long rebateScorec) {
    this.rebateScorec = rebateScorec;
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Date getCompleteDate() {
    return completeDate;
  }

  public void setCompleteDate(Date completeDate) {
    this.completeDate = completeDate;
  }

  public Long getState() {
    return state;
  }

  public void setState(Long state) {
    this.state = state;
  }

  public Long getOrderType() {
    return orderType;
  }

  public void setOrderType(Long orderType) {
    this.orderType = orderType;
  }

  public GrouponProduct getGrouponProduct() {
    return grouponProduct;
  }

  public void setGrouponProduct(GrouponProduct grouponProduct) {
    this.grouponProduct = grouponProduct;
  }

  public Integer getPayOrigin() {
    return payOrigin;
  }

  public void setPayOrigin(Integer payOrigin) {
    this.payOrigin = payOrigin;
  }

  public List<GrouponCode> getGrouponCodes() {
    return grouponCodes;
  }

  public void setGrouponCodes(List<GrouponCode> grouponCodes) {
    this.grouponCodes = grouponCodes;
  }

  public Integer getOrderState() {
    return orderState;
  }

  public void setOrderState(Integer orderState) {
    this.orderState = orderState;
  }

  public Date getExpiredDate() {
    return expiredDate;
  }

  public void setExpiredDate(Date expiredDate) {
    this.expiredDate = expiredDate;
  }

  public Integer getBuyNum() {
    return buyNum;
  }

  public void setBuyNum(Integer buyNum) {
    this.buyNum = buyNum;
  }
}
