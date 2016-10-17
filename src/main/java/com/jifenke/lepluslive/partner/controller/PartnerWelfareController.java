package com.jifenke.lepluslive.partner.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.partner.controller.dto.ExclusiveArrayDto;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerWelfareLog;
import com.jifenke.lepluslive.partner.service.PartnerService;
import com.jifenke.lepluslive.partner.service.PartnerWelfareService;
import com.jifenke.lepluslive.security.SecurityUtils;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by wcg on 16/10/12.
 */
@RestController
@RequestMapping("/api")
public class PartnerWelfareController {

    @Inject
    private PartnerService partnerService;

    @Inject
    private PartnerWelfareService partnerWelfareService;

    @RequestMapping(value = "/partner/welfare/{id}", method = RequestMethod.GET)
    public LejiaResult checkUserWelfareLimit(@PathVariable Long id) {
        Partner
            partner =
            partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin());
        return LejiaResult.ok(!partnerWelfareService.checkUserWelfareLimit(partner, id));
    }

    @RequestMapping(value = "/partner/welfare/inclusive_check", method = RequestMethod.POST)
    public LejiaResult checkInclusiveArrayWelfareLimit(@RequestBody Long[] ids) {
        Partner
            partner =
            partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin());
        return LejiaResult.ok(partnerWelfareService.checkInclusiveArrayWelfareLimit(partner, ids));
    }

    @RequestMapping(value = "/partner/welfare/exclusive_check", method = RequestMethod.POST)
    public LejiaResult checkExclusiveArrayWelfareLimit(
        @RequestBody ExclusiveArrayDto exclusiveArrayDto) {
        Partner
            partner =
            partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin());
        exclusiveArrayDto.getLeJiaUserCriteria().setPartner(partner);
        return LejiaResult
            .ok(partnerWelfareService.checkExclusiveArrayWelfareLimit(partner, exclusiveArrayDto));
    }


    @RequestMapping(value = "/partner/welfare", method = RequestMethod.POST)
    public LejiaResult welfareOneUser(
        @RequestBody Map map) {
        Partner
            partner =
            partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin());
        PartnerWelfareLog partnerWelfareLog = new PartnerWelfareLog();
        partnerWelfareLog.setDescription((String) map.get("description"));
        partnerWelfareLog.setScoreA(
            new BigDecimal(map.get("scoreA").toString()).multiply(new BigDecimal(100)).longValue());
        partnerWelfareLog.setScoreB(new BigDecimal(map.get("scoreB").toString()).longValue());
        partnerWelfareLog.setUserCount(1L);
        partnerWelfareLog.setPartner(partner);
        partnerWelfareLog.setRedirectUrl(Integer.parseInt(map.get("redirectUrl").toString()));
        partnerWelfareService.savePartnerWelfareLog(partnerWelfareLog);
        Boolean flag = true;
        try {
            partnerWelfareService
                .welFareToOneUser(map.get("userId").toString(), partnerWelfareLog);
        } catch (Exception e) {
            partnerWelfareLog.setFailCount(1L);
            partnerWelfareService.savePartnerWelfareLog(partnerWelfareLog);
            flag = false;
        }

        return LejiaResult
            .ok(flag);
    }

    @RequestMapping(value = "/partner/welfare/exclusive", method = RequestMethod.POST)
    public LejiaResult batchWelfareExclusive(
        @RequestBody ExclusiveArrayDto exclusiveArrayDto) {
        Partner
            partner =
            partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin());
        exclusiveArrayDto.getLeJiaUserCriteria().setPartner(partner);
        PartnerWelfareLog partnerWelfareLog = exclusiveArrayDto.getPartnerWelfareLog();
        partnerWelfareLog.setPartner(partner);
        partnerWelfareService.savePartnerWelfareLog(partnerWelfareLog);

        return LejiaResult
            .ok(partnerWelfareService.batchWelfareExclusive(partner, exclusiveArrayDto));
    }

    @RequestMapping(value = "/partner/welfare/inclusive", method = RequestMethod.POST)
    public LejiaResult batchWelfareInclusive(
        @RequestBody ExclusiveArrayDto exclusiveArrayDto) {
        Partner
            partner =
            partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin());
        PartnerWelfareLog partnerWelfareLog = exclusiveArrayDto.getPartnerWelfareLog();
        partnerWelfareLog.setPartner(partner);
        partnerWelfareService.savePartnerWelfareLog(partnerWelfareLog);
        return LejiaResult
            .ok(partnerWelfareService.batchWelfareInclusive(partner, exclusiveArrayDto));
    }
}
