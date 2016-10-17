package com.jifenke.lepluslive.partner.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;
import com.jifenke.lepluslive.partner.domain.criteria.PartnerScoreLogCriteria;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerWallet;
import com.jifenke.lepluslive.partner.service.PartnerScoreLogService;
import com.jifenke.lepluslive.partner.service.PartnerService;
import com.jifenke.lepluslive.security.SecurityUtils;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;


/**
 * Created by xf on 2016/10/12.
 */
@RestController
@RequestMapping("/api")
public class PartnerScoreLogController {

    @Inject
    private PartnerScoreLogService scoreLogService;
    @Inject
    private LeJiaUserService leJiaUserService;
    @Inject
    private PartnerService partnerService;

    @RequestMapping(value="/partner/scorelog/findByCriteria",method = RequestMethod.POST)
    @ResponseBody
    public LejiaResult findScoreLogByCriteria(
        @RequestBody PartnerScoreLogCriteria scoreLogCriteria) {
        Partner partner = partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin());
        if (partner.getId() == null) {
            return LejiaResult.build(400, "安全认证失败 , 请登陆 !");
        }
        scoreLogCriteria.setPartnerId(partner.getId());
        if (scoreLogCriteria.getOffset() == null) {
            scoreLogCriteria.setOffset(1);
        }
        Page page = scoreLogService.findPageScoreByCriteria(scoreLogCriteria, 10);
        return LejiaResult.ok(page);
    }


    @RequestMapping(value="/partner/scorelog/issuedScore",method = RequestMethod.GET)
    @ResponseBody
    public Map findIssuedScore() {
        Map result = new HashMap();
        Partner partner = partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin());
        PartnerWallet partnerWallet = partnerService.findPartnerWalletByPartner(partner);
        result.put("totalScorea", partnerWallet.getTotalScoreA() / 100.0);
        result.put("totalScoreb", partnerWallet.getTotalScoreB());
        return result;
    }
}
