package com.jifenke.lepluslive.groupon.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.groupon.controller.view.GrouponCodeExcel;
import com.jifenke.lepluslive.groupon.domain.criteria.GrouponCodeCriteria;
import com.jifenke.lepluslive.groupon.domain.entities.GrouponCode;
import com.jifenke.lepluslive.groupon.domain.entities.GrouponOrder;
import com.jifenke.lepluslive.groupon.domain.entities.GrouponProduct;
import com.jifenke.lepluslive.groupon.service.GrouponCodeService;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.merchant.service.MerchantUserService;
import com.jifenke.lepluslive.security.SecurityUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.*;


/**
 * 团购券码 Controller
 *
 * @author XF
 * @date 2017/6/19
 */

@RestController
@RequestMapping("/api")
public class GrouponCodeController {

    @Inject
    private GrouponCodeService grouponCodeService;
    @Inject
    private GrouponCodeExcel grouponCodeExcel;
    @Inject
    private MerchantUserService merchantUserService;
    @Inject
    private MerchantService merchantService;

    /**
     * 核销明细  - 分页展示
     * Created by xf on 2017-12-06.
     */
    @RequestMapping("/grouponCode/findByCriteria")
    public LejiaResult findByCriteria(@RequestBody GrouponCodeCriteria codeCriteria) {
        if (codeCriteria.getOffset() == null) {
            codeCriteria.setOffset(1);
        }
        Page<GrouponCode> page = grouponCodeService.findByCriteria(codeCriteria, 10);
        List<GrouponCode> codes = page.getContent();
        List<Map<String,Objects>>  content = new ArrayList<>();
        for (GrouponCode code : codes) {
            Map map = new HashMap();
            GrouponOrder order = code.getGrouponOrder();
            GrouponProduct product = code.getGrouponProduct();
            map.put("productName",product.getName());
            map.put("orderSid",order.getOrderSid());
            map.put("productId",product.getSid());
            map.put("orderType",order.getOrderType());
            map.put("orderNum",order.getGrouponCodes().size());
            map.put("userName",order.getLeJiaUser().getWeiXinUser().getNickname());
            map.put("phoneNumber",order.getLeJiaUser().getPhoneNumber());
            map.put("orderPrice",order.getTotalPrice());
            map.put("vaildDate",code.getExpiredDate());
            map.put("checkDate",code.getCheckDate());
            content.add(map);
        }
        Map data = new HashedMap();
        data.put("content", content);
        data.put("totalPages", page.getTotalPages() );
        data.put("totalElements", page.getTotalElements());
        return LejiaResult.ok(data);
    }


    /**
     * 核销卷码
     *
     * @param sid
     * @return
     */
    @RequestMapping(value = "/grouponCode/check/{sid}/{mid}", method = RequestMethod.GET)
    public LejiaResult checkGrouponCode(@PathVariable String sid,@PathVariable Long mid) {
        GrouponCode grouponCode = grouponCodeService.findGrouponCodeBySid(sid);
        MerchantUser merchantUser = merchantService.findMerchantUserBySid(SecurityUtils.getCurrentUserLogin());
        Merchant merchant = merchantService.findMerchantById(mid);
        if(merchantUser==null) {
            return LejiaResult.build(400,"请重新登录！");
        }
        if (grouponCode != null && merchantUser != null) {
            if (grouponCode.getGrouponProduct().getMerchantUser().getId().equals(merchantUser.getCreateUserId())) {
                if (grouponCode.getState() != 0) {
                    return LejiaResult.build(500, "无效的卷码");
                } else {
                    //判断优惠券是否过期或未到使用时间
                    LejiaResult result = grouponCodeService.checkUseDate(grouponCode);
                    if (result != null) {
                        return result;
                    }
                    grouponCodeService.chargeOffGrouponCode(grouponCode, merchant, merchantUser.getName());
                    return LejiaResult.build(200, "核销成功");
                }
            } else {
                return LejiaResult.build(401, "无权限核销");
            }
        } else {
            return LejiaResult.build(500, "无效的卷码");
        }
    }

    @RequestMapping(value = "/grouponCode/grouponStatistics", method = RequestMethod.GET)
    public LejiaResult findGrouponStatistics(Long merchantId) {
        Merchant merchant = merchantService.findMerchantById(merchantId);
        if(merchant==null) {
            return LejiaResult.build(400,"参数有误");
        }
        Map map = new HashMap();
        List<Map<String, Object>> dailyData = grouponCodeService.findDailyStatistics(merchantId);
        List<Map<String, Object>> totalData = grouponCodeService.findTotalStatistics(merchantId);
        List<Map<String, Object>> dailyDetail = grouponCodeService.findDetailStatistics(merchantId);
        map.put("dailyData",dailyData);
        map.put("totalData",totalData);
        map.put("dailyDetail",dailyDetail);
        return LejiaResult.ok(map);
    }

}
