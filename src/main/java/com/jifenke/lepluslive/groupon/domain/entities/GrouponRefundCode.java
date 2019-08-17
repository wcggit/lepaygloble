package com.jifenke.lepluslive.groupon.domain.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wcg on 2017/6/20. 退款单对应的团购码
 */
@Entity
@Table(name = "GROUPON_REFUND_CODE")
public class GrouponRefundCode {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Date createDate = new Date();

  @ManyToOne
  private GrouponRefund grouponRefund;

  @OneToOne
  private GrouponCode grouponCode;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public GrouponRefund getGrouponRefund() {
    return grouponRefund;
  }

  public void setGrouponRefund(GrouponRefund grouponRefund) {
    this.grouponRefund = grouponRefund;
  }

  public GrouponCode getGrouponCode() {
    return grouponCode;
  }

  public void setGrouponCode(GrouponCode grouponCode) {
    this.grouponCode = grouponCode;
  }
}
