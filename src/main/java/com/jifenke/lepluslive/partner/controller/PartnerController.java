package com.jifenke.lepluslive.partner.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.lejiauser.domain.criteria.LeJiaUserCriteria;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.service.PartnerService;
import com.jifenke.lepluslive.security.SecurityUtils;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by wcg on 16/7/21.
 */
@RestController
@RequestMapping("/api")
public class PartnerController {

    @Inject
    private PartnerService partnerService;

    @Inject
    private LeJiaUserService leJiaUserService;

    @RequestMapping(value = "/partner/bindUsers", method = RequestMethod.POST)
    public
    @ResponseBody
    LejiaResult getUsersByBindPartner(@RequestBody LeJiaUserCriteria leJiaUserCriteria) {
        if (leJiaUserCriteria.getOffset() == null) {
            leJiaUserCriteria.setOffset(1);
        }
        Partner
            partner =
            partnerService
                .findPartnerByName(SecurityUtils.getCurrentUserLogin());
        leJiaUserCriteria.setPartner(partner);
        return LejiaResult.ok(leJiaUserService.getUserByBindPartner(leJiaUserCriteria));
    }

    @RequestMapping(value = "/partner/userTotalPages", method = RequestMethod.POST)
    public
    @ResponseBody
    LejiaResult getTotalPageByBindPartner(@RequestBody LeJiaUserCriteria leJiaUserCriteria) {
        if (leJiaUserCriteria.getOffset() == null) {
            leJiaUserCriteria.setOffset(1);
        }
        Partner
            partner =
            partnerService
                .findPartnerByName(SecurityUtils.getCurrentUserLogin());
        leJiaUserCriteria.setPartner(partner);
        return LejiaResult.ok(leJiaUserService.getTotalPagesByBindPartner(leJiaUserCriteria));
    }

}
