package com.jifenke.lepluslive.merchant.domain.criteria;

import com.jifenke.lepluslive.order.domain.entities.OffLineOrder;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 绑定会员
 * Created by tqy on 16/12/15.
 */
public class LockMemberCriteria {

    /**
     * 会员信息
     */
    private List<Object[]> lockMembers;

    /**
     * 会员数量
     */
    private Integer lockMemberCount;

    /**
     * 会员佣金收入
     */
    private Double commissionIncome;


    private Object [] storeIds;//门店ID
    private String startDate;//开始时间
    private String endDate;//结束时间

    private Integer currentPage;//当前第几页
    private Integer pageSize = 10;//每页size
    private Integer totalPages;//总页数



    public Double getCommissionIncome() {
        return commissionIncome;
    }

    public void setCommissionIncome(Double commissionIncome) {
        this.commissionIncome = commissionIncome;
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

    public Integer getLockMemberCount() {
        return lockMemberCount;
    }

    public void setLockMemberCount(Integer lockMemberCount) {
        this.lockMemberCount = lockMemberCount;
    }

    public List<Object[]> getLockMembers() {
        return lockMembers;
    }

    public void setLockMembers(List<Object[]> lockMembers) {
        this.lockMembers = lockMembers;
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
}
