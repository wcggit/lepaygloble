package com.jifenke.lepluslive.merchant.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MD5Util;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;
import com.jifenke.lepluslive.merchant.controller.dto.PwdDto;
import com.jifenke.lepluslive.merchant.controller.view.MerchantDataView;
import com.jifenke.lepluslive.merchant.controller.view.MerchantUserView;
import com.jifenke.lepluslive.merchant.domain.criteria.MerchantCriteria;
import com.jifenke.lepluslive.merchant.domain.criteria.MerchantUserCriteria;
import com.jifenke.lepluslive.merchant.domain.criteria.PosOrderCriteria;
import com.jifenke.lepluslive.merchant.domain.criteria.StatsMerDailyDataCriteria;
import com.jifenke.lepluslive.merchant.domain.entities.*;
import com.jifenke.lepluslive.merchant.service.*;
import com.jifenke.lepluslive.order.service.MerchantScanPayWayService;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerManager;
import com.jifenke.lepluslive.partner.service.PartnerManagerService;
import com.jifenke.lepluslive.partner.service.PartnerService;
import com.jifenke.lepluslive.security.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import java.util.*;

/**
 * Created by xf on 16-12-5.
 */
@RestController
@RequestMapping(value = "/api")
public class MerchantUserController {

    @Inject
    private MerchantService merchantService;
    @Inject
    private MerchantUserResourceService merchantUserResourceService;
    @Inject
    private MerchantUserService merchantUserService;
    @Inject
    private MerchantScanPayWayService merchantScanPayWayService;
    @Inject
    private MerchantWeiXinUserService merchantWeiXinUserService;
    @Inject
    private MerchantUserShopService merchantUserShopService;
    @Inject
    private MerchantWalletOnlineService merchantWalletOnlineService;
    @Inject
    private MerchantWalletService merchantWalletService;
    @Inject
    private PartnerService partnerService;
    @Inject
    private StatsMerDailyDataService statsMerDailyDataService;
    @Inject
    private LeJiaUserService leJiaUserService;
    @Inject
    private MerchantUserView merchantUserView;
    @Inject
    private MerchantDataView merchantDataView;
    @Inject
    private PartnerManagerService partnerManagerService;
    /**
     * 获取当前登录用户信息
     */
    @RequestMapping(value = "/merchantUser", method = RequestMethod.GET)
    public LejiaResult getMerchant() {
        MerchantUser merchantUser = merchantService.findMerchantUserBySid(SecurityUtils.getCurrentUserLogin());
        MerchantUser merchantInfo = new MerchantUser();
        merchantInfo.setName(merchantUser.getName());
        merchantInfo.setMerchantSid(merchantUser.getPassword());            // 连接
        merchantInfo.setMerchantName(merchantUser.getMerchantName());
        merchantInfo.setLinkMan(merchantUser.getLinkMan());
        merchantInfo.setCity(merchantUser.getCity());
        merchantInfo.setId(merchantUser.getId());
        return LejiaResult.ok(merchantInfo);
    }

    /**
     * 查询当前商户下佣金数据
     */
    @RequestMapping(value = "/merchantUser/comission", method = RequestMethod.GET)
    public LejiaResult getMerchantAvalibleCommission() {
        MerchantUser merchantUser = merchantService.findMerchantUserBySid(SecurityUtils.getCurrentUserLogin());
        List<Merchant> merchants = merchantUserResourceService.findMerchantsByMerchantUser(merchantUser);
        Map map = merchantService.findCommissionByMerchants(merchants);
        return LejiaResult.ok(map);
    }

    /**
     * 查询商户下所有的门店
     */
    @RequestMapping(value = "/merchantUser/merchants", method = RequestMethod.GET)
    public LejiaResult getMerchants() {
        MerchantUser merchantUser = merchantService.findMerchantUserBySid(SecurityUtils.getCurrentUserLogin());
        List<Merchant> merchants = merchantUserResourceService.findMerchantsByMerchantUser(merchantUser);
        return LejiaResult.ok(merchants);
    }

