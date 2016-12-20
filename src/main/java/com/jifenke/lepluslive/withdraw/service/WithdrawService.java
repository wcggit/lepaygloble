package com.jifenke.lepluslive.withdraw.service;

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

/**
 * Created by xf on 2016/9/18.
 */
@Service
@Transactional
public class WithdrawService {

    @Inject
    private WithdrawRepository withdrawRepository;

    @Transactional(propagation = Propagation.REQUIRED,readOnly = false)
    public void saveWithdrawBill(WithdrawBill withdrawBill) {
        withdrawRepository.save(withdrawBill);
    }

    @Transactional(propagation = Propagation.REQUIRED,readOnly = true)
    public Page findPartnerWithDrawByCriteria(WithdrawCriteria withdrawCriteria,Integer limit) {
        Sort sort = new Sort(Sort.Direction.DESC,"createdDate");
        return withdrawRepository.findAll(getWhereClause(withdrawCriteria),new PageRequest(withdrawCriteria.getOffset()-1,limit,sort));
    }

    public static Specification<WithdrawBill> getWhereClause(WithdrawCriteria criteria) {
        return  new Specification<WithdrawBill>() {
            @Override
            public Predicate toPredicate(Root<WithdrawBill> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if(criteria.getBillType()!=null) {
                    predicate.getExpressions().add(cb.equal(root.get("billType"),criteria.getBillType()));
                }
                if(criteria.getPartner()!=null)  {
                    predicate.getExpressions().add(cb.equal(root.get("partner"),criteria.getPartner()));
                }
                if(criteria.getState()!=null) {
                    predicate.getExpressions().add(cb.equal(root.get("state"),criteria.getState()));
                }
                //  提现开始时间
                if(criteria.getWithDrawStartDate()!=null&&criteria.getWithDrawEndDate()!=null) {
                    predicate.getExpressions().add(cb.between(root.get("createdDate"),new Date(criteria.getWithDrawStartDate()),new Date(criteria.getWithDrawEndDate())));
                }
                //  提现结束时间
                if(criteria.getCompleteDateStartDate()!=null&&criteria.getCompleteDateEndDate()!=null) {
                    predicate.getExpressions().add(cb.between(root.get("completeDate"),new Date(criteria.getCompleteDateStartDate()),new Date(criteria.getCompleteDateEndDate())));
                }
                return predicate;
            }
        };
    }
}
