package com.jifenke.lepluslive.order.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantWallet;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.order.controller.view.OrderViewExcel;
import com.jifenke.lepluslive.order.domain.criteria.OLOrderCriteria;
import com.jifenke.lepluslive.order.domain.criteria.OrderShareCriteria;
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
import java.util.HashMap;
import java.util.Map;

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

    @Inject
    private LeJiaUserService leJiaUserService;

    @Inject
    private OrderViewExcel orderViewExcel;


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

    @RequestMapping(value = "/offLineOrder/statistic", method = RequestMethod.POST)
    public
    @ResponseBody
    LejiaResult
    getOrderStatistic(@RequestBody OLOrderCriteria olOrderCriteria) {

        Merchant
            merchant =
            merchantService.findMerchantUserByName(SecurityUtils.getCurrentUserLogin())
                .getMerchant();
        olOrderCriteria.setMerchant(merchant);

        return LejiaResult.ok(offLineOrderService.orderStatistic(olOrderCriteria));
    }

    @RequestMapping(value = "/offLineOrder/dayCommission", method = RequestMethod.GET)
    public
    @ResponseBody
    LejiaResult
    getDayCommissionInfo() {

        Merchant
            merchant =
            merchantService.findMerchantUserByName(SecurityUtils.getCurrentUserLogin())
                .getMerchant();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date start = calendar.getTime();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date end = calendar.getTime();

        Map map = offLineOrderService.countDayCommission(merchant, start, end);

        Long shareCount = offLineOrderService.countShareByMerchant(merchant);

        map.put("shareCount",shareCount);

        return LejiaResult.ok(map);
    }

    @RequestMapping(value = "/offLineOrder/commission", method = RequestMethod.GET)
    public
    @ResponseBody
    LejiaResult
    getCommissionInfo() {

        Merchant
            merchant =
            merchantService.findMerchantUserByName(SecurityUtils.getCurrentUserLogin())
                .getMerchant();
        MerchantWallet
            merchantWallet =
            merchantService.findMerchantWalletByMerchant(merchant);



        Long currentBind =  leJiaUserService.countBindMerchant(merchant);

        Map map = new HashMap<>();

        map.put("available",merchantWallet.getAvailableBalance());
        map.put("totalCommission",merchantWallet.getTotalMoney());
        map.put("userLimit",merchant.getUserLimit());
        map.put("currentBind",currentBind);

        return LejiaResult.ok(map);
    }



    @RequestMapping(value = "/offLineOrder/share", method = RequestMethod.POST)
    public
    @ResponseBody
    LejiaResult searchOrderShareListBycriterial(@RequestBody OrderShareCriteria olOrderCriteria) {
        if (olOrderCriteria.getOffset() == null) {
            olOrderCriteria.setOffset(1);
        }
        MerchantUser
            merchantUserByName =
            merchantService
                .findMerchantUserByName(SecurityUtils.getCurrentUserLogin());
        olOrderCriteria.setMerchant(merchantUserByName.getMerchant());
        Page page = offLineOrderService.findOrderShareByPage(olOrderCriteria, 10);
        return LejiaResult.ok(page);
    }

    @RequestMapping(value = "/offLineOrder/share/statistic", method = RequestMethod.POST)
    public
    @ResponseBody
    LejiaResult statisticOrderShare(@RequestBody OrderShareCriteria olOrderCriteria) {
        MerchantUser
            merchantUserByName =
            merchantService
                .findMerchantUserByName(SecurityUtils.getCurrentUserLogin());
        olOrderCriteria.setMerchant(merchantUserByName.getMerchant());
        return LejiaResult.ok(offLineOrderService.orderShareStatistic(olOrderCriteria));
    }

    @RequestMapping(value = "/offLineOrder/export", method = RequestMethod.GET)
    public
    @ResponseBody
    ModelAndView exportExcel(@RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate,@RequestParam(required = false) String orderSid) {
        OLOrderCriteria olOrderCriteria = new OLOrderCriteria();
        olOrderCriteria.setStartDate(startDate);
        olOrderCriteria.setEndDate(endDate);
        olOrderCriteria.setOrderSid(orderSid);
        olOrderCriteria.setState(1);
        if (olOrderCriteria.getOffset() == null) {
            olOrderCriteria.setOffset(1);
        }
        MerchantUser
            merchantUserByName =
            merchantService
                .findMerchantUserByName(SecurityUtils.getCurrentUserLogin());
        olOrderCriteria.setMerchant(merchantUserByName.getMerchant());
        Page page = offLineOrderService.findOrderByPage(olOrderCriteria,10000);
        Map map = new HashMap();
        map.put("orderList", page.getContent());
        return new ModelAndView(orderViewExcel, map);
    }

}
