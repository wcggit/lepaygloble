package com.jifenke.lepluslive.cmbc.controller;

import com.jifenke.lepluslive.cmbc.domain.criteria.CmbcSettlementCriteria;
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
import org.springframework.web.bind.annotation.*;

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

    /**
     * 民生结算单列表
     */
    @RequestMapping(value = "/cmbcSettlement/findByPage", method = RequestMethod.POST)
    public LejiaResult list(@RequestBody CmbcSettlementCriteria settlementCriteria) {
        Merchant merchant = merchantService.findMerchantById(settlementCriteria.getMerchantId());
        CmbcSubMerNexus cmbcSubMerNexus = cmbcSubMerNexusService.findByMerchant(merchant);
        settlementCriteria.setCmbcMerchantNo(cmbcSubMerNexus.getCmbcSubMerchant().getSubMerchantNo());
        Map map = new HashMap();
        List<Map<String, Object>> list = cmbcSettlementService.listByCriteria(settlementCriteria);
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


}
