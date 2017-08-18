package com.jifenke.lepluslive.merchant.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MD5Util;
import com.jifenke.lepluslive.merchant.controller.dto.PwdDto;
import com.jifenke.lepluslive.merchant.domain.criteria.MerchantCriteria;
import com.jifenke.lepluslive.merchant.domain.criteria.PosOrderCriteria;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantBank;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.repository.MerchantUserResourceRepository;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.merchant.service.MerchantUserResourceService;
import com.jifenke.lepluslive.merchant.service.MerchantUserService;
import com.jifenke.lepluslive.security.SecurityUtils;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by xf on 16-12-5.
 */
@RestController
@RequestMapping(value="/api")
public class MerchantUserController {

    @Inject
    private MerchantService merchantService;
    @Inject
    private MerchantUserResourceService merchantUserResourceService;
    @Inject
    private MerchantUserService merchantUserService;

    /**
     *  获取当前登录用户信息
     * */
    @RequestMapping(value = "/merchantUser", method = RequestMethod.GET)
    public LejiaResult getMerchant() {
        MerchantUser merchantUser =  merchantService.findMerchantUserBySid(SecurityUtils.getCurrentUserLogin());
        MerchantUser merchantInfo = new MerchantUser();
        merchantInfo.setName(merchantUser.getName());
        merchantInfo.setMerchantName(merchantUser.getMerchantName());
        merchantInfo.setLinkMan(merchantUser.getLinkMan());
        merchantInfo.setCity(merchantUser.getCity());
        return LejiaResult.ok(merchantInfo);
    }

    /**
     *  查询当前商户下佣金数据
     */
    @RequestMapping(value="/merchantUser/comission",method = RequestMethod.GET)
    public LejiaResult getMerchantAvalibleCommission() {
        MerchantUser merchantUser = merchantService.findMerchantUserBySid(SecurityUtils.getCurrentUserLogin());
        List<Merchant> merchants = merchantUserResourceService.findMerchantsByMerchantUser(merchantUser);
        Map map = merchantService.findCommissionByMerchants(merchants);
        return LejiaResult.ok(map);
    }

    /**
     *  查询商户下所有的门店
     */
    @RequestMapping(value="/merchantUser/merchants",method=RequestMethod.GET)
    public LejiaResult getMerchants() {
        MerchantUser merchantUser = merchantService.findMerchantUserBySid(SecurityUtils.getCurrentUserLogin());
        List<Merchant> merchants = merchantUserResourceService.findMerchantsByMerchantUser(merchantUser);
        return LejiaResult.ok(merchants);
    }



    @RequestMapping(value = "/merchantUser/merchantsInfo",method = RequestMethod.GET)
    @ResponseBody
    public LejiaResult getMerchantsInfo(){
        MerchantUser merchantUser = merchantService.findMerchantUserBySid(SecurityUtils.getCurrentUserLogin());
        List<Object []>  merchantList = merchantUserResourceService.findMerchantsByMerchantUserSql(merchantUser.getName());
        return LejiaResult.ok(merchantList);
    }

    /**
     *  查询商户下所有门店的pos机信息
     */
    @RequestMapping(value="/merchantUser/findByMerchantPosUser",method=RequestMethod.GET)
    public LejiaResult findByMerchantPosUser() {
        MerchantUser user = merchantService.findMerchantUserBySid(SecurityUtils.getCurrentUserLogin());
        return LejiaResult.ok(merchantUserResourceService.findByMerchantPosUser(user.getName()));
    }

    @RequestMapping(value = "/merchantUser/posOrderByMerchantUser")
    @ResponseBody
    public LejiaResult findPosOrderByMerchantUser(@RequestBody PosOrderCriteria posOrderCriteria){
        posOrderCriteria.setState(1);
        PosOrderCriteria result = merchantUserResourceService.findPosOrderByMerchantUser(posOrderCriteria);
        return LejiaResult.ok(result);
    }


    /**
     * 根据商户信息查询商户旗下门店所有pos机信息
     * @param posOrderCriteria
     * @return
     */
    @RequestMapping(value = "/merchantUser/findPosInfoByMerchantUser")
    @ResponseBody
    public LejiaResult findPosInfoByMerchantUser(@RequestBody PosOrderCriteria posOrderCriteria){
        PosOrderCriteria result = merchantUserResourceService.findPosInfoByMerchantUser(posOrderCriteria);
        return LejiaResult.ok(result);
    }

