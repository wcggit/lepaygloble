package com.jifenke.lepluslive.yibao.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.order.domain.criteria.DailyOrderCriteria;
import com.jifenke.lepluslive.order.domain.entities.ScanCodeOrderCriteria;
import com.jifenke.lepluslive.order.service.ScanCodeOrderService;
import com.jifenke.lepluslive.yibao.domain.criteria.LedgerRefundOrderCriteria;
import com.jifenke.lepluslive.yibao.domain.entities.LedgerRefundOrder;
import com.jifenke.lepluslive.yibao.domain.entities.MerchantLedger;
import com.jifenke.lepluslive.yibao.domain.entities.MerchantUserLedger;
import com.jifenke.lepluslive.yibao.domain.entities.StoreSettlement;
import com.jifenke.lepluslive.yibao.service.LedgerRefundOrderService;
import com.jifenke.lepluslive.yibao.service.MerchantLedgerService;
import com.jifenke.lepluslive.yibao.service.StoreSettlementService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

/**
 * Created by xf on 17-8-21.
 */
@RestController
@RequestMapping("/api")
public class StoreSettlementController {

    @Inject
    private MerchantService merchantService;

    @Inject
    private ScanCodeOrderService scanCodeOrderService;

    @Inject
    private StoreSettlementService storeSettlementService;

    @Inject
    private LedgerRefundOrderService ledgerRefundOrderService;

    @Inject
    private MerchantLedgerService merchantLedgerService;
    /**
     *  根据门店查询子商户，及子商户下其他门店
     */
    @RequestMapping(value = "/settelement/merchantLedger/{mid}", method = RequestMethod.GET)
    public LejiaResult findMerchantLedgerByMerchant(@PathVariable  Long mid) {
        Merchant merchant = merchantService.findMerchantById(mid);
        MerchantLedger merchantLedger = merchantLedgerService.findMerchantLedgerByMerchant(merchant);
        MerchantUserLedger merchantUserLedger = merchantLedger.getMerchantUserLedger();
        List<Object[]> merchantNames = merchantLedgerService.findMerchantLedgerByMerchantUserLedger(merchantUserLedger);
        Map map = new HashMap();
        if(merchantNames!=null&&merchantNames.size()>0) {
            map.put("count",merchantNames.size());
            map.put("merchantNames",merchantNames);
        }else {
            map.put("count",0);
        }
        return LejiaResult.ok(map);
    }
    /**
     * 到账详情页面基本数据 【到账详情】
     * - 门店应入账
     * - 有交易的门店名称
     */
    @RequestMapping(value = "/settelement/detail", method = RequestMethod.GET)
    @ResponseBody
    public LejiaResult findSettlementDetial(String ledgerNo, String tradeDate) {
        String tradeBefore = getTradeDateStrBefore(tradeDate);                          // 获取前一天的结算数据
        List<Object[]> storeSettlements = storeSettlementService.findByTradeDateAndLedgerNo(tradeBefore,ledgerNo);
        return LejiaResult.ok(storeSettlements);
    }

    /**
     * 查看交易记录 - 易宝 【到账详情】
     */
    @RequestMapping(value = "/settlement/tradeList", method = RequestMethod.POST)
    @ResponseBody
    public LejiaResult findTradeList(@RequestBody ScanCodeOrderCriteria detailCriteria) {
        if (detailCriteria.getOffset() == null) {
            detailCriteria.setOffset(1);
        }
        if (detailCriteria.getMerchantId() == null) {
            return LejiaResult.build(400, "无相关数据");
        }
        //  获取前一天
        if(detailCriteria.getTradeDate()!=null && !"".equals(detailCriteria.getTradeDate())) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            detailCriteria =setStartAndEndDate(detailCriteria,simpleDateFormat);
        }else {
            return LejiaResult.build(400, "无相关数据");
        }
        Page page = scanCodeOrderService.findOrderByPage(detailCriteria, 10);

