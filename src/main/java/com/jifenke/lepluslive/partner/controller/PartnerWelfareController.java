package com.jifenke.lepluslive.partner.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.service.PartnerService;
import com.jifenke.lepluslive.partner.service.PartnerWelfareService;
import com.jifenke.lepluslive.security.SecurityUtils;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "/partner/welfare", method = RequestMethod.POST)
    public LejiaResult checkUserArrayWelfareLimit(@RequestBody Long[] ids) {
        Partner
            partner =
            partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin());
        return LejiaResult.ok(partnerWelfareService.checkUserArrayWelfareLimit(partner, ids));
    }


}
