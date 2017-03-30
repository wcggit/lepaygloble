package com.jifenke.lepluslive.withdraw.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.merchant.service.MerchantUserResourceService;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerManager;
import com.jifenke.lepluslive.partner.service.PartnerManagerService;
import com.jifenke.lepluslive.partner.service.PartnerService;
import com.jifenke.lepluslive.security.SecurityUtils;
import com.jifenke.lepluslive.withdraw.domain.criteria.WithdrawCriteria;
import com.jifenke.lepluslive.withdraw.domain.entities.WithdrawBill;
import com.jifenke.lepluslive.withdraw.service.WithdrawService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by xf on 2016/9/18.
 */
@RestController
@RequestMapping("/withdraw")
public class WithdrawController {

    private final Logger LOG = LoggerFactory.getLogger(WithdrawController.class);

    @Inject
    private MerchantService merchantService;
    @Inject
    private PartnerService partnerService;
    @Inject
    private WithdrawService withdrawService;
    @Inject
    private MerchantUserResourceService merchantUserResourceService;
    @Inject
    private PartnerManagerService partnerManagerService;

    @RequestMapping(value = "/merchant_withdraw", method = RequestMethod.POST)
    public LejiaResult merchantWithDraw(HttpServletRequest request) {
        try {
            Long amount = new Long(request.getParameter("amount"));
            //  获取 merchant 账户信息 , 生成随机订单号
            Merchant
                merchant =
                merchantService.findMerchantUserBySid(SecurityUtils.getCurrentUserLogin())
                    .getMerchant();
            String randomBillSid = MvUtil.getOrderNumber();
            //  生成订单实例
            WithdrawBill withdrawBill = new WithdrawBill();
            withdrawBill.setMerchant(merchant);
            withdrawBill.setBankNumber(merchant.getMerchantBank().getBankNumber());
            withdrawBill.setBankName(merchant.getMerchantBank().getBankName());
            withdrawBill.setBillType(2);     //0是合伙人管理员  1是合伙人 2是商户
            withdrawBill.setState(0);        //0是申请中        1是提现完成   2已驳回
            withdrawBill.setWithdrawBillSid(randomBillSid);
            withdrawBill.setPayee(merchant.getPayee());
            Long totalPrice = amount * 100;    //  RMB:积分  (比率)  1:100
            withdrawBill.setTotalPrice(totalPrice);
            withdrawBill.setCreatedDate(new Date());
            withdrawService.saveWithdrawBill(withdrawBill);
            return LejiaResult.ok();
        } catch (Exception e) {
            String msg = "提现订单生成失败:" + e.getMessage();
            LOG.error(msg);
            return LejiaResult.build(400, msg);
        }
    }

    @RequestMapping(value = "/partner_withdraw", method = RequestMethod.POST)
    public LejiaResult partnerWithdraw(HttpServletRequest request) {
        try {
            Long amount = new Long(request.getParameter("amount"));
            //  查询合伙人信息 ，生成随机订单号
            Partner partner = partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin());
            String randomBillSid = MvUtil.getOrderNumber();
            //  生成订单实例
            WithdrawBill withdrawBill = new WithdrawBill();
            withdrawBill.setPartner(partner);
            withdrawBill.setBankNumber(partner.getBankNumber());
            withdrawBill.setBankName(partner.getBankName());
            withdrawBill.setBillType(1);
            withdrawBill.setState(0);
            withdrawBill.setWithdrawBillSid(randomBillSid);
            withdrawBill.setPayee(partner.getPayee());
            Long totalPrice = amount * 100;
            withdrawBill.setTotalPrice(totalPrice);
            withdrawBill.setCreatedDate(new Date());
            withdrawService.saveWithdrawBill(withdrawBill);
            return LejiaResult.ok();
        } catch (Exception e) {
            String msg = "提现订单生成失败:" + e.getMessage();
            LOG.error(msg);
            return LejiaResult.build(400, msg);
        }

    }

    /**
     * 分页查询提现详情
     * @param withdrawCriteria
     * @return
     */
    @RequestMapping(value = "/partner_withdraw/findAll", method = RequestMethod.POST)
    @ResponseBody
    public LejiaResult partnerFindByCriteria(@RequestBody WithdrawCriteria withdrawCriteria) {
        // 获取当前登录合伙人
        Partner partner = partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin());
        withdrawCriteria.setPartner(partner);
        withdrawCriteria.setBillType(1);
        if(withdrawCriteria.getOffset()==null) {
            withdrawCriteria.setOffset(1);
        }
        // 查询数据
        Page page  = withdrawService.findPartnerWithDrawByCriteria(withdrawCriteria,10);
        return  new LejiaResult(page);
    }

    //  2.0 版本生成提现单
    /**
     *  生成提现单
     */
     @RequestMapping(value="/merchant_user_withdraw",method = RequestMethod.POST)
     @ResponseBody
     public LejiaResult merchantUserWithDraw(HttpServletRequest request) {
         try {
             Double amount = new Double(request.getParameter("amount"));
             MerchantUser merchantUser = merchantService.findMerchantUserBySid(SecurityUtils.getCurrentUserLogin());
             List<Merchant> merchants = merchantUserResourceService.findMerchantsByMerchantUser(merchantUser);
             withdrawService.createWithDrawBill(merchants,merchantUser,amount);
             return LejiaResult.ok();
         } catch (Exception e) {
             String msg = "提现订单生成失败:" + e.getMessage();
             LOG.error(msg);
             return LejiaResult.build(400, msg);
         }
     }

    /**
     * 分页查询提现详情
     * @param withdrawCriteria
     * @return
     */
    @RequestMapping(value = "/cityPartner_withdraw/findAll", method = RequestMethod.POST)
    @ResponseBody
    public LejiaResult cityPartnerFindByCriteria(@RequestBody WithdrawCriteria withdrawCriteria) {
        PartnerManager partnerManager = partnerManagerService.findByPartnerManagerSid(SecurityUtils.getCurrentUserLogin());
        withdrawCriteria.setPartnerManager(partnerManager);
        withdrawCriteria.setBillType(0);
        if(withdrawCriteria.getOffset()==null) {
            withdrawCriteria.setOffset(1);
        }
        // 查询数据
        Page page  = withdrawService.findPartnerWithDrawByCriteria(withdrawCriteria,10);
        return  new LejiaResult(page);
    }
}
