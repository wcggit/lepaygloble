package com.jifenke.lepluslive.partner.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.partner.domain.criteria.PartnerWalletLogCriteria;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerWallet;
import com.jifenke.lepluslive.partner.domain.entities.PartnerWalletOnline;
import com.jifenke.lepluslive.partner.service.*;
import com.jifenke.lepluslive.security.SecurityUtils;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xf on 17-11-15.
 */
@RestController
@RequestMapping("/api")
public class PartnerWalletController {
    @Inject
    private PartnerWalletService partnerWalletService;

    @Inject
    private PartnerWalletOnlineService partnerWalletOnlineService;

    @Inject
    private PartnerWalletOnlineLogService partnerWalletOnlineLogService;

    @Inject
    private PartnerWalletLogService partnerWalletLogService;

    @Inject
    private PartnerService partnerService;

    /**
     * 佣金明细 - 数据
     * @return
     */
    @RequestMapping(value = "/partner/commission/data", method = RequestMethod.GET)
    @ResponseBody
    public LejiaResult partnerCommission() {
        Partner
            partner =
            partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin());
        if(partner==null) {
            return LejiaResult.build(400,"登录状态异常");
        }
        PartnerWallet offWallet = partnerWalletService.findByPartner(partner);
        PartnerWalletOnline onWallet = partnerWalletOnlineService.findByPartner(partner);
        Long totalMoney = 0L;
        Long totalAvail = 0L;
        if(offWallet!=null&&offWallet.getTotalMoney()!=null) {
            totalMoney+=onWallet.getTotalMoney();
        }
        if(onWallet!=null&&onWallet.getTotalMoney()!=null) {
            totalMoney+=onWallet.getTotalMoney();
        }
        if(offWallet!=null&&offWallet.getAvailableBalance()!=null) {
            totalAvail+=offWallet.getAvailableBalance();
        }
        if(onWallet!=null&&onWallet.getAvailableBalance()!=null) {
            totalAvail+=onWallet.getAvailableBalance();
        }
        Long onExpend = partnerWalletOnlineLogService.countDailyExpendCommissionByPartner(partner.getId());
        Long onIncome = partnerWalletOnlineLogService.countDailyIncomeCommissionByPartner(partner.getId());
        Long offExpend = partnerWalletLogService.countDailyExpendCommissionByPartner(partner.getId());
        Long offIncome = partnerWalletLogService.countDailyIncomeCommissionByPartner(partner.getId());
        Map map = new HashMap<>();
        map.put("totalMoney",totalMoney);
        map.put("totalAvail",totalAvail);
        map.put("todayIncome",onIncome+offIncome);
        map.put("todayExpend",onExpend+offExpend);
        return LejiaResult.ok(map);
    }

    /**
     * 佣金明细 - 线上佣金记录
     * @return
     */
    @RequestMapping(value = "/partner/onlineCommission/findByPage", method = RequestMethod.POST)
    @ResponseBody
    public LejiaResult partnerOnlineCommission(@RequestBody PartnerWalletLogCriteria criteria) {
        Partner
            partner =
            partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin());
        if(partner==null) {
            return LejiaResult.build(400,"登录状态异常");
        }else {
            criteria.setPartnerId(partner.getId());
        }
        if(criteria.getOffset()==null) {
            criteria.setOffset(1);
        }
        Map<String, Object> map = partnerWalletOnlineLogService.listByCriteria(criteria);
        return LejiaResult.ok(map);
    }
    /**
     * 佣金明细 - 线下佣金记录
     * @return
     */
    @RequestMapping(value = "/partner/offlineCommission/findByPage", method = RequestMethod.POST)
    @ResponseBody
    public LejiaResult partnerOfflineCommission(@RequestBody PartnerWalletLogCriteria criteria) {
        Partner
            partner =
            partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin());
        if(partner==null) {
            return LejiaResult.build(400,"登录状态异常");
        }else {
            criteria.setPartnerId(partner.getId());
        }
        if(criteria.getOffset()==null) {
            criteria.setOffset(1);
        }
        Map<String, Object> map = partnerWalletLogService.listByCriteria(criteria);
        return LejiaResult.ok(map);
    }
}
