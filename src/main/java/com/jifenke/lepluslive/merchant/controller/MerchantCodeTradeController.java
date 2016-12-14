package com.jifenke.lepluslive.merchant.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.merchant.domain.criteria.CodeOrderCriteria;
import com.jifenke.lepluslive.merchant.domain.criteria.MyCodeCriteria;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.merchant.service.MerchantUserResourceService;
import com.jifenke.lepluslive.order.service.OffLineOrderService;
import com.jifenke.lepluslive.security.SecurityUtils;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 扫码交易
 * Created by tqy on 16/12/9.
 */
@RestController
@RequestMapping(value="/api")
public class MerchantCodeTradeController {

    @Inject
    private MerchantService merchantService;
    @Inject
    private MerchantUserResourceService merchantUserResourceService;
    @Inject
    private OffLineOrderService offLineOrderService;

    /**
     *  查询商户下所有的门店
     */
    @RequestMapping(value = "/codeTrade/getMerchants", method = RequestMethod.GET)
    @ResponseBody
    public LejiaResult getMerchants() {
        MerchantUser merchantUser = merchantService.findMerchantUserByName(SecurityUtils.getCurrentUserLogin());
        List<Merchant> merchants = merchantUserResourceService.findMerchantsByMerchantUser(merchantUser);
        return LejiaResult.ok(merchants);
    }

    /**
     * 查询 扫码交易订单
     * @param codeOrderCriteria
     * @return
     */
    @RequestMapping(value = "/codeTrade/codeOrderByMerchantUser")
    @ResponseBody
    public LejiaResult findPosOrderByMerchantUser(@RequestBody CodeOrderCriteria codeOrderCriteria){
        codeOrderCriteria.setState(1);
        CodeOrderCriteria result = offLineOrderService.findCodeOrderByMerchantUser(codeOrderCriteria);
        return LejiaResult.ok(result);
    }

    /**
     *  查询所有的门店的二维码
     */
    @RequestMapping(value = "/codeTrade/getMyCodes", method = RequestMethod.GET)
    @ResponseBody
    public LejiaResult getMyCodes() {
        MerchantUser merchantUser = merchantService.findMerchantUserByName(SecurityUtils.getCurrentUserLogin());
        List<Merchant> merchants = merchantUserResourceService.findMerchantsByMerchantUser(merchantUser);
//        Merchant merchant = merchantUser.getMerchant();
//        merchants.add(merchant);

        List<MyCodeCriteria> listMycode = new ArrayList<>();
        for (Merchant m : merchants){
            MyCodeCriteria myCodeCriteria = new MyCodeCriteria();
            myCodeCriteria.setMerchantId(m.getId());
            myCodeCriteria.setMerchantName(m.getName());
            myCodeCriteria.setSid(m.getMerchantSid());
            myCodeCriteria.setQrCodePicture(m.getQrCodePicture());
            List<Object[]> listo = offLineOrderService.findMyCodePriceByMerchantid(m.getId());
            myCodeCriteria.setTruePay(Double.valueOf(listo.get(0)[1] == null ? "0.0" : listo.get(0)[1].toString()));
            myCodeCriteria.setTotalPrice(Double.valueOf(listo.get(0)[2] == null ? "0.0" : listo.get(0)[2].toString()));
            listMycode.add(myCodeCriteria);
        }

//        return LejiaResult.ok(merchants);
        return LejiaResult.ok(listMycode);
    }

}
