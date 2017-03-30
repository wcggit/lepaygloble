package com.jifenke.lepluslive.merchant.domain.criteria;


import java.util.List;

/**
 * Created by sunxingfei on 2016/12/29.
 */
public class MerchantCriteria{

    private List<Object[]> mList;

    /**
     * 当前第几页
     */
    private Integer currentPage;
    /**
     * 数据总的记录数
     */
    private Integer totalPages;

    public List<Object[]> getmList() {
        return mList;
    }

    public void setmList(List<Object[]> mList) {
        this.mList = mList;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }
}
