package com.jifenke.lepluslive.partner.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.partner.domain.criteria.WeiXinUserInfoCriteria;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.service.PartnerService;
import com.jifenke.lepluslive.security.SecurityUtils;
import com.jifenke.lepluslive.weixin.service.WeiXinUserService;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import javax.inject.Inject;

/**
 * Created by xf on 2016/10/13.
 * 合伙人虚拟商户邀请会员
 */
@RestController
@RequestMapping("/api")
public class PartnerMerchantInfoController {

    @Inject
    private PartnerService partnerService;
    @Inject
    private WeiXinUserService weiXinUserService;
    @Inject
    private MerchantService merchantService;


    /**
     * 查询合伙人-虚拟商户-邀请会员数据
     * @return
     */
    @RequestMapping(value="/partner/merchantInfo/count",method = RequestMethod.GET)
    @ResponseBody
    public LejiaResult getMerchantInfoCount() {
        Partner partner = partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin());
        Merchant virtualMerchant = merchantService.findPartnerVirtualMerchant(partner);
        Map merchantCodeData = merchantService.findMerchantCodeData(virtualMerchant);
        return LejiaResult.ok(merchantCodeData);
    }

    /**
     * 查询合伙人-邀请关注用户-关注红包/积分
     * @param infoCriteria
     * @return
     */
    @RequestMapping(value="/partner/merchantInfo",method = RequestMethod.POST)
    @ResponseBody
    public LejiaResult getMerchantInfoPage(@RequestBody WeiXinUserInfoCriteria infoCriteria) {
        if(infoCriteria.getOffset()==null) {
            infoCriteria.setOffset(1);
        }
        Partner partner = partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin());
        Merchant virtualMerchant = merchantService.findPartnerVirtualMerchant(partner);
        String subSource = "4_0_"+virtualMerchant.getId();
        infoCriteria.setSubSource(subSource);
        Map map = weiXinUserService.findPageByInfoCriteria(infoCriteria,10);
        return LejiaResult.ok(map);
    }

}