    /***
     *  查询商户下所有的门店和支付方式
     */
    @RequestMapping(value = "/merchantUser/merchantsAndPayWay", method = RequestMethod.GET)
    public LejiaResult getMerchantsAndPayway() {
        MerchantUser merchantUser = merchantService.findMerchantUserBySid(SecurityUtils.getCurrentUserLogin());
        List<Object[]> merchantList = merchantUserResourceService.findMerchantsByMerchantUserSql(merchantUser.getName());
        Map map = new HashMap();
        map.put("merchants", merchantList);
        for (Object[] obj : merchantList) {
            Long merchantId = new Long(obj[0].toString());
            MerchantScanPayWay payWay = merchantScanPayWayService.findByMerchant(merchantId);
            if (payWay == null) {
                map.put("merchant-" + merchantId, payWay.getType());
            } else {
                map.put("merchant-" + payWay.getMerchantId(), payWay.getType());
            }

        }
        return LejiaResult.ok(map);
    }


    @RequestMapping(value = "/merchantUser/merchantsInfo", method = RequestMethod.GET)
    @ResponseBody
    public LejiaResult getMerchantsInfo() {
        MerchantUser merchantUser = merchantService.findMerchantUserBySid(SecurityUtils.getCurrentUserLogin());
        List<Object[]> merchantList = merchantUserResourceService.findMerchantsByMerchantUserSql(merchantUser.getName());
        return LejiaResult.ok(merchantList);
    }

    /**
     * 查询商户下所有门店的pos机信息
     */
    @RequestMapping(value = "/merchantUser/findByMerchantPosUser", method = RequestMethod.GET)
    public LejiaResult findByMerchantPosUser() {
        MerchantUser user = merchantService.findMerchantUserBySid(SecurityUtils.getCurrentUserLogin());
        return LejiaResult.ok(merchantUserResourceService.findByMerchantPosUser(user.getName()));
    }

    @RequestMapping(value = "/merchantUser/posOrderByMerchantUser")
    @ResponseBody
    public LejiaResult findPosOrderByMerchantUser(@RequestBody PosOrderCriteria posOrderCriteria) {
        posOrderCriteria.setState(1);
        PosOrderCriteria result = merchantUserResourceService.findPosOrderByMerchantUser(posOrderCriteria);
        return LejiaResult.ok(result);
    }


    /**
     * 根据商户信息查询商户旗下门店所有pos机信息
     *
     * @param posOrderCriteria
     * @return
     */
    @RequestMapping(value = "/merchantUser/findPosInfoByMerchantUser")
    @ResponseBody
    public LejiaResult findPosInfoByMerchantUser(@RequestBody PosOrderCriteria posOrderCriteria) {
        PosOrderCriteria result = merchantUserResourceService.findPosInfoByMerchantUser(posOrderCriteria);
        return LejiaResult.ok(result);
    }

    @RequestMapping(value = "/merchantUser/pageFindMerchantInfoByMerchantUser")
    @ResponseBody
    public LejiaResult pageFindMerchantInfoByMerchantUser(@RequestBody MerchantCriteria merchantCriteria) {
        return LejiaResult.ok(merchantUserResourceService.pageFindMerchantInfoByMerchantUser(merchantCriteria));
    }

    /**
     * 获取当前商户下 :
     * 1.今日邀请会员;
     * 2.累计邀请会员;
     * 3.注册邀请红包;
     * 4.注册获得积分.
     */
    @RequestMapping(value = "/merchantUser/findLockerInfoByMerchantUser", method = RequestMethod.GET)
    @ResponseBody
    public LejiaResult findLockerInfoByMerchantUser() {
        String merchantSid = SecurityUtils.getCurrentUserLogin();
        MerchantUser merchantUser = merchantService.findMerchantUserBySid(merchantSid);
        Map map = merchantUserService.getLockerInfoByMerchant(merchantUser.getName());
        return LejiaResult.ok(map);
    }

