package com.jifenke.lepluslive.order.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.order.domain.criteria.OLOrderCriteria;
import com.jifenke.lepluslive.order.service.OffLineOrderService;
import com.jifenke.lepluslive.security.SecurityUtils;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

/**
 * Created by wcg on 16/6/29.
 */
@RestController
@RequestMapping("/api")
public class OrderController {


    @Inject
    private OffLineOrderService offLineOrderService;

    @Inject
    private MerchantService merchantService;


    @RequestMapping(value = "/order/todayOrderDetail", method = RequestMethod.GET)
    public LejiaResult getTodayOrderDetail() {
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
            merchantService.findMerchantUserByName(SecurityUtils.getCurrentUserLogin())
                .getMerchant();

        return LejiaResult.ok(offLineOrderService.countTodayOrderDetail(merchant, start, end));
    }

    @RequestMapping(value = "/order/monthOrderDetail", method = RequestMethod.GET)
    public LejiaResult getMonthOrderDetail() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date start = calendar.getTime();
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date end = calendar.getTime();

        Merchant
            merchant =
            merchantService.findMerchantUserByName(SecurityUtils.getCurrentUserLogin())
                .getMerchant();

        return LejiaResult.ok(offLineOrderService.countTodayOrderDetail(merchant, start, end));
    }

    @RequestMapping(value = "/offLineOrder", method = RequestMethod.POST)
    public
    @ResponseBody
    LejiaResult searchOrderListBycriterial(@RequestBody OLOrderCriteria olOrderCriteria) {
        if (olOrderCriteria.getOffset() == null) {
            olOrderCriteria.setOffset(1);
        }
        olOrderCriteria.setState(1);
        MerchantUser
            merchantUserByName =
            merchantService
                .findMerchantUserByName(SecurityUtils.getCurrentUserLogin());
        olOrderCriteria.setMerchant(merchantUserByName.getMerchant());
        Page page = offLineOrderService.findOrderByPage(olOrderCriteria, 10);
        return LejiaResult.ok(page);
    }

    @RequestMapping(value = "/order/orderDetail", method = RequestMethod.GET)
    public LejiaResult getOrderDetail() {

        Merchant
            merchant =
            merchantService.findMerchantUserByName(SecurityUtils.getCurrentUserLogin())
                .getMerchant();

        return LejiaResult.ok(offLineOrderService.countOrderDetail(merchant));
    }

}
