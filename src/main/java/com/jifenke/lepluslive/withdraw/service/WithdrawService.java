package com.jifenke.lepluslive.withdraw.service;

import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantWallet;
import com.jifenke.lepluslive.merchant.repository.MerchantWalletRepository;
import com.jifenke.lepluslive.withdraw.domain.criteria.WithdrawCriteria;
import com.jifenke.lepluslive.withdraw.domain.entities.WithdrawBill;
import com.jifenke.lepluslive.withdraw.repository.WithdrawRepository;

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
import java.util.List;

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


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void saveWithdrawBill(WithdrawBill withdrawBill) {
        withdrawRepository.save(withdrawBill);
    }

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
    public void createWithDrawBill(List<Merchant> merchants, MerchantUser merchantUser, Double amount) {
        // 账户清算：  所有门店佣金余额转账到商户钱包下
        Long transfer = 0L;
        for (Merchant merchant : merchants) {
            MerchantWallet wallet = merchantWalletRepository.findByMerchant(merchant);
            transfer += wallet.getAvailableBalance();
            wallet.setAvailableBalance(0L);
        }
        MerchantWallet merchantUserWallet = merchantWalletRepository.findByMerchantUser(merchantUser.getId());
        if(merchantUserWallet==null) {
            merchantUserWallet = new MerchantWallet();
            merchantUserWallet.setAvailableBalance(0L);
            merchantUserWallet.setMerchantUserId(merchantUser.getId());
            merchantWalletRepository.save(merchantUserWallet);
        }
        merchantUserWallet.setAvailableBalance(merchantUserWallet.getAvailableBalance()+transfer);
        //  生成提现单
        String randomBillSid = MvUtil.getOrderNumber();
        WithdrawBill withdrawBill = new WithdrawBill();
        withdrawBill.setMerchantUserId(merchantUser.getId());
        withdrawBill.setBankNumber(merchantUser.getMerchantBank().getBankNumber());
        withdrawBill.setBankName(merchantUser.getMerchantBank().getBankName());
        withdrawBill.setBillType(2);     //0是合伙人管理员  1是合伙人 2是商户
        withdrawBill.setState(0);        //0是申请中        1是提现完成   2已驳回
        withdrawBill.setWithdrawBillSid(randomBillSid);
        withdrawBill.setPayee(merchantUser.getPayee());
        int account = amount.intValue();
        Long totalPrice = account * 100L;    //  RMB:积分  (比率)  1:100
        withdrawBill.setTotalPrice(totalPrice);
        withdrawBill.setCreatedDate(new Date());
        withdrawRepository.save(withdrawBill);
        merchantWalletRepository.save(merchantUserWallet);
    }
}
