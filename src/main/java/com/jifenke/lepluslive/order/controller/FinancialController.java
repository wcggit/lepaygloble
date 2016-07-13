package com.jifenke.lepluslive.order.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.order.controller.view.FinancialViewExcel;
import com.jifenke.lepluslive.order.domain.criteria.FinancialCriteria;
import com.jifenke.lepluslive.order.domain.entities.FinancialStatistic;
import com.jifenke.lepluslive.order.service.FinanicalStatisticService;
import com.jifenke.lepluslive.order.service.OffLineOrderService;
import com.jifenke.lepluslive.security.SecurityUtils;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by wcg on 16/6/17.
 */
@RestController
@RequestMapping("/api")
public class FinancialController {

    @Inject
    private MerchantService merchantService;

    @Inject
    private OffLineOrderService offLineOrderService;

    @Inject
    private FinancialViewExcel financialViewExcel;

    @Inject
    protected FinanicalStatisticService finanicalStatisticService;


    @RequestMapping(value = "/financial", method = RequestMethod.POST)
    public
    @ResponseBody
    LejiaResult searchFinancialBycriterial(@RequestBody FinancialCriteria financialCriteria) {
        if (financialCriteria.getOffset() == null) {
            financialCriteria.setOffset(1);
        }
        MerchantUser
            merchantUserByName =
            merchantService
                .findMerchantUserByName(SecurityUtils.getCurrentUserLogin());
        financialCriteria.setMerchant(merchantUserByName.getMerchant());
        Page page = offLineOrderService.findFinancialByCirterial(financialCriteria, 10);
        return LejiaResult.ok(page);
    }

    @RequestMapping(value = "/financial/export", method = RequestMethod.GET)
    public ModelAndView exporeExcel(@RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate) {
        FinancialCriteria financialCriteria = new FinancialCriteria();
        financialCriteria.setStartDate(startDate);
        financialCriteria.setEndDate(endDate);
        if (financialCriteria.getOffset() == null) {
            financialCriteria.setOffset(1);
        }
        MerchantUser
            merchantUserByName =
            merchantService
                .findMerchantUserByName(SecurityUtils.getCurrentUserLogin());
        financialCriteria.setMerchant(merchantUserByName.getMerchant());
        Page page = offLineOrderService.findFinancialByCirterial(financialCriteria, 10000);
        Map map = new HashMap();
        map.put("financialList", page.getContent());
        return new ModelAndView(financialViewExcel, map);
    }

    @RequestMapping(value = "/financial/dayTrade", method = RequestMethod.GET)
    public LejiaResult dayTrade(@RequestParam(required = false) String startDate,
                                @RequestParam(required = false) String endDate) {
            Date start;
            Date end;
        if (startDate==null||"".equals(startDate)) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            end = calendar.getTime();
            calendar.add(Calendar.DAY_OF_MONTH, -3);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            start = calendar.getTime();
        } else {
            Date date = new Date(startDate);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            start = calendar.getTime();
            Date date2 = new Date(endDate);
            calendar.setTime(date2);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            end = calendar.getTime();
        }
        MerchantUser
            merchantUserByName =
            merchantService
                .findMerchantUserByName(SecurityUtils.getCurrentUserLogin());
        List<FinancialStatistic>
            financialStatistics =
            finanicalStatisticService
                .findByMerchantAndBalanceDate(merchantUserByName.getMerchant(), start, end);
        return LejiaResult.ok(financialStatistics);
    }


}
