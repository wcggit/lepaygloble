package com.jifenke.lepluslive.partner.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.lejiauser.domain.criteria.LeJiaUserCriteria;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerWallet;
import com.jifenke.lepluslive.partner.service.PartnerService;
import com.jifenke.lepluslive.security.SecurityUtils;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

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

    @Inject
    private MerchantService merchantService;

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

    @RequestMapping(value = "/partner/homeData", method = RequestMethod.GET)
    public
    @ResponseBody
    Map getPartnerHomePageData() {
        Map result = new HashMap();
        Partner
            partner =
            partnerService.findPartnerByName(SecurityUtils.getCurrentUserLogin());
        PartnerWallet partnerWallet = partnerService.findPartnerWalletByPartner(partner);
        Long bindLeJiaUser = leJiaUserService.countPartnerBindLeJiaUser(partner);

        result.put("available", partnerWallet.getAvailableBalance() / 100.0);
        result.put("total", partnerWallet.getTotalMoney() / 100.0);
        result.put("userLimit", partner.getUserLimit());
        result.put("merchantLimit", partner.getMerchantLimit());
        result.put("dayCommission", partnerService.countPartnerDayCommission(partner));
        result.put("bindLeJiaUser", leJiaUserService.countPartnerBindLeJiaUser(partner));
        result.put("bindMerchant", merchantService.countPartnerBindMerchant(partner));
        result.put("dayBindLeJiaUser", leJiaUserService.countPartnerBindLeJiaUserByDate(partner));
        result.put("perCommission", new BigDecimal(partnerWallet.getTotalMoney() / 100.0)
            .divide(new BigDecimal(bindLeJiaUser), 2,BigDecimal.ROUND_HALF_UP).doubleValue());
        return result;
    }

    @RequestMapping(value = "/partner", method = RequestMethod.GET)
    public
    @ResponseBody
    LejiaResult getCurrentPartner() {
        Partner
            partner =
            partnerService.findPartnerByName(SecurityUtils.getCurrentUserLogin());

        return LejiaResult.ok(partner);
    }


}
