package com.jifenke.lepluslive.partner.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerInfo;
import com.jifenke.lepluslive.partner.service.PartnerInfoService;
import com.jifenke.lepluslive.partner.service.PartnerService;
import com.jifenke.lepluslive.security.SecurityUtils;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

/**
 * Created by xf on 2016/10/14. 合伙人邀请礼包Controller
 */
@RestController
@RequestMapping("/api")
public class PartnerInfoController {

    @Inject
    private PartnerInfoService partnerInfoService;
    @Inject
    private PartnerService partnerService;


    @RequestMapping(value = "/partner/loadParnerInfo",method = RequestMethod.GET)
    @ResponseBody
    public PartnerInfo loadPartnerInfo() {
        PartnerInfo partnerInfo =
            partnerService.findPartnerInfoByPartnerSid(SecurityUtils.getCurrentUserLogin());
        return partnerInfo;
    }

    @RequestMapping(value="/partner/partnerInfo/save",method = RequestMethod.POST)
    @ResponseBody
    public LejiaResult savePartnerInfo(@RequestBody PartnerInfo partnerInfo) {
        try {
            Partner partner = partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin());
            partnerInfo.setPartner(partner);
            partnerInfoService.saveOrUpdatePartnerInfo(partnerInfo);
            return LejiaResult.ok("保存成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return LejiaResult.build(400, "保存失败!");
        }
    }
}
