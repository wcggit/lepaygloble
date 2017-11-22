package com.jifenke.lepluslive.withdraw.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.merchant.domain.entities.*;
import com.jifenke.lepluslive.merchant.service.*;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerManager;
import com.jifenke.lepluslive.partner.service.PartnerManagerService;
import com.jifenke.lepluslive.partner.service.PartnerService;
import com.jifenke.lepluslive.partner.service.PartnerWalletService;
import com.jifenke.lepluslive.security.SecurityUtils;
import com.jifenke.lepluslive.withdraw.domain.criteria.WithdrawCriteria;
import com.jifenke.lepluslive.withdraw.service.WithdrawService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Inject
    private MerchantWalletService merchantWalletService;
    @Inject
    private MerchantWalletOnlineService merchantWalletOnlineService;
    @Inject
    private MerchantUserService merchantUserService;
    @Inject
    private PartnerWalletService partnerWalletService;

    @RequestMapping(value = "/merchant_withdraw", method = RequestMethod.POST)
    public LejiaResult merchantWithDraw(HttpServletRequest request) {
        try {
            Double amount = new Double(request.getParameter("amount"));
            Long mid = new Long(request.getParameter("mid"));
            MerchantUser merchantUser = merchantService.findMerchantUserBySid(SecurityUtils.getCurrentUserLogin());
            if(merchantUser==null) {
                return LejiaResult.build(400,"无权限");
            }else if(merchantUser.getType()!=8 && merchantUser.getType()!=0) {
                return LejiaResult.build(400,"无权限");
            }
            //  获取 merchant 账户信息 , 生成随机订单号
            Merchant merchant = merchantService.findMerchantById(mid);
            boolean result = withdrawService.createWithDrawBill(merchant, amount);
            if(result) {
                return LejiaResult.ok();
            }else {
                String msg = "余额不足，提现订单生成失败。";
                LOG.error(msg);
                return LejiaResult.build(400, msg);
            }
        } catch (Exception e) {
            String msg = "提现订单生成失败:" + e.getMessage();
            LOG.error(msg);
            return LejiaResult.build(400, msg);
        }
    }

    @RequestMapping(value = "/partner_withdraw", method = RequestMethod.POST)
    public LejiaResult partnerWithdraw(HttpServletRequest request) {
        try {
            Double amount = new Double(request.getParameter("amount"));
            //  查询合伙人信息 ，生成随机订单号
            Partner partner = partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin());
            boolean result = withdrawService.createPartnerWithDrawBill(partner, amount);
            if(result) {
                return LejiaResult.ok();
            }else {
                return LejiaResult.build(400, "佣金余额不足！");
            }
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
        Partner
            partner = null;
        if(withdrawCriteria.getPartnerId()!=null) {
            partner = partnerService.findPartnerById(withdrawCriteria.getPartnerId());
            if(partner!=null) {
                PartnerManager pm = partner.getPartnerManager();
                PartnerManager currPm = partnerManagerService.findByPartnerManagerSid(SecurityUtils.getCurrentUserLogin());
                if(!pm.getId().equals(currPm.getId())){
                    return LejiaResult.build(400, "登录状态异常");
                }
            }
        }else {
            partner = partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin());
        }
        if (partner == null) {
            return LejiaResult.build(400, "登录状态异常");
        } else {
            withdrawCriteria.setPartner(partner);
        }
        withdrawCriteria.setBillType(1);
        if(withdrawCriteria.getOffset()==null) {
            withdrawCriteria.setOffset(1);
        }
        // 查询数据
        Page page  = withdrawService.findPartnerWithDrawByCriteria(withdrawCriteria,10);
        return  new LejiaResult(page);
    }

    /***
     *  提现详情
     *   - 佣金余额
     *   - 结算账户
     */
    @RequestMapping(value="/merchant_with_info/findById/{mid}",method = RequestMethod.GET)
    @ResponseBody
    public LejiaResult findMerchantInfoById(@PathVariable Long mid) {
        Merchant merchant = merchantService.findMerchantById(mid);
        MerchantWallet merchantWallet = merchantWalletService.findByMerchant(merchant.getId());
        MerchantWalletOnline merchantWalletOnline = merchantWalletOnlineService.findByMerchant(merchant.getId());
        Long avaiWith = 0L;
        if(merchantWallet!=null) {
            avaiWith+=merchantWallet.getAvailableBalance();
        }
        if(merchantWalletOnline!=null) {
            avaiWith+=merchantWalletOnline.getAvailableBalance();
        }
        MerchantBank merchantBank = merchant.getMerchantBank();
        String bankName = merchantBank.getBankName();
        String bankNumb = merchantBank.getBankNumber();
        Map map = new HashMap();
        map.put("bankName",bankName);
        map.put("bankNumb",bankNumb);
        map.put("avaiWith",avaiWith);
        map.put("merchantName",merchant.getName());
        return LejiaResult.ok(map);
    }
    /***
     *  提现记录
     */
    @RequestMapping(value = "/merchant_withdraw/findAll", method = RequestMethod.POST)
    @ResponseBody
    public LejiaResult merchantWithdrawFindByCriteria(@RequestBody WithdrawCriteria withdrawCriteria) {
        MerchantUser merchantUser = merchantUserService.findByMerchantSid(SecurityUtils.getCurrentUserLogin());
        if(merchantUser==null) {
            return LejiaResult.build(400,"无权限");
        }
        if(withdrawCriteria.getOffset()==null) {
            withdrawCriteria.setOffset(1);
        }
        if(withdrawCriteria.getMerchant()==null) {
            withdrawCriteria.setMerchantUser(merchantUser);
        }
        List<Object[]> list = withdrawService.findByMerchantWithDrawByCriteria(withdrawCriteria);
        Long totalElements = withdrawService.findByMerchantWithDrawCountByCriteria(withdrawCriteria);
        Long totalPages = 1L;
        if(totalElements>10) {
            Double pages = Math.ceil(new Double(totalElements) / 10.0);
            totalPages = new Long(pages.intValue());
        }
        Map map = new HashMap();
        map.put("content",list);
        map.put("totalPages",totalPages);
        return LejiaResult.ok(map);
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