    /**
     * 获取当前商户下的店主账号
     */
    @RequestMapping(value = "/merchantUser/owerAccount")
    public LejiaResult getOwerAccount() {
        MerchantUser merchantUser = merchantService.findMerchantUserBySid(SecurityUtils.getCurrentUserLogin());
        List<MerchantUser> owerAccount = merchantUserService.findOwerAccount(merchantUser.getId());
        return LejiaResult.ok(owerAccount);
    }

    /**
     * 获取当前商户下的收银员账号及绑定微信号
     */
    @RequestMapping(value = "/merchantUser/cashierAccount")
    public LejiaResult getCashierAccount() {
        Map map = new HashMap();
        MerchantUser merchantUser = merchantService.findMerchantUserBySid(SecurityUtils.getCurrentUserLogin());
        List<MerchantUser> cashierAccount = merchantUserService.findCashierAccount(merchantUser.getId());
        List<MerchantWeiXinUser> merchantWeiXinUsers = merchantWeiXinUserService.findMerchantWeixinUserByMerchanUsers(cashierAccount);
        map.put("cashierAccount", cashierAccount);
        map.put("cashiers", merchantWeiXinUsers);
        return LejiaResult.ok(map);
    }

    /**
     * 解除登录账号与微信号的绑定关系
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/merchant/unBindWeiXinUser/{id}")
    public
    @ResponseBody
    LejiaResult unBindMerchantWeiXinUser(@PathVariable Long id) {
        merchantWeiXinUserService.unBindMerchantWeiXinUser(id);
        return LejiaResult.ok("成功解绑用户");
    }

    /***
     * 当前账户下所有账号
     */
    @RequestMapping(value = "/merchantUser/userAccount")
    public LejiaResult getUserAccount() {
        MerchantUser merchantUser = merchantService.findMerchantUserBySid(SecurityUtils.getCurrentUserLogin());
        if (merchantUser.getType() != 8) {
            return LejiaResult.build(400, "无权限");
        }
        List<MerchantUser> userAccount = merchantUserService.findUserAccount(merchantUser.getId());
        return LejiaResult.ok(userAccount);
    }

    /**
     * 根据门店获取银行信息
     */
    @RequestMapping(value = "/merchantUser/merchantBankInfo")
    public LejiaResult getMerchantBankInfo(Long id) {
        Merchant merchant = merchantService.findMerchantById(id);
        return LejiaResult.ok(merchant);
    }

    /**
     * 校验账号密码
     */
    @RequestMapping(value = "/merchantUser/checkInfo", method = RequestMethod.POST)
    public LejiaResult checkUserInfo(@RequestBody String passwd) {
        MerchantUser merchantUser = merchantService.findMerchantUserBySid(SecurityUtils.getCurrentUserLogin());
        String md5Pwd = MD5Util.MD5Encode(passwd, null);
        if (merchantUser.getPassword().equals(md5Pwd)) {
            return LejiaResult.ok();
        } else {
            return LejiaResult.build(400, "密码错误");
        }
    }

    /**
     * 创建账号
     */
    @RequestMapping(value = "/merchantUser/createAccount", method = RequestMethod.POST)
    public LejiaResult createAccount(@RequestBody MerchantUser merchantUser) {
        try {
            MerchantUser user = merchantUserService.findByName(merchantUser.getName());
            MerchantUser currentUser = merchantService.findMerchantUserBySid(SecurityUtils.getCurrentUserLogin());
            if (user != null || currentUser == null || currentUser.getType() != 8) {
                return LejiaResult.build(400, "保存失败 ！");
            }
            String idStr = merchantUser.getLinkMan();         //  该账号所拥有的门店
            List<Merchant> merchants = null;
            if (idStr != null && !"".equals(idStr)) {
                if (idStr.contains("-1")) {                    //  所有门店
                    merchants = merchantUserResourceService.findMerchantsByMerchantUser(currentUser);
                } else {                                       //  选中门店
                    merchants = new ArrayList<>();
                    String[] ids = idStr.split("~");
                    for (String id : ids) {
                        if (!"".equals(id) && id != null) {
                            Merchant merchant = merchantService.findMerchantById(new Long(id));
                            merchants.add(merchant);
                        }
                    }
                }
            }
            merchantUser.setCreateUserId(currentUser.getId());
            merchantUser.setCreatedDate(new Date());
            merchantService.saveUserAccount(merchantUser, merchants);
            return LejiaResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return LejiaResult.build(400, "保存失败 ！");
        }
    }

