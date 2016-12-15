package com.jifenke.lepluslive.order.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jifenke.lepluslive.product.domain.entities.Product;
import com.jifenke.lepluslive.product.domain.entities.ProductSpec;

import javax.persistence.*;

/**
 * Created by wcg on 16/3/20.
 */
@Entity
@Table(name = "ORDER_DETAIL")
public class OrderDetail {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @JsonIgnore
  @ManyToOne
  private OnLineOrder onLineOrder;

  @ManyToOne
  private Product product;

  private Integer productNumber;

  @ManyToOne
  private ProductSpec productSpec;


  private Integer state;


  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public ProductSpec getProductSpec() {
    return productSpec;
  }

  public void setProductSpec(ProductSpec productSpec) {
    this.productSpec = productSpec;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public Integer getProductNumber() {
    return productNumber;
  }

  public void setProductNumber(Integer productNumber) {
    this.productNumber = productNumber;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public OnLineOrder getOnLineOrder() {
    return onLineOrder;
  }

  public void setOnLineOrder(OnLineOrder onLineOrder) {
    this.onLineOrder = onLineOrder;
  }
}
