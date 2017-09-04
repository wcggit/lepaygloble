package com.jifenke.lepluslive.withdraw.service;

import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.merchant.domain.entities.*;
import com.jifenke.lepluslive.merchant.repository.MerchantWalletLogRepository;
import com.jifenke.lepluslive.merchant.repository.MerchantWalletOnlineLogRepository;
import com.jifenke.lepluslive.merchant.repository.MerchantWalletOnlineRepository;
import com.jifenke.lepluslive.merchant.repository.MerchantWalletRepository;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.merchant.service.MerchantUserResourceService;
import com.jifenke.lepluslive.partner.domain.criteria.PartnerManagerCriteria;
import com.jifenke.lepluslive.partner.domain.entities.PartnerManager;
import com.jifenke.lepluslive.withdraw.domain.criteria.WithdrawCriteria;
import com.jifenke.lepluslive.withdraw.domain.entities.WithdrawBill;
import com.jifenke.lepluslive.withdraw.repository.WithdrawRepository;

import org.omg.CORBA.Object;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xf on 2016/9/18.
 */
@Service
@Transactional
public class WithdrawService {

    @Inject
    private WithdrawRepository withdrawRepository;
    @Inject
    private MerchantWalletRepository merchantWalletRepository;
    @Inject
    private MerchantWalletLogRepository merchantWalletLogRepository;
    @Inject
    private MerchantWalletOnlineRepository merchantWalletOnlineRepository;
    @Inject
    private MerchantWalletOnlineLogRepository merchantWalletOnlineLogRepository;