    @RequestMapping(value = "/merchantUser/accountUnbind/{sid}", method = RequestMethod.GET)
    public LejiaResult unbindAccount(@PathVariable String sid) {
        MerchantUser currentUser = merchantService.findMerchantUserBySid(SecurityUtils.getCurrentUserLogin());
        MerchantUser user = merchantUserService.findByMerchantSid(sid);
        if (user == null || currentUser == null || currentUser.getType() != 8) {
            return LejiaResult.build(400, "解绑失败 ！");
        }
        boolean result = merchantUserService.unbindAccount(user);
        if (result) {
            return LejiaResult.ok();
        } else {
            return LejiaResult.build(400, "解绑失败 ！");
        }
    }

    /**
     * 校验用户名是否重复
     */
    @RequestMapping(value = "/merchantUser/checkRepeat", method = RequestMethod.POST)
    public LejiaResult checkUserRepeat(@RequestBody String username) {
        MerchantUser user = merchantUserService.findByName(username);
        if (user == null) {
            return LejiaResult.ok();
        } else {
            return LejiaResult.build(400, "用户名已存在");
        }
    }

    /**
     * 修改登录密码  17/05/08
     */
    @RequestMapping(value = "/merchantUser/updatePwd", method = RequestMethod.POST)
    @ResponseBody
    public LejiaResult updatePwd(@RequestBody PwdDto pwdDto) {
        MerchantUser merchantUser = merchantService.findMerchantUserBySid(pwdDto.getUserSid());
        boolean result = merchantUserService.updatePwd(merchantUser, pwdDto.getOldPwd(), pwdDto.getNewPwd());
        if (result) {
            return LejiaResult.ok();
        } else {
            return LejiaResult.build(400, "管理员密码错误！");
        }
    }


    /**
     * 我的商户 - 商户列表
     * 17/11/13
     */
    @RequestMapping(value = "/merchantUser/findByCriteria", method = RequestMethod.POST)
    @ResponseBody
    public LejiaResult findByPage(@RequestBody MerchantUserCriteria merchantUserCriteria) {
        if (merchantUserCriteria.getOffset() == null) {
            merchantUserCriteria.setOffset(1);
        }
        Partner partner = null;
        if(merchantUserCriteria.getPartnerId()!=null) {
            partner = partnerService
                .findPartnerById(merchantUserCriteria.getPartnerId());
            if(partner!=null) {
                PartnerManager pm = partner.getPartnerManager();
                PartnerManager currPm = partnerManagerService.findByPartnerManagerSid(SecurityUtils.getCurrentUserLogin());
                if(!pm.getId().equals(currPm.getId())){
                    return LejiaResult.build(400, "登录状态异常");
                }
            }
        }else {
            partner = partnerService
                .findByPartnerSid(SecurityUtils.getCurrentUserLogin());
        }
        if (partner == null) {
            return LejiaResult.build(400, "登录状态异常");
        }
        merchantUserCriteria.setPartner(partner);
        merchantUserCriteria.setType(8);
        Page<MerchantUser> page = merchantUserService.findMerchantUserByPage(merchantUserCriteria, 10);
        List<MerchantUser> content = page.getContent();
        Map result = new HashMap();
        List<Map> data = new ArrayList<>();
        if (content.size() > 0) {
            for (MerchantUser merchantUser : content) {
                Map<String, Object> map = new HashMap();
                map.put("merchantNo", merchantUser.getId());
                map.put("merchantName", merchantUser.getMerchantName());
                map.put("cityName", merchantUser.getCity().getName());
                Long merchantCount = merchantUserShopService.countByMerchantUserId(merchantUser);
                Integer totalBind = merchantService.countBindUserByMerchantUser(merchantUser.getId());
                Long totalLimit = merchantService.countBindLimitByMerchantUser(merchantUser.getId());
                Long totalCommission = merchantWalletService.countTotalMoneyByMerchantUser(merchantUser.getId());
                map.put("merchantCount", merchantCount);
                map.put("totalBind", totalBind);
                map.put("totalLimit", totalLimit);
                map.put("totalCommission", totalCommission);
                map.put("linkMan", merchantUser.getLinkMan());
                map.put("phoneNum", merchantUser.getPhoneNum());
                map.put("createdDate", merchantUser.getCreatedDate());
                data.add(map);
            }
        }
        result.put("data", data);
        result.put("totalElements", page.getTotalElements());
        result.put("totalPages", page.getTotalPages());
        return LejiaResult.ok(result);
    }


