package com.jifenke.lepluslive.groupon.domain.entities;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;

import javax.persistence.*;

/**
 * Created by wcg on 2017/6/14. 团购产品对应门店
 */
@Entity
@Table(name = "GROUPON_MERCHANT")
public class GrouponMerchant {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne
  private GrouponProduct grouponProduct;

  @ManyToOne
  private Merchant merchant;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public GrouponProduct getGrouponProduct() {
    return grouponProduct;
  }

  public void setGrouponProduct(GrouponProduct grouponProduct) {
    this.grouponProduct = grouponProduct;
  }

  public Merchant getMerchant() {
    return merchant;
  }

  public void setMerchant(Merchant merchant) {
    this.merchant = merchant;
  }
}