    @Inject
    private MerchantUserResourceService merchantUserResourceService;


    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<WithdrawBill> findByMerchantId(Long id) {
        return withdrawRepository.findByMerchantId(id);
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<WithdrawBill> findByPartnerId(Long id) {
        return withdrawRepository.findByPartnerId(id);
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void saveWithdrawBill(WithdrawBill withdrawBill) {
        withdrawRepository.save(withdrawBill);
    }

    /***
     *  城市合伙人 - 提现单
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Page findPartnerWithDrawByCriteria(WithdrawCriteria withdrawCriteria, Integer limit) {
        Sort sort = new Sort(Sort.Direction.DESC, "createdDate");
        return withdrawRepository.findAll(getWhereClause(withdrawCriteria), new PageRequest(withdrawCriteria.getOffset() - 1, limit, sort));
    }

    public static Specification<WithdrawBill> getWhereClause(WithdrawCriteria criteria) {
        return new Specification<WithdrawBill>() {
            @Override
            public Predicate toPredicate(Root<WithdrawBill> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (criteria.getBillType() != null) {
                    predicate.getExpressions().add(cb.equal(root.get("billType"), criteria.getBillType()));
                }
                if (criteria.getPartner() != null) {
                    predicate.getExpressions().add(cb.equal(root.get("partner"), criteria.getPartner()));
                }
                if (criteria.getPartnerManager() != null) {
                    predicate.getExpressions().add(cb.equal(root.get("partnerManager"), criteria.getPartnerManager()));
                }
                if (criteria.getState() != null) {
                    predicate.getExpressions().add(cb.equal(root.get("state"), criteria.getState()));
                }
                //  提现开始时间
                if (criteria.getWithDrawStartDate() != null && criteria.getWithDrawEndDate() != null) {
                    predicate.getExpressions().add(cb.between(root.get("createdDate"), new Date(criteria.getWithDrawStartDate()), new Date(criteria.getWithDrawEndDate())));
                }
                //  提现结束时间
                if (criteria.getCompleteDateStartDate() != null && criteria.getCompleteDateEndDate() != null) {
                    predicate.getExpressions().add(cb.between(root.get("completeDate"), new Date(criteria.getCompleteDateStartDate()), new Date(criteria.getCompleteDateEndDate())));
                }
                return predicate;
            }
        };
    }

    /**
     * 2.0 提现流程
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public boolean createWithDrawBill(MerchantUser merchantUser, Double amount) {
        //  获取该商户下所有门店
        List<Merchant> merchants = merchantUserResourceService.findMerchantsByMerchantUser(merchantUser);
        Double dbAmount = amount * 100;
        Long withDrawAmount = dbAmount.longValue();
        //  判断佣金总额是否大于提现金额
        Long available = 0L;
        for (Merchant merchant : merchants) {
            MerchantWallet merchantWallet = merchantWalletRepository.findByMerchantId(merchant.getId());
            MerchantWalletOnline merchantWalletOnline = merchantWalletOnlineRepository.findByMerchantId(merchant.getId());
            if (merchantWallet != null) {
                available += merchantWallet.getAvailableBalance();
            }
            if (merchantWalletOnline != null) {
                available += merchantWalletOnline.getAvailableBalance();
            }
        }
        // 如果可用金额小于提现金额 :  提现失败
        if (available < withDrawAmount) {
            return false;
        }
        // 对账户可用余额进行修改
        for (Merchant merchant : merchants) {
            MerchantWallet merchantWallet = merchantWalletRepository.findByMerchantId(merchant.getId());
            MerchantWalletOnline merchantWalletOnline = merchantWalletOnlineRepository.findByMerchantId(merchant.getId());
            if (merchantWallet != null && withDrawAmount > 0) {
                Long thisAva = merchantWallet.getAvailableBalance();
                if (thisAva >= withDrawAmount) {                            // 如果当前余额满足
                    thisAva = thisAva - withDrawAmount;
                    withDrawAmount = 0L;
                    // 创建日志
                    MerchantWalletLog merchantWalletLog = new MerchantWalletLog();
                    merchantWalletLog.setCreateDate(new Date());
                    merchantWalletLog.setMerchantId(merchant.getId());
                    merchantWalletLog.setBeforeChangeMoney(merchantWallet.getAvailableBalance());
                    merchantWalletLog.setAfterChangeMoney(thisAva);
                    merchantWalletLog.setType(3L);
                    merchantWalletLogRepository.save(merchantWalletLog);
                    // 更改金额
                    merchantWallet.setAvailableBalance(thisAva);
                    merchantWallet.setLastUpdate(new Date());
                    merchantWalletRepository.save(merchantWallet);
                } else {                                                  // 如果当前余额不足
                    withDrawAmount -= thisAva;
                    thisAva = 0L;
                    MerchantWalletLog merchantWalletLog = new MerchantWalletLog();
                    merchantWalletLog.setCreateDate(new Date());
                    merchantWalletLog.setMerchantId(merchant.getId());
                    merchantWalletLog.setBeforeChangeMoney(merchantWallet.getAvailableBalance());
                    merchantWalletLog.setAfterChangeMoney(thisAva);
                    merchantWalletLog.setType(3L);
                    merchantWalletLogRepository.save(merchantWalletLog);
                    merchantWallet.setAvailableBalance(thisAva);
                    merchantWallet.setLastUpdate(new Date());
                    merchantWalletRepository.save(merchantWallet);
                }
            }
            if (merchantWalletOnline != null && withDrawAmount > 0) {
                Long thisAva = merchantWalletOnline.getAvailableBalance();
                if (thisAva >= withDrawAmount) {                            // 如果当前余额满足
                    thisAva = thisAva - withDrawAmount;
                    withDrawAmount = 0L;
                    // 创建日志
                    MerchantWalletOnlineLog merchantWalletOnlineLog = new MerchantWalletOnlineLog();
                    merchantWalletOnlineLog.setCreateDate(new Date());
                    merchantWalletOnlineLog.setMerchantId(merchant.getId());
                    merchantWalletOnlineLog.setBeforeChangeMoney(merchantWalletOnline.getAvailableBalance());
                    merchantWalletOnlineLog.setAfterChangeMoney(thisAva);
                    merchantWalletOnlineLog.setChangeMoney(merchantWalletOnline.getAvailableBalance()-thisAva);
                    merchantWalletOnlineLog.setType(3L);
                    merchantWalletOnlineLogRepository.save(merchantWalletOnlineLog);
                    // 更改金额
                    merchantWalletOnline.setAvailableBalance(thisAva);
                    merchantWalletOnline.setLastUpdate(new Date());
                    merchantWalletOnlineRepository.save(merchantWalletOnline);
                } else {                                                  // 如果当前余额不足
                    withDrawAmount -= thisAva;
                    thisAva = 0L;
                    MerchantWalletOnlineLog merchantWalletOnlineLog = new MerchantWalletOnlineLog();
                    merchantWalletOnlineLog.setCreateDate(new Date());
                    merchantWalletOnlineLog.setMerchantId(merchant.getId());
                    merchantWalletOnlineLog.setBeforeChangeMoney(merchantWalletOnline.getAvailableBalance());
                    merchantWalletOnlineLog.setAfterChangeMoney(thisAva);
                    merchantWalletOnlineLog.setChangeMoney(merchantWalletOnline.getAvailableBalance()-thisAva);
                    merchantWalletOnlineLog.setType(3L);
                    merchantWalletOnlineLogRepository.save(merchantWalletOnlineLog);
                    // 更改金额
                    merchantWalletOnline.setAvailableBalance(thisAva);
                    merchantWalletOnline.setLastUpdate(new Date());
                    merchantWalletOnlineRepository.save(merchantWalletOnline);
                }
            }
        }
        //  生成提现单
        String randomBillSid = MvUtil.getOrderNumber();
        WithdrawBill withdrawBill = new WithdrawBill();
        withdrawBill.setMerchantUserId(merchantUser.getId());
        withdrawBill.setBankNumber(merchantUser.getMerchantBank().getBankNumber());
        withdrawBill.setBankName(merchantUser.getMerchantBank().getBankName());
        withdrawBill.setBillType(3);     //0是合伙人管理员  1是合伙人    2是门店  3是商户
        withdrawBill.setState(0);        //0是申请中       1是提现完成  2已驳回
        withdrawBill.setWithdrawBillSid(randomBillSid);
        withdrawBill.setPayee(merchantUser.getPayee());
        int account = amount.intValue();
        Long totalPrice = account * 100L;    //  RMB:积分  (比率)  1:100
        withdrawBill.setTotalPrice(totalPrice);
        withdrawBill.setCreatedDate(new Date());
        withdrawRepository.save(withdrawBill);
        return true;
    }
}
