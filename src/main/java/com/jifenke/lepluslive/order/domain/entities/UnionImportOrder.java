package com.jifenke.lepluslive.order.domain.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * 银联Excel导入分类别订单数据
 * Created by zhangwen on 2017/9/5.
 */
@Entity
public class UnionImportOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date createdAt = new Date();

    private int type;//订单类别 1=银联卡，2=公共支付，3=借记卡，4=公共支付-手机，5=营销联盟

    private String settleDate;//清算日期

    private Long batchNo;//文件批次号=UnionUploadLog.id

    private String merNum;//商户号

    private int payWay;//支付方式 0=银行卡|1=微信或支付宝

    private int orderType;//订单类型 0=普通订单|1=乐加订单

    private String data; //json格式订单数据

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getPayWay() {
        return payWay;
    }

    public void setPayWay(int payWay) {
        this.payWay = payWay;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(String settleDate) {
        this.settleDate = settleDate;
    }

    public Long getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(Long batchNo) {
        this.batchNo = batchNo;
    }

    public String getMerNum() {
        return merNum;
    }

    public void setMerNum(String merNum) {
        this.merNum = merNum;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
