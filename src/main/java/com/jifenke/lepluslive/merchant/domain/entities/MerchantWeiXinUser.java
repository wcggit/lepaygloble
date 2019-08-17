package com.jifenke.lepluslive.merchant.domain.entities;


import java.util.Date;

import javax.persistence.*;

/**
 * Created by wcg on 16/5/17.
 */
@Entity
@Table(name = "MERCHANT_WX_USER")
public class MerchantWeiXinUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String openId;

    private String nickname;

    private Long sex;

    private String language;

    private String city;

    private String province;

    private String country;

    private String headImageUrl;

    private String accessToken;

    private String refreshToken;

    @ManyToOne(fetch = FetchType.LAZY)
    private MerchantUser merchantUser;


    Date dateCreated;

    Date lastUpdated;

    Date lastUserInfoDate;   //上次从微信服务器抓取用户信息的时间

    public MerchantUser getMerchantUser() {
        return merchantUser;
    }

    public void setMerchantUser(MerchantUser merchantUser) {
        this.merchantUser = merchantUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Long getSex() {
        return sex;
    }

    public void setSex(Long sex) {
        this.sex = sex;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadImageUrl() {
        return headImageUrl;
    }

    public void setHeadImageUrl(String headImageUrl) {
        this.headImageUrl = headImageUrl;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }


    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Date getLastUserInfoDate() {
        return lastUserInfoDate;
    }

    public void setLastUserInfoDate(Date lastUserInfoDate) {
        this.lastUserInfoDate = lastUserInfoDate;
    }
}
