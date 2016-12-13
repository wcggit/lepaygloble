package com.jifenke.lepluslive.order.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.websocket.dto.ActivityDTO;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantWallet;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.merchant.service.MerchantUserResourceService;
import com.jifenke.lepluslive.order.controller.view.LejiaOrderDTO;
import com.jifenke.lepluslive.order.controller.view.OrderViewExcel;
import com.jifenke.lepluslive.order.domain.criteria.DailyOrderCriteria;
import com.jifenke.lepluslive.order.domain.criteria.OLOrderCriteria;
import com.jifenke.lepluslive.order.domain.criteria.OrderShareCriteria;
import com.jifenke.lepluslive.order.service.LejiaOrderService;
import com.jifenke.lepluslive.order.service.OffLineOrderService;
import com.jifenke.lepluslive.order.service.PosOrderSerivce;
import com.jifenke.lepluslive.security.SecurityUtils;

import org.springframework.data.domain.Page;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

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

    @Inject
    SimpMessageSendingOperations messagingTemplate;

    @Inject
    private MerchantUserResourceService merchantUserResourceService;

    @Inject
    private PosOrderSerivce posOrderSerivce;

    @Inject
    private LejiaOrderService lejiaOrderService;

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

        map.put("shareCount", shareCount);

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

        Long currentBind = leJiaUserService.countBindMerchant(merchant);

        Map map = new HashMap<>();

        map.put("available", merchantWallet.getAvailableBalance());
        map.put("totalCommission", merchantWallet.getTotalMoney());
        map.put("userLimit", merchant.getUserLimit());
        map.put("currentBind", currentBind);

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
    public ModelAndView exportExcel(@RequestParam(required = false) String startDate,
                                    @RequestParam(required = false) String endDate,
                                    @RequestParam(required = false) String orderSid,
                                    @RequestParam(required = false) Integer rebateWay) {
        OLOrderCriteria olOrderCriteria = new OLOrderCriteria();
        olOrderCriteria.setStartDate(startDate);
        olOrderCriteria.setEndDate(endDate);
        olOrderCriteria.setOrderSid(orderSid);
        olOrderCriteria.setRebateWay(rebateWay);
        olOrderCriteria.setState(1);
        if (olOrderCriteria.getOffset() == null) {
            olOrderCriteria.setOffset(1);
        }
        MerchantUser
            merchantUserByName =
            merchantService
                .findMerchantUserByName(SecurityUtils.getCurrentUserLogin());
        olOrderCriteria.setMerchant(merchantUserByName.getMerchant());
        Page page = offLineOrderService.findOrderByPage(olOrderCriteria, 10000);
        Map map = new HashMap();
        map.put("orderList", page.getContent());
        return new ModelAndView(orderViewExcel, map);
    }

    @RequestMapping(value = "/offLineOrder/message/{id}", method = RequestMethod.GET)
    public void sendOffLineOrderToMerchant(@PathVariable String id) {
        ActivityDTO activityDTO = new ActivityDTO();
        activityDTO.setPage("logout");
        messagingTemplate.convertAndSendToUser("wcg", "/reply", activityDTO);
    }

    /**
     * 每日订单数据 (所有门店)
     */
    @RequestMapping(value = "/order/dailyOrder", method = RequestMethod.GET)
    public LejiaResult getDailyOrderData() {
        MerchantUser merchantUser = merchantService.findMerchantUserByName(SecurityUtils.getCurrentUserLogin());
        List<Merchant> merchants = merchantUserResourceService.findMerchantsByMerchantUser(merchantUser);
        Long offLineDailyCount = offLineOrderService.countOffLineOrder(merchants);          //  线下订单
        Long posDailyCount = posOrderSerivce.countPosOrder(merchants);                      //  pos 订单
        Map<String, Long> map = offLineOrderService.countMemberDailyOrdersDetail(merchants);     //  会员消费
        map.put("offLineDailyCount", offLineDailyCount);
        map.put("posDailyCount", posDailyCount);
        return LejiaResult.ok(map);
    }

    /**
     * 每日订单数据 (指定门店)
     */
    @RequestMapping(value = "/order/dailyOrder/merchant/{id}", method = RequestMethod.GET)
    public LejiaResult getDailyOrderData(@PathVariable Long id) {
        Merchant merchant = merchantService.findMerchantById(id);
        List<Merchant> merchants = new ArrayList<>();
        merchants.add(merchant);
        Long offLineDailyCount = offLineOrderService.countOffLineOrder(merchants);          //  线下订单
        Long posDailyCount = posOrderSerivce.countPosOrder(merchants);                      //  pos 订单
        Map<String, Long> map = offLineOrderService.countMemberDailyOrdersDetail(merchants);     //  会员消费
        map.put("offLineDailyCount", offLineDailyCount == null ? 0L : offLineDailyCount);
        map.put("posDailyCount", posDailyCount == null ? 0L : posDailyCount);
        return LejiaResult.ok(map);
    }

    /**
     * 每日订单列表 (所有门店)
     */
    @RequestMapping(value = "/order/orderList/{offset}", method = RequestMethod.GET)
    public LejiaResult getOrderList(@PathVariable Long offset) {
        if(offset==null) {
            offset = 0L;
        }
        MerchantUser merchantUser = merchantService.findMerchantUserByName(SecurityUtils.getCurrentUserLogin());
        List<Merchant> merchants = merchantUserResourceService.findMerchantsByMerchantUser(merchantUser);
        List<Object[]> allOrderList = new ArrayList<>();
        for (Merchant merchant : merchants) {
            List<Object[]> orderList = merchantService.findOrderList(merchant,offset);
            for (Object[] objects : orderList) {
                objects[7] = merchant.getName();
            }
            allOrderList.addAll(orderList);
            // TO-DO   设置门店名称
        }
        return LejiaResult.ok(allOrderList);
    }

    /**
     * 每日订单列表 (指定门店)
     */
    @RequestMapping(value = "/order/orderList/merchant/{param}", method = RequestMethod.GET)
    public LejiaResult getOrderListByMerchant(@PathVariable String param) {
        String[] split = param.split("-");
        Long id = new Long(split[0]);
        Long offset = new Long(split[1]);
        if(offset==null) {
            offset = 0L;
        }
        List<Merchant> merchants = new ArrayList<>();
        Merchant merchant = merchantService.findMerchantById(id);
        merchants.add(merchant);
        List<Object[]> orderList = merchantService.findOrderList(merchant,offset);
        for (Object[] objects : orderList) {
            objects[7] = merchant.getName();
        }
        return LejiaResult.ok(orderList);
    }

    /**
     * 乐加账单 - 每日账单
     */
    @RequestMapping(value="/lejiaOrder/daily",method = RequestMethod.POST)
    public LejiaResult getDailyOrderDataByCriteria(@RequestBody DailyOrderCriteria dailyOrderCriteria) {
        LejiaOrderDTO orderDTO = lejiaOrderService.findDailyOrderByMerchant(dailyOrderCriteria);
        return LejiaResult.ok(orderDTO);
    }
}
