package com.jifenke.lepluslive.partner.service;


import com.jifenke.lepluslive.partner.domain.criteria.PartnerScoreLogCriteria;
import com.jifenke.lepluslive.partner.domain.entities.PartnerScoreLog;
import com.jifenke.lepluslive.partner.repository.PartnerScoreLogRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created by xf on 2016/10/12.
 */
@Transactional(readOnly=true)
@Service
public class PartnerScoreLogService {

    @Inject
    private PartnerScoreLogRepository scoreLogRepository;

    @Transactional(readOnly = true)
    public Page findPageScoreByCriteria(PartnerScoreLogCriteria scoreLogCriteria,Integer limit) {
        Sort sort = new Sort(Sort.Direction.DESC,"createDate");
        return scoreLogRepository.findAll(getWhereClause(scoreLogCriteria),new PageRequest(scoreLogCriteria.getOffset()-1,10,sort));
    }

    public static Specification<PartnerScoreLog> getWhereClause(PartnerScoreLogCriteria scoreLogCriteria) {
        return new Specification<PartnerScoreLog>() {
            @Override
            public Predicate toPredicate(Root<PartnerScoreLog> root, CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                // 合伙人
                if(scoreLogCriteria.getPartnerId()!=null ) {
                    predicate.getExpressions().add(cb.equal(root.get("partnerId"),scoreLogCriteria.getPartnerId()));
                }
                // 1 增值记录
                if(scoreLogCriteria.getNumberType()!=null && scoreLogCriteria.getNumberType()==1)  {
                    predicate.getExpressions().add(cb.greaterThan(root.get("number"),0));
                }
                // -1 减扣记录
                if(scoreLogCriteria.getNumberType()!=null && scoreLogCriteria.getNumberType()==-1) {
                    predicate.getExpressions().add(cb.lessThan(root.get("number"),0));
                }
                return predicate;
            }
        };
    }

}
