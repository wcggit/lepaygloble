package com.jifenke.lepluslive.partner.domain.entities;

import com.jifenke.lepluslive.global.util.MvUtil;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by wcg on 16/10/10. 发福利活动日志
 */
@Entity
@Table(name = "PARTNER_WELFARE_LOG")
public class PartnerWelfareLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String userArray; //用户ID数组

    private String sid = MvUtil.getOrderNumber();

    private Long scoreA;

    private Long scoreB;

    private String description;

    private Integer redirectUrl; //跳转页面 0我的钱包 1 臻品商城 2周边好店

    @ManyToOne
    private Partner partner;

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserArray() {
        return userArray;
    }

    public void setUserArray(String userArray) {
        this.userArray = userArray;
    }

    public Long getScoreA() {
        return scoreA;
    }

    public void setScoreA(Long scoreA) {
        this.scoreA = scoreA;
    }

    public Long getScoreB() {
        return scoreB;
    }

    public void setScoreB(Long scoreB) {
        this.scoreB = scoreB;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(Integer redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
