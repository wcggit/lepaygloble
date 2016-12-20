package com.jifenke.lepluslive.partner.domain.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by xf on 2016/10/9. 合伙人充值
 */
@Entity
@Table(name = "PARTNER_RECHARGE")
public class PartnerRecharge {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Partner partner;                        //  合伙人


    private Long scorea;                            //  红包 (1:100)

    private Long scoreb;                            //  积分

    private Date createTime;                        //  创建时间

    private Date lastUpdateDate;                    //  最后更新时间

    private Integer rechargeState;                  //  充值状态:    0 未审核    1 已审核   2 驳回

    private String partnerPhoneNumber;              //  合伙人电话

    private String remark;                          //  备注

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public String getPartnerPhoneNumber() {
        return partnerPhoneNumber;
    }

    public void setPartnerPhoneNumber(String partnerPhoneNumber) {
        this.partnerPhoneNumber = partnerPhoneNumber;
    }

    public Integer getRechargeState() {
        return rechargeState;
    }

    public void setRechargeState(Integer rechargeState) {
        this.rechargeState = rechargeState;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getScorea() {
        return scorea;
    }

    public void setScorea(Long scorea) {
        this.scorea = scorea;
    }

    public Long getScoreb() {
        return scoreb;
    }

    public void setScoreb(Long scoreb) {
        this.scoreb = scoreb;
    }
}
