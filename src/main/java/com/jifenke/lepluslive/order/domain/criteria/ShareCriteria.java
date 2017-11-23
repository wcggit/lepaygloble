package com.jifenke.lepluslive.order.domain.criteria;

/**
 * 线下先下分润查询条件
 * Created by zhangwen on 17/9/16.
 */
public class ShareCriteria {

    private int offset;//页码

    private int size = 10;  //每页显示条数

    private String startDate;

    private String endDate;

    private String orderSid;//关联订单号

    private int channel;//业务渠道 参照category.type(category.category=11) 0=不分渠道

    private String userSid;//消费者sid

    private String userPhone;//消费者手机号

    private String tradeMerchant;//交易门店名称

    private String tradePartner;//交易天使名称

    private String tradeManager;//交易城市名称

    private String lockMerchant;//锁定门店名称

    /**
     * 锁定门店ID
     */
    private Long lockMerchantId;

    private String lockPartner;//锁定天使名称

    private String lockManager;//锁定城市名称

    private Long salesStaff;

    public Long getLockMerchantId() {
        return lockMerchantId;
    }

    public void setLockMerchantId(Long lockMerchantId) {
        this.lockMerchantId = lockMerchantId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getOrderSid() {
        return orderSid;
    }

    public void setOrderSid(String orderSid) {
        this.orderSid = orderSid;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String getUserSid() {
        return userSid;
    }

    public Long getSalesStaff() {
        return salesStaff;
    }

    public void setSalesStaff(Long salesStaff) {
        this.salesStaff = salesStaff;
    }

    public void setUserSid(String userSid) {
        this.userSid = userSid;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getTradeMerchant() {
        return tradeMerchant;
    }

    public void setTradeMerchant(String tradeMerchant) {
        this.tradeMerchant = tradeMerchant;
    }

    public String getTradePartner() {
        return tradePartner;
    }

    public void setTradePartner(String tradePartner) {
        this.tradePartner = tradePartner;
    }

    public String getTradeManager() {
        return tradeManager;
    }

    public void setTradeManager(String tradeManager) {
        this.tradeManager = tradeManager;
    }

    public String getLockMerchant() {
        return lockMerchant;
    }

    public void setLockMerchant(String lockMerchant) {
        this.lockMerchant = lockMerchant;
    }

    public String getLockPartner() {
        return lockPartner;
    }

    public void setLockPartner(String lockPartner) {
        this.lockPartner = lockPartner;
    }

    public String getLockManager() {
        return lockManager;
    }

    public void setLockManager(String lockManager) {
        this.lockManager = lockManager;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