    @RequestMapping(value = "/merchantUser/merchantList", method = RequestMethod.POST)
    @ResponseBody
    public LejiaResult findByMerchantList(@RequestBody StatsMerDailyDataCriteria criteria) {
        if (criteria.getOffset() == null) {
            criteria.setOffset(1);
        }
        Partner partner = null;
        if(criteria.getPartner()!=null) {                               // 城市合伙人
            partner = partnerService
                .findPartnerById(criteria.getPartner());
            if(partner!=null) {
                PartnerManager pm = partner.getPartnerManager();
                PartnerManager currPm = partnerManagerService.findByPartnerManagerSid(SecurityUtils.getCurrentUserLogin());
                if(!pm.getId().equals(currPm.getId())){
                    return LejiaResult.build(400, "登录状态异常");
                }
            }
        }else {                                                       // 天使合伙人
            partner = partnerService
                .findByPartnerSid(SecurityUtils.getCurrentUserLogin());
        }
        if (partner == null) {
            return LejiaResult.build(400, "登录状态异常");
        }
        Map<String, Object> map = statsMerDailyDataService.listByCriteria(criteria);
        List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("data");
        if (list != null && list.size() > 0) {
            for (Map<String, Object> m : list) {
                Long mid = Long.valueOf(String.valueOf(m.get("mid")));
                Long bindCount = leJiaUserService.countBindMerchantBindLeJiaUser(mid);
                m.put("bindCount", bindCount);
            }
        }
        return LejiaResult.ok(map);
    }


