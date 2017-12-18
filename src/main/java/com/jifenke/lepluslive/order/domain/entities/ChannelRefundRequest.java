package com.jifenke.lepluslive.order.domain.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * ChannelRefundRequest
 * 商户退款申请
 *
 * @author XiFeng
 * @date 2017/12/13
 */
@Entity
public class ChannelRefundRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * 退款申请时间
     */
    private Date dateCreated;
    /**
     * 退款审核时间
     */
    private Date dateCompleted;
    /**
     * 申请状态:  0=申请中  1=已审核  2=已驳回
     */
    private Integer state;
    /**
     * 对应要退款订单的订单号
     */
    @Column(nullable = false, length = 30)
    private String orderSid;
    /**
     * 0=普通订单,1=乐加订单
     */
    private Integer orderType;
    /**
     *  订单确认码
     */
    private String lepayCode;
    /**
     *   订单通道  0=乐加订单 1=订单通道
     */
    private Integer orderFrom;

    /**
     * 对应要退款订单号
     */
    private String refundOrderSid;
    /**
     * 交易门店SID
     */
    private Long merchantId;
    /**
     * 门店名称
     */
    private String merhcantName;
    /**
     *  审核备注
     */
    private String note;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(Date dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getOrderSid() {
        return orderSid;
    }

    public void setOrderSid(String orderSid) {
        this.orderSid = orderSid;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getLepayCode() {
        return lepayCode;
    }

    public void setLepayCode(String lepayCode) {
        this.lepayCode = lepayCode;
    }

    public Integer getOrderFrom() {
        return orderFrom;
    }

    public void setOrderFrom(Integer orderFrom) {
        this.orderFrom = orderFrom;
    }

    public String getRefundOrderSid() {
        return refundOrderSid;
    }

    public void setRefundOrderSid(String refundOrderSid) {
        this.refundOrderSid = refundOrderSid;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerhcantName() {
        return merhcantName;
    }

    public void setMerhcantName(String merhcantName) {
        this.merhcantName = merhcantName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
