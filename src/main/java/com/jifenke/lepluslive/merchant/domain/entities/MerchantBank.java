package com.jifenke.lepluslive.merchant.domain.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by wcg on 16/5/5.
 */
@Entity
@Table(name = "MERCHANT_BANK")
public class MerchantBank {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String bankNumber;

  private String bankName;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getBankNumber() {
    return bankNumber;
  }

  public void setBankNumber(String bankNumber) {
    this.bankNumber = bankNumber;
  }

  public String getBankName() {
    return bankName;
  }

  public void setBankName(String bankName) {
    this.bankName = bankName;
  }
}
