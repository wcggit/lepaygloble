package com.jifenke.lepluslive.order.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.websocket.dto.ActivityDTO;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;
import com.jifenke.lepluslive.merchant.domain.criteria.CodeOrderCriteria;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantScanPayWay;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantWallet;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.merchant.service.MerchantUserResourceService;
import com.jifenke.lepluslive.merchant.service.MerchantUserService;
import com.jifenke.lepluslive.order.controller.view.*;
import com.jifenke.lepluslive.order.domain.criteria.DailyOrderCriteria;
import com.jifenke.lepluslive.order.domain.criteria.OLOrderCriteria;
import com.jifenke.lepluslive.order.domain.criteria.OrderShareCriteria;
import com.jifenke.lepluslive.order.domain.entities.ScanCodeOrderCriteria;
import com.jifenke.lepluslive.order.service.*;
import com.jifenke.lepluslive.security.SecurityUtils;

import com.jifenke.lepluslive.withdraw.domain.entities.WithdrawBill;
import com.jifenke.lepluslive.withdraw.service.WithdrawService;
import org.springframework.data.domain.Page;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import javax.inject.Inject;
import javax.print.attribute.standard.MediaSize;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

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
    WithdrawService withdrawService;

    @Inject
    private MerchantUserResourceService merchantUserResourceService;

    @Inject
    private PosOrderSerivce posOrderSerivce;

    @Inject
    private LejiaOrderService lejiaOrderService;

    @Inject
    private MerchantOrderExcel merchantOrderExcel;

    @Inject
    private MerchantScanPayWayService merchantScanPayWayService;

    @Inject
    private ScanCodeOrderService scanCodeOrderService;

    @Inject
    private OffLineOrderExcel offLineOrderExcel;

    @Inject
    private ScanCodeOrderExcel scanCodeOrderExcel;

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
            merchantService.findMerchantUserBySid(SecurityUtils.getCurrentUserLogin())
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
            merchantService.findMerchantUserBySid(SecurityUtils.getCurrentUserLogin())
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
                .findMerchantUserBySid(SecurityUtils.getCurrentUserLogin());
        olOrderCriteria.setMerchant(merchantUserByName.getMerchant());
        Page page = offLineOrderService.findOrderByPage(olOrderCriteria, 10);
        return LejiaResult.ok(page);
    }

    /***
     *
     * @param olOrderCriteria
     * @return
     */
    @RequestMapping(value = "/offLineOrder/tradeDetail", method = RequestMethod.POST)
    public
    @ResponseBody
    LejiaResult searchTradeDetailByCriterial(@RequestBody OLOrderCriteria olOrderCriteria) {
        if (olOrderCriteria.getOffset() == null) {
            olOrderCriteria.setOffset(1);
        }
        Merchant merchant = olOrderCriteria.getMerchant();
        if (merchant == null) {
            return LejiaResult.build(400, "无数据");
        }
        olOrderCriteria.setState(1);
        Page page = offLineOrderService.findOrderByPage(olOrderCriteria, 10);
        return LejiaResult.ok(page);
    }

    /**
     * 2.0 版本  乐加账单 - 查看详情
     *
     * @param olOrderCriteria
     * @return
     */
    @RequestMapping(value = "/offLineOrder/findByCriteria", method = RequestMethod.POST)
    public
    @ResponseBody
    LejiaResult searchOrderListByCriteria(@RequestBody OLOrderCriteria olOrderCriteria) {
        if (olOrderCriteria.getOffset() == null) {
            olOrderCriteria.setOffset(1);
        }
        if (olOrderCriteria.getMerchant() == null) {
            return LejiaResult.build(400, "无数据");
        }
        olOrderCriteria.setState(1);
        Page page = offLineOrderService.findOrderByPage(olOrderCriteria, 10);
        return LejiaResult.ok(page);
    }

    @RequestMapping(value = "/order/orderDetail", method = RequestMethod.GET)
    public LejiaResult getOrderDetail() {

        Merchant
            merchant =
            merchantService.findMerchantUserBySid(SecurityUtils.getCurrentUserLogin())
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
            merchantService.findMerchantUserBySid(SecurityUtils.getCurrentUserLogin())
                .getMerchant();
        olOrderCriteria.setMerchant(merchant);

        return LejiaResult.ok(offLineOrderService.orderStatistic(olOrderCriteria));
    }


    /***
     * 乐加结算到账详情 - 数据概览
     * @param olOrderCriteria
     * @return
     */
    @RequestMapping(value = "/offLineOrder/orderStatistic", method = RequestMethod.POST)
    public
    @ResponseBody
    LejiaResult getOfflineOrderStatistic(@RequestBody OLOrderCriteria olOrderCriteria) {
        if (olOrderCriteria.getMerchant() != null) {
            return LejiaResult.ok(offLineOrderService.orderStatistic(olOrderCriteria));
        } else {
            return LejiaResult.build(400, "无数据");
        }
    }

    @RequestMapping(value = "/offLineOrder/dayCommission", method = RequestMethod.GET)
    public
    @ResponseBody
    LejiaResult
    getDayCommissionInfo() {

        Merchant
            merchant =
            merchantService.findMerchantUserBySid(SecurityUtils.getCurrentUserLogin())
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
            merchantService.findMerchantUserBySid(SecurityUtils.getCurrentUserLogin())
                .getMerchant();
        MerchantWallet
            merchantWallet =
            merchantService.findMerchantWalletByMerchant(merchant);

        Long currentBind = leJiaUserService.countBindMerchant(merchant);

        Map map = new HashMap<>();
        List<WithdrawBill> withdrawBillList = withdrawService.findByMerchantId(merchant.getId());
        Long withdrawTotalPrice = 0l;
        for (WithdrawBill withdrawBill : withdrawBillList) {
            withdrawTotalPrice = withdrawTotalPrice + withdrawBill.getTotalPrice();
        }
        map.put("withdrawTotalPrice", withdrawTotalPrice);
        map.put("available", merchantWallet.getAvailableBalance());
        map.put("totalCommission", merchantWallet.getTotalMoney());
        map.put("userLimit", merchant.getUserLimit());
        map.put("currentBind", currentBind);

        return LejiaResult.ok(map);
    }

    @RequestMapping(value = "/offLineOrder/merchantUserCommission", method = RequestMethod.GET)
    public
    @ResponseBody
    LejiaResult
    getCommissionByMerchantUser() {
        Long totalCommission = 0L;
        Long available = 0L;
        MerchantUser merchantUser = merchantService.findMerchantUserBySid(SecurityUtils.getCurrentUserLogin());
        List<Merchant> merchants = merchantUserResourceService.findMerchantsByMerchantUser(merchantUser);
        for (Merchant merchant : merchants) {
            MerchantWallet
                merchantWallet =
                merchantService.findMerchantWalletByMerchant(merchant);
            available += merchantWallet.getAvailableBalance();
            totalCommission += merchantWallet.getTotalMoney();
        }
        Map map = new HashMap<>();
        map.put("available", available);
        map.put("totalCommission", totalCommission);
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
                .findMerchantUserBySid(SecurityUtils.getCurrentUserLogin());
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
                .findMerchantUserBySid(SecurityUtils.getCurrentUserLogin());
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
                .findMerchantUserBySid(SecurityUtils.getCurrentUserLogin());
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
     * 订单语音提示
     */
    @RequestMapping(value = "/leJiaOrder/message/{id}", method = RequestMethod.GET)
    public void sendLejiaOrderMessage(@PathVariable String id) {
        ActivityDTO activityDTO = new ActivityDTO();
        activityDTO.setPage("Hello World " + id);
        messagingTemplate
            .convertAndSendToUser(SecurityUtils.getCurrentUserLogin(), "/reply", activityDTO);
    }


    /**
     * 每日订单数据 (所有门店)
     */
    @RequestMapping(value = "/order/dailyOrder", method = RequestMethod.GET)
    public LejiaResult getDailyOrderData() {
        MerchantUser
            merchantUser =
            merchantService.findMerchantUserBySid(SecurityUtils.getCurrentUserLogin());
        List<Merchant>
            merchants =
            merchantUserResourceService.findMerchantsByMerchantUser(merchantUser);
        Long offLineDailyCount = offLineOrderService.countOffLineOrder(merchants);          //  线下订单
        Long
            posDailyCount =
            posOrderSerivce.countPosOrder(merchants);                      //  pos 订单
        Map<String, Long>
            map =
            offLineOrderService.countMemberDailyOrdersDetail(merchants);     //  会员消费
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
        Long
            posDailyCount =
            posOrderSerivce.countPosOrder(merchants);                      //  pos 订单
        Map<String, Long>
            map =
            offLineOrderService.countMemberDailyOrdersDetail(merchants);     //  会员消费
        map.put("offLineDailyCount", offLineDailyCount == null ? 0L : offLineDailyCount);
        map.put("posDailyCount", posDailyCount == null ? 0L : posDailyCount);
        return LejiaResult.ok(map);
    }

    /**
     * 每日订单列表 (所有门店)
     */
    @RequestMapping(value = "/order/orderList/{offset}", method = RequestMethod.GET)
    public LejiaResult getOrderList(@PathVariable Long offset) {
        if (offset == null) {
            offset = 0L;
        }
        MerchantUser
            merchantUser =
            merchantService.findMerchantUserBySid(SecurityUtils.getCurrentUserLogin());
        List<Merchant>
            merchants =
            merchantUserResourceService.findMerchantsByMerchantUser(merchantUser);
        List<Object[]> allOrderList = new ArrayList<>();
        for (Merchant merchant : merchants) {
            List<Object[]> orderList = merchantService.findOrderList(merchant, offset);
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
        if (offset == null) {
            offset = 0L;
        }
        Merchant merchant = merchantService.findMerchantById(id);
        List<Object[]> orderList = merchantService.findOrderList(merchant, offset);
        for (Object[] objects : orderList) {
            objects[7] = merchant.getName();
        }
        return LejiaResult.ok(orderList);
    }

    /**
     * 乐加账单 - 每日账单
     */
    @RequestMapping(value = "/lejiaOrder/daily", method = RequestMethod.POST)
    public LejiaResult getDailyOrderDataByCriteria(
        @RequestBody DailyOrderCriteria dailyOrderCriteria) {
        LejiaOrderDTO orderDTO = lejiaOrderService.findDailyOrderByMerchant(dailyOrderCriteria);
        return LejiaResult.ok(orderDTO);
    }

    /**
     * 乐加账单 - 门店账单
     */
    @RequestMapping(value = "/lejiaOrder/merchant", method = RequestMethod.POST)
    public LejiaResult getMerchantOrderDataByCriteria(
        @RequestBody DailyOrderCriteria dailyOrderCriteria) {
        // 设置默认时间 - 最近七天
        setDefaultDailyCriteria(dailyOrderCriteria);
        //  根据条件查询账单
        MerchantUser
            merchantUser =
            merchantService.findMerchantUserBySid(SecurityUtils.getCurrentUserLogin());
        List<Merchant>
            merchants =
            merchantUserResourceService.findMerchantsByMerchantUser(merchantUser);
        List<MerchantOrderDto>
            orderDatas =
            lejiaOrderService.findMerchantOrderData(dailyOrderCriteria, merchants);
        return LejiaResult.ok(orderDatas);
    }

    /**
     * 导出指定门店订单数据 Excel
     */
    @RequestMapping(value = "/lejiaOrder/merchant/export", method = RequestMethod.GET)
    public ModelAndView exportOrderExcel(@RequestParam(required = false) String startDate,
                                         @RequestParam(required = false) String endDate,
                                         @RequestParam(required = true) Long merchantId) {
        String[]
            titles1 =
            {"订单编号", "交易完成时间", "订单状态", "支付渠道", "消费金额", "使用红包", "实际支付", "订单类型", "微信手续费", "红包手续费",
                "总入账金额", "微信支付入账", "红包支付入账", "退款时间"};
        String[]
            titles2 =
            {"退款单号", "退款完成时间", "订单编号", "订单类型", "订单完成时间", "微信渠道退款", "微信渠道退款", "微信支付少转账", "红包支付少转账"};
        String[] titles3 = {"微信支付", "红包支付", "微信退款", "红包退款", "微信支付入账", "红包支付入账"};
        // 设置默认时间 - 最近七天
        DailyOrderCriteria dailyOrderCriteria = new DailyOrderCriteria();
        dailyOrderCriteria.setStartDate(startDate);
        dailyOrderCriteria.setEndDate(endDate);
        Merchant merchant = merchantService.findMerchantById(merchantId);
        dailyOrderCriteria.setMerchant(merchant);
        setDefaultDailyCriteria(dailyOrderCriteria);
        // 导出 Excel
        Map map = lejiaOrderService.findOrderExcelData(dailyOrderCriteria);
        map.put("titles1", titles1);
        map.put("titles2", titles2);
        map.put("titles3", titles3);
        map.put("dailyOrderCriteria", dailyOrderCriteria);
        return new ModelAndView(merchantOrderExcel, map);
    }

    public DailyOrderCriteria setDefaultDailyCriteria(DailyOrderCriteria dailyOrderCriteria) {
        if (dailyOrderCriteria.getStartDate() == null || dailyOrderCriteria.getEndDate() == null) {
            Date endDate = new Date();
            Date startDate = new Date();
            startDate.setTime(endDate.getTime() - (86400000L * 7));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dailyOrderCriteria.setStartDate(simpleDateFormat.format(startDate));
            dailyOrderCriteria.setEndDate(simpleDateFormat.format(endDate));
        }
        return dailyOrderCriteria;
    }


    /**
     * 门店订单数据
     * 乐加 / 易宝
     */
    @RequestMapping(value = "/codeTrade/codeOrderByCriteria", method = RequestMethod.POST)
    @ResponseBody
    public LejiaResult codeOrderByCriteria(@RequestBody CodeOrderCriteria codeOrderCriteria) {
        if (codeOrderCriteria.getMerchant() == null) {
            return LejiaResult.build(400, "无数据");
        } else {
            MerchantScanPayWay scanPayWay = merchantScanPayWayService.findByMerchant(codeOrderCriteria.getMerchant().getId());
            if (scanPayWay.getType() == 3) {
                // 易宝结算
                ScanCodeOrderCriteria scanCodeOrderCriteria = new ScanCodeOrderCriteria();
                scanCodeOrderCriteria.setMerchantId(codeOrderCriteria.getMerchant().getId());
                scanCodeOrderCriteria.setState(1);
                scanCodeOrderCriteria.setPayment(codeOrderCriteria.getPayWay());
                scanCodeOrderCriteria.setOrderSid(codeOrderCriteria.getOrderSid());
                scanCodeOrderCriteria.setOrderType(codeOrderCriteria.getOrderType());
                scanCodeOrderCriteria.setStartDate(codeOrderCriteria.getStartDate());
                scanCodeOrderCriteria.setEndDate(codeOrderCriteria.getEndDate());
                if (codeOrderCriteria.getOffset() == null) {
                    scanCodeOrderCriteria.setOffset(1);
                } else {
                    scanCodeOrderCriteria.setOffset(codeOrderCriteria.getOffset());
                }
                Page page = scanCodeOrderService.findOrderByPage(scanCodeOrderCriteria, 10);
                Map map = new HashMap();
                map.put("page", page);
                map.put("payWay", scanPayWay.getType());
                return LejiaResult.ok(map);
            } else {
                // 乐加结算
                OLOrderCriteria olOrderCriteria = new OLOrderCriteria();
                olOrderCriteria.setMerchant(codeOrderCriteria.getMerchant());
                olOrderCriteria.setState(1);
                if (codeOrderCriteria.getPayWay() != null && codeOrderCriteria.getPayWay() == 0) {
                    olOrderCriteria.setPayWay(1L);
                } else if (codeOrderCriteria.getPayWay() != null && codeOrderCriteria.getPayWay() == 1) {
                    olOrderCriteria.setPayWay(2L);
                } else {
                    olOrderCriteria.setPayWay(null);
                }
                olOrderCriteria.setOrderSid(codeOrderCriteria.getOrderSid());
                olOrderCriteria.setOrderType(codeOrderCriteria.getOrderType());
                olOrderCriteria.setStartDate(codeOrderCriteria.getStartDate());
                olOrderCriteria.setEndDate(codeOrderCriteria.getEndDate());
                if (codeOrderCriteria.getOffset() == null) {
                    olOrderCriteria.setOffset(1);
                } else {
                    olOrderCriteria.setOffset(codeOrderCriteria.getOffset());
                }
                Page page = offLineOrderService.findOrderByPage(olOrderCriteria, 10);
                Map map = new HashMap();
                map.put("page", page);
                map.put("payWay", scanPayWay.getType());
                return LejiaResult.ok(map);
            }
        }
    }

    @RequestMapping(value = "/codeTradeList/export", method = RequestMethod.GET)
    public ModelAndView exporeExcel(@RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate, @RequestParam(required = true) Long merchantId,
                                    @RequestParam(required = false) Integer payWay, @RequestParam(required = false) Integer orderType, @RequestParam(required = false) String orderSid) {
        MerchantScanPayWay scanPayWay = merchantScanPayWayService.findByMerchant(merchantId);
        if (scanPayWay.getType() == 3) {
            // 易宝结算
            ScanCodeOrderCriteria scanCodeOrderCriteria = new ScanCodeOrderCriteria();
            scanCodeOrderCriteria.setMerchantId(merchantId);
            scanCodeOrderCriteria.setState(1);
            scanCodeOrderCriteria.setPayment(payWay);
            scanCodeOrderCriteria.setOrderSid(orderSid);
            scanCodeOrderCriteria.setOrderType(orderType);
            scanCodeOrderCriteria.setStartDate(startDate);
            scanCodeOrderCriteria.setEndDate(endDate);
            scanCodeOrderCriteria.setOffset(1);
            Page page = scanCodeOrderService.findOrderByPage(scanCodeOrderCriteria, 1000);
            Map map = new HashMap();
            map.put("scOrderList", page.getContent());
            return new ModelAndView(scanCodeOrderExcel, map);
        } else {
            // 乐加结算
            OLOrderCriteria olOrderCriteria = new OLOrderCriteria();
            Merchant merchant = merchantService.findMerchantById(merchantId);
            olOrderCriteria.setMerchant(merchant);
            olOrderCriteria.setState(1);
            if (payWay != null && payWay == 0) {
                olOrderCriteria.setPayWay(1L);
            } else if (payWay != null && payWay == 1) {
                olOrderCriteria.setPayWay(2L);
            } else {
                olOrderCriteria.setPayWay(null);
            }
            olOrderCriteria.setOrderSid(orderSid);
            olOrderCriteria.setOrderType(orderType);
            olOrderCriteria.setStartDate(startDate);
            olOrderCriteria.setEndDate(endDate);
            olOrderCriteria.setOffset(1);
            Page page = offLineOrderService.findOrderByPage(olOrderCriteria, 1000);
            Map map = new HashMap();
            map.put("olOrderList", page.getContent());
            return new ModelAndView(offLineOrderExcel, map);
        }
    }

    /***
     *  订单数据统计
     */
    @RequestMapping(value = "/codeTrade/codeOrderStatistic", method = RequestMethod.POST)
    @ResponseBody
    public LejiaResult codeOrderStatistic(@RequestBody CodeOrderCriteria codeOrderCriteria) {
        MerchantScanPayWay scanPayWay = merchantScanPayWayService.findByMerchant(codeOrderCriteria.getMerchant().getId());
        if (scanPayWay.getType() == 3) {
            // 易宝结算
            ScanCodeOrderCriteria scanCodeOrderCriteria = new ScanCodeOrderCriteria();
            scanCodeOrderCriteria.setMerchantId(codeOrderCriteria.getMerchant().getId());
            scanCodeOrderCriteria.setState(1);
            scanCodeOrderCriteria.setPayment(codeOrderCriteria.getPayWay());
            scanCodeOrderCriteria.setOrderSid(codeOrderCriteria.getOrderSid());
            scanCodeOrderCriteria.setStartDate(codeOrderCriteria.getStartDate());
            scanCodeOrderCriteria.setEndDate(codeOrderCriteria.getEndDate());
            Map map = new HashMap();
            if(scanCodeOrderCriteria.getOrderType()!=null && scanCodeOrderCriteria.getOrderType()==0) {
                List<Object[]> commonData = scanCodeOrderService.findTotalDailyTransferAndIncome(scanCodeOrderCriteria);
                map.put("totalData",commonData);
                map.put("commonData",commonData);
                map.put("lejiaData",null);
            }else if(scanCodeOrderCriteria.getOrderType()!=null && scanCodeOrderCriteria.getOrderType()==1) {
                List<Object[]> lejiaData = scanCodeOrderService.findTotalDailyTransferAndIncome(scanCodeOrderCriteria);
                map.put("totalData",lejiaData);
                map.put("lejiaData",lejiaData);
                map.put("commonData",null);
            }else {
                scanCodeOrderCriteria.setOrderType(null);
                List<Object[]> totalData = scanCodeOrderService.findTotalDailyTransferAndIncome(scanCodeOrderCriteria);
                scanCodeOrderCriteria.setOrderType(0);
                List<Object[]> commonData = scanCodeOrderService.findTotalDailyTransferAndIncome(scanCodeOrderCriteria);
                scanCodeOrderCriteria.setOrderType(1);
                List<Object[]> lejiaData = scanCodeOrderService.findTotalDailyTransferAndIncome(scanCodeOrderCriteria);
                map.put("totalData",totalData);
                map.put("commonData",commonData);
                map.put("lejiaData",lejiaData);
            }
            return LejiaResult.ok(map);
        } else {
            // 乐加结算
            OLOrderCriteria olOrderCriteria = new OLOrderCriteria();
            olOrderCriteria.setMerchant(codeOrderCriteria.getMerchant());
            olOrderCriteria.setState(1);
            if (codeOrderCriteria.getPayWay() != null && codeOrderCriteria.getPayWay() == 0) {
                olOrderCriteria.setPayWay(1L);
            } else if (codeOrderCriteria.getPayWay() != null && codeOrderCriteria.getPayWay() == 1) {
                olOrderCriteria.setPayWay(2L);
            } else {
                olOrderCriteria.setPayWay(null);
            }
            olOrderCriteria.setOrderSid(codeOrderCriteria.getOrderSid());
            olOrderCriteria.setOrderType(codeOrderCriteria.getOrderType());
            olOrderCriteria.setStartDate(codeOrderCriteria.getStartDate());
            olOrderCriteria.setEndDate(codeOrderCriteria.getEndDate());
            Map map = new HashMap();
            if(olOrderCriteria.getOrderType()!=null && olOrderCriteria.getOrderType()==0) {
                List<Object[]> commonData = offLineOrderService.findOfflineOrderStatistic(olOrderCriteria);
                map.put("totalData",commonData);
                map.put("commonData",commonData);
                map.put("lejiaData",null);
            }else if(olOrderCriteria.getOrderType()!=null && olOrderCriteria.getOrderType()==1) {
                List<Object[]> lejiaData = offLineOrderService.findOfflineOrderStatistic(olOrderCriteria);
                map.put("totalData",lejiaData);
                map.put("lejiaData",lejiaData);
                map.put("commonData",null);
            }else {
                olOrderCriteria.setOrderType(null);
                List<Object[]> totalData = offLineOrderService.findOfflineOrderStatistic(olOrderCriteria);
                olOrderCriteria.setOrderType(0);
                List<Object[]> commonData= offLineOrderService.findOfflineOrderStatistic(olOrderCriteria);
                olOrderCriteria.setOrderType(1);
                List<Object[]> lejiaData = offLineOrderService.findOfflineOrderStatistic(olOrderCriteria);
                map.put("totalData",totalData);
                map.put("commonData",commonData);
                map.put("lejiaData",lejiaData);
            }
            return LejiaResult.ok(map);
        }
    }


    @RequestMapping(value = "/offLineOrder/olOrderStatistic", method = RequestMethod.POST)
    @ResponseBody
    public LejiaResult codeOrderStatistic(@RequestBody OLOrderCriteria olOrderCriteria) {
            // 乐加结算
            olOrderCriteria.setState(1);
            Map map = new HashMap();
            if(olOrderCriteria.getOrderType()!=null && olOrderCriteria.getOrderType()==0) {
                List<Object[]> commonData = offLineOrderService.findOfflineOrderStatistic(olOrderCriteria);
                map.put("totalData",commonData);
                map.put("commonData",commonData);
                map.put("lejiaData",null);
            }else if(olOrderCriteria.getOrderType()!=null && olOrderCriteria.getOrderType()==1) {
                List<Object[]> lejiaData = offLineOrderService.findOfflineOrderStatistic(olOrderCriteria);
                map.put("totalData",lejiaData);
                map.put("lejiaData",lejiaData);
                map.put("commonData",null);
            }else {
                olOrderCriteria.setOrderType(null);
                List<Object[]> totalData = offLineOrderService.findOfflineOrderStatistic(olOrderCriteria);
                olOrderCriteria.setOrderType(0);
                List<Object[]> commonData= offLineOrderService.findOfflineOrderStatistic(olOrderCriteria);
                olOrderCriteria.setOrderType(1);
                List<Object[]> lejiaData = offLineOrderService.findOfflineOrderStatistic(olOrderCriteria);
                map.put("totalData",totalData);
                map.put("commonData",commonData);
                map.put("lejiaData",lejiaData);
            }
            return LejiaResult.ok(map);
    }
}
