package com.jifenke.lepluslive.order.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.order.domain.criteria.UnionImportSettlementCriteria;
import com.jifenke.lepluslive.order.domain.criteria.UnionSettlementCriteria;
import com.jifenke.lepluslive.order.service.UnionImportSettlementService;
import com.jifenke.lepluslive.order.service.UnionSettlementService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

/**
 * Created by xf on 17-10-18.
 */
@RestController
@RequestMapping("/api")
public class UnionSettlementController {

    @Inject
    private UnionImportSettlementService unionImportSettlementService;

    @Inject
    private UnionSettlementService unionSettlementService;


    @RequestMapping(value = "/unionSettlement/findByCriteria", method = RequestMethod.POST)
    public
    @ResponseBody
    LejiaResult searchSettlementByCriteria(@RequestBody UnionSettlementCriteria unionSettlementCriteria) {
        if (unionSettlementCriteria.getOffset() == null) {
            unionSettlementCriteria.setOffset(1);
        }
        if (unionSettlementCriteria.getMerchantUserId() == null) {
            return LejiaResult.build(400, "无数据");
        }
        Page page = unionSettlementService.findOrderByPage(unionSettlementCriteria, 10);
        return LejiaResult.ok(page);
    }

    @RequestMapping(value = "/unionImportSettlement/findByCriteria", method = RequestMethod.POST)
    public
    @ResponseBody
    LejiaResult searchImportSettlementByCriteria(@RequestBody UnionImportSettlementCriteria unionImportSettlementCriteria) {
        if (unionImportSettlementCriteria.getOffset() == null) {
            unionImportSettlementCriteria.setOffset(1);
        }
        if (unionImportSettlementCriteria.getMerchantUserId() == null) {
            return LejiaResult.build(400, "无数据");
        }
        Page page = unionImportSettlementService.findOrderByPage(unionImportSettlementCriteria, 10);
        return LejiaResult.ok(page);
    }
}
