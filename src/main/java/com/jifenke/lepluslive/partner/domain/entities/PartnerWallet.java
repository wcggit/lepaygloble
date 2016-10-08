package com.jifenke.lepluslive.partner.domain.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * Created by wcg on 16/6/21.
 */
@Entity
@Table(name = "PARTNER_WALLET")
public class PartnerWallet {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Long availableBalance = 0L;

  private Long totalMoney = 0L; //获取的总佣金

  private Long totalWithdrawals = 0L;//已经提现总额

  private Long availableScoreA = 0L; //可用红包

  private Long availableScoreB = 0L;//可用积分

  private Long totalScoreA = 0L; //累计红包

  private Long totalScoreB = 0L;//累计积分

  @OneToOne
  private Partner partner;

  @Version
  private Long version = 0L;

  public Long getAvailableScoreA() {
    return availableScoreA;
  }

  public void setAvailableScoreA(Long availableScoreA) {
    this.availableScoreA = availableScoreA;
  }

  public Long getAvailableScoreB() {
    return availableScoreB;
  }

  public void setAvailableScoreB(Long availableScoreB) {
    this.availableScoreB = availableScoreB;
  }

  public Long getTotalScoreA() {
    return totalScoreA;
  }

  public void setTotalScoreA(Long totalScoreA) {
    this.totalScoreA = totalScoreA;
  }

  public Long getTotalScoreB() {
    return totalScoreB;
  }

  public void setTotalScoreB(Long totalScoreB) {
    this.totalScoreB = totalScoreB;
  }

  public Partner getPartner() {
    return partner;
  }

  public void setPartner(Partner partner) {
    this.partner = partner;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getAvailableBalance() {
    return availableBalance;
  }

  public void setAvailableBalance(Long availableBalance) {
    this.availableBalance = availableBalance;
  }

  public Long getTotalMoney() {
    return totalMoney;
  }

  public void setTotalMoney(Long totalMoney) {
    this.totalMoney = totalMoney;
  }

  public Long getTotalWithdrawals() {
    return totalWithdrawals;
  }

  public void setTotalWithdrawals(Long totalWithdrawals) {
    this.totalWithdrawals = totalWithdrawals;
  }
}
