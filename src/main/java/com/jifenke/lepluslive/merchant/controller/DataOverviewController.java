package com.jifenke.lepluslive.merchant.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.merchant.domain.criteria.DataOverviewCriteria;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantWallet;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantWalletOnline;
import com.jifenke.lepluslive.merchant.repository.MerchantWalletRepository;
import com.jifenke.lepluslive.merchant.service.DataOverviewService;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.merchant.service.MerchantWalletOnlineService;
import com.jifenke.lepluslive.merchant.service.MerchantWalletService;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 数据概览
 * Created by wanjun on 2016/12/15.
 */
@RestController
@RequestMapping(value = "/api")
public class DataOverviewController {


    @Inject
    private DataOverviewService dataOverviewService;

    @Inject
    private MerchantService merchantService;

    @Inject
    private MerchantWalletOnlineService merchantWalletOnlineService;

    @Inject
    private MerchantWalletService merchantWalletService;

    @RequestMapping(value = "/dataOverview/findCommissionAndLockNumber", method = RequestMethod.GET)
    @ResponseBody
    public LejiaResult findCommissionAndLockNumber() {
        DataOverviewCriteria doc = dataOverviewService.findCommissionAndLockNumber();
        return LejiaResult.ok(doc);
    }

    @RequestMapping(value = "/dataOverview/findPageMerchantMemberLockNumber", method = RequestMethod.POST)
    public LejiaResult findPageMerchantMemberLockNumber(@RequestBody DataOverviewCriteria dataOverviewCriteria) {
        List<Object[]> list = dataOverviewService.findPageMerchantMemberLockNumber(dataOverviewCriteria);
        List<Long> totalCommissions = new ArrayList<>();
        List<Long> availableCommissions = new ArrayList<>();
        List<Object> merchantIds = dataOverviewCriteria.getMerchantIds();
        for (Object merchantId : merchantIds) {
            Long mid = new Long(merchantId.toString());
            MerchantWallet offWallet = merchantWalletService.findByMerchant(mid);
            MerchantWalletOnline onlineWallet = merchantWalletOnlineService.findByMerchant(mid);
            Long totalCommission = 0L;
            Long availableCommission = 0L;
            if(offWallet!=null) {
                totalCommission = offWallet.getTotalMoney() + onlineWallet.getTotalMoney();
                availableCommission = offWallet.getAvailableBalance()+onlineWallet.getAvailableBalance();
            }
            if(onlineWallet!=null) {
                totalCommission = offWallet.getTotalMoney();
                availableCommission = offWallet.getAvailableBalance();
            }
            totalCommissions.add(totalCommission);
            availableCommissions.add(availableCommission);
        }
        dataOverviewCriteria.setMerchants(list);
        Collections.reverse(availableCommissions);
        Collections.reverse(totalCommissions);
        dataOverviewCriteria.setAvailableCommissions(availableCommissions);
        dataOverviewCriteria.setTotalCommissions(totalCommissions);
        return LejiaResult.ok(dataOverviewCriteria);
    }

}
