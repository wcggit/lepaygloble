package com.jifenke.lepluslive.merchant.domain.criteria;

import com.jifenke.lepluslive.order.domain.entities.MerchantPos;
import com.jifenke.lepluslive.order.domain.entities.PosOrder;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by wanjun on 2016/12/7.
 */
public class PosOrderCriteria {

    /**
     * 订单数据
     */
    private Page<PosOrder> page;

    /**
     * pos机信息数据分页
     */
    private Page<MerchantPos> posPage;

    /**
     * 门店ID
     */
    private Object [] storeIds;

    /**
     * 订单类型
     */
    private Integer rebateWay; //1 代表非会员消费 2 代表会员订单 3 导流订单 4 会员普通订单

    /**
     * 支付方式
     */
    private Integer tradeFlag; //0支付宝、3POS刷卡、4微信、5纯积分（会员登录后不能用现金交易） 6现金支付

    /**
     * 商户pos机ID
     */
    private Long merchantPosId;

    /**
     * pos订单编号
     */
    private String orderSid;

    /**
     * 开始时间
     */
    private String startDate;

    /**
     * 结束时间
     */
    private String endDate;

    private Double totalPrice;
    private Double trueScore;
    private Double transferByBank;
    private Double transferByHB;
    private Double zhifubao;
    private Double shuaka;
    private Double weixin;
    private Integer state = 0; //支付状态
    /**
     * 当前第几页
     */
    private Integer currentPage;

    public Page<PosOrder> getPage() {
        return page;
    }

    public void setPage(Page<PosOrder> page) {
        this.page = page;
    }

    public Object[] getStoreIds() {
        return storeIds;
    }

    public void setStoreIds(Object[] storeIds) {
        this.storeIds = storeIds;
    }

    public Integer getRebateWay() {
        return rebateWay;
    }

    public void setRebateWay(Integer rebateWay) {
        this.rebateWay = rebateWay;
    }

    public Integer getTradeFlag() {
        return tradeFlag;
    }

    public void setTradeFlag(Integer tradeFlag) {
        this.tradeFlag = tradeFlag;
    }

    public Long getMerchantPosId() {
        return merchantPosId;
    }

    public void setMerchantPosId(Long merchantPosId) {
        this.merchantPosId = merchantPosId;
    }

    public String getOrderSid() {
        return orderSid;
    }

    public Page<MerchantPos> getPosPage() {
        return posPage;
    }

    public void setPosPage(Page<MerchantPos> posPage) {
        this.posPage = posPage;
    }

    public void setOrderSid(String orderSid) {
        this.orderSid = orderSid;
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

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getTrueScore() {
        return trueScore;
    }

    public void setTrueScore(Double trueScore) {
        this.trueScore = trueScore;
    }

    public Double getTransferByBank() {
        return transferByBank;
    }

    public void setTransferByBank(Double transferByBank) {
        this.transferByBank = transferByBank;
    }

    public Double getTransferByHB() {
        return transferByHB;
    }

    public void setTransferByHB(Double transferByHB) {
        this.transferByHB = transferByHB;
    }

    public Double getZhifubao() {
        return zhifubao;
    }

    public void setZhifubao(Double zhifubao) {
        this.zhifubao = zhifubao;
    }

    public Double getShuaka() {
        return shuaka;
    }

    public void setShuaka(Double shuaka) {
        this.shuaka = shuaka;
    }

    public Double getWeixin() {
        return weixin;
    }

    public void setWeixin(Double weixin) {
        this.weixin = weixin;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
