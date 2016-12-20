package com.jifenke.lepluslive.partner.service;

import com.jifenke.lepluslive.partner.domain.criteria.PartnerRechargeCriteria;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerRecharge;
import com.jifenke.lepluslive.partner.domain.entities.PartnerWallet;
import com.jifenke.lepluslive.partner.repository.PartnerRechargeRepository;
import com.jifenke.lepluslive.partner.repository.PartnerWalletRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


/**
 * Created by xf on 2016/10/9.
 */
@Service
@Transactional(readOnly = true)
public class PartnerRechargeService {


    @Inject
    private PartnerRechargeRepository partnerRechargeRepository;
    @Inject
    private PartnerWalletRepository partnerWalletRepository;

    @Transactional(readOnly = false)
    public void saveParterRecharge(PartnerRecharge partnerRecharge) {
        partnerRecharge.setScorea(partnerRecharge.getScorea() * 100);     // 红包 1:100
        partnerRecharge.setCreateTime(new Date());
        partnerRecharge.setRechargeState(0);                              //  0 未审核   1 已审核   2 已驳回
        partnerRechargeRepository.save(partnerRecharge);
    }


    @Transactional(readOnly = true)
    public Page<PartnerRecharge> findByCriteria(PartnerRechargeCriteria criteria, Integer limit) {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        return partnerRechargeRepository
            .findAll(getWhereCause(criteria),
                     new PageRequest(criteria.getOffset() - 1, limit, sort));
    }

    public static Specification<PartnerRecharge> getWhereCause(PartnerRechargeCriteria criteria) {
        return new Specification<PartnerRecharge>() {
            @Override
            public Predicate toPredicate(Root<PartnerRecharge> root, CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                //  根据合伙人查询
                if (criteria.getPartner() != null) {
                    predicate.getExpressions()
                        .add(cb.equal(root.get("partner"), criteria.getPartner()));
                }
                //  根据申请状态查询
                if (criteria.getRechargeState() != null) {
                    predicate.getExpressions().add(
                        cb.equal(root.get("rechargeState"), criteria.getRechargeState()));
                }
                return predicate;
            }
        };
    }
}
