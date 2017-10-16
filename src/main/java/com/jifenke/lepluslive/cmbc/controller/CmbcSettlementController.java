package com.jifenke.lepluslive.cmbc.controller;

import com.jifenke.lepluslive.cmbc.domain.criteria.CmbcSettlementCriteria;
import com.jifenke.lepluslive.cmbc.domain.criteria.view.CmbcSettlementExcel;
import com.jifenke.lepluslive.cmbc.domain.entities.CmbcSettlement;
import com.jifenke.lepluslive.cmbc.domain.entities.CmbcSubMerNexus;
import com.jifenke.lepluslive.cmbc.domain.entities.CmbcSubMerchant;
import com.jifenke.lepluslive.cmbc.service.CmbcSettlementService;
import com.jifenke.lepluslive.cmbc.service.CmbcSubMerNexusService;
import com.jifenke.lepluslive.cmbc.service.CmbcSubMerchantService;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.merchant.service.MerchantUserService;
import com.jifenke.lepluslive.order.service.ChannelRefundOrderService;
import com.jifenke.lepluslive.order.service.ScanCodeOrderService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 民生结算相关
 * Created by zhangwen on 2017/9/21.
 */
@RestController
@RequestMapping("/api")
public class CmbcSettlementController {

    @Inject
    private CmbcSettlementService cmbcSettlementService;

    @Inject
    private MerchantUserService merchantUserService;

    @Inject
    private MerchantService merchantService;

    @Inject
    private CmbcSubMerchantService cmbcSubMerchantService;

    @Inject
    private CmbcSubMerNexusService cmbcSubMerNexusService;

    @Inject
    private ScanCodeOrderService scanCodeOrderService;

    @Inject
    private ChannelRefundOrderService channelRefundOrderService;

    @Inject
    private CmbcSettlementExcel cmbcSettlementExcel;
    /***
     *  查询同一子商户号下的门店名称
     */
    @RequestMapping(value = "/cmbcMerchant/findByMerchant", method = RequestMethod.GET)
    @ResponseBody
    public LejiaResult searchCmbcMerchant(Long mid) {
        Merchant merchant = merchantService.findMerchantById(mid);
        if(merchant==null) {
            return LejiaResult.build(400,"数据异常！");
        }else {
            CmbcSubMerNexus cmbcSubMerNexus = cmbcSubMerNexusService.findByMerchant(merchant);
            CmbcSubMerchant cmbcSubMerchant = cmbcSubMerNexus.getCmbcSubMerchant();
            List<CmbcSubMerNexus> nexuses = cmbcSubMerNexusService.findAllByCmbcSubMerchant(cmbcSubMerchant);
            List<String> names = new ArrayList<>();
            Map map = new HashMap();
            for (CmbcSubMerNexus merNexus : nexuses) {
                String name = merNexus.getMerchant().getName();
                names.add(name);
            }
            map.put("ledgerCount",names.size());
            map.put("ledgerNames",names);
            return LejiaResult.ok(map);
        }
    }
    @RequestMapping(value = "/cmbcMerchant/findByMerNO", method = RequestMethod.GET)
    @ResponseBody
    public LejiaResult searchCmbcMerchant(String merNo) {
        CmbcSubMerchant subMerchant = cmbcSubMerchantService.findBySubMerchantNo(merNo);
        List<CmbcSubMerNexus> nexuses = cmbcSubMerNexusService.findAllByCmbcSubMerchant(subMerchant);
        List<Merchant> merchants = new ArrayList<>();
        if(nexuses!=null&&nexuses.size()>0) {
            for (CmbcSubMerNexus nexuse : nexuses) {
                Merchant merchant = nexuse.getMerchant();
                merchants.add(merchant);
            }
        }
        return LejiaResult.ok(merchants);
    }

    /**
     * 民生结算单列表
     */
    @RequestMapping(value = "/cmbcSettlement/findByPage", method = RequestMethod.POST)
    public LejiaResult list(@RequestBody CmbcSettlementCriteria settlementCriteria) {
        Merchant merchant = merchantService.findMerchantById(settlementCriteria.getMerchantId());
        CmbcSubMerNexus cmbcSubMerNexus = cmbcSubMerNexusService.findByMerchant(merchant);
        settlementCriteria.setCmbcMerchantNo(cmbcSubMerNexus.getCmbcSubMerchant().getSubMerchantNo());
        Map map = new HashMap();
        List<Map<String, Object>> list = cmbcSettlementService.listByCriteria(settlementCriteria,10);
        Map<String, Object> count = cmbcSettlementService.countByCriteria(settlementCriteria);
        Long totalElements =  Long.valueOf(count.get("totalElements").toString());
        Long totalPages = 1L;
        if(totalElements>10) {
            Double pages = Math.ceil(new Double(totalElements) / 10.0);
            totalPages = new Long(pages.intValue());
        }
        map.put("content",list);
        map.put("totalPages",totalPages);
        return LejiaResult.ok(map);
    }


