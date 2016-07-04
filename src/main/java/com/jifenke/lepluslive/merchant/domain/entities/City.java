package com.jifenke.lepluslive.merchant.domain.entities;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Created by wcg on 16/3/17.
 */
@Entity
@Table(name = "CITY")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class City {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private int sid;

  private String name;

  public City() {
  }

  public City(Long id) {
    this.id = id;
  }



  @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "city")
  private List<Area> areas;

  public List<Area> getAreas() {
    return areas;
  }

  public void setAreas(List<Area> areas) {
    this.areas = areas;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public int getSid() {
    return sid;
  }

  public void setSid(int sid) {
    this.sid = sid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
