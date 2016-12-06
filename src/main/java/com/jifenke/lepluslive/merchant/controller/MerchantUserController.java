package com.jifenke.lepluslive.merchant.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.merchant.service.MerchantUserResourceService;
import com.jifenke.lepluslive.security.SecurityUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xf on 16-12-5.
 */
@RestController
@RequestMapping(value="/api")
public class MerchantUserController {

    @Inject
    private MerchantService merchantService;
    @Inject
    private MerchantUserResourceService merchantUserResourceService;

    /**
     *  获取当前登录用户信息
     * */
    @RequestMapping(value = "/merchantUser", method = RequestMethod.GET)
    public LejiaResult getMerchant() {
        MerchantUser merchantUser =  merchantService.findMerchantUserByName(SecurityUtils.getCurrentUserLogin());
        MerchantUser merchantInfo = new MerchantUser();
        merchantInfo.setName(merchantUser.getName());
        merchantInfo.setMerchantName(merchantUser.getMerchantName());
        merchantInfo.setLinkMan(merchantUser.getLinkMan());
        merchantInfo.setCity(merchantUser.getCity());
        return LejiaResult.ok(merchantInfo);
    }

    /**
     *  查询当前商户下佣金数据
     */
    @RequestMapping(value="/merchantUser/comission",method = RequestMethod.GET)
    public LejiaResult getMerchantAvalibleCommission() {
        MerchantUser merchantUser = merchantService.findMerchantUserByName(SecurityUtils.getCurrentUserLogin());
        List<Merchant> merchants = merchantUserResourceService.findMerchantsByMerchantUser(merchantUser);
        Map map = merchantService.findCommissionByMerchants(merchants);
        return LejiaResult.ok(map);
    }

    /**
     *  查询商户下所有的门店
     */
    @RequestMapping(value="/merchantUser/merchants",method=RequestMethod.GET)
    public LejiaResult getMerchants() {
        MerchantUser merchantUser = merchantService.findMerchantUserByName(SecurityUtils.getCurrentUserLogin());
        List<Merchant> merchants = merchantUserResourceService.findMerchantsByMerchantUser(merchantUser);
        return LejiaResult.ok(merchants);
    }

}
