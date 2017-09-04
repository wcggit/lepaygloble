package com.jifenke.lepluslive.merchant.domain.criteria;

import java.util.List;

/**
 * 商户下所有门店 佣金明细
 * Created by tqy on 16/12/19.
 */
public class CommissionDetailsCriteria {

    /**
     * 订单 佣金明细
     */
    private List<Object[]> commissionDetails;

    private Long merchantId;

    /**
     * 佣金明细 数量
     */
    private Integer commissionDetailsCount;

    /**
     * 佣金收入
     */
    private Double commissionIncome;

    private Integer  consumeType;//消费方式,1线下消费 2线上消费

    private Object [] storeIds;//门店ID
    private String startDate;//开始时间
    private String endDate;//结束时间

    private Integer currentPage;//当前第几页
    private Integer pageSize = 10;//每页size
    private Integer totalPages;//总页数



    public List<Object[]> getCommissionDetails() {
        return commissionDetails;
    }

    public void setCommissionDetails(List<Object[]> commissionDetails) {
        this.commissionDetails = commissionDetails;
    }

    public Double getCommissionIncome() {
        return commissionIncome;
    }

    public void setCommissionIncome(Double commissionIncome) {
        this.commissionIncome = commissionIncome;
    }

    public Integer getConsumeType() {
        return consumeType;
    }

    public void setConsumeType(Integer consumeType) {
        this.consumeType = consumeType;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getCommissionDetailsCount() {
        return commissionDetailsCount;
    }

    public void setCommissionDetailsCount(Integer commissionDetailsCount) {
        this.commissionDetailsCount = commissionDetailsCount;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Object[] getStoreIds() {
        return storeIds;
    }

    public void setStoreIds(Object[] storeIds) {
        this.storeIds = storeIds;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }
}
