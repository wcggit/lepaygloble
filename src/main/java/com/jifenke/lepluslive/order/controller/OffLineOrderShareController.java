package com.jifenke.lepluslive.order.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.merchant.service.MerchantUserResourceService;
import com.jifenke.lepluslive.merchant.service.MerchantUserService;
import com.jifenke.lepluslive.order.domain.criteria.OrderShareCriteria;
import com.jifenke.lepluslive.order.domain.entities.OffLineOrder;
import com.jifenke.lepluslive.order.service.OffLineOrderService;
import com.jifenke.lepluslive.order.service.OffLineOrderShareService;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerManager;
import com.jifenke.lepluslive.partner.service.PartnerManagerService;
import com.jifenke.lepluslive.partner.service.PartnerService;
import com.jifenke.lepluslive.security.SecurityUtils;
import com.jifenke.lepluslive.withdraw.domain.criteria.WithdrawCriteria;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wanjun on 2016/12/15.
 */
@RestController
@RequestMapping("/api")
public class OffLineOrderShareController {

    @Inject
    private OffLineOrderShareService offLineOrderShareService;
    @Inject
    private MerchantUserResourceService merchantUserResourceService;
    @Inject
    private MerchantService merchantService;
    @Inject
    private PartnerManagerService partnerManagerService;
    @Inject
    private OffLineOrderService offLineOrderService;

    @RequestMapping(value = "/offLineOrder/todayCommissionAndTodayNumber", method = RequestMethod.GET)
    @ResponseBody
    public LejiaResult findTodayCommissionAndTodayNumber(){
        MerchantUser merchantUser = merchantService.findMerchantUserBySid(SecurityUtils.getCurrentUserLogin());
        List<Object []> list = merchantUserResourceService.findMerchantsByMerchantUserSql(merchantUser.getName());
        List<Object> mlist = new ArrayList<Object>();
        for (int i =0;i<list.size();i++){
            mlist.add(list.get(i)[0]);
        }
        Object obj = offLineOrderShareService.findTodayCommissionAndTodayNumber(mlist);
        return LejiaResult.ok(obj);
    }

    /**
     *  合伙人 - 佣金分润
     */
    @RequestMapping(value = "/offLineOrder/partner/share", method = RequestMethod.POST)
    public
    @ResponseBody
    LejiaResult searchPartnerShareListBycriterial(@RequestBody OrderShareCriteria olOrderCriteria) {
        if (olOrderCriteria.getOffset() == null) {
            olOrderCriteria.setOffset(1);
        }
        PartnerManager partnerManager = partnerManagerService.findByPartnerManagerSid(SecurityUtils.getCurrentUserLogin());
        olOrderCriteria.setPartnerManager(partnerManager);
        Page page = offLineOrderService.findOrderShareByPage(olOrderCriteria, 10);
        return LejiaResult.ok(page);
    }

    @RequestMapping(value = "/offLineOrder/partner/otherData", method = RequestMethod.POST)
    public LejiaResult cityPartnerFindOtherData(@RequestBody OrderShareCriteria orderShareCriteria) {
        PartnerManager partnerManager = partnerManagerService.findByPartnerManagerSid(SecurityUtils.getCurrentUserLogin());
        orderShareCriteria.setPartnerManager(partnerManager);
        Map map = offLineOrderService.findOtherData(orderShareCriteria);
        return  new LejiaResult(map);
    }
}
