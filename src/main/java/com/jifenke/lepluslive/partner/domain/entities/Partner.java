package com.jifenke.lepluslive.partner.domain.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by wcg on 16/6/3. 合伙人表
 */
@Entity
@Table(name = "PARTNER")
public class Partner {


  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;


  private String name;

  public Partner(Long id) {
    this.id = id;
  }

  public Partner() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
