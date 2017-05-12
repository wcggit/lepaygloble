package com.jifenke.lepluslive.partner.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.lejiauser.domain.criteria.LeJiaUserCriteria;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.partner.controller.dto.PartnerDto;
import com.jifenke.lepluslive.partner.controller.dto.PartnerManagerDto;
import com.jifenke.lepluslive.partner.domain.criteria.PartnerCriteria;
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
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        PartnerManager partnerManager = partnerManagerService.findByPartnerManagerSid(SecurityUtils.getCurrentUserLogin());
        return LejiaResult.ok(partnerManager);
    }

    /***
     *  城市合伙人 - 首页信息
     */
    @RequestMapping(value = "/partnerManager/wallet", method = RequestMethod.GET)
    @ResponseBody
    public LejiaResult getPartnerWalletInfo(Model model) {
        PartnerManager partnerManager = partnerManagerService.findByPartnerManagerSid(SecurityUtils.getCurrentUserLogin());
        PartnerManagerWallet partnerManagerWallet = partnerManagerService.findWalletByPartnerManager(partnerManager);
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
        PartnerManager partnerManager = partnerManagerService.findByPartnerManagerSid(SecurityUtils.getCurrentUserLogin());
        Long dailyCommission = partnerManagerService.findDailyCommissionByPartnerManager(partnerManager.getPartnerId());
        return LejiaResult.ok(dailyCommission);
    }

    /**
     * 城市合伙人 - 会员、门店、合伙人  数量/上限
     */
    @RequestMapping(value = "/partnerManager/members", method = RequestMethod.GET)
    @ResponseBody
    public LejiaResult getPartnerManagerNumbers(Model model) {
        PartnerManager partnerManager = partnerManagerService.findByPartnerManagerSid(SecurityUtils.getCurrentUserLogin());
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
        PartnerManager partnerManager = partnerManagerService.findByPartnerManagerSid(SecurityUtils.getCurrentUserLogin());
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
    public LejiaResult findPartnersByCriteria(@RequestBody PartnerCriteria partnerCriteria) {
        PartnerManager partnerManager = partnerManagerService.findByPartnerManagerSid(SecurityUtils.getCurrentUserLogin());
        partnerCriteria.setPartnerManager(partnerManager);
        if (partnerCriteria.getOffset() == null) {
            partnerCriteria.setOffset(1);
        }
        List<PartnerDto> dtoList = Collections.synchronizedList(new ArrayList<PartnerDto>());
        Page page = partnerService.findPartnerByCriteria(partnerCriteria, 10);
        List<Partner> content = page.getContent();
        ExecutorService executor =  Executors.newFixedThreadPool(content.size());
        for (Partner partner : content) {
            executor.execute(new Thread(() -> {
                PartnerDto dto = new PartnerDto();
                dto.setPartner(partner);
                Long merchantNum = merchantService.countPartnerBindMerchant(partner);
                Long userNum = leJiaUserService.countPartnerBindLeJiaUser(partner);
                PartnerWallet wallet = partnerWalletService.findByPartner(partner);
                dto.setBindMerchantNum(merchantNum == null ? 0L : merchantNum);
                dto.setBindUserNum(userNum == null ? 0L : userNum);
                dto.setOnLineCommission(wallet == null ? 0L : wallet.getAvailableBalance());
                dto.setOffLineCommission(wallet == null ? 0L : wallet.getTotalMoney());
                dtoList.add(dto);
            }));
        }
        Map map = new HashMap<>();
        map.put("content", dtoList);
        map.put("totalPages", page.getTotalPages());
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        return LejiaResult.ok(map);
    }

    @RequestMapping(value = "/partnerManager/partner/pages", method = RequestMethod.GET)
    @ResponseBody
    public LejiaResult getPartnerPages() {
        PartnerManager partnerManager = partnerManagerService.findByPartnerManagerSid(SecurityUtils.getCurrentUserLogin());
        Long count = partnerService.countPartnerByManager(partnerManager);
        Long pages = Math.round(new Double(count) / 10.0);
        return LejiaResult.ok(pages);
    }


    /**
     * 合伙人 - 折线图
     *
     * @return
     */
    @RequestMapping(value = "/partnerManager/chart/week", method = RequestMethod.POST)
    @ResponseBody
    public LejiaResult findManagerWeekNumberChart(@RequestBody PartnerManagerCriteria managerCriteria) {
        PartnerManager partnerManager = partnerManagerService.findByPartnerManagerSid(SecurityUtils.getCurrentUserLogin());
        managerCriteria.setPartnerManager(partnerManager);
        Map map = partnerManagerService.findManagerWeekNumberChartData(managerCriteria);
        return LejiaResult.ok(map);
    }

    /**
     * 指定合伙人 - 信息
     */
    @RequestMapping(value = "/partnerManager/chart/partner", method = RequestMethod.POST)
    @ResponseBody
    public LejiaResult findPartnerChartData(@RequestBody PartnerManagerCriteria partnerManagerCriteria) {
        Partner partner =
            partnerService.findByPartnerSid(partnerManagerCriteria.getPartner().getPartnerSid());
        Map map = partnerManagerService.findPartnerData(partnerManagerCriteria, partner);
        PartnerWallet partnerWallet = partnerWalletService.findByPartner(partner);
        map.put("partnerWallet", partnerWallet);
        Long bindMerchants = merchantService.countPartnerBindMerchant(partner);
        Long bindUsers = leJiaUserService.countPartnerBindLeJiaUser(partner);
        Long dayCommission = partnerService.countPartnerDayCommission(partner);
        map.put("bindMerchants", bindMerchants);
        map.put("bindUsers", bindUsers);
        map.put("dayCommission", dayCommission);
        map.put("userLimit", partner.getUserLimit());
        map.put("merchantLimit", partner.getMerchantLimit());
        return LejiaResult.ok(map);
    }


}
