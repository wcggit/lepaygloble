package com.jifenke.lepluslive.merchant.domain.entities;

import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.partner.domain.entities.Partner;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Created by wcg on 16/3/17.
 */
@Entity
@Table(name = "MERCHANT")
public class Merchant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer sid;

    @OneToOne(cascade = CascadeType.ALL)
    private MerchantBank merchantBank;

    private String merchantSid = MvUtil.getMerchantSid();

    @ManyToOne
    private City city;

    private String thumb;//缩略图

    @ManyToOne
    private MerchantType merchantType;

    @ManyToOne
    private Partner partner;

    private String location;

    private String name;

    private String picture;

    private String phoneNumber; //服务电话

    private Integer partnership; //合作关系 0普通商户  1 联盟商户 2 虚拟商户 未天使合伙人创建默认自带商户

    private Double lng = 0.0;

    private Double lat = 0.0;

    private String payee; //收款人

    private Integer state = 0;  //状态0 代表未开启乐店 ,

    private Integer receiptAuth; //收款权限为1 代表可以使用乐加红包支付,0代表不能使用乐加红包

    private String qrCodePicture; //商户收款码

    private String pureQrCode; //纯支付码

    private Long userLimit; //会员绑定上线

    private BigDecimal ljCommission; //乐加佣金 单位百分比

    private BigDecimal ljBrokerage = new BigDecimal(0); //只有联盟商户才不为空 , 代表非乐加会员消费时,收取的手续费

    private BigDecimal memberCommission = new BigDecimal(0); //只有联盟商户才不为空 , 代表会员在绑定商户消费时的手续费

    private BigDecimal scoreARebate; //返a积分比 单位百分比

    private BigDecimal scoreBRebate;

    private String contact; //联系人

    private Date createDate = new Date();

    public String getPureQrCode() {
        return pureQrCode;
    }

    public void setPureQrCode(String pureQrCode) {
        this.pureQrCode = pureQrCode;
    }

    public BigDecimal getMemberCommission() {
        return memberCommission;
    }

    public void setMemberCommission(BigDecimal memberCommission) {
        this.memberCommission = memberCommission;
    }

    public BigDecimal getLjBrokerage() {
        return ljBrokerage;
    }

    public void setLjBrokerage(BigDecimal ljBrokerage) {
        this.ljBrokerage = ljBrokerage;
    }

    @OneToMany(mappedBy = "merchant", fetch = FetchType.EAGER)
    private List<MerchantProtocol> merchantProtocols;

    private Integer cycle;  //结算周期  1 一个工作日 2 2个工作日

    private String merchantPhone; //绑定电话

    private String officeHour; //营业时间

    public String getMerchantPhone() {
        return merchantPhone;
    }

    public void setMerchantPhone(String merchantPhone) {
        this.merchantPhone = merchantPhone;
    }

    public String getOfficeHour() {
        return officeHour;
    }

    public void setOfficeHour(String officeHour) {
        this.officeHour = officeHour;
    }

    public Integer getCycle() {
        return cycle;
    }

    public void setCycle(Integer cycle) {
        this.cycle = cycle;
    }

    public List<MerchantProtocol> getMerchantProtocols() {
        return merchantProtocols;
    }

    public void setMerchantProtocols(List<MerchantProtocol> merchantProtocols) {
        this.merchantProtocols = merchantProtocols;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public BigDecimal getScoreBRebate() {
        return scoreBRebate;
    }

    public void setScoreBRebate(BigDecimal scoreBRebate) {
        this.scoreBRebate = scoreBRebate;
    }

    public BigDecimal getLjCommission() {
        return ljCommission;
    }

    public void setLjCommission(BigDecimal ljCommission) {
        this.ljCommission = ljCommission;
    }

    public BigDecimal getScoreARebate() {
        return scoreARebate;
    }

    public void setScoreARebate(BigDecimal scoreARebate) {
        this.scoreARebate = scoreARebate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getUserLimit() {
        return userLimit;
    }

    public void setUserLimit(Long userLimit) {
        this.userLimit = userLimit;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }


    @ManyToOne
    private Area area;

    public Integer getReceiptAuth() {
        return receiptAuth;
    }

    public void setReceiptAuth(Integer receiptAuth) {
        this.receiptAuth = receiptAuth;
    }


    public String getQrCodePicture() {
        return qrCodePicture;
    }

    public void setQrCodePicture(String qrCodePicture) {
        this.qrCodePicture = qrCodePicture;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }


    public Integer getPartnership() {
        return partnership;
    }

    public void setPartnership(Integer partnership) {
        this.partnership = partnership;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public MerchantBank getMerchantBank() {
        return merchantBank;
    }

    public void setMerchantBank(MerchantBank merchantBank) {
        this.merchantBank = merchantBank;
    }

    public String getMerchantSid() {
        return merchantSid;
    }

    public void setMerchantSid(String merchantSid) {
        this.merchantSid = merchantSid;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public MerchantType getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(MerchantType merchantType) {
        this.merchantType = merchantType;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }


    @OneToOne(cascade = CascadeType.ALL)
    private MerchantInfo merchantInfo;   //商家详情介绍

    public MerchantInfo getMerchantInfo() {
        return merchantInfo;
    }

    public void setMerchantInfo(MerchantInfo merchantInfo) {
        this.merchantInfo = merchantInfo;
    }
}
