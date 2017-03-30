package com.jifenke.lepluslive.partner.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.lejiauser.domain.criteria.LeJiaUserCriteria;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.partner.controller.dto.PartnerDto;
import com.jifenke.lepluslive.partner.controller.dto.PartnerManagerDto;
import com.jifenke.lepluslive.partner.domain.criteria.PartnerManagerCriteria;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerManager;
import com.jifenke.lepluslive.partner.domain.entities.PartnerManagerWallet;
import com.jifenke.lepluslive.partner.domain.entities.PartnerWallet;
import com.jifenke.lepluslive.partner.service.PartnerManagerService;
import com.jifenke.lepluslive.partner.service.PartnerService;
import com.jifenke.lepluslive.partner.service.PartnerWalletService;
import com.jifenke.lepluslive.security.SecurityUtils;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by xf on 17-3-17.
 */
@Controller
@RequestMapping("/api")
public class PartnerManagerController {
    @Inject
    private PartnerManagerService partnerManagerService;
    @Inject
    private PartnerService partnerService;
    @Inject
    private LeJiaUserService leJiaUserService;
    @Inject
    private MerchantService merchantService;
    @Inject
    private PartnerWalletService partnerWalletService;

    /**
     * 城市合伙人 - 基本信息
     */
    @RequestMapping(value = "/partnerManager/basic", method = RequestMethod.GET)
    @ResponseBody
    public LejiaResult getPartnerManagerInfo() {
        Partner
            partner =
            partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin());
        PartnerManager partnerManager = partnerManagerService.findByPartnerAccountId(partner.getId());
        return LejiaResult.ok(partnerManager);
    }

    /***
     *  城市合伙人 - 首页信息
     */
    @RequestMapping(value = "/partnerManager/wallet", method = RequestMethod.GET)
    @ResponseBody
    public LejiaResult getPartnerWalletInfo(Model model) {
        Partner
            partner =
            partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin());
        PartnerManager partnerManager = partnerManagerService.findByPartnerAccountId(partner.getId());
        PartnerManagerWallet partnerManagerWallet = partnerManagerService.findWalletByPartnerManager(partnerManager);
        model.addAttribute("partner", partner);
        model.addAttribute("partnerManager", partnerManager);
        model.addAttribute("partnerManagerWallet", partnerManagerWallet);
        return LejiaResult.ok(model);
    }

    /**
     * 城市合伙人 - 每日佣金
     */
    @RequestMapping(value = "/partnerManager/commission", method = RequestMethod.GET)
    @ResponseBody
    public LejiaResult getPartnerWalletCommission() {
        Partner
            partner =
            partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin());
        PartnerManager partnerManager = partnerManagerService.findByPartnerAccountId(partner.getId());
        Long dailyCommission = partnerManagerService.findDailyCommissionByPartnerManager(partnerManager.getPartnerId());
        return LejiaResult.ok(dailyCommission);
    }

    /**
     * 城市合伙人 - 会员、门店、合伙人  数量/上限
     */
    @RequestMapping(value = "/partnerManager/members", method = RequestMethod.GET)
    @ResponseBody
    public LejiaResult getPartnerManagerNumbers(Model model) {
        Partner
            partner =
            partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin());
        PartnerManager partnerManager = partnerManagerService.findByPartnerAccountId(partner.getId());
        List<Partner> partners = partnerService.findPartnerByManager(partnerManager);
        model.addAttribute("partnerLimit", partnerManager.getBindPartnerLimit());
        model.addAttribute("merchantLimit", partnerManager.getBindMerchantLimit());
        model.addAttribute("userLimit", partnerManager.getUserLimit());
        model.addAttribute("bindUserNumber", 0L);
        model.addAttribute("bindMerchantNumber", 0L);
        model.addAttribute("bindPartnerNumber", 0L);
        if (partners != null) {
            Long bindUserNumber = 0L;                // 绑定会员数
            Long bindMerchantNumber = 0L;            // 绑定门店数
            for (Partner partner1 : partners) {
                LeJiaUserCriteria leJiaUserCriteria = new LeJiaUserCriteria();
                Long userNum = leJiaUserService.countPartnerBindLeJiaUser(partner1);
                Long merchantNUm = merchantService.countPartnerBindMerchant(partner1);
                bindUserNumber += userNum;
                bindMerchantNumber += merchantNUm;
            }
            model.addAttribute("bindUserNumber", bindUserNumber);
            model.addAttribute("bindMerchantNumber", bindMerchantNumber);
            model.addAttribute("bindPartnerNumber", partners.size());
        }
        return LejiaResult.ok(model);
    }

    /**
     * 城市合伙人 -  按日期查询会员、门店、合伙人
     */
    @RequestMapping(value = "/partnerManager/members", method = RequestMethod.POST)
    @ResponseBody
    public LejiaResult getBindsByCriteria(@RequestBody PartnerManagerCriteria partnerManagerCriteria) {
        // 设置默认时间 （近一周）
        if (partnerManagerCriteria.getStartDate() == null || "".equals(partnerManagerCriteria.getStartDate())) {
            Date nowDate = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 7);
            Date aWeekBefore = calendar.getTime();
            partnerManagerCriteria.setStartDate(new SimpleDateFormat("yyyy/MM/dd").format(aWeekBefore));
            partnerManagerCriteria.setEndDate(new SimpleDateFormat("yyyy/MM/dd").format(nowDate));
        }
        Partner
            partner =
            partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin());
        PartnerManager partnerManager = partnerManagerService.findByPartnerAccountId(partner.getId());
        partnerManagerCriteria.setPartnerManager(partnerManager);
        if (partnerManagerCriteria.getType() != null && partnerManagerCriteria.getType() != 2) {
            List<Partner> partners = partnerService.findPartnerByManager(partnerManager);
            if (partners == null || partners.size() == 0) {
                return LejiaResult.ok();
            }
            partnerManagerCriteria.setPartners(partners);
        }
        List<PartnerManagerDto> list = partnerManagerService.getBindsByCriteria(partnerManagerCriteria);
        return LejiaResult.ok(list);
    }

    /**
     * 根据条件查询合伙人
     */
    @RequestMapping(value = "/partnerManager/partners", method = RequestMethod.POST)
    @ResponseBody
    public LejiaResult findPartnersByCriteria(@RequestBody PartnerManagerCriteria partnerManagerCriteria) {
        Partner
            loginPartner =
            partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin());
        PartnerManager partnerManager = partnerManagerService.findByPartnerAccountId(loginPartner.getId());
        partnerManagerCriteria.setPartnerManager(partnerManager);
        if (partnerManagerCriteria.getLimit() == null) {
            partnerManagerCriteria.setLimit(1);
        }
        List<PartnerDto> dtoList = new ArrayList<>();
        List result = partnerService.findPartnerByPageAndManager(partnerManagerCriteria);
        for (Object obj : result) {
            JSONArray json = JSONArray.fromObject(obj);
            PartnerDto dto = new PartnerDto();
            Partner partner = partnerService.findPartnerById(new Long(json.get(0).toString()));
            dto.setPartner(partner);
            Long merchantNum = merchantService.countPartnerBindMerchant(partner);
            Long userNum = leJiaUserService.countPartnerBindLeJiaUser(partner);
            PartnerWallet wallet = partnerWalletService.findByPartner(partner);
            dto.setOnLineCommission(wallet.getTotalScoreB());
            dto.setOffLineCommission(wallet.getTotalScoreA());
            dto.setBindMerchanNum(merchantNum);
            dto.setBindUserNum(userNum);
            dtoList.add(dto);

        }
        return LejiaResult.ok(dtoList);
    }

    @RequestMapping(value = "/partnerManager/partner/pages", method = RequestMethod.GET)
    @ResponseBody
    public LejiaResult getPartnerPages() {
        Partner
            partner =
            partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin());
        PartnerManager partnerManager = partnerManagerService.findByPartnerAccountId(partner.getId());
        Long count = partnerService.countPartnerByManager(partnerManager);
        Long pages = Math.round(new Double(count) / 10.0);
        return LejiaResult.ok(pages);
    }
}
