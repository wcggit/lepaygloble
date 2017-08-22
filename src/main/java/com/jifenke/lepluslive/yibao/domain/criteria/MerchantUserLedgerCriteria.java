package com.jifenke.lepluslive.yibao.domain.criteria;

/**
 * Created by zhangwen on 2017/7/17.
 */
public class MerchantUserLedgerCriteria {

  private Integer currPage;

  private Integer pageSize = 10;

  private String ledgerNo;

  private Long merchantUserId;

  private String merchantName;

  private Integer state;

  private Integer costSide;

  public Integer getCurrPage() {
    return currPage;
  }

  public void setCurrPage(Integer currPage) {
    this.currPage = currPage;
  }

  public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public String getLedgerNo() {
    return ledgerNo;
  }

  public void setLedgerNo(String ledgerNo) {
    this.ledgerNo = ledgerNo;
  }

  public Long getMerchantUserId() {
    return merchantUserId;
  }

  public void setMerchantUserId(Long merchantUserId) {
    this.merchantUserId = merchantUserId;
  }

  public String getMerchantName() {
    return merchantName;
  }

  public void setMerchantName(String merchantName) {
    this.merchantName = merchantName;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public Integer getCostSide() {
    return costSide;
  }

  public void setCostSide(Integer costSide) {
    this.costSide = costSide;
  }
}
