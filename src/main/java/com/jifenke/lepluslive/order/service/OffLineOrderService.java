package com.jifenke.lepluslive.order.service;

import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.order.domain.criteria.FinancialCriteria;
import com.jifenke.lepluslive.order.domain.criteria.OLOrderCriteria;
import com.jifenke.lepluslive.order.domain.entities.FinancialStatistic;
import com.jifenke.lepluslive.order.domain.entities.OffLineOrder;
import com.jifenke.lepluslive.order.domain.entities.PayWay;
import com.jifenke.lepluslive.order.repository.FinancialStatisticRepository;
import com.jifenke.lepluslive.order.repository.OffLineOrderRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class OffLineOrderService {

    @Inject
    private OffLineOrderRepository offLineOrderRepository;

    @Inject
    private FinancialStatisticRepository financialStatisticRepository;

    public Page findOrderByPage(OLOrderCriteria orderCriteria, Integer limit) {
        Sort sort = new Sort(Sort.Direction.DESC, "createdDate");
        return offLineOrderRepository
            .findAll(getWhereClause(orderCriteria),
                     new PageRequest(orderCriteria.getOffset() - 1, limit, sort));
    }

    public static Specification<OffLineOrder> getWhereClause(final OLOrderCriteria orderCriteria) {
        return new Specification<OffLineOrder>() {
            @Override
            public Predicate toPredicate(Root<OffLineOrder> r, CriteriaQuery<?> q,
                                         CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (orderCriteria.getState() != null) {
                    predicate.getExpressions().add(
                        cb.equal(r.get("state"),
                                 orderCriteria.getState()));
                }


                if (orderCriteria.getStartDate() != null && orderCriteria.getStartDate() != "") {
                    predicate.getExpressions().add(
                        cb.between(r.<Date>get("completeDate"),
                                   new Date(orderCriteria.getStartDate()),
                                   new Date(orderCriteria.getEndDate())));
                }

                if (orderCriteria.getMerchant() != null) {
                    predicate.getExpressions().add(
                        cb.equal(r.<Merchant>get("merchant"),
                                 orderCriteria.getMerchant()));
                }
                return predicate;
            }
        };
    }

    public Page findFinancialByCirterial(FinancialCriteria financialCriteria, Integer limit) {
        Sort sort = new Sort(Sort.Direction.DESC, "balanceDate");
        return financialStatisticRepository
            .findAll(getFinancialClause(financialCriteria),
                     new PageRequest(financialCriteria.getOffset() - 1, limit, sort));
    }


    public static Specification<FinancialStatistic> getFinancialClause(
        final FinancialCriteria financialCriteria) {
        return new Specification<FinancialStatistic>() {
            @Override
            public Predicate toPredicate(Root<FinancialStatistic> r, CriteriaQuery<?> q,
                                         CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (financialCriteria.getState() != null) {
                    predicate.getExpressions().add(
                        cb.equal(r.get("state"),
                                 financialCriteria.getState()));
                }

                if (financialCriteria.getStartDate() != null
                    && financialCriteria.getStartDate() != "") {
                    predicate.getExpressions().add(
                        cb.between(r.<Date>get("balanceDate"),
                                   new Date(financialCriteria.getStartDate()),
                                   new Date(financialCriteria.getEndDate())));
                }

                if (financialCriteria.getMerchant() != null) {
                    predicate.getExpressions().add(
                        cb.equal(r.<Merchant>get("merchant"),
                                 financialCriteria.getMerchant()));
                }
                return predicate;
            }
        };
    }



    public Map countTodayOrderDetail(Merchant merchant, Date start, Date end) {
        List<Object[]>
            details =
            offLineOrderRepository.countTodayOrderDetail(merchant.getId(), start, end);
        Map map = new HashMap<>();
            map.put("count", details.get(0)[0].toString());
            map.put("sales", details.get(0)[1]==null?0:details.get(0)[1]);
            map.put("commission", details.get(0)[2]==null?0:details.get(0)[2]);
            map.put("trueSales", details.get(0)[3]==null?0:details.get(0)[3]);
        return map;
    }

    public Map countOrderDetail(Merchant merchant) {
        List<Object[]>
            details =
            offLineOrderRepository.countOrderDetail(merchant.getId());
        Map map = new HashMap<>();
        map.put("count", details.get(0)[0].toString());
        map.put("sales", details.get(0)[1]==null?0:details.get(0)[1]);
        map.put("commission", details.get(0)[2]==null?0:details.get(0)[2]);
        map.put("trueSales", details.get(0)[3]==null?0:details.get(0)[3]);
        return map;
    }
}
