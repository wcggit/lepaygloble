package com.jifenke.lepluslive.lejiauser.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerManager;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Created by wcg on 16/3/22.
 */
@Entity
@Table(name = "LE_JIA_USER")
public class LeJiaUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String oneBarCodeUrl;

    private String userSid = MvUtil.getBarCodeStr();

    private Date createDate = new Date();

    private Date phoneBindDate;

    private String phoneNumber;

    private String token;

    private String pwd;    //加密后

    private String headImageUrl;

    private String userName;  //用户名

    @ManyToOne(fetch = FetchType.LAZY)
    private RegisterOrigin registerOrigin;

    @ManyToOne
    private Merchant bindMerchant;

    @ManyToOne
    private Partner bindPartner;

    @ManyToOne
    private PartnerManager bindPartnerManager;

    private Date bindMerchantDate;

    private Date bindPartnerDate;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private WeiXinUser weiXinUser;


    public WeiXinUser getWeiXinUser() {
        return weiXinUser;
    }

    public void setWeiXinUser(WeiXinUser weiXinUser) {
        this.weiXinUser = weiXinUser;
    }

    public Date getBindMerchantDate() {
        return bindMerchantDate;
    }

    public void setBindMerchantDate(Date bindMerchantDate) {
        this.bindMerchantDate = bindMerchantDate;
    }

    public Date getBindPartnerDate() {
        return bindPartnerDate;
    }

    public void setBindPartnerDate(Date bindPartnerDate) {
        this.bindPartnerDate = bindPartnerDate;
    }


    public Partner getBindPartner() {
        return bindPartner;
    }

    public void setBindPartner(Partner bindPartner) {
        this.bindPartner = bindPartner;
    }

    public Merchant getBindMerchant() {
        return bindMerchant;
    }

    public void setBindMerchant(Merchant bindMerchant) {
        this.bindMerchant = bindMerchant;
    }

    public RegisterOrigin getRegisterOrigin() {
        return registerOrigin;
    }

    public void setRegisterOrigin(RegisterOrigin registerOrigin) {
        this.registerOrigin = registerOrigin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getHeadImageUrl() {
        return headImageUrl;
    }

    public void setHeadImageUrl(String headImageUrl) {
        this.headImageUrl = headImageUrl;
    }

    public Date getPhoneBindDate() {
        return phoneBindDate;
    }

    public void setPhoneBindDate(Date phoneBindDate) {
        this.phoneBindDate = phoneBindDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOneBarCodeUrl() {
        return oneBarCodeUrl;
    }

    public void setOneBarCodeUrl(String oneBarCodeUrl) {
        this.oneBarCodeUrl = oneBarCodeUrl;
    }

    public String getUserSid() {
        return userSid;
    }

    public void setUserSid(String userSid) {
        this.userSid = userSid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public PartnerManager getBindPartnerManager() {
        return bindPartnerManager;
    }

    public void setBindPartnerManager(PartnerManager bindPartnerManager) {
        this.bindPartnerManager = bindPartnerManager;
    }
}
