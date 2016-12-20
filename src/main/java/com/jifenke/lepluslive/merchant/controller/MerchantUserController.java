package com.jifenke.lepluslive.merchant.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.merchant.domain.criteria.PosOrderCriteria;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.merchant.service.MerchantUserResourceService;
import com.jifenke.lepluslive.security.SecurityUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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



    @RequestMapping(value = "/merchantUser/merchantsInfo",method = RequestMethod.GET)
    @ResponseBody
    public LejiaResult getMerchantsInfo(){
        List<Object []>  merchantList = merchantUserResourceService.findMerchantsByMerchantUserSql(SecurityUtils.getCurrentUserLogin());
        return LejiaResult.ok(merchantList);
    }

    /**
     *  查询商户下所有门店的pos机信息
     */
    @RequestMapping(value="/merchantUser/findByMerchantPosUser",method=RequestMethod.GET)
    public LejiaResult findByMerchantPosUser() {
        return LejiaResult.ok(merchantUserResourceService.findByMerchantPosUser(SecurityUtils.getCurrentUserLogin()));
    }

    @RequestMapping(value = "/merchantUser/posOrderByMerchantUser")
    @ResponseBody
    public LejiaResult findPosOrderByMerchantUser(@RequestBody PosOrderCriteria posOrderCriteria){
        posOrderCriteria.setState(1);
        PosOrderCriteria result = merchantUserResourceService.findPosOrderByMerchantUser(posOrderCriteria);
        return LejiaResult.ok(result);
    }


    /**
     * 根据商户信息查询商户旗下门店所有pos机信息
     * @param posOrderCriteria
     * @return
     */
    @RequestMapping(value = "/merchantUser/findPosInfoByMerchantUser")
    @ResponseBody
    public LejiaResult findPosInfoByMerchantUser(@RequestBody PosOrderCriteria posOrderCriteria){
        PosOrderCriteria result = merchantUserResourceService.findPosInfoByMerchantUser(posOrderCriteria);
        return LejiaResult.ok(result);
    }

}