    @RequestMapping(value = "/merchantUser/pageFindMerchantInfoByMerchantUser")
    @ResponseBody
    public LejiaResult pageFindMerchantInfoByMerchantUser(@RequestBody MerchantCriteria merchantCriteria){
        return LejiaResult.ok(merchantUserResourceService.pageFindMerchantInfoByMerchantUser(merchantCriteria));
    }
    /**
     * 获取当前商户下 :
     *      1.今日邀请会员;
     *      2.累计邀请会员;
     *      3.注册邀请红包;
     *      4.注册获得积分.
     */
    @RequestMapping(value = "/merchantUser/findLockerInfoByMerchantUser",method = RequestMethod.GET)
    @ResponseBody
    public LejiaResult findLockerInfoByMerchantUser() {
        String merchantSid = SecurityUtils.getCurrentUserLogin();
        MerchantUser merchantUser = merchantService.findMerchantUserBySid(merchantSid);
        Map map = merchantUserService.getLockerInfoByMerchant(merchantUser.getName());
        return LejiaResult.ok(map);
    }

    /**
     *  获取当前商户下的店主账号
     */
    @RequestMapping(value="/merchantUser/owerAccount")
    public LejiaResult getOwerAccount() {
        MerchantUser merchantUser = merchantService.findMerchantUserBySid(SecurityUtils.getCurrentUserLogin());
        List<MerchantUser> owerAccount = merchantUserService.findOwerAccount(merchantUser.getId());
        return LejiaResult.ok(owerAccount);
    }

    /**
     *  获取当前商户下的收银员账号
     */
    @RequestMapping(value="/merchantUser/cashierAccount")
    public LejiaResult getCashierAccount() {
        MerchantUser merchantUser = merchantService.findMerchantUserBySid(SecurityUtils.getCurrentUserLogin());
        List<MerchantUser> cashierAccount = merchantUserService.findCashierAccount(merchantUser.getId());
        return LejiaResult.ok(cashierAccount);
    }

    /**
     *  根据门店获取银行信息
     */
    @RequestMapping(value="/merchantUser/merchantBankInfo")
    public LejiaResult getMerchantBankInfo(Long id) {
        Merchant merchant = merchantService.findMerchantById(id);
        return LejiaResult.ok(merchant);
    }

    /**
     *  校验账号密码
     */
    @RequestMapping(value = "/merchantUser/checkInfo",method = RequestMethod.POST)
    public LejiaResult checkUserInfo(@RequestBody String passwd) {
        MerchantUser merchantUser = merchantService.findMerchantUserBySid(SecurityUtils.getCurrentUserLogin());
        String md5Pwd = MD5Util.MD5Encode(passwd,null);
        if(merchantUser.getPassword().equals(md5Pwd)) {
            return LejiaResult.ok();
        }else {
            return LejiaResult.build(400,"密码错误");
        }
    }

    /**
     *  创建账号
     */
    @RequestMapping(value="/merchantUser/createAccount",method = RequestMethod.POST)
    public LejiaResult createAccount(@RequestBody MerchantUser merchantUser) {
        try{
            MerchantUser user = merchantUserService.findByName(merchantUser.getName());
            MerchantUser currentUser = merchantService.findMerchantUserBySid(SecurityUtils.getCurrentUserLogin());
            if(user!=null) {
                return LejiaResult.build(400,"保存失败 ！");
            }
            String idStr = merchantUser.getLinkMan();         //  该账号所拥有的门店
            List<Merchant> merchants = null;
            if(idStr!=null&&!"".equals(idStr)) {
                if(idStr.contains("-1")) {                    //  所有门店
                    merchants = merchantUserResourceService.findMerchantsByMerchantUser(currentUser);
                }else {                                       //  选中门店
                    merchants = new ArrayList<>();
                    String[] ids = idStr.split("~");
                    for (String id : ids) {
                        if(!"".equals(id)&&id!=null) {
                            Merchant merchant = merchantService.findMerchantById(new Long(id));
                            merchants.add(merchant);
                        }
                    }
                }
            }
            merchantUser.setCreateUserId(currentUser.getId());
            merchantUser.setCreatedDate(new Date());
            merchantService.saveUserAccount(merchantUser,merchants);
            return LejiaResult.ok();
        }catch (Exception e) {
            e.printStackTrace();
            return LejiaResult.build(400,"保存失败 ！");
        }
    }

    /**
     * 校验用户名是否重复
     */
    @RequestMapping(value = "/merchantUser/checkRepeat",method = RequestMethod.POST)
    public LejiaResult checkUserRepeat(@RequestBody String username) {
        MerchantUser user = merchantUserService.findByName(username);
        if(user==null) {
            return LejiaResult.ok();
        }else {
            return LejiaResult.build(400,"用户名已存在");
        }
    }

    /**
     *  修改登录密码  17/05/08
     */
    @RequestMapping(value="/merchantUser/updatePwd",method = RequestMethod.POST)
    @ResponseBody
    public LejiaResult updatePwd(@RequestBody PwdDto pwdDto) {
        MerchantUser merchantUser = merchantService.findMerchantUserBySid(SecurityUtils.getCurrentUserLogin());
        boolean result = merchantUserService.updatePwd(merchantUser,pwdDto.getOldPwd(),pwdDto.getNewPwd());
        if(result) {
            return LejiaResult.ok();
        }else {
            return LejiaResult.build(400,"原密码错误！");
        }
    }

}
