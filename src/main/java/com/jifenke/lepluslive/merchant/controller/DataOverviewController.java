package com.jifenke.lepluslive.merchant.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.merchant.domain.criteria.DataOverviewCriteria;
import com.jifenke.lepluslive.merchant.service.DataOverviewService;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
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

    @RequestMapping(value = "/dataOverview/findCommissionAndLockNumber", method = RequestMethod.GET)
    @ResponseBody
    public LejiaResult findCommissionAndLockNumber() {
        DataOverviewCriteria doc = dataOverviewService.findCommissionAndLockNumber();
        return LejiaResult.ok(doc);
    }

    @RequestMapping(value = "/dataOverview/findPageMerchantMemberLockNumber", method = RequestMethod.POST)
    public LejiaResult findPageMerchantMemberLockNumber(@RequestBody DataOverviewCriteria dataOverviewCriteria) {
        List<Object[]> list = dataOverviewService.findPageMerchantMemberLockNumber(dataOverviewCriteria);
        dataOverviewCriteria.setMerchants(list);
        return LejiaResult.ok(dataOverviewCriteria);
    }

}
