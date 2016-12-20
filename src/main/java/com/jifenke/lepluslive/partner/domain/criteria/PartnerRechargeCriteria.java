package com.jifenke.lepluslive.partner.domain.criteria;

import com.jifenke.lepluslive.partner.domain.entities.Partner;

/**
 * Created by xf on 2016/10/9.
 */
public class PartnerRechargeCriteria {


    private Integer offset;                       // 当前页数

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    private Partner partner;

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    private Integer rechargeState;                  // 订单审核状态  0 - 未审核  1-已审核  2 - 未通过

    public PartnerRechargeCriteria(Integer offset) {
        this.offset = offset;
    }

    public Integer getRechargeState() {
        return rechargeState;
    }

    public void setRechargeState(Integer rechargeState) {
        this.rechargeState = rechargeState;
    }
}
