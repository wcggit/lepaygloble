package com.jifenke.lepluslive.order.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.order.controller.view.ScanCodeOrderExcel;
import com.jifenke.lepluslive.order.domain.criteria.ScanCodeOrderCriteria;
import com.jifenke.lepluslive.order.service.ScanCodeOrderService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xf on 17-10-11.
 */
@RestController
@RequestMapping("/api")
public class ScanCodeOrderController {

    @Inject
    private ScanCodeOrderService scanCodeOrderService;
    @Inject
    private ScanCodeOrderExcel scanCodeOrderExcel;

    @RequestMapping(value="/scanCodeOrder/findByCriteria",method = RequestMethod.POST)
    @ResponseBody
    public LejiaResult findByCriteria(@RequestBody ScanCodeOrderCriteria scanCodeOrderCriteria) {
        if(scanCodeOrderCriteria.getOffset()==null) {
            scanCodeOrderCriteria.setOffset(1);
        }
        Page page = scanCodeOrderService.findOrderByPage(scanCodeOrderCriteria, 10);
        return LejiaResult.ok(page);
    }

    @RequestMapping(value="/scanCodeOrder/export",method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView exporeExcel(@RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate, @RequestParam Long merchantId,
                                    @RequestParam(required = false) Integer payType, @RequestParam(required = false) Integer orderType, @RequestParam(required = false) String orderSid,
                                    @RequestParam(required = false) String settleDate, @RequestParam(required = false) Integer state,@RequestParam(required = false) Integer gatewayType) {
        ScanCodeOrderCriteria scanCodeOrderCriteria = new ScanCodeOrderCriteria();
        scanCodeOrderCriteria.setMerchantId(merchantId);
        scanCodeOrderCriteria.setPayType(payType);
        scanCodeOrderCriteria.setOrderType(orderType);
        scanCodeOrderCriteria.setState(state);
        scanCodeOrderCriteria.setGatewayType(gatewayType);
        if(settleDate!=null&&!"".equals(settleDate)) {
            scanCodeOrderCriteria.setSettleDate(settleDate);
        }
        if(scanCodeOrderCriteria.getOffset()==null) {
            scanCodeOrderCriteria.setOffset(1);
        }
        Map map = new HashMap<>();
        Page page = scanCodeOrderService.findOrderByPage(scanCodeOrderCriteria, 10);
        map.put("scOrderList",page.getContent());
        return new ModelAndView(scanCodeOrderExcel, map);
    }


}
