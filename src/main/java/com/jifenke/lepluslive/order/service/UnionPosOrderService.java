package com.jifenke.lepluslive.order.service;

import com.jifenke.lepluslive.order.domain.criteria.UnionPosOrderCriteria;
import com.jifenke.lepluslive.order.domain.entities.UnionPosOrder;
import com.jifenke.lepluslive.order.repository.UnionPosOrderRepository;
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
 * Created by wcg on 16/5/5.
 */
@Service
@Transactional(readOnly = true)
public class UnionPosOrderService {

    @Inject
    private UnionPosOrderRepository UnionPosOrder;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Page findOrderByPage(UnionPosOrderCriteria unionCriteria, Integer limit) {
        Sort sort = new Sort(Sort.Direction.DESC, "tradeDate");
        return UnionPosOrder
            .findAll(getWhereClause(unionCriteria),
                new PageRequest(unionCriteria.getOffset() - 1, limit, sort));
    }

    public static Specification<UnionPosOrder> getWhereClause(final UnionPosOrderCriteria unionCriteria) {
        return new Specification<UnionPosOrder>() {
            @Override
            public Predicate toPredicate(Root<UnionPosOrder> r, CriteriaQuery<?> q,
                                         CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (unionCriteria.getStartDate() != null &&!"".equals(unionCriteria.getStartDate())) {
                    predicate.getExpressions().add(
                        cb.between(r.<Date>get("tradeDate"),
                            new Date(unionCriteria.getStartDate()),
                            new Date(unionCriteria.getEndDate())));
                }
                if(unionCriteria.getMerchantUserId()!=null&&!"".equals(unionCriteria.getMerchantUserId())) {
                    predicate.getExpressions().add(
                        cb.equal(r.get("merchantUserId"),unionCriteria.getMerchantUserId()));
                }
                return predicate;
            }
        };
    }

}
