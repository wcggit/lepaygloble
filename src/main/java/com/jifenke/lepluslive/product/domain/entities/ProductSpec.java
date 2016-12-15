package com.jifenke.lepluslive.product.domain.entities;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by wcg on 16/3/16.
 */
@Entity
@Table(name = "PRODUCT_SPEC")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProductSpec {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @ManyToOne
  private Product product;

  @NotNull
  private Integer repository = 0;

  private String specDetail;

  @NotNull
  private String picture;

  @Version
  private Long version = 0L;

  @NotNull
  private Long price;   //市场价

  @Column(name = "min_price")
  @NotNull
  private Long minPrice; //购买最低金额

  private Integer minScore = 0;  //兑换最低所需积分

  private Integer saleNumber = 0; //销售量

  private Integer state = 1;

  private Long toMerchant = 0L; //绑定商户返佣金额

  private Long toPartner = 0L;  //绑定合伙人返佣金额

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public String getSpecDetail() {
    return specDetail;
  }

  public void setSpecDetail(String specDetail) {
    this.specDetail = specDetail;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getPicture() {
    return picture;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }

  public Long getPrice() {
    return price;
  }

  public void setPrice(Long price) {
    this.price = price;
  }

  public Long getMinPrice() {
    return minPrice;
  }

  public void setMinPrice(Long minPrice) {
    this.minPrice = minPrice;
  }

  public Integer getMinScore() {
    return minScore;
  }

  public void setMinScore(Integer minScore) {
    this.minScore = minScore;
  }

  public Integer getSaleNumber() {
    return saleNumber;
  }

  public void setSaleNumber(Integer saleNumber) {
    this.saleNumber = saleNumber;
  }

  public Integer getRepository() {
    return repository;
  }

  public void setRepository(Integer repository) {
    this.repository = repository;
  }

  public Long getToMerchant() {
    return toMerchant;
  }

  public void setToMerchant(Long toMerchant) {
    this.toMerchant = toMerchant;
  }

  public Long getToPartner() {
    return toPartner;
  }

  public void setToPartner(Long toPartner) {
    this.toPartner = toPartner;
  }
}
