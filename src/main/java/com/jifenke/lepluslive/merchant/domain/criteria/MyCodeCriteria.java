package com.jifenke.lepluslive.merchant.domain.criteria;

import com.jifenke.lepluslive.order.domain.entities.OffLineOrder;

import org.springframework.data.domain.Page;

/**
 * 收款二维码
 * Created by tqy on 16/12/14.
 */
public class MyCodeCriteria {

    /**
     * 门店id
     */
    private Long merchantId; //
    /**
     * 门店名称
     */
    private String merchantName; //
    /**
     * 二维码URL
     */
    private String qrCodePicture;

    /**
     * 二维码收款金额(实际支付)
     */
    private Double truePay;
    /**
     * 二维码收款订单金额
     */
    private Double totalPrice;
    /**
     * 门店sid
     */
    private Integer sid;



    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getQrCodePicture() {
        return qrCodePicture;
    }

    public void setQrCodePicture(String qrCodePicture) {
        this.qrCodePicture = qrCodePicture;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getTruePay() {
        return truePay;
    }

    public void setTruePay(Double truePay) {
        this.truePay = truePay;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }
}
