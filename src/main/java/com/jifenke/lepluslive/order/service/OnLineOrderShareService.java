package com.jifenke.lepluslive.order.service;

import com.jifenke.lepluslive.groupon.domain.entities.GrouponCode;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.order.domain.entities.OnLineOrderShare;
import com.jifenke.lepluslive.order.repository.OnLineOrderShareRepository;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerManager;
import com.jifenke.lepluslive.partner.service.PartnerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Created by wanjun on 2016/12/15.
 */
@Service
@Transactional(readOnly = true)
public class OnLineOrderShareService {

    @Inject
    private PartnerService partnerService;
    @Inject
    private OnLineOrderShareRepository orderShareRepository;


    /**
     * 订单分润  核销时分润   17/09/17
     *
     * @param grouponCode 分润数据
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void share(GrouponCode grouponCode) {
        long shareMoney = grouponCode.getShareToTradePartner() + grouponCode.getShareToTradePartnerManager() + grouponCode.getShareToLockMerchant() + grouponCode.getShareToLockPartner() + grouponCode.getShareToLockPartnerManager();
        //添加分润记录
        if (shareMoney > 0) {
            OnLineOrderShare onLineOrderShare = new OnLineOrderShare();
            LeJiaUser leJiaUser = grouponCode.getLeJiaUser();
            onLineOrderShare.setType(3);
            onLineOrderShare.setOrderSid(grouponCode.getSid());
            onLineOrderShare.setShareMoney(shareMoney);
            onLineOrderShare.setUserId(leJiaUser.getId());

            long toMerchant = grouponCode.getShareToLockMerchant();
            long toPartner = grouponCode.getShareToLockPartner();
            long toPartnerManager = grouponCode.getShareToLockPartnerManager();
            long toTradePartner = grouponCode.getShareToTradePartner();
            long toTradePartnerManager = grouponCode.getShareToTradePartnerManager();
            long toLePlusLife = 0;

            if (leJiaUser.getBindMerchant() != null) {
                if (toMerchant > 0) {
                    Merchant merchant = leJiaUser.getBindMerchant();
                    //分润给绑定商户
                    onLineOrderShare.setLockMerchant(merchant);
                    if (merchant.getPartnership() == 2) {     //如果是虚拟商户分润方式改变
                        toPartner += toMerchant;
                        toMerchant = 0;
                    } else {
                        onLineOrderShare.setLockMerchant(merchant);
                    }
                }
            } else {
                toLePlusLife += toMerchant;
                toMerchant = 0;
            }

            // 对用户绑定合伙人进行分润
            Partner bindPartner = leJiaUser.getBindPartner();
            if (bindPartner != null) {
                if (toPartner > 0) {
                    onLineOrderShare.setLockPartner(bindPartner);
                    partnerService.shareToPartner(toPartner, bindPartner, grouponCode.getSid(), 15006L);
                }
                // 对用户绑定的合伙人管理员
                PartnerManager bindPartnerManager = bindPartner.getPartnerManager();
                if (bindPartnerManager != null) {
                    if (toPartnerManager > 0) {
                        onLineOrderShare.setLockPartnerManager(bindPartnerManager);
                        partnerService.shareToPartnerManager(toPartnerManager, bindPartnerManager, grouponCode.getSid(), 15006L);
                    }
                } else {
                    toLePlusLife += toPartnerManager;
                    toPartnerManager = 0;
                }
            } else {
                toLePlusLife = toLePlusLife + toPartner + toPartnerManager;
                toPartner = 0;
                toPartnerManager = 0;
            }

            if (grouponCode.getMerchant() != null) {
                // 对交易商户及合伙人进行分润
                Merchant merchant = grouponCode.getMerchant();
                Partner tradePartner = merchant.getPartner();
                if (tradePartner != null) {
                    if (toTradePartner > 0) {
                        onLineOrderShare.setTradePartner(tradePartner);
                        partnerService.shareToPartner(toTradePartner, tradePartner, grouponCode.getSid(), 15006L);
                    }
                    //分润给交易商户的合伙人管理员
                    PartnerManager tradePartnerManager = tradePartner.getPartnerManager();
                    if (tradePartnerManager != null) {
                        if (toTradePartnerManager > 0) {
                            onLineOrderShare.setTradePartnerManager(tradePartnerManager);
                            partnerService.shareToPartnerManager(toTradePartnerManager, tradePartnerManager, grouponCode.getSid(), 15006L);
                        }
                    } else {
                        toLePlusLife += toTradePartnerManager;
                        toTradePartnerManager = 0;
                    }
                } else {
                    toLePlusLife = toLePlusLife + toTradePartner + toTradePartnerManager;
                    toTradePartner = 0;
                    toTradePartnerManager = 0;
                }
            }
            onLineOrderShare.setToLockMerchant(toMerchant);
            onLineOrderShare.setToLockPartner(toPartner);
            onLineOrderShare.setToLockPartnerManager(toPartnerManager);
            onLineOrderShare.setToTradePartner(toTradePartner);
            onLineOrderShare.setToTradePartnerManager(toTradePartnerManager);
            onLineOrderShare.setToLePlusLife(toLePlusLife);

            orderShareRepository.save(onLineOrderShare);
        }
    }
}
