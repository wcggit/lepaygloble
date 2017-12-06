package com.jifenke.lepluslive.groupon.controller.dto;

import com.jifenke.lepluslive.groupon.domain.entities.GrouponProduct;
import com.jifenke.lepluslive.groupon.domain.entities.GrouponProductDetail;
import com.jifenke.lepluslive.groupon.domain.entities.GrouponScrollPicture;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;

import java.util.List;

/**
 * GrouponProductDto
 * 团购产品数据封装类
 * @author XF
 * @date 2017/6/20
 */
public class GrouponProductDto {
    private GrouponProduct grouponProduct;
    private List<Merchant> merchantList;                       // 团购适用门店
    private List<GrouponScrollPicture> delScrollList;       // 轮播图
    private List<GrouponProductDetail> delDetailList;       // 详情图

    public GrouponProduct getGrouponProduct() {
        return grouponProduct;
    }

    public void setGrouponProduct(GrouponProduct grouponProduct) {
        this.grouponProduct = grouponProduct;
    }

    public List<GrouponScrollPicture> getDelScrollList() {
        return delScrollList;
    }

    public void setDelScrollList(List<GrouponScrollPicture> delScrollList) {
        this.delScrollList = delScrollList;
    }

    public List<GrouponProductDetail> getDelDetailList() {
        return delDetailList;
    }

    public void setDelDetailList(List<GrouponProductDetail> delDetailList) {
        this.delDetailList = delDetailList;
    }

    public List<Merchant> getMerchantList() {
        return merchantList;
    }

    public void setMerchantList(List<Merchant> merchantList) {
        this.merchantList = merchantList;
    }
}
