package com.jifenke.lepluslive.order.service;

import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.order.domain.criteria.FinancialCriteria;
import com.jifenke.lepluslive.order.domain.criteria.OLOrderCriteria;
import com.jifenke.lepluslive.order.domain.criteria.OrderShareCriteria;
import com.jifenke.lepluslive.order.domain.entities.FinancialStatistic;
import com.jifenke.lepluslive.order.domain.entities.OffLineOrder;
import com.jifenke.lepluslive.order.domain.entities.OffLineOrderShare;
import com.jifenke.lepluslive.order.domain.entities.PayWay;
import com.jifenke.lepluslive.order.repository.FinancialStatisticRepository;
import com.jifenke.lepluslive.order.repository.OffLineOrderRepository;
import com.jifenke.lepluslive.order.repository.OffLineOrderShareRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
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
    private EntityManager em;

    @Inject
    private OffLineOrderShareRepository offLineOrderShareRepository;

    @Inject
    private FinancialStatisticRepository financialStatisticRepository;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Page findOrderByPage(OLOrderCriteria orderCriteria, Integer limit) {
        Sort sort = new Sort(Sort.Direction.DESC, "completeDate");
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

                if (orderCriteria.getOrderSid() != null && orderCriteria.getOrderSid() != "") {
                    predicate.getExpressions().add(
                        cb.like(r.get("orderSid"), "%" + orderCriteria.getOrderSid() + "%"));
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

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
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
                    Date start;
                    Date end;
                    Date date = new Date(financialCriteria.getStartDate());
                    Calendar calendar = new GregorianCalendar();
                    calendar.setTime(date);
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    start = calendar.getTime();
                    Date date2 = new Date(financialCriteria.getEndDate());
                    calendar.setTime(date2);
                    calendar.set(Calendar.HOUR_OF_DAY, 23);
                    calendar.set(Calendar.MINUTE, 59);
                    calendar.set(Calendar.SECOND, 59);
                    end = calendar.getTime();
                    predicate.getExpressions().add(
                        cb.between(r.<Date>get("balanceDate"),
                                   start,
                                   end));
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

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Map countTodayOrderDetail(Merchant merchant, Date start, Date end) {
        List<Object[]>
            details =
            offLineOrderRepository.countTodayOrderDetail(merchant.getId(), start, end);
        Map map = new HashMap<>();
        map.put("count", details.get(0)[0].toString());
        map.put("sales", details.get(0)[1] == null ? 0 : details.get(0)[1]);
        map.put("commission", details.get(0)[2] == null ? 0 : details.get(0)[2]);
        map.put("trueSales", details.get(0)[3] == null ? 0 : details.get(0)[3]);
        return map;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Map countOrderDetail(Merchant merchant) {
        List<Object[]>
            details =
            offLineOrderRepository.countOrderDetail(merchant.getId());
        Map map = new HashMap<>();
        map.put("count", details.get(0)[0].toString());
        map.put("sales", details.get(0)[1] == null ? 0 : details.get(0)[1]);
        map.put("commission", details.get(0)[2] == null ? 0 : details.get(0)[2]);
        map.put("trueSales", details.get(0)[3] == null ? 0 : details.get(0)[3]);
        return map;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Map orderStatistic(OLOrderCriteria olOrderCriteria) {
//        EntityManager em = entityManagerFactory.createEntityManager();

        StringBuffer sql = new StringBuffer();
        sql.append(
            "select sum(total_price),sum(lj_commission),sum(transfer_money) from off_line_order where merchant_id = ");
        sql.append(olOrderCriteria.getMerchant().getId());

        if (olOrderCriteria.getStartDate() != null && olOrderCriteria.getStartDate() != "") {
            sql.append(" and complete_date between '");
            sql.append(olOrderCriteria.getStartDate());
            sql.append("' and '");
            sql.append(olOrderCriteria.getEndDate());
            sql.append("'");
        }
        if (olOrderCriteria.getOrderSid() != null && olOrderCriteria.getOrderSid() != "") {
            sql.append(" and order_sid like '%");
            sql.append(olOrderCriteria.getOrderSid());
            sql.append("%'");
        }
        sql.append(" and state = 1 group by merchant_id");

        Query query = em.createNativeQuery(sql.toString());

        List<Object[]> details = query.getResultList();

//        em.close();

        Map map = new HashMap<>();
        if (details.size() == 1) {
            map.put("sales", details.get(0)[0] == null ? 0 : details.get(0)[0]);
            map.put("commission", details.get(0)[1] == null ? 0 : details.get(0)[1]);
            map.put("trueSales", details.get(0)[2] == null ? 0 : details.get(0)[2]);
        } else {
            map.put("sales", "0");
            map.put("commission", "0");
            map.put("trueSales", "0");
        }
        return map;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Map countDayCommission(Merchant merchant, Date start, Date end) {
        List<Object[]>
            details =
            offLineOrderShareRepository.countTodayCommission(merchant.getId(), start, end);
        Map map = new HashMap<>();
        map.put("count", details.get(0)[0]);
        map.put("dayCommission", details.get(0)[1]);
        return map;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Long countShareByMerchant(Merchant merchant) {
        return offLineOrderShareRepository.countByLockMerchant(merchant);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Page findOrderShareByPage(OrderShareCriteria orderCriteria, int limit) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        return offLineOrderShareRepository
            .findAll(getOrderShareClause(orderCriteria),
                     new PageRequest(orderCriteria.getOffset() - 1, limit, sort));
    }


    public static Specification<OffLineOrderShare> getOrderShareClause(
        final OrderShareCriteria orderCriteria) {
        return new Specification<OffLineOrderShare>() {
            @Override
            public Predicate toPredicate(Root<OffLineOrderShare> r, CriteriaQuery<?> q,
                                         CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();

                if (orderCriteria.getStartDate() != null
                    && orderCriteria.getStartDate() != "") {
                    Date start = new Date(orderCriteria.getStartDate());
                    Date end = new Date(orderCriteria.getEndDate());
                    predicate.getExpressions().add(
                        cb.between(r.<Date>get("createDate"),
                                   start,
                                   end));
                }

                if (orderCriteria.getMerchant() != null) {
                    predicate.getExpressions().add(
                        cb.equal(r.<Merchant>get("lockMerchant"),
                                 orderCriteria.getMerchant()));
                }
                return predicate;
            }
        };
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Map orderShareStatistic(OrderShareCriteria olOrderCriteria) {
//        EntityManager em = entityManagerFactory.createEntityManager();

        StringBuffer sql = new StringBuffer();
        sql.append(
            "select sum(off_line_order.total_price),sum(off_line_order_share.to_lock_merchant) from off_line_order,off_line_order_share where off_line_order_share.off_line_order_id = off_line_order.id and off_line_order_share.lock_merchant_id = ");
        sql.append(olOrderCriteria.getMerchant().getId());

        if (olOrderCriteria.getStartDate() != null && olOrderCriteria.getStartDate() != "") {
            sql.append(
                " and off_line_order.complete_date between '");
            sql.append(olOrderCriteria.getStartDate());
            sql.append("' and '");
            sql.append(olOrderCriteria.getEndDate() + "'");
        }
        Query query = em.createNativeQuery(sql.toString());

        List<Object[]> details = query.getResultList();

//        em.close();

        Map map = new HashMap<>();
        map.put("sales", details.get(0)[0] == null ? 0 : details.get(0)[0]);
        map.put("commission", details.get(0)[1] == null ? 0 : details.get(0)[1]);
        return map;
    }
}
