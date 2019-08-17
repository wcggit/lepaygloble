package com.jifenke.lepluslive.order.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.order.domain.criteria.UnionImportOrderCriteria;
import com.jifenke.lepluslive.order.domain.criteria.UnionPosOrderCriteria;
import com.jifenke.lepluslive.order.service.UnionImportOrderService;
import com.jifenke.lepluslive.order.service.UnionPosOrderService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

/**
 * Created by xf on 17-10-19.
 */
@RestController
@RequestMapping("/api")
public class UnionOrderController {
    @Inject
    private UnionImportOrderService importOrderService;
    @Inject
    private UnionPosOrderService unionPosOrderService;

    /***
     *  银商结算单
     * @param importOrderCriteria
     * @return
     */
    @RequestMapping(value = "/unionImportOrder/findByCriteria", method = RequestMethod.POST)
    public
    @ResponseBody
    LejiaResult searchImportOrderByCriteria(@RequestBody UnionImportOrderCriteria importOrderCriteria) {
        if (importOrderCriteria.getOffset() == null) {
            importOrderCriteria.setOffset(1);
        }
        if (importOrderCriteria.getMerNum() == null) {
            return LejiaResult.build(400, "无数据");
        }
        Page page = importOrderService.findOrderByPage(importOrderCriteria, 10);
        return LejiaResult.ok(page);
    }

    /***
     *     乐加鼓励金结算单
     * @param unionPosOrderCriteria
     * @return
     */
    @RequestMapping(value = "/unionPosOrder/findByCriteria", method = RequestMethod.POST)
    public
    @ResponseBody
    LejiaResult searchPosOrderByCriteria(@RequestBody UnionPosOrderCriteria unionPosOrderCriteria) {
        if (unionPosOrderCriteria.getOffset() == null) {
            unionPosOrderCriteria.setOffset(1);
        }
        if (unionPosOrderCriteria.getMerchantUserId() == null) {
            return LejiaResult.build(400, "无数据");
        }
        Page page = unionPosOrderService.findOrderByPage(unionPosOrderCriteria, 10);
        return LejiaResult.ok(page);
    }

}
