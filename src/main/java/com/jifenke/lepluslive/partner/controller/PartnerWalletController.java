package com.jifenke.lepluslive.partner.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerWallet;
import com.jifenke.lepluslive.partner.domain.entities.PartnerWalletOnline;
import com.jifenke.lepluslive.partner.service.*;
import com.jifenke.lepluslive.security.SecurityUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "/partner/commission/data", method = RequestMethod.GET)
    public LejiaResult partnerCommission() {
        Partner
            partner =
            partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin());
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
}
