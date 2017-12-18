package com.jifenke.lepluslive.score.domain.criteria;

/**
 * Created by lss on 2016/9/21.
 */
public class ScoreDetailCriteria {

  private Integer offset;


  public void setOffset(Integer offset) {
    this.offset = offset;
  }

  private String dateCreated;

  private Integer origin;

  public String getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(String dateCreated) {
    this.dateCreated = dateCreated;
  }

  public Integer getOrigin() {
    return origin;
  }

  public void setOrigin(Integer origin) {
    this.origin = origin;
  }

  public Integer getOffset() {
    return offset;
  }
}
