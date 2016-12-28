package com.jifenke.lepluslive.order.service;

import com.jifenke.lepluslive.merchant.domain.criteria.CodeOrderCriteria;
import com.jifenke.lepluslive.merchant.domain.criteria.CommissionDetailsCriteria;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.order.domain.criteria.FinancialCriteria;
import com.jifenke.lepluslive.order.domain.criteria.OLOrderCriteria;
import com.jifenke.lepluslive.order.domain.criteria.OrderShareCriteria;
import com.jifenke.lepluslive.order.domain.entities.FinancialStatistic;
import com.jifenke.lepluslive.order.domain.entities.MerchantScanPayWay;
import com.jifenke.lepluslive.order.domain.entities.OffLineOrder;
import com.jifenke.lepluslive.order.domain.entities.OffLineOrderShare;
import com.jifenke.lepluslive.order.repository.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
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

    @Inject
    private MerchantScanPayWayRepository merchantScanPayWayRepository;

    @Inject
    private ScanCodeOrderRepository scanCodeOrderRepository;

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

                if (orderCriteria.getRebateWay() != null) {
                    if (orderCriteria.getRebateWay() == 1) {
                        predicate.getExpressions().add(
                            cb.equal(r.get("rebateWay"),
                                     1));
                    } else if (orderCriteria.getRebateWay() == 2) {
                        predicate.getExpressions().add(
                            cb.notEqual(r.get("rebateWay"),
                                        1));
                        predicate.getExpressions().add(
                            cb.notEqual(r.get("rebateWay"),
                                        3));
                    } else {
                        predicate.getExpressions().add(
                            cb.equal(r.get("rebateWay"),
                                     3));
                    }

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

    /**
     *  计算会员消费次数,总金额和红包 (全部)
     * @param
     * @return
     */
    public Map<String,Long> countMemberOrdersDetail(List<Merchant> merchants) {
        Map<String,Long> map = new HashMap<>();
        Long totalCount = 0L;       // 次数
        Long totalSales = 0L;       // 总金额
        Long totalRebate = 0L;      // 共收到红包
        for (Merchant merchant : merchants) {
            MerchantScanPayWay payway = merchantScanPayWayRepository.findByMerchantId(merchant.getId());
            List<Object[]> details = null;
            if (payway==null) {
                details = offLineOrderRepository.countMemberOrderDetail(merchant.getId());
            }else {
                details = scanCodeOrderRepository.countScanOrderDetail(merchant.getId());
            }
            Long count = new Long(details.get(0)[0] ==null ?"0" : details.get(0)[0].toString());
            Long sales = new Long(details.get(0)[1] == null ? "0" : details.get(0)[1].toString());
            Long rebate = new Long(details.get(0)[2] == null ? "0" : details.get(0)[2].toString());
            totalCount+=(count==null?0L:count);
            totalSales+=(sales==null?0L:sales);
            totalRebate+=(rebate==null?0L:rebate);
        }
        map.put("totalCount",totalCount);
        map.put("totalSales",totalSales);
        map.put("totalRebate",totalRebate);
        return map;
    }


    /**
     *  计算会员消费次数,总金额和红包  (每日)
     * @param
     * @return
     */
    public Map<String,Long> countMemberDailyOrdersDetail(List<Merchant> merchants) {
        Map<String,Long> map = new HashMap<>();
        Long dailyCount = 0L;       // 次数
        Long dailySales = 0L;       // 总金额
        Long dailyRebate = 0L;      // 共收到红包
        for (Merchant merchant : merchants) {
            List<Object[]> details = offLineOrderRepository.countMemberDailyOrderDetail(merchant.getId());
            Long count = new Long(details.get(0)[0] ==null ?"0" : details.get(0)[0].toString());
            Long sales = new Long(details.get(0)[1] == null ? "0" : details.get(0)[1].toString());
            Long rebate = new Long(details.get(0)[2] == null ? "0" : details.get(0)[2].toString());
            dailyCount+=(count==null?0L:count);
            dailySales+=(sales==null?0L:sales);
            dailyRebate+=(rebate==null?0L:rebate);
        }
        map.put("dailyCount",dailyCount);
        map.put("dailySales",dailySales);
        map.put("dailyRebate",dailyRebate);
        return map;
    }


    /**
     * 多门店-今日交易金额：
     * @param merchants
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED,readOnly = true)
    public Long countDailyTransfering(List<Merchant> merchants) {
        Long totalTransfering = 0L;
        for(Merchant merchant:merchants) {
            Long transfering = null;
            MerchantScanPayWay payway = merchantScanPayWayRepository.findByMerchantId(merchant.getId());
             if (payway==null) {
                 transfering = offLineOrderRepository.countDailyTransferMoney(merchant.getId());
             }else {
                 transfering = scanCodeOrderRepository.countDailyScanTransferMoney(merchant.getId());
             }
             if(transfering!=null) {
                 totalTransfering += (transfering==null?0L:transfering);
             }
        }
        return totalTransfering;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Map countOrderDetail(Merchant merchant) {
        List<Object[]>
            details =
            offLineOrderRepository.countOrderDetail(merchant.getId());
        Map map = new HashMap<>();
        map.put("count", details.get(0)[0] == null ? 0 : details.get(0)[0]);
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
        if (olOrderCriteria.getRebateWay() != null) {
            if (olOrderCriteria.getRebateWay() == 2) {
                sql.append(" and rebate_way != 1");
            } else {
                sql.append(" and rebate_way = 1");
            }
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

    /**
     *  查看门店每日订单总额(线下)
     */
    @Transactional(propagation = Propagation.REQUIRED,readOnly = true)
    public Long countOffLineOrder(List<Merchant> merchants) {
        Long offLineDailyCount = 0L;
        for (Merchant merchant : merchants) {
            Long totalPrice = offLineOrderRepository.countTotalPrice(merchant.getId());
            offLineDailyCount += totalPrice==null?0L:totalPrice;
        }
        return offLineDailyCount;
    }


//    /**
//     * 根据商户ID查找旗下门店 扫码订单信息
//     *
//     * @param codeOrderCriteria 商户查询信息
//     * @return 商户旗下门店 扫码订单信息
//     *
//     */
//    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
//    public CodeOrderCriteria findCodeOrderByMerchantUser(CodeOrderCriteria codeOrderCriteria) {
//
//        int pageSize = codeOrderCriteria.getPageSize();
//        Sort.Order order =  new Sort.Order(Sort.Direction.DESC,"completeDate");
//        Sort sort =  new Sort(order);
//        PageRequest pagerequest = new PageRequest(codeOrderCriteria.getCurrentPage()-1, pageSize,sort);
//
//        Specification<OffLineOrder> specification = new Specification<OffLineOrder>() {
//            @Override
//            public Predicate toPredicate(Root<OffLineOrder> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//                Predicate predicate = cb.conjunction();
//                if (codeOrderCriteria.getStoreIds() != null) {
//                    predicate.getExpressions().add(root.get("merchant").get("id").in(codeOrderCriteria.getStoreIds()));
//                }
//                if (codeOrderCriteria.getRebateWay() != null) {
//                    predicate.getExpressions().add(cb.equal(root.get("rebateWay"), codeOrderCriteria.getRebateWay()));
//                }
//                if (codeOrderCriteria.getPayWay() != null) {
//                    predicate.getExpressions().add(cb.equal(root.get("payWay").get("id"), codeOrderCriteria.getPayWay()));
//                }
//                if (codeOrderCriteria.getOrderSid() != null && !"".equals(codeOrderCriteria.getOrderSid())) {
//                    predicate.getExpressions().add(cb.equal(root.get("orderSid"), codeOrderCriteria.getOrderSid()));
//                }
//                if (codeOrderCriteria.getStartDate() != null && !"".equals(codeOrderCriteria.getStartDate())
//                    && codeOrderCriteria.getEndDate() != null && !"".equals(codeOrderCriteria.getEndDate())) {
//                    predicate.getExpressions().add(cb.between(root.<Date>get("completeDate"),
//                                   new Date(codeOrderCriteria.getStartDate()),
//                                   new Date(codeOrderCriteria.getEndDate()))
//                    );
//                }
//                if(codeOrderCriteria.getState()!=null){
//                    predicate.getExpressions().add(cb.equal(root.get("state"),codeOrderCriteria.getState()));
//                }
//                return predicate;
//            }
//        };
//
//        /**
//         * 查询条件后的统计数据
//         */
//        StringBuffer sb = new StringBuffer();
//        sb.append("SELECT sum(o.total_price) as totalPrice , sum(o.true_pay) as truePay , sum(o.true_score) as trueScore , \n" +
//                  "sum(o.transfer_money_from_true_pay) as transferMoneyFromTruePay , \n" +
//                  "(sum(o.transfer_money)-sum(o.transfer_money_from_true_pay)) as transferMoneyHB \n" +
//                  "from off_line_order as o where");
//        if (codeOrderCriteria.getStoreIds() != null) {
//            sb.append(" o.merchant_id in (");
//            for(int i =0;i<codeOrderCriteria.getStoreIds().length;i++){
//                sb.append(codeOrderCriteria.getStoreIds()[i]+",");
//            }
//            sb.deleteCharAt(sb.length()-1);
//            sb.append(")");
//        }
//        if (codeOrderCriteria.getRebateWay() != null) {
//            sb.append(" and o.rebate_way = ");
//            sb.append(codeOrderCriteria.getRebateWay());
//        }
//        if (codeOrderCriteria.getPayWay() != null) {
//            sb.append(" and o.pay_way_id = ");
//            sb.append(codeOrderCriteria.getPayWay());
//        }
//        if (codeOrderCriteria.getOrderSid() != null && !"".equals(codeOrderCriteria.getOrderSid())) {
//            sb.append(" and o.order_sid=");
//            sb.append(codeOrderCriteria.getOrderSid());
//        }
//        if (codeOrderCriteria.getStartDate() != null && !"".equals(codeOrderCriteria.getStartDate())
//            && codeOrderCriteria.getEndDate() != null && !"".equals(codeOrderCriteria.getEndDate())) {
//            sb.append(" and o.complete_date between '");
//            sb.append(codeOrderCriteria.getStartDate());
//            sb.append("' and '");
//            sb.append(codeOrderCriteria.getEndDate());
//            sb.append("'");
//        }
//        if(codeOrderCriteria.getState()!=null){
//            sb.append(" and o.state=");
//            sb.append(codeOrderCriteria.getState());
//        }
//        Query query = em.createNativeQuery(sb.toString());
//
//        List<Object[]> details = query.getResultList();
//        codeOrderCriteria.setTotalPrice(Double.valueOf(details.get(0)[0] == null ? "0.0" : details.get(0)[0].toString()));
//        codeOrderCriteria.setTruePay(Double.valueOf(details.get(0)[1] == null ? "0.0" : details.get(0)[1].toString()));
//        codeOrderCriteria.setTrueScore(Double.valueOf(details.get(0)[2] == null ? "0.0" : details.get(0)[2].toString()));
//        codeOrderCriteria.setTransferMoneyFromTruePay(Double.valueOf(details.get(0)[3] == null ? "0.0" : details.get(0)[3].toString()));
//        codeOrderCriteria.setTransferMoneyHB(Double.valueOf(details.get(0)[4] == null ? "0.0" : details.get(0)[4].toString()));
//
//        Page<OffLineOrder> page = offLineOrderRepository.findAll(specification,pagerequest);
//        codeOrderCriteria.setPage(page);
//
//        return codeOrderCriteria;
//    }

    /**
     * 根据商户ID查找旗下门店 扫码订单信息
     *
     * @param codeOrderCriteria 商户查询信息
     * @return 商户旗下门店 扫码订单信息
     *
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public CodeOrderCriteria findCodeOrderByMerchantUser222(CodeOrderCriteria codeOrderCriteria) {

        if (codeOrderCriteria.getPayWay() != null && codeOrderCriteria.getPayWay() == 0) {
            codeOrderCriteria.setPayWay(1);//微信
        }else if (codeOrderCriteria.getPayWay() != null && codeOrderCriteria.getPayWay() == 1){
            codeOrderCriteria.setPayWay(2);//红包
        }
        /**
         * 查询条件后的统计数据
         */
        int start = codeOrderCriteria.getPageSize() * (codeOrderCriteria.getCurrentPage() - 1);

        StringBuffer sql = new StringBuffer();
        sql.append("select olo.order_sid,  m.name,  olo.pay_way_id,  olo.lepay_code,  olo.complete_date,  olo.total_price,  olo.true_score,  olo.rebate_way, "
                   + " olo.transfer_money_from_true_pay,  (olo.transfer_money - olo.transfer_money_from_true_pay),  ifnull(m.lj_commission,0),  olo.lj_commission, 222  "
                   + " from off_line_order olo INNER JOIN merchant m ON olo.merchant_id = m.id  where  ");
        if (codeOrderCriteria.getStoreIds() != null) {
            sql.append(" olo.merchant_id in (");
//            for(int i =0;i<codeOrderCriteria.getStoreIds().length;i++){
//                sql.append(codeOrderCriteria.getStoreIds()[i]+",");
//            }
//            sql.deleteCharAt(sql.length()-1);

            sql.append(codeOrderCriteria.getStoreIds()[0]);//改为单门店查询
            sql.append(")");
        }
        if (codeOrderCriteria.getRebateWay() != null && !"".equals(codeOrderCriteria.getRebateWay())) {//订单类型 3种
            sql.append(" and olo.rebate_way in ");
            sql.append(codeOrderCriteria.getRebateWay());
        }
        if (codeOrderCriteria.getPayWay() != null) {
            sql.append(" and olo.pay_way_id = ");
            sql.append(codeOrderCriteria.getPayWay());
        }
        if (codeOrderCriteria.getOrderSid() != null && !"".equals(codeOrderCriteria.getOrderSid())) {
            sql.append(" and olo.order_sid=");
            sql.append(codeOrderCriteria.getOrderSid());
        }
        if(codeOrderCriteria.getState()!=null){
            sql.append(" and olo.state=");
            sql.append(codeOrderCriteria.getState());
        }
        if (codeOrderCriteria.getStartDate() != null && codeOrderCriteria.getStartDate() != "") {
            sql.append(" and olo.complete_date between '");
            sql.append(codeOrderCriteria.getStartDate());
            sql.append("' and '");
            sql.append(codeOrderCriteria.getEndDate());
            sql.append("'");
        }
        sql.append(" order by olo.complete_date desc limit ");
        sql.append(start);
        sql.append(",");
        sql.append(codeOrderCriteria.getPageSize());

        Query query = em.createNativeQuery(sql.toString());
        List<Object[]> off_line_order = query.getResultList();
        codeOrderCriteria.setListCodeOrder(off_line_order);


        /**
         * 查询条件后的统计数据
         */
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT sum(o.total_price) as totalPrice , sum(o.true_pay) as truePay , sum(o.true_score) as trueScore , \n" +
                  "sum(o.transfer_money_from_true_pay) as transferMoneyFromTruePay , \n" +
                  "(sum(o.transfer_money)-sum(o.transfer_money_from_true_pay)) as transferMoneyHB, count(o.id) \n" +
                  "from off_line_order as o where");
        if (codeOrderCriteria.getStoreIds() != null) {
            sb.append(" o.merchant_id in (");
//            for(int i =0;i<codeOrderCriteria.getStoreIds().length;i++){
//                sb.append(codeOrderCriteria.getStoreIds()[i]+",");
//            }
//            sb.deleteCharAt(sb.length()-1);

            sb.append(codeOrderCriteria.getStoreIds()[0]);//改为单门店查询
            sb.append(")");
        }
        if (codeOrderCriteria.getRebateWay() != null && !"".equals(codeOrderCriteria.getRebateWay())) {//订单类型 3种
            sb.append(" and o.rebate_way in ");
            sb.append(codeOrderCriteria.getRebateWay());
        }
        if (codeOrderCriteria.getPayWay() != null) {
            sb.append(" and o.pay_way_id = ");
            sb.append(codeOrderCriteria.getPayWay());
        }
        if (codeOrderCriteria.getOrderSid() != null && !"".equals(codeOrderCriteria.getOrderSid())) {
            sb.append(" and o.order_sid=");
            sb.append(codeOrderCriteria.getOrderSid());
        }
        if (codeOrderCriteria.getStartDate() != null && !"".equals(codeOrderCriteria.getStartDate())
            && codeOrderCriteria.getEndDate() != null && !"".equals(codeOrderCriteria.getEndDate())) {
            sb.append(" and o.complete_date between '");
            sb.append(codeOrderCriteria.getStartDate());
            sb.append("' and '");
            sb.append(codeOrderCriteria.getEndDate());
            sb.append("'");
        }
        if(codeOrderCriteria.getState()!=null){
            sb.append(" and o.state=");
            sb.append(codeOrderCriteria.getState());
        }
        Query query_count = em.createNativeQuery(sb.toString());

        List<Object[]> details = query_count.getResultList();
        codeOrderCriteria.setTotalPrice(Double.valueOf(details.get(0)[0] == null ? "0.0" : details.get(0)[0].toString()));
        codeOrderCriteria.setTruePay(Double.valueOf(details.get(0)[1] == null ? "0.0" : details.get(0)[1].toString()));
        codeOrderCriteria.setTrueScore(Double.valueOf(details.get(0)[2] == null ? "0.0" : details.get(0)[2].toString()));
        codeOrderCriteria.setTransferMoneyFromTruePay(Double.valueOf(details.get(0)[3] == null ? "0.0" : details.get(0)[3].toString()));
        codeOrderCriteria.setTransferMoneyHB(Double.valueOf(details.get(0)[4] == null ? "0.0" : details.get(0)[4].toString()));

        //分页
        Integer listCount = Integer.valueOf(details.get(0)[5] == null ? "0" : details.get(0)[5].toString());
        codeOrderCriteria.setTotalCount(listCount);
        Integer pageSize = codeOrderCriteria.getPageSize();
        if (listCount <= pageSize){
            codeOrderCriteria.setTotalPages(1);
        }else {
            codeOrderCriteria.setTotalPages(listCount % pageSize == 0 ? listCount / pageSize : (listCount / pageSize) + 1);
        }

        return codeOrderCriteria;
    }

    /**
     * 根据门店ID查询二维码收款金额
     * @param merchantId
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Object[]> findMyCodePriceByMerchantid(Long merchantId) {
        return offLineOrderRepository.findMyCodePriceByMerchantid(merchantId);
    }

    /**
     *  查询 线下 佣金明细
     * @param commissionDetailsCriteria
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List getCommissionDetailsList_off_line(CommissionDetailsCriteria commissionDetailsCriteria) {

        int start = commissionDetailsCriteria.getPageSize() * (commissionDetailsCriteria.getCurrentPage() - 1);

        StringBuffer sql = new StringBuffer();
        sql.append("select  olo_olos.complete_date, wxu.nickname, wxu.head_image_url, merchant.name, olo_olos.total_price, olo_olos.true_pay, olo_olos.share_ from "
                   + " (select olo.complete_date as complete_date,  olo.le_jia_user_id as user_id,  olos.lock_merchant_id as merchant_id, "
                   + " olo.total_price as total_price,  olo.true_pay as true_pay,  olos.to_lock_merchant as share_ "
                   + " from off_line_order olo, off_line_order_share olos where olo.id = olos.off_line_order_id and olo.state = 1 ");
        if (commissionDetailsCriteria.getStoreIds() != null) {
            sql.append(" and olos.lock_merchant_id in (");
            for(int i =0;i<commissionDetailsCriteria.getStoreIds().length;i++){
                sql.append(commissionDetailsCriteria.getStoreIds()[i]+",");
            }
            sql.deleteCharAt(sql.length()-1);
            sql.append(")");
        }
        if (commissionDetailsCriteria.getStartDate() != null && commissionDetailsCriteria.getStartDate() != "") {
            sql.append(" and olo.complete_date between '");
            sql.append(commissionDetailsCriteria.getStartDate());
            sql.append("' and '");
            sql.append(commissionDetailsCriteria.getEndDate());
        }
        sql.append(" order by olo.complete_date desc limit ");
        sql.append(start);
        sql.append(",");
        sql.append(commissionDetailsCriteria.getPageSize());

        sql.append(" ) as olo_olos  left join merchant on olo_olos.merchant_id = merchant.id ");
        sql.append(" left join wei_xin_user wxu on olo_olos.user_id = wxu.le_jia_user_id ");

        Query query = em.createNativeQuery(sql.toString());
        List<Object[]> details = query.getResultList();
        return details;
    }

    /**
     *  查询 线下 佣金明细count
     * @param commissionDetailsCriteria
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List getCommissionDetails_off_line_count(CommissionDetailsCriteria commissionDetailsCriteria) {

        StringBuffer sql = new StringBuffer();
        sql.append(" select count(olo.id), sum(olos.to_lock_merchant) "
                   + " from off_line_order olo, off_line_order_share olos where olo.id = olos.off_line_order_id and olo.state = 1 ");
        if (commissionDetailsCriteria.getStoreIds() != null) {
            sql.append(" and olos.lock_merchant_id in (");
            for(int i =0;i<commissionDetailsCriteria.getStoreIds().length;i++){
                sql.append(commissionDetailsCriteria.getStoreIds()[i]+",");
            }
            sql.deleteCharAt(sql.length()-1);
            sql.append(")");
        }
        if (commissionDetailsCriteria.getStartDate() != null && commissionDetailsCriteria.getStartDate() != "") {
            sql.append(" and olo.complete_date between '");
            sql.append(commissionDetailsCriteria.getStartDate());
            sql.append("' and '");
            sql.append(commissionDetailsCriteria.getEndDate());
        }

        Query query = em.createNativeQuery(sql.toString());
        List<Object[]> details = query.getResultList();
        return details;
    }

}

