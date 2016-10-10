package com.jifenke.lepluslive.partner.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.partner.domain.criteria.PartnerRechargeCriteria;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerRecharge;
import com.jifenke.lepluslive.partner.service.PartnerRechargeService;
import com.jifenke.lepluslive.partner.service.PartnerService;
import com.jifenke.lepluslive.security.SecurityUtils;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

/**
 * Created by xf on 2016/10/9. 合伙人充值Controller
 */
@RestController
@RequestMapping("/api")
public class PartnerRechargeController {

    @Inject
    private PartnerService partnerService;

    @Inject
    private PartnerRechargeService partnerRechargeService;

    /**
     * 新增合伙人充值请求
     */
    @RequestMapping(value = "/partner/recharge/save", method = RequestMethod.POST)
    @ResponseBody
    public LejiaResult savePartnerRecharge(@RequestBody PartnerRecharge partnerRecharge) {
        try {
            Partner
                partner =
                partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin());
            partnerRecharge.setPartnerPhoneNumber(partner.getPhoneNumber());
            partnerRecharge.setPartner(partner);
            partnerRechargeService.saveParterRecharge(partnerRecharge);
            return LejiaResult.ok("您的充值申请已提交, 乐加客服会尽快联系您 !");
        } catch (Exception e) {
            e.printStackTrace();
            return LejiaResult.build(400, "系统异常,请与客服联系!");
        }
    }


    /**
     * 根据条件查询充值请求
     */
    @RequestMapping("/partner/recharge/findByPartner")
    @ResponseBody
    public LejiaResult findByPage(@RequestBody PartnerRechargeCriteria rechargeCriteria) {
        Page<PartnerRecharge> page = partnerRechargeService.findByCriteria(rechargeCriteria, 10);
        return LejiaResult.ok(page);
    }

}
