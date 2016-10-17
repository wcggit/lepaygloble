package com.jifenke.lepluslive.partner.domain.criteria;

/**
 * Created by xf on 2016/10/12. 营销账户查询条件
 */
public class PartnerScoreLogCriteria {


    private Integer offset;             //  页数
    private Integer numberType;         //  -1 减扣记录  1 增值记录   (不传则查询全部)
    private Long partnerId;

    public Integer getNumberType() {
        return numberType;
    }

    public void setNumberType(Integer numberType) {
        this.numberType = numberType;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }
}
