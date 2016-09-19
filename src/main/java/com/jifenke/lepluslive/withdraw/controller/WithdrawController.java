package com.jifenke.lepluslive.withdraw.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.service.PartnerService;
import com.jifenke.lepluslive.withdraw.domain.entities.WithdrawBill;
import com.jifenke.lepluslive.withdraw.service.WithdrawService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

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

    @RequestMapping(value = "/merchant_withdraw", method = RequestMethod.POST)
    public LejiaResult merchantWithDraw(HttpServletRequest request) {
        try {
            Long id = new Long(request.getParameter("id"));
            Long amount = new Long(request.getParameter("amount"));
            //  根据 id 获取 merchant 账户信息 , 生成随机订单号
            Merchant merchant = merchantService.findMerchantById(id);
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
            Long id = new Long(request.getParameter("id"));
            Long amount = new Long(request.getParameter("amount"));
            //  根据 id 查询合伙人信息 ，生成随机订单号
            Partner partner = partnerService.findPartnerById(id);
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

}
