package com.jifenke.lepluslive.merchant.domain.criteria;

/**
 * 查询条件
 *
 * @author zhangwen at 2017-11-07 15:44
 **/
public class StatsMerDailyDataCriteria {

    private String startDate;

    private String endDate;

    private Integer offset;

    private Integer limit = 10;

    private Long city;
    /**
     * 销售
     */
    private Long sale;
    /**
     * 商户ID
     */
    private Long merchantUser;
    /**
     * 合伙人ID
     */
    private Long partner;
    /**
     * 门店序号
     */
    private String merchant;

    /**
     *  商户名称
     */
    private String merchantUserName;

    /**
     *  协议类型
     */
    private Integer partnership;

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

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Long getCity() {
        return city;
    }

    public void setCity(Long city) {
        this.city = city;
    }

    public Long getSale() {
        return sale;
    }

    public void setSale(Long sale) {
        this.sale = sale;
    }

    public Long getMerchantUser() {
        return merchantUser;
    }

    public void setMerchantUser(Long merchantUser) {
        this.merchantUser = merchantUser;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public Long getPartner() {
        return partner;
    }

    public void setPartner(Long partner) {
        this.partner = partner;
    }

    public String getMerchantUserName() {
        return merchantUserName;
    }

    public void setMerchantUserName(String merchantUserName) {
        this.merchantUserName = merchantUserName;
    }

    public Integer getPartnership() {
        return partnership;
    }

    public void setPartnership(Integer partnership) {
        this.partnership = partnership;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
