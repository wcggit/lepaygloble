package com.jifenke.lepluslive.partner.domain.criteria;

/**
 * Created by xf on 2016/10/13. 合伙人邀请微信会员查询条件
 */
public class WeiXinUserInfoCriteria {

    private Integer offset;             // 分页数
    private String subSource;           // 关注来源
    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public String getSubSource() {
        return subSource;
    }

    public void setSubSource(String subSource) {
        this.subSource = subSource;
    }
}
