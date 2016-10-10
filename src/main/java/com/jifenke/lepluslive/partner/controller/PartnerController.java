package com.jifenke.lepluslive.partner.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.websocket.dto.ActivityDTO;
import com.jifenke.lepluslive.lejiauser.domain.criteria.LeJiaUserCriteria;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.partner.domain.criteria.MerchantCriteria;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerWallet;
import com.jifenke.lepluslive.partner.service.PartnerService;
import com.jifenke.lepluslive.security.SecurityUtils;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

    @Inject
    SimpMessageSendingOperations messagingTemplate;


    @RequestMapping(value = "/partner", method = RequestMethod.GET)
    public
    @ResponseBody
    LejiaResult getPartner() {
        return LejiaResult.ok(partnerService
                                  .findByPartnerSid(SecurityUtils.getCurrentUserLogin()));
    }

    @RequestMapping(value = "/partner/info", method = RequestMethod.GET)
    public
    @ResponseBody
    LejiaResult getPartnerInfo() {
        return LejiaResult.ok(partnerService
                                  .findPartnerInfoByPartnerSid(
                                      SecurityUtils.getCurrentUserLogin()));
    }


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
                .findByPartnerSid(SecurityUtils.getCurrentUserLogin());
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
                .findByPartnerSid(SecurityUtils.getCurrentUserLogin());
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
            partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin());
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
        result.put("availableScoreA", partnerWallet.getAvailableScoreA() / 100.0);
        result.put("availableScoreB", partnerWallet.getAvailableScoreB());
        if (bindLeJiaUser == 0) {
            result.put("perCommission", 0);
        } else {

            result.put("perCommission", new BigDecimal(partnerWallet.getTotalMoney() / 100.0)
                .divide(new BigDecimal(bindLeJiaUser), 2, BigDecimal.ROUND_HALF_UP).doubleValue());
        }
        return result;
    }

    @RequestMapping(value = "/partner/merchant_top5", method = RequestMethod.GET)
    public
    @ResponseBody
    LejiaResult getMerchantTop5(Integer sortType) {
        Partner
            partner =
            partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin());

        return LejiaResult.ok(partnerService.findMerchantTop5(sortType, partner));
    }


    @RequestMapping(value = "/partner/merchant_list", method = RequestMethod.POST)
    public
    @ResponseBody
    LejiaResult getPartnerBindMerchantList(@RequestBody MerchantCriteria merchantCriteria) {
        merchantCriteria
            .setPartner(partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin()));
        return LejiaResult.ok(partnerService.getMerchantList(merchantCriteria));
    }

    @RequestMapping(value = "/partner/merchant_list_page", method = RequestMethod.POST)
    public
    @ResponseBody
    LejiaResult getPartnerBindMerchantListPage(@RequestBody MerchantCriteria merchantCriteria) {
        merchantCriteria
            .setPartner(partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin()));
        return LejiaResult.ok(partnerService.getMerchantListPage(merchantCriteria));
    }

    @RequestMapping(value = "/partner/merchant_list_count", method = RequestMethod.POST)
    public
    @ResponseBody
    LejiaResult getPartnerBindMerchantListCount(@RequestBody MerchantCriteria merchantCriteria) {
        merchantCriteria
            .setPartner(partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin()));
        return LejiaResult.ok(partnerService.getMerchantListCount(merchantCriteria));
    }

    @RequestMapping(value = "/partner/count_full_merchant", method = RequestMethod.GET)
    public
    @ResponseBody
    LejiaResult countFullBindMerchant() {
        return LejiaResult.ok(partnerService.countPartnerBindFullMerchant(
            partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin())));
    }

    @RequestMapping(value = "/partner/merchant", method = RequestMethod.GET)
    public
    @ResponseBody
    LejiaResult getMerchantBySid(@RequestParam String sid) {
        Merchant merchant = merchantService.findmerchantBySid(sid);
        Partner
            partner =
            partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin());
        if (merchant.getPartner().getId().toString().equals(partner.getId().toString())) {
            return LejiaResult.ok(merchant);
        }
        return LejiaResult.build(401, "无权限");
    }

    @RequestMapping(value = "/partner/merchant_user", method = RequestMethod.GET)
    public
    @ResponseBody
    LejiaResult getMerchantUserBySid(@RequestParam String sid) {
        Merchant merchant = merchantService.findmerchantBySid(sid);
        Partner
            partner =
            partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin());
        if (merchant.getPartner().getId().toString().equals(partner.getId().toString())) {
            return LejiaResult.ok(merchantService.findMerchantUserByMerchant(merchant));
        }
        return LejiaResult.build(401, "无权限");
    }

    @RequestMapping(value = "/partner/resetPassword", method = RequestMethod.POST)
    public LejiaResult resetPassword(HttpServletRequest request) {
        String reset = request.getParameter("reset");
        String password = request.getParameter("password");
        Partner
            partner =
            partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin());
        try {
            partnerService.resetPassword(partner, reset, password);
            return LejiaResult.ok();
        } catch (Exception e) {
            return LejiaResult.build(400, "密码不正确");
        }
    }

    @RequestMapping(value = "/partner/create_merchant_user", method = RequestMethod.POST)
    public LejiaResult createMerchantUser(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String sid = request.getParameter("sid");
        Merchant merchant = merchantService.findmerchantBySid(sid);
        merchantService.createMerchantUser(merchant, username, password);
        return LejiaResult.ok("创建成功");
    }

    @RequestMapping(value = "/partner/delete_merchant_user", method = RequestMethod.POST)
    public LejiaResult deleteMerchantUser(HttpServletRequest request) {
        String param = request.getParameter("param");
        Partner
            partner =
            partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin());
        try {
            merchantService.deleteMerchantUser(Long.parseLong(param), partner);
            return LejiaResult.ok("删除成功");
        } catch (Exception e) {
            return LejiaResult.build(400, "无权限");
        }
    }

    @RequestMapping(value = "/partner/exceed_limit", method = RequestMethod.GET)
    public LejiaResult checkPartnerBindMerchantLimit() {
        Partner
            partner =
            partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin());
        return LejiaResult.ok(partnerService.checkPartnerBindMerchantLimit(partner)
        );
    }

    @RequestMapping(value = "/partner/bind_wx_user/{sid}", method = RequestMethod.GET)
    public void partner(@PathVariable String sid) {
        ActivityDTO activityDTO = new ActivityDTO();
        messagingTemplate.convertAndSendToUser(sid, "/reply", activityDTO);
    }
}
