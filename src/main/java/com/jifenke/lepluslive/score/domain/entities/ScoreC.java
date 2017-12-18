package com.jifenke.lepluslive.score.domain.entities;


import com.jifenke.lepluslive.user.domain.entities.LeJiaUser;

import javax.persistence.*;
import java.util.Date;

/**
 * 金币账户 Created by wcg on 17/02/17.
 */
@Entity
@Table(name = "SCOREC")
public class ScoreC {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Long score = 0L; //单位分

  @OneToOne
  private LeJiaUser leJiaUser;

  private Date createdDate = new Date();

  private Long totalScore = 0L;

  private Date lastUpdateDate;

  public Long getTotalScore() {
    return totalScore;
  }

  public void setTotalScore(Long totalScore) {
    this.totalScore = totalScore;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public Date getLastUpdateDate() {
    return lastUpdateDate;
  }

  public void setLastUpdateDate(Date lastUpdateDate) {
    this.lastUpdateDate = lastUpdateDate;
  }


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getScore() {
    return score;
  }

  public void setScore(Long score) {
    this.score = score;
  }

  public LeJiaUser getLeJiaUser() {
    return leJiaUser;
  }

  public void setLeJiaUser(LeJiaUser leJiaUser) {
    this.leJiaUser = leJiaUser;
  }
}
