package com.jifenke.lepluslive.merchant.domain.criteria;

import java.util.List;

/**
 * 数据概览参数接收和数据返回
 * Created by wanjun on 2016/12/15.
 */
public class DataOverviewCriteria {

    private Integer offset;    //当前第几页
    private Integer merchantUserLockLimit;   //商户当前锁定会员数
    private Integer merchantUserTotalLockLimit;     //商户锁定会员上限数
    private Double offTodayTotalCommission;    //线下今日佣金收入
    private Double onTodayTotalCommission;     //线上今日佣金收入
    private Integer offTodayTotalNumber;       //线下今日佣金单数
    private Integer onTodayTotalNumber;        //线上今日佣金单数
    private Double offPerCapita;              //线下人均佣金收入
    private Double onPerCapita;               //线上人均佣金收入
    private Integer offTotalNumber;           //线下累计佣金单数
    private Integer onTotalNumber;            //线上累计佣金单数
    private List<Object[]> merchants;        //商户旗下门店会员锁定信息
    private List<Object> merchantIds;         //商户旗下门店ids

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getMerchantUserLockLimit() {
        return merchantUserLockLimit;
    }

    public void setMerchantUserLockLimit(Integer merchantUserLockLimit) {
        this.merchantUserLockLimit = merchantUserLockLimit;
    }

    public Integer getMerchantUserTotalLockLimit() {
        return merchantUserTotalLockLimit;
    }

    public void setMerchantUserTotalLockLimit(Integer merchantUserTotalLockLimit) {
        this.merchantUserTotalLockLimit = merchantUserTotalLockLimit;
    }

    public Double getOffTodayTotalCommission() {
        return offTodayTotalCommission;
    }

    public void setOffTodayTotalCommission(Double offTodayTotalCommission) {
        this.offTodayTotalCommission = offTodayTotalCommission;
    }

    public Double getOnTodayTotalCommission() {
        return onTodayTotalCommission;
    }

    public void setOnTodayTotalCommission(Double onTodayTotalCommission) {
        this.onTodayTotalCommission = onTodayTotalCommission;
    }

    public Integer getOffTodayTotalNumber() {
        return offTodayTotalNumber;
    }

    public void setOffTodayTotalNumber(Integer offTodayTotalNumber) {
        this.offTodayTotalNumber = offTodayTotalNumber;
    }

    public Integer getOnTodayTotalNumber() {
        return onTodayTotalNumber;
    }

    public void setOnTodayTotalNumber(Integer onTodayTotalNumber) {
        this.onTodayTotalNumber = onTodayTotalNumber;
    }

    public Double getOffPerCapita() {
        return offPerCapita;
    }

    public void setOffPerCapita(Double offPerCapita) {
        this.offPerCapita = offPerCapita;
    }

    public Double getOnPerCapita() {
        return onPerCapita;
    }

    public void setOnPerCapita(Double onPerCapita) {
        this.onPerCapita = onPerCapita;
    }

    public Integer getOffTotalNumber() {
        return offTotalNumber;
    }

    public void setOffTotalNumber(Integer offTotalNumber) {
        this.offTotalNumber = offTotalNumber;
    }

    public Integer getOnTotalNumber() {
        return onTotalNumber;
    }

    public void setOnTotalNumber(Integer onTotalNumber) {
        this.onTotalNumber = onTotalNumber;
    }

    public List<Object[]> getMerchants() {
        return merchants;
    }

    public void setMerchants(List<Object[]> merchants) {
        this.merchants = merchants;
    }

    public List<Object> getMerchantIds() {
        return merchantIds;
    }

    public void setMerchantIds(List<Object> merchantIds) {
        this.merchantIds = merchantIds;
    }

    private List<Long> totalCommissions;                //  全部佣金

    private List<Long> availableCommissions;            //  可用佣金

    public List<Long> getTotalCommissions() {
        return totalCommissions;
    }

    public void setTotalCommissions(List<Long> totalCommissions) {
        this.totalCommissions = totalCommissions;
    }

    public List<Long> getAvailableCommissions() {
        return availableCommissions;
    }

    public void setAvailableCommissions(List<Long> availableCommissions) {
        this.availableCommissions = availableCommissions;
    }
}