    @RequestMapping(value = "/merchantUser/findByCriteria/export", method = RequestMethod.GET)
    public ModelAndView exporeExcel(@RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate,
                                    @RequestParam(required = false) String linkMan,
                                    @RequestParam(required = false) String merchantName, @RequestParam(required = false) String phoneNum,@RequestParam(required = false) Long partnerId) {
        MerchantUserCriteria merchantUserCriteria = new MerchantUserCriteria();
        if(StringUtils.isNotBlank(startDate)) {
            merchantUserCriteria.setStartDate(startDate);
            merchantUserCriteria.setEndDate(endDate);
        }
        if(StringUtils.isNotBlank(linkMan)) {
            merchantUserCriteria.setLinkMan(linkMan);
        }
        if(StringUtils.isNoneBlank(merchantName)) {
            merchantUserCriteria.setMerchantName(merchantName);
        }
        if(StringUtils.isNoneBlank(phoneNum)) {
            merchantUserCriteria.setPhoneNum(phoneNum);
        }
        merchantUserCriteria.setOffset(1);
        Partner partner = null;
        if(partnerId!=null) {
            partnerService
                .findPartnerById(partnerId);
            if(partner!=null) {
                PartnerManager pm = partner.getPartnerManager();
                PartnerManager currPm = partnerManagerService.findByPartnerManagerSid(SecurityUtils.getCurrentUserLogin());
                if(!pm.getId().equals(currPm.getId())){
                    return null;
                }
            }
        }else {
            partnerService
                .findByPartnerSid(SecurityUtils.getCurrentUserLogin());
        }
        if (partner == null) {
            return null;
        } else {
            merchantUserCriteria.setPartner(partner);
        }
        merchantUserCriteria.setType(8);
        Page<MerchantUser> page = merchantUserService.findMerchantUserByPage(merchantUserCriteria, 1000);
        List<MerchantUser> content = page.getContent();
        List<Map> data = new ArrayList<>();
        if (content.size() > 0) {
            for (MerchantUser merchantUser : content) {
                Map<String, Object> map = new HashMap();
                map.put("merchantNo", merchantUser.getId());
                map.put("merchantName", merchantUser.getMerchantName());
                map.put("cityName", merchantUser.getCity().getName());
                Long merchantCount = merchantUserShopService.countByMerchantUserId(merchantUser);
                Integer totalBind = merchantService.countBindUserByMerchantUser(merchantUser.getId());
                Long totalLimit = merchantService.countBindLimitByMerchantUser(merchantUser.getId());
                Long totalCommission = merchantWalletService.countTotalMoneyByMerchantUser(merchantUser.getId());
                map.put("merchantCount", merchantCount);
                map.put("totalBind", totalBind);
                map.put("totalLimit", totalLimit);
                map.put("totalCommission", totalCommission);
                map.put("linkMan", merchantUser.getLinkMan());
                map.put("phoneNum", merchantUser.getPhoneNum());
                map.put("createdDate", merchantUser.getCreatedDate());
                data.add(map);
            }
        }
        Map map = new HashMap<>();
        map.put("merList", data);
        return new ModelAndView(merchantUserView, map);
    }


    @RequestMapping(value = "/merchantUser/merchantList/export", method = RequestMethod.GET)
    public ModelAndView merchantListExport(@RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate,
                                          @RequestParam(required = false) Integer partnership, @RequestParam(required = false) String merchant,@RequestParam(required = false) String merchantUserName,@RequestParam(required = false) Long partnerId) {
        StatsMerDailyDataCriteria criteria = new StatsMerDailyDataCriteria();
        if(StringUtils.isNotBlank(startDate)) {
            criteria.setStartDate(startDate);
            criteria.setEndDate(endDate);
        }
        if(StringUtils.isNotBlank(merchant)) {
            criteria.setMerchant(merchant);
        }
        if(StringUtils.isNoneBlank(merchantUserName)) {
            criteria.setMerchantUserName(merchantUserName);
        }
        if(partnership!=null) {
            criteria.setPartnership(partnership);
        }
        criteria.setOffset(1);
        criteria.setLimit(2000);
        Partner partner = null;
        if(partnerId!=null) {
            partnerService
                .findPartnerById(partnerId);
            if(partner!=null) {
                PartnerManager pm = partner.getPartnerManager();
                PartnerManager currPm = partnerManagerService.findByPartnerManagerSid(SecurityUtils.getCurrentUserLogin());
                if(!pm.getId().equals(currPm.getId())){
                    return null;
                }
            }
        }else {
            partnerService
                .findByPartnerSid(SecurityUtils.getCurrentUserLogin());
        }
        if (partner == null) {
            return null;
        } else {
            criteria.setPartner(partner.getId());
        }
        Map<String, Object> map = statsMerDailyDataService.listByCriteria(criteria);
        List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("data");
        if (list != null && list.size() > 0) {
            for (Map<String, Object> m : list) {
                Long mid = Long.valueOf(String.valueOf(m.get("mid")));
                Long bindCount = leJiaUserService.countBindMerchantBindLeJiaUser(mid);
                m.put("bindCount", bindCount);
            }
        }
        Map hashMap = new HashMap<>();
        hashMap.put("merDataList", list);
        return new ModelAndView(merchantDataView, hashMap);
    }

}
