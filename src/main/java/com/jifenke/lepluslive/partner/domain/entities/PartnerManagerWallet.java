package com.jifenke.lepluslive.partner.domain.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * Created by wcg on 16/6/22.
 */
@Entity
@Table(name = "PARTNER_MANAGER_WALLET")
public class PartnerManagerWallet {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Long availableBalance = 0L;

  private Long totalMoney = 0L;

  @OneToOne
  private PartnerManager partnerManager;


  @Version
  private Long version = 0L;

  public PartnerManager getPartnerManager() {
    return partnerManager;
  }

  public void setPartnerManager(PartnerManager partnerManager) {
    this.partnerManager = partnerManager;
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
}
