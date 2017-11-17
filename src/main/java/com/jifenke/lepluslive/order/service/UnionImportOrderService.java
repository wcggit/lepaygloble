package com.jifenke.lepluslive.order.service;

import com.jifenke.lepluslive.order.domain.criteria.UnionImportOrderCriteria;
import com.jifenke.lepluslive.order.domain.entities.UnionImportOrder;
import com.jifenke.lepluslive.order.repository.UnionImportOrderRepository;
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

/**
 * Created by wcg on 16/5/5.
 */
@Service
@Transactional(readOnly = true)
public class UnionImportOrderService {

    @Inject
    private UnionImportOrderRepository unionImportOrderRepository;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Page findOrderByPage(UnionImportOrderCriteria unionCriteria, Integer limit) {
        Sort sort = new Sort(Sort.Direction.DESC, "settleDate");
        return unionImportOrderRepository
            .findAll(getWhereClause(unionCriteria),new PageRequest(unionCriteria.getOffset() - 1, limit, sort));
    }

    public static Specification<UnionImportOrder> getWhereClause(final UnionImportOrderCriteria unionCriteria) {
        return new Specification<UnionImportOrder>() {
            @Override
            public Predicate toPredicate(Root<UnionImportOrder> r, CriteriaQuery<?> q,CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (unionCriteria.getSettleDate() != null &&!"".equals(unionCriteria.getSettleDate())) {
                    predicate.getExpressions().add(
                        cb.equal(r.get("settleDate"),unionCriteria.getSettleDate()));
                }
                if(unionCriteria.getMerNum()!=null&&!"".equals(unionCriteria.getMerNum())) {
                    predicate.getExpressions().add(
                        cb.equal(r.get("merNum"),unionCriteria.getMerNum()));
                }
                if(unionCriteria.getPayWay()!=null) {
                    predicate.getExpressions().add(
                        cb.equal(r.get("payWay"),unionCriteria.getPayWay()));
                }
                return predicate;
            }
        };
    }

}
