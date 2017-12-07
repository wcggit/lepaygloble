package com.jifenke.lepluslive.groupon.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.groupon.controller.view.GrouponOrderExcel;
import com.jifenke.lepluslive.groupon.domain.criteria.GrouponOrderCriteria;
import com.jifenke.lepluslive.groupon.domain.entities.GrouponOrder;
import com.jifenke.lepluslive.groupon.service.GrouponOrderService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * 团购订单 Controller
 *
 * @author XF
 * @date 2017/6/19
 */
@RestController
@RequestMapping("/manage")
public class GrouponOrderController {

    @Inject
    private GrouponOrderService grouponOrderService;
    @Inject
    private GrouponOrderExcel grouponOrderExcel;

    /**
     * 跳转到列表页面
     * Created by xf on 2017-06-21.
     */
    @RequestMapping("/grouponOrder/list")
    public ModelAndView toListPage() {
        return MvUtil.go("/groupon/orderList");
    }

    /**
     * 分页展示
     * Created by xf on 2017-06-19.
     */
    @RequestMapping("/grouponOrder/findByCriteria")
    public LejiaResult findByCriteria(@RequestBody GrouponOrderCriteria orderCriteria) {
        if (orderCriteria.getOffset() == null) {
            orderCriteria.setOffset(1);
        }
        Page<GrouponOrder> page = grouponOrderService.findByCriteria(orderCriteria, 10);
        return LejiaResult.ok(page);
    }


    /**
     * 导出 Excel
     * Created by xf on 2017-06-19.
     */
    @RequestMapping(value = "/grouponOrder/export", method = RequestMethod.POST)
    public ModelAndView export(GrouponOrderCriteria orderCriteria) {
        if (orderCriteria.getOffset() == null) {
            orderCriteria.setOffset(1);
        }
        Page<GrouponOrder> page = grouponOrderService.findByCriteria(orderCriteria, 10000);
        Map map = new HashMap();
        map.put("orderList", page.getContent());
        return new ModelAndView(grouponOrderExcel, map);
    }
}
