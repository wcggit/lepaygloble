package com.jifenke.lepluslive.lejiauser.domain.entities;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Created by wcg on 16/4/22.
 */
@Entity
@Table(name = "REGISTER_ORiGIN")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RegisterOrigin {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  private Merchant merchant;

  private Integer originType; // 0 代表微信注册 1 代表app注册 2 代表商户注册

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Merchant getMerchant() {
    return merchant;
  }

  public void setMerchant(Merchant merchant) {
    this.merchant = merchant;
  }

  public Integer getOriginType() {
    return originType;
  }

  public void setOriginType(Integer originType) {
    this.originType = originType;
  }
}
