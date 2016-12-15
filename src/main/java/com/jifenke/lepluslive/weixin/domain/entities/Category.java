package com.jifenke.lepluslive.weixin.domain.entities;

import javax.persistence.*;

/**
 * 数据字典 Created by zhangwen on 2016/11/01.
 */
@Entity
@Table(name = "CATEGORY")
public class Category {

  //ID共五位数字，前两位表示大分类，后两位表示小类别
  //分类规则：每种大分类在11-99之间 大分类的每种类别在01-00之间 例如: 11001   11002 | 12001   12002
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Integer state;  //分类是否有效 1=有效（防止某一分类不使用时无法删除，又不想后台展示）

  private Integer category;  //大分类类别，此值为实际筛选标识

  private Integer type;    //大分类下的小分类类别，此值为实际筛选标识

  private String value;   //小分类获取到的值（例：category=1&type=1 代表商品角标值）

  private String categoryExplain; //大分类代表的意思，用于后台识别添加

  private String typeExplain; //小分类代表的意思，用于后台识别添加

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public Integer getCategory() {
    return category;
  }

  public void setCategory(Integer category) {
    this.category = category;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getCategoryExplain() {
    return categoryExplain;
  }

  public void setCategoryExplain(String categoryExplain) {
    this.categoryExplain = categoryExplain;
  }

  public String getTypeExplain() {
    return typeExplain;
  }

  public void setTypeExplain(String typeExplain) {
    this.typeExplain = typeExplain;
  }
}
