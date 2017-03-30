package com.jifenke.lepluslive.merchant.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.merchant.service.MerchantWalletOnlineService;
import com.jifenke.lepluslive.security.SecurityUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.*;

/**
 * Created by lss on 16-11-28.
 */
@RestController
@RequestMapping("/api")
public class MerchantWalletOnlineController {
    @Inject
    private MerchantWalletOnlineService merchantWalletOnlineService;

    @Inject
    private MerchantService merchantService;

    @RequestMapping(value = "/merchant/getOnLineCommissionIncome", method = RequestMethod.GET)
    public LejiaResult getOnLineCommissionIncome() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date start = calendar.getTime();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date end = calendar.getTime();
        Merchant
            merchant =
            merchantService.findMerchantUserBySid(SecurityUtils.getCurrentUserLogin())
                .getMerchant();
        Long totalMoney = merchantWalletOnlineService.findTotalMoney(merchant.getId());
        List<Object[]> objs = merchantWalletOnlineService.findOneDayLogCountAndSum(merchant.getId(), start, end);
        Integer totalCount = merchantWalletOnlineService.findTotalCount(merchant.getId());//累计佣金总单数
        Integer oneDayCount = Integer.valueOf(objs.get(0)[0].toString());//当天佣金单数
        Long oneDaySum = 0L;
        if (objs.get(0)[1] != null) {
            oneDaySum = Long.valueOf(objs.get(0)[1].toString());
        }
        double dOneDaySum = oneDaySum.doubleValue() / 100.0;//当日佣金收入
        double dTotalMoney = totalMoney.doubleValue() / 100.0;
        double dTotalCount = totalCount.doubleValue();
        double onLinePer = Math.round((dTotalMoney / dTotalCount) * 100) / 100.0;//人均佣金收入
        Map map = new HashMap<>();

        map.put("dOneDaySum",dOneDaySum);
        map.put("onLinePer", onLinePer);
        map.put("oneDayCount",oneDayCount);
        map.put("totalCount", totalCount);
        return LejiaResult.ok(map);
    }
}
