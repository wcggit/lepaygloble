package com.jifenke.lepluslive.order.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUserShop;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.merchant.service.MerchantUserShopService;
import com.jifenke.lepluslive.order.domain.entities.OffLineOrder;
import com.jifenke.lepluslive.order.domain.entities.ScanCodeOrder;
import com.jifenke.lepluslive.order.service.ChannelRefundOrderService;
import com.jifenke.lepluslive.order.service.OffLineOrderService;
import com.jifenke.lepluslive.order.service.ScanCodeOrderService;
import com.jifenke.lepluslive.security.SecurityUtils;
import com.jifenke.lepluslive.yibao.domain.criteria.LedgerRefundOrderCriteria;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xf on 17-10-12.
 */
@RestController
@RequestMapping("/api")
public class ChannelRefundOrderController {
    @Inject
    private ChannelRefundOrderService channelRefundOrderService;
    @Inject
    private ScanCodeOrderService scanCodeOrderService;
    @Inject
    private OffLineOrderService offLineOrderService;
    @Inject
    private MerchantService merchantService;


    /**
     *  查询退款单记录
     */
    @RequestMapping(value = "/channel/refundList", method = RequestMethod.POST)
    @ResponseBody
    public LejiaResult findRefundList(@RequestBody LedgerRefundOrderCriteria refundCriteria) {
        if (refundCriteria.getOffset() == null) {
            refundCriteria.setOffset(1);
        }
        if (refundCriteria.getMerchantId() == null) {
            return LejiaResult.build(400, "无相关数据");
        }
     /*   if(refundCriteria.getState()==null) {
            refundCriteria.setState(2);
        }*/
        // 设置前一天
        if(refundCriteria.getTradeDate()!=null && !"".equals(refundCriteria.getTradeDate())) {
            String tradeBefore = refundCriteria.getTradeDate();
            refundCriteria.setTradeDate(tradeBefore);
        }else {
            return LejiaResult.build(400, "无相关数据");
        }
        Page page = channelRefundOrderService.findByCriteria(refundCriteria, 50);
        return LejiaResult.ok(page);
    }


    /**
     * 查询通道订单信息
     *
     * @param orderSid  订单sid
     * @param
     */
    @RequestMapping(value = "/channel/refund/{orderSid}", method = RequestMethod.GET)
    public Map queryOrder(@PathVariable String orderSid) {
        ScanCodeOrder scanCodeOrder =  scanCodeOrderService.findByOrderSid(orderSid);
        OffLineOrder offLineOrder =  offLineOrderService.findByOrderSid(orderSid);
        MerchantUser merchantUser =  merchantService.findMerchantUserBySid(SecurityUtils.getCurrentUserLogin());
        Map map = new HashMap();
        if (scanCodeOrder != null) {
            if(!channelRefundOrderService.checkSecurity(scanCodeOrder.getMerchant(),merchantUser)) {
                map.put("status",400);
                map.put("msg","无操作权限！");
                return map;
            }
            return scanCodeOrderService.getRefundInfo(orderSid);
        } else if (offLineOrder != null) {
            if(!channelRefundOrderService.checkSecurity(offLineOrder.getMerchant(),merchantUser)) {
                map.put("status",400);
                map.put("msg","无操作权限！");
                return map;
            }
            return offLineOrderService.getRefundInfo(orderSid);
        } else {
            map.put("status",400);
            map.put("msg","未找到相应数据");
        }
    }

    /**
     *  发起退款申请
     */
    @RequestMapping(value = "/refund/request",method = RequestMethod.GET)
    public LejiaResult createRefund(@RequestParam String orderSid,@RequestParam Integer orderFrom) {
        Map<String, Object> map = channelRefundOrderService.createRefundRequest(orderSid, orderFrom);
        return LejiaResult.build(Integer.valueOf(String.valueOf(map.get("status"))),String.valueOf(map.get("msg")));
    }



}
