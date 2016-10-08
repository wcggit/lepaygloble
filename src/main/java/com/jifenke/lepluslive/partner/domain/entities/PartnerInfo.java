package com.jifenke.lepluslive.partner.domain.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Created by wcg on 16/9/28.
 */
@Entity
@Table(name = "PARTNER_INFO")
public class PartnerInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer maxScoreA;

    private Integer minScoreA;

    private Integer scoreAType; //0固定数值 1 随机

    private Integer maxScoreB;

    private Integer minScoreB;

    private Integer scoreBType; //0固定数值 1 随机

    private String qrCodeUrl;//绑定微信号地址

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    @OneToOne
    private Partner partner;

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMaxScoreA() {
        return maxScoreA;
    }

    public void setMaxScoreA(Integer maxScoreA) {
        this.maxScoreA = maxScoreA;
    }

    public Integer getMinScoreA() {
        return minScoreA;
    }

    public void setMinScoreA(Integer minScoreA) {
        this.minScoreA = minScoreA;
    }

    public Integer getScoreAType() {
        return scoreAType;
    }

    public void setScoreAType(Integer scoreAType) {
        this.scoreAType = scoreAType;
    }

    public Integer getMaxScoreB() {
        return maxScoreB;
    }

    public void setMaxScoreB(Integer maxScoreB) {
        this.maxScoreB = maxScoreB;
    }

    public Integer getMinScoreB() {
        return minScoreB;
    }

    public void setMinScoreB(Integer minScoreB) {
        this.minScoreB = minScoreB;
    }

    public Integer getScoreBType() {
        return scoreBType;
    }

    public void setScoreBType(Integer scoreBType) {
        this.scoreBType = scoreBType;
    }
}
