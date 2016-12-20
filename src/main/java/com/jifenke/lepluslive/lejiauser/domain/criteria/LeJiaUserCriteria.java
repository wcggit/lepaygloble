package com.jifenke.lepluslive.lejiauser.domain.criteria;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.partner.domain.entities.Partner;

/**
 * Created by wcg on 16/7/8.
 */
public class LeJiaUserCriteria {

    private String startDate; //绑定商户时间

    private String endDate;

    private Integer offset;

    private Merchant merchant;

    private String partnerStartDate; //绑定合伙人时间

    private String partnerEndDate;

    private String merchantName; //绑定商户名

    private String phone; //手机号

    private Partner partner;

    private Integer consumptionTimes;

    private Integer timeSelect; //0< 1= 2>

    private String consumptionCount;//消费总价

    private Integer countSelect; //0< 1= 2>

    private String nickname; // 微信昵称

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getConsumptionTimes() {
        return consumptionTimes;
    }

    public void setConsumptionTimes(Integer consumptionTimes) {
        this.consumptionTimes = consumptionTimes;
    }

    public Integer getTimeSelect() {
        return timeSelect;
    }

    public void setTimeSelect(Integer timeSelect) {
        this.timeSelect = timeSelect;
    }

    public String getConsumptionCount() {
        return consumptionCount;
    }

    public void setConsumptionCount(String consumptionCount) {
        this.consumptionCount = consumptionCount;
    }

    public Integer getCountSelect() {
        return countSelect;
    }

    public void setCountSelect(Integer countSelect) {
        this.countSelect = countSelect;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public String getPartnerStartDate() {
        return partnerStartDate;
    }

    public void setPartnerStartDate(String partnerStartDate) {
        this.partnerStartDate = partnerStartDate;
    }

    public String getPartnerEndDate() {
        return partnerEndDate;
    }

    public void setPartnerEndDate(String partnerEndDate) {
        this.partnerEndDate = partnerEndDate;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }
}
