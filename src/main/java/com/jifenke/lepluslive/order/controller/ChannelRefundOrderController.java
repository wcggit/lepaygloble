package com.jifenke.lepluslive.order.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.order.service.ChannelRefundOrderService;
import com.jifenke.lepluslive.yibao.domain.criteria.LedgerRefundOrderCriteria;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

/**
 * Created by xf on 17-10-12.
 */
@RestController
@RequestMapping("/api")
public class ChannelRefundOrderController {
    @Inject
    private ChannelRefundOrderService channelRefundOrderService;

    @RequestMapping(value = "/channel/refundList", method = RequestMethod.POST)
    @ResponseBody
    public LejiaResult findRefundList(@RequestBody LedgerRefundOrderCriteria refundCriteria) {
        if (refundCriteria.getOffset() == null) {
            refundCriteria.setOffset(1);
        }
        if (refundCriteria.getMerchantId() == null) {
            return LejiaResult.build(400, "无相关数据");
        }
        if(refundCriteria.getState()==null) {
            refundCriteria.setState(2);
        }
        // 设置前一天
        if(refundCriteria.getTradeDate()!=null && !"".equals(refundCriteria.getTradeDate())) {
            String tradeBefore = refundCriteria.getTradeDate();
            refundCriteria.setTradeDate(tradeBefore);
        }else {
            return LejiaResult.build(400, "无相关数据");
        }
        refundCriteria.setTradeDate("2017-09-19");
        Page page = channelRefundOrderService.findByCriteria(refundCriteria, 50);
        return LejiaResult.ok(page);
    }
}
