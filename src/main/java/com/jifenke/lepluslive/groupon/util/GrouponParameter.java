package com.jifenke.lepluslive.groupon.util;

/**
 * 计算的返利及佣金分配值
 * Created by zhangwen on 2017/8/16.
 */
public class GrouponParameter {

  private long commission;

  private long truePayCommission;

  private long scoreA;

  private long scoreC;

  private long share;

  private long tradePartner;

  private long tradePartnerManager;

  private long lockMerchant;

  private long lockPartner;

  private long lockPartnerManager;

  private long lePlus;

  public long getCommission() {
    return commission;
  }

  public void setCommission(long commission) {
    this.commission = commission;
  }

  public long getTruePayCommission() {
    return truePayCommission;
  }

  public void setTruePayCommission(long truePayCommission) {
    this.truePayCommission = truePayCommission;
  }

  public long getScoreA() {
    return scoreA;
  }

  public void setScoreA(long scoreA) {
    this.scoreA = scoreA;
  }

  public long getScoreC() {
    return scoreC;
  }

  public void setScoreC(long scoreC) {
    this.scoreC = scoreC;
  }

  public long getShare() {
    return share;
  }

  public void setShare(long share) {
    this.share = share;
  }

  public long getTradePartner() {
    return tradePartner;
  }

  public void setTradePartner(long tradePartner) {
    this.tradePartner = tradePartner;
  }

  public long getTradePartnerManager() {
    return tradePartnerManager;
  }

  public void setTradePartnerManager(long tradePartnerManager) {
    this.tradePartnerManager = tradePartnerManager;
  }

  public long getLockMerchant() {
    return lockMerchant;
  }

  public void setLockMerchant(long lockMerchant) {
    this.lockMerchant = lockMerchant;
  }

  public long getLockPartner() {
    return lockPartner;
  }

  public void setLockPartner(long lockPartner) {
    this.lockPartner = lockPartner;
  }

  public long getLockPartnerManager() {
    return lockPartnerManager;
  }

  public void setLockPartnerManager(long lockPartnerManager) {
    this.lockPartnerManager = lockPartnerManager;
  }

  public long getLePlus() {
    return lePlus;
  }

  public void setLePlus(long lePlus) {
    this.lePlus = lePlus;
  }

  @Override
  public String toString() {
    return "GrouponParameter{" +
           "scoreA=" + scoreA +
           ", scoreC=" + scoreC +
           ", share=" + share +
           ", tradePartner=" + tradePartner +
           ", tradePartnerManager=" + tradePartnerManager +
           ", lockMerchant=" + lockMerchant +
           ", lockPartner=" + lockPartner +
           ", lockPartnerManager=" + lockPartnerManager +
           ", lePlus=" + lePlus +
           '}';
  }
}
