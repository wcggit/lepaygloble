package com.jifenke.lepluslive.partner.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.partner.controller.view.PartnerWalletLogExcel;
import com.jifenke.lepluslive.partner.domain.criteria.PartnerWalletLogCriteria;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerWallet;
import com.jifenke.lepluslive.partner.domain.entities.PartnerWalletOnline;
import com.jifenke.lepluslive.partner.service.*;
import com.jifenke.lepluslive.security.SecurityUtils;
import com.jifenke.lepluslive.weixin.domain.entities.Category;
import com.jifenke.lepluslive.weixin.service.CategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xf on 17-11-15.
 */
@RestController
@RequestMapping("/api")
public class PartnerWalletController {
    @Inject
    private PartnerWalletService partnerWalletService;

    @Inject
    private PartnerWalletOnlineService partnerWalletOnlineService;

    @Inject
    private PartnerWalletOnlineLogService partnerWalletOnlineLogService;

    @Inject
    private PartnerWalletLogService partnerWalletLogService;

    @Inject
    private PartnerService partnerService;

    @Inject
    private PartnerWalletLogExcel logtExcel;

    @Inject
    private CategoryService categoryService;

    /**
     * 佣金明细 - 数据
     *
     * @return
     */
    @RequestMapping(value = "/partner/commission/data", method = RequestMethod.GET)
    @ResponseBody
    public LejiaResult partnerCommission() {
        Partner
            partner =
            partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin());
        if (partner == null) {
            return LejiaResult.build(400, "登录状态异常");
        }
        PartnerWallet offWallet = partnerWalletService.findByPartner(partner);
        PartnerWalletOnline onWallet = partnerWalletOnlineService.findByPartner(partner);
        Long totalMoney = 0L;
        Long totalAvail = 0L;
        if (offWallet != null && offWallet.getTotalMoney() != null) {
            totalMoney += offWallet.getTotalMoney();
        }
        if (onWallet != null && onWallet.getTotalMoney() != null) {
            totalMoney += onWallet.getTotalMoney();
        }
        if (offWallet != null && offWallet.getAvailableBalance() != null) {
            totalAvail += offWallet.getAvailableBalance();
        }
        if (onWallet != null && onWallet.getAvailableBalance() != null) {
            totalAvail += onWallet.getAvailableBalance();
        }
        Long onIncome = partnerWalletOnlineLogService.countDailyIncomeCommissionByPartner(partner.getId());
        Long offIncome = partnerWalletLogService.countDailyIncomeCommissionByPartner(partner.getId());
        List<Category> offlineChangeOrigins = categoryService.findAllByCategory(5);
        List<Category> onlineChangeOrigins = categoryService.findAllByCategory(6);
        Map map = new HashMap<>();
        map.put("totalMoney", totalMoney);
        map.put("totalAvail", totalAvail);
        map.put("todayIncome", onIncome + offIncome);
        map.put("totalExpend", totalMoney - totalAvail);
        map.put("offlineOrigins", offlineChangeOrigins);
        map.put("onlineOrigins", onlineChangeOrigins);
        return LejiaResult.ok(map);
    }

    /**
     * 佣金明细 - 线上佣金记录 / 线下佣金记录
     *
     * @return
     */
    @RequestMapping(value = "/partner/commission/findByPage", method = RequestMethod.POST)
    @ResponseBody
    public LejiaResult partnerCommission(@RequestBody PartnerWalletLogCriteria criteria) {
        Partner
            partner =
            partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin());
        if (partner == null) {
            return LejiaResult.build(400, "登录状态异常");
        } else {
            criteria.setPartnerId(partner.getId());
        }
        if (criteria.getOffset() == null) {
            criteria.setOffset(1);
        }
        Map<String, Object> map = null;
        if(criteria.getLineType()==0) {
            map = partnerWalletOnlineLogService.listByCriteria(criteria);
        }else if(criteria.getLineType()==1){
            map =  partnerWalletLogService.listByCriteria(criteria);
        }else {
            map = partnerWalletOnlineLogService.listByCriteria(criteria);
        }
        return LejiaResult.ok(map);
    }


    @RequestMapping(value = "/partner/commission/findByPage/export", method = RequestMethod.GET)
    public ModelAndView offLineCommissionExport(@RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate, @RequestParam(required = false) Integer type,@RequestParam Integer lineType) {
        PartnerWalletLogCriteria criteria = new PartnerWalletLogCriteria();
        Partner
            partner =
            partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin());
        if (partner == null) {
            return null;
        } else {
            criteria.setPartnerId(partner.getId());
        }
        criteria.setOffset(1);
        criteria.setLimit(1000);
        criteria.setLineType(lineType);
        if(StringUtils.isNoneBlank(startDate)) {
            criteria.setStartDate(startDate);
            criteria.setEndDate(endDate);
        }
        if(type!=null){
            criteria.setType(type);
        }
        Map<String, Object> result = null;
        if(lineType==0) {
            result = partnerWalletOnlineLogService.listByCriteria(criteria);
        }else if(lineType==1){
            result =  partnerWalletLogService.listByCriteria(criteria);
        }else {
            result = partnerWalletOnlineLogService.listByCriteria(criteria);
        }
        return new ModelAndView(logtExcel,result);
    }


}
