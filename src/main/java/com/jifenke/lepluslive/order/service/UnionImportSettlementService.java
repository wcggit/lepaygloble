package com.jifenke.lepluslive.order.service;

import com.jifenke.lepluslive.order.domain.criteria.UnionImportSettlementCriteria;
import com.jifenke.lepluslive.order.domain.entities.UnionImportSettlement;
import com.jifenke.lepluslive.order.repository.UnionImportSettlementRepository;
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
public class UnionImportSettlementService {

    @Inject
    private UnionImportSettlementRepository unionImportSettlementRepository;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Page findOrderByPage(UnionImportSettlementCriteria unionCriteria, Integer limit) {
        Sort sort = new Sort(Sort.Direction.DESC, "settleDate");
        return unionImportSettlementRepository
            .findAll(getWhereClause(unionCriteria),
                new PageRequest(unionCriteria.getOffset() - 1, limit, sort));
    }

    public static Specification<UnionImportSettlement> getWhereClause(final UnionImportSettlementCriteria unionCriteria) {
        return new Specification<UnionImportSettlement>() {
            @Override
            public Predicate toPredicate(Root<UnionImportSettlement> r, CriteriaQuery<?> q,
                                         CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (unionCriteria.getStartDate() != null &&!"".equals(unionCriteria.getStartDate())) {
                    predicate.getExpressions().add(
                        cb.between(r.<Date>get("settleDate"),
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
