package com.jifenke.lepluslive.yibao.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.yibao.domain.criteria.LedgerSettlementCriteria;
import com.jifenke.lepluslive.yibao.domain.criteria.StoreSettlementCriteria;
import com.jifenke.lepluslive.yibao.domain.entities.LedgerSettlement;
import com.jifenke.lepluslive.yibao.domain.entities.MerchantLedger;
import com.jifenke.lepluslive.yibao.domain.entities.MerchantUserLedger;
import com.jifenke.lepluslive.yibao.domain.entities.StoreSettlement;
import com.jifenke.lepluslive.yibao.service.LedgerSettlementService;
import com.jifenke.lepluslive.yibao.service.MerchantLedgerService;
import com.jifenke.lepluslive.yibao.service.MerchantUserLedgerService;
import com.jifenke.lepluslive.yibao.service.StoreSettlementService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 通道结算单 Controller
 * Created by xf on 17-8-21.
 */
@RestController
@RequestMapping("/api")
public class LedgerSettlementController {

    @Inject
    private MerchantService merchantService;
    @Inject
    private MerchantLedgerService merchantLedgerService;
    @Inject
    private MerchantUserLedgerService merchantUserLedgerService;

    @Inject
    private LedgerSettlementService ledgerSettlementService;

    @Inject
    private StoreSettlementService storeSettlementService;


    /***
     *  根据门店查询通道结算单  [到账记录]
     * @param settlementCriteria
     * @return
     */
    @RequestMapping(value = "/ledgerSettlement/findByPage", method = RequestMethod.POST)
    public LejiaResult findSettlemntByMerchant(@RequestBody LedgerSettlementCriteria settlementCriteria) {
        if (settlementCriteria.getOffset() == null) {
            settlementCriteria.setOffset(1);
        }
        Long merchantId = settlementCriteria.getMerchant().getId();
        if (merchantId == null) {
            return LejiaResult.build(400, "无门店信息");
        }
        if(settlementCriteria.getStartDate()!=null&&!"".equals(settlementCriteria.getStartDate())) {
            String startAfter = getDateStrAfter(settlementCriteria.getStartDate()); //  结算单差异天数
            String endAfter = getDateStrAfter(settlementCriteria.getEndDate());
            settlementCriteria.setStartDate(getDateStrBefore(startAfter));          //  数据偏移处理 between end
            settlementCriteria.setEndDate(getDateStrAfter(endAfter));
        }
        Merchant merchant = merchantService.findMerchantById(merchantId);
        MerchantLedger merchantLedger = merchantLedgerService.findMerchantLedgerByMerchant(merchant);
        MerchantUserLedger merchantUserLedger = merchantLedger.getMerchantUserLedger();
        settlementCriteria.setLedgerNo(merchantUserLedger.getLedgerNo());               //  获取子商户号
        Map map = new HashMap<>();
        Page<LedgerSettlement> page = ledgerSettlementService.findByCriteria(settlementCriteria, 10);
        List<String> dateBefores = new ArrayList<>();
        List<LedgerSettlement> content = page.getContent();
        for (LedgerSettlement settlement : content) {
            String dateBefore = getTradeDateStrBefore(settlement.getTradeDate());
            dateBefores.add(dateBefore);
        }
        map.put("page",page);
        map.put("dateBefores",dateBefores);
        return LejiaResult.ok(map);
    }

    /***
     *  根据通道结算单查询门店结算单 [到账详情]
     */
    public LejiaResult findSettlementDetialByNo(StoreSettlementCriteria criteria) {
        if (criteria.getOffset() == null) {
            criteria.setOffset(1);
        }
        String sid = criteria.getLedgerSid();
        criteria.setLedgerSid(null);                                        // sid 仅用作封装数据不做查询条件
        if (sid == null) {
            return LejiaResult.build(400, "无通道信息");
        }
        LedgerSettlement ledgerSettlement = ledgerSettlementService.findBySid(sid);
        String tradeDate = ledgerSettlement.getTradeDate();
        String ledgerNo = ledgerSettlement.getLedgerNo();
        criteria.setLedgerNo(ledgerNo);
        String tradeBefore = getTradeDateStrBefore(tradeDate);             // 获取前一天的结算日期
        criteria.setTradeDate(tradeBefore);
        Page<StoreSettlement> page = storeSettlementService.findByCriteria(criteria, 10);
        return LejiaResult.ok(page);
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

    public String getDateStrBefore(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(dateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, -1);
            date = calendar.getTime();
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getDateStrAfter(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(dateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, 1);
            date = calendar.getTime();
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
