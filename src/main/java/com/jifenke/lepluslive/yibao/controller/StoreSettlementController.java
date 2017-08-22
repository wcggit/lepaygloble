package com.jifenke.lepluslive.yibao.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.order.domain.entities.ScanCodeOrderCriteria;
import com.jifenke.lepluslive.order.service.ScanCodeOrderService;
import com.jifenke.lepluslive.yibao.domain.criteria.LedgerRefundOrderCriteria;
import com.jifenke.lepluslive.yibao.domain.entities.StoreSettlement;
import com.jifenke.lepluslive.yibao.service.LedgerRefundOrderService;
import com.jifenke.lepluslive.yibao.service.StoreSettlementService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    /**
     * 到账详情页面基本数据 【到账详情】
     * - 门店应入账
     * - 有交易的门店名称
     */
    @RequestMapping(value = "/settelement/detail", method = RequestMethod.GET)
    @ResponseBody
    public LejiaResult findSettlementDetial(String ledgerNo, String tradeDate) {
        List<StoreSettlement> storeSettlements = storeSettlementService.findByTradeDateAndLedgerNo(ledgerNo, tradeDate);
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
        Page page = scanCodeOrderService.findOrderByPage(detailCriteria, 10);
        return LejiaResult.ok(page);
    }

    /**
     * 指定条件下的流水和入账 -交易记录  【到账详情】
     */
    @RequestMapping(value = "/settlement/tradeData", method = RequestMethod.POST)
    @ResponseBody
    public LejiaResult findTradeData(@RequestBody ScanCodeOrderCriteria detailCriteria) {
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
        Page page = ledgerRefundOrderService.findByCriteria(refundCriteria, 10);
        return LejiaResult.ok(page);
    }

}