        return LejiaResult.ok(page);
    }

    /**
     * 指定条件下的流水和入账 -交易记录  【到账详情】
     */
    @RequestMapping(value = "/settlement/tradeData", method = RequestMethod.POST)
    @ResponseBody
    public LejiaResult findTradeData(@RequestBody ScanCodeOrderCriteria detailCriteria) {
        //  获取前一天
        if(detailCriteria.getTradeDate()!=null && !"".equals(detailCriteria.getTradeDate())) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            detailCriteria =setStartAndEndDate(detailCriteria,simpleDateFormat);
        }else {
            return LejiaResult.build(400, "无相关数据");
        }
        Map<String, List<Object[]>> map = new HashMap<>();
        // 全部类型订单
        detailCriteria.setOrderType(null);
        List<Object[]> totalTransferAndIncome = scanCodeOrderService.findTotalDailyTransferAndIncome(detailCriteria);
        // 普通订单
        detailCriteria.setOrderType(0);
        List<Object[]> commonTransferAndIncome = scanCodeOrderService.findTotalDailyTransferAndIncome(detailCriteria);
        // 乐加订单
        detailCriteria.setOrderType(1);
        List<Object[]> lejiaTransferAndIncome = scanCodeOrderService.findTotalDailyTransferAndIncome(detailCriteria);
        map.put("totalData", totalTransferAndIncome);
        map.put("commonData", commonTransferAndIncome);
        map.put("lejiaData", lejiaTransferAndIncome);
        return LejiaResult.ok(map);
    }



    /***
     * 查看退款记录 - 易宝 【到账详情】
     */
    @RequestMapping(value = "/settlement/refundList", method = RequestMethod.POST)
    @ResponseBody
    public LejiaResult findRefundList(@RequestBody LedgerRefundOrderCriteria refundCriteria) {
        if (refundCriteria.getOffset() == null) {
            refundCriteria.setOffset(1);
        }
        if (refundCriteria.getMerchantId() == null) {
            return LejiaResult.build(400, "无相关数据");
        }
        // 设置前一天
        if(refundCriteria.getTradeDate()!=null && !"".equals(refundCriteria.getTradeDate())) {
            String tradeBefore = getTradeDateStrBefore(refundCriteria.getTradeDate());
            refundCriteria.setTradeDate(tradeBefore);
        }else {
            return LejiaResult.build(400, "无相关数据");
        }
        Map map = new HashMap();
        Page page = ledgerRefundOrderService.findByCriteria(refundCriteria, 10);
        map.put("page",page);
        Long dailyTotal = ledgerRefundOrderService.sumDailyTotalRefund(refundCriteria.getMerchantId(), refundCriteria.getTradeDate());
        map.put("dailyTotal",dailyTotal==null?0:dailyTotal);
        map.put("dailyCount",page.getTotalElements());
        return LejiaResult.ok(map);

    }



    // 日期转换
    public String getTradeDateStrBefore(String tradeDateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date tradeDate = null;
        try {
            tradeDate = sdf.parse(tradeDateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(tradeDate);
            calendar.add(Calendar.DATE, -1);
            tradeDate = calendar.getTime();
            return sdf.format(tradeDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ScanCodeOrderCriteria setStartAndEndDate(ScanCodeOrderCriteria detailCriteria,SimpleDateFormat simpleDateFormat) {
        try{
            String tradeBefore = getTradeDateStrBefore(detailCriteria.getTradeDate());
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
            Date tradeDate = sdf2.parse(tradeBefore);
            Date start = tradeDate;
            Date end = tradeDate;
            // 日期计算
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(end);                          // 计算结束时间
            calendar.add(Calendar.HOUR,23);
            calendar.add(Calendar.MINUTE,59);
            calendar.add(Calendar.SECOND,59);
            end = calendar.getTime();
            String startDate = simpleDateFormat.format(start);
            String endDate =simpleDateFormat.format(end);
            detailCriteria.setStartDate(startDate);
            detailCriteria.setEndDate(endDate);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return detailCriteria;
    }

}