    /**
     * 民生结算单详情页获取基本结算数据
     *
     * @param id 结算单ID
     * @return 结算单
     */
    @RequestMapping(value = "/cmbcSettlement/{id}", method = RequestMethod.GET)
    public CmbcSettlement settlement(@PathVariable Long id) {
        return cmbcSettlementService.findById(id);
    }

    /**
     * 民生结算单详情页获取某一种类型的列表数据
     *
     * @param id   结算单ID
     * @param type 类型 0=普通微信|1=普通支付宝|2=乐加
     */
    @RequestMapping(value = "/cmbcSettlement/detailList", method = RequestMethod.GET)
    public Map settlementDetailList(@RequestParam Long id, @RequestParam Integer type) {
        return cmbcSettlementService.settlementDetailList(id, type);
    }

    /**
     *  根据结算单号获取
     *  - 1.门店信息
     *  - 2.订单信息
     */
    @RequestMapping(value = "/cmbcSettlement/settlementInfo/{id}", method = RequestMethod.GET)
    public LejiaResult loadSettlementsDetail(@PathVariable Long id) {
        CmbcSettlement cmbcSettlement = cmbcSettlementService.findById(id);
        Map map = new HashMap();
        // 普通订单金额、实际入账、手续费、订单数、退款单数
        map.put("ptActual",(cmbcSettlement.getWxActual()+cmbcSettlement.getAliActual()));
        map.put("ptTotal",(cmbcSettlement.getWxTotal()+cmbcSettlement.getAliTotal()));
        map.put("ptCommission",(cmbcSettlement.getWxCommision()+cmbcSettlement.getAliCommision()));
        map.put("ptCount",(cmbcSettlement.getWxCount()+cmbcSettlement.getAliCount()));
        map.put("ptRefundCount",(cmbcSettlement.getWxRefundCount()+cmbcSettlement.getAliRefundCount()));
        // 普通（微信）订单金额、实际入账、手续费、订单数、退款单数、退款单手续费
        map.put("wxTotal",cmbcSettlement.getWxTotal());
        map.put("wxActual",cmbcSettlement.getWxActual());
        map.put("wxCount",cmbcSettlement.getWxCount());
        map.put("wxCommission",cmbcSettlement.getWxCommision());
        map.put("wxRefundCount",cmbcSettlement.getWxRefundCount());
        map.put("wxRefundTotal",cmbcSettlement.getWxRefundTotal());
        map.put("wxRefundCommision",cmbcSettlement.getWxRefundCommision());
        // 普通（支付宝）订单金额、实际入账、手续费、订单数、退款单数、退款单手续费
        map.put("aliTotal",cmbcSettlement.getAliTotal());
        map.put("aliActual",cmbcSettlement.getAliActual());
        map.put("aliCount",cmbcSettlement.getAliCount());
        map.put("aliCommision",cmbcSettlement.getAliCommision());
        map.put("aliRefundCount",cmbcSettlement.getAliRefundCount());
        map.put("aliRefundTotal",cmbcSettlement.getAliRefundTotal());
        map.put("aliRefundCommision",cmbcSettlement.getAliRefundCommision());
        // 乐加订单金额、实际入账、手续费、订单数、退款单数
        map.put("ljTotal",cmbcSettlement.getLeTotal());
        map.put("ljActual",cmbcSettlement.getLeActual());
        map.put("ljCount",cmbcSettlement.getLeCount());
        map.put("ljRefundCount",cmbcSettlement.getLeRefundCount());
        map.put("ljRefundTotal",cmbcSettlement.getLeRefundTotal());
        map.put("ljCommission",cmbcSettlement.getLeCommision());
        return LejiaResult.ok(map);
    }

    @RequestMapping(value = "/cmbcSettlement/export", method = RequestMethod.GET)
    public ModelAndView export(@RequestParam Long mid, @RequestParam(required = false)String startDate, @RequestParam(required = false)String endDate, @RequestParam(required = false)Integer state) {
        Merchant merchant = merchantService.findMerchantById(mid);
        CmbcSubMerNexus cmbcSubMerNexus = cmbcSubMerNexusService.findByMerchant(merchant);
        CmbcSettlementCriteria settlementCriteria = new CmbcSettlementCriteria();
        settlementCriteria.setCmbcMerchantNo(cmbcSubMerNexus.getCmbcSubMerchant().getSubMerchantNo());
        settlementCriteria.setOffset(1);
        if(startDate!=null&&!"".equals(startDate)) {
            settlementCriteria.setStartDate(startDate);
            settlementCriteria.setEndDate(endDate);
        }
        if(state!=null) {
            settlementCriteria.setState(state);
        }
        Map map = new HashMap();
        List<Map<String, Object>> list = cmbcSettlementService.listByCriteria(settlementCriteria,1000);
        map.put("settlementList",list);
        return new ModelAndView(cmbcSettlementExcel, map);
    }
}
