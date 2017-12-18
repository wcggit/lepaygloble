package com.jifenke.lepluslive.order.service;

import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.merchant.domain.criteria.CodeOrderCriteria;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantScanPayWay;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantWallet;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.order.domain.criteria.FinancialCriteria;
import com.jifenke.lepluslive.order.domain.criteria.OLOrderCriteria;
import com.jifenke.lepluslive.order.domain.criteria.OrderShareCriteria;
import com.jifenke.lepluslive.order.domain.entities.FinancialStatistic;
import com.jifenke.lepluslive.order.domain.entities.OffLineOrder;
import com.jifenke.lepluslive.order.domain.entities.OffLineOrderShare;
import com.jifenke.lepluslive.order.repository.*;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerManager;
import com.jifenke.lepluslive.partner.domain.entities.PartnerManagerWallet;
import com.jifenke.lepluslive.partner.domain.entities.PartnerWallet;
import com.jifenke.lepluslive.partner.service.PartnerService;
import com.jifenke.lepluslive.partner.service.PartnerWalletService;
import com.jifenke.lepluslive.score.domain.entities.ScoreA;
import com.jifenke.lepluslive.score.domain.entities.ScoreC;
import com.jifenke.lepluslive.score.service.ScoreAService;
import com.jifenke.lepluslive.score.service.ScoreCService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

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

    @Inject
    private ScoreAService scoreAService;

    @Inject
    private ScoreCService scoreCService;

    @Inject
    private ShareService shareService;

    @Inject
    private MerchantService merchantService;

    @Inject
    private PartnerService partnerService;

    @Inject
    private PartnerWalletService partnerWalletService;

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

                if (orderCriteria.getStartDate() != null &&!"".equals(orderCriteria.getStartDate())) {
                    predicate.getExpressions().add(
                        cb.between(r.<Date>get("completeDate"),
                            new Date(orderCriteria.getStartDate()),
                            new Date(orderCriteria.getEndDate())));
                }

                if (orderCriteria.getRebateWay() != null&&!"".equals(orderCriteria.getRebateWay())) {
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

                if(orderCriteria.getPayWay()!=null) {
                    if(orderCriteria.getPayWay()==0) {
                        predicate.getExpressions().add(
                            cb.equal(r.get("trueScore"), 0));
                    }else if(orderCriteria.getPayWay()==1) {
                        predicate.getExpressions().add(
                            cb.equal(r.get("truePay"), 0));
                    }else {
                        predicate.getExpressions().add(
                            cb.notEqual(r.get("trueScore"), 0));
                        predicate.getExpressions().add(
                            cb.notEqual(r.get("truePay"), 0));
                    }
                }

                if (orderCriteria.getOrderSid() != null && !"".equals(orderCriteria.getOrderSid())) {
                    predicate.getExpressions().add(
                        cb.like(r.get("orderSid"), "%" + orderCriteria.getOrderSid() + "%"));
                }


                if (orderCriteria.getPayType() != null && !"".equals(orderCriteria.getPayType())) {
                    predicate.getExpressions().add(
                        cb.equal(r.get("payType"), orderCriteria.getPayType()));
                }


                if (orderCriteria.getOrderType() != null) {
                    if(orderCriteria.getOrderType()==0) {
                        predicate.getExpressions().add(cb.equal(r.get("basicType"),0));
                    }else if(orderCriteria.getOrderType()==1) {
                        predicate.getExpressions().add(cb.equal(r.get("basicType"),1));
                    }
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
                if (financialCriteria.getState() != null && financialCriteria.getState() != -1) {
                    predicate.getExpressions().add(
                        cb.equal(r.<Merchant>get("state"),
                            financialCriteria.getState()));
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
     * 计算会员消费次数,总金额和红包 (全部)
     *
     * @param
     * @return
     */
    public Map<String, Long> countMemberOrdersDetail(List<Merchant> merchants) {
        Map<String, Long> map = new HashMap<>();
        Long totalCount = 0L;       // 次数
        Long totalSales = 0L;       // 总金额
        Long totalRebate = 0L;      // 共收到红包
        for (Merchant merchant : merchants) {
            MerchantScanPayWay payway = merchantScanPayWayRepository.findByMerchantId(merchant.getId());
            List<Object[]> details = null;
            if (payway == null||payway.getType()==1) {
                details = offLineOrderRepository.countMemberOrderDetail(merchant.getId());
            } else {
                details = scanCodeOrderRepository.countScanOrderDetail(merchant.getId());
            }
            Long count = new Long(details.get(0)[0] == null ? "0" : details.get(0)[0].toString());
            Long sales = new Long(details.get(0)[1] == null ? "0" : details.get(0)[1].toString());
            Long rebate = new Long(details.get(0)[2] == null ? "0" : details.get(0)[2].toString());
            totalCount += (count == null ? 0L : count);
            totalSales += (sales == null ? 0L : sales);
            totalRebate += (rebate == null ? 0L : rebate);
        }
        map.put("totalCount", totalCount);
        map.put("totalSales", totalSales);
        map.put("totalRebate", totalRebate);
        return map;
    }


    /**
     * 计算会员消费次数,总金额和红包  (每日)
     *
     * @param
     * @return
     */
    public Map<String, Long> countMemberDailyOrdersDetail(List<Merchant> merchants) {
        Map<String, Long> map = new HashMap<>();
        Long dailyCount = 0L;       // 次数
        Long dailySales = 0L;       // 总金额
        Long dailyRebate = 0L;      // 共收到红包
        for (Merchant merchant : merchants) {
            List<Object[]> details = offLineOrderRepository.countMemberDailyOrderDetail(merchant.getId());
            Long count = new Long(details.get(0)[0] == null ? "0" : details.get(0)[0].toString());
            Long sales = new Long(details.get(0)[1] == null ? "0" : details.get(0)[1].toString());
            Long rebate = new Long(details.get(0)[2] == null ? "0" : details.get(0)[2].toString());
            dailyCount += (count == null ? 0L : count);
            dailySales += (sales == null ? 0L : sales);
            dailyRebate += (rebate == null ? 0L : rebate);
        }
        map.put("dailyCount", dailyCount);
        map.put("dailySales", dailySales);
        map.put("dailyRebate", dailyRebate);
        return map;
    }


    /**
     * 多门店-今日交易金额：
     *
     * @param merchants
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Long countDailyTransfering(List<Merchant> merchants) {
        Long totalTransfering = 0L;
        for (Merchant merchant : merchants) {
            Long transfering = null;
            MerchantScanPayWay payway = merchantScanPayWayRepository.findByMerchantId(merchant.getId());
            if (payway.getType()==null||payway.getType()==0||payway.getType()==1) {
                transfering = offLineOrderRepository.countDailyTotalPrice(merchant.getId());
            } else {
                transfering = scanCodeOrderRepository.countDailyTotalPrice(merchant.getId());
            }
            if (transfering != null) {
                totalTransfering += (transfering == null ? 0L : transfering);
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
                sql.append(" and rebate_way != 1 and rebate_way !=3");
            } else if (olOrderCriteria.getRebateWay() == 1) {
                sql.append(" and rebate_way = 1");
            } else if (olOrderCriteria.getRebateWay() == 3) {
                sql.append(" and rebate_way = 3");
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


    /**
     * 城市合伙人 , 佣金明细
     *
     * @param orderCriteria
     * @param limit
     * @return
     */
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
                //   合伙人 -  佣金分润 - 查询条件
                if (orderCriteria.getPartnerManager() != null) {
                    predicate.getExpressions().add(cb.equal(r.get("lockPartnerManager"), orderCriteria.getPartnerManager()));
                }
                if (orderCriteria.getLockMerchant() != null) {
                    predicate.getExpressions().add(cb.like(r.get("lockMerchant").get("name"), "%" + orderCriteria.getLockMerchant().getName() + "%"));
                }
                if (orderCriteria.getTradeMerchant() != null) {
                    predicate.getExpressions().add(cb.like(r.get("tradeMerchant").get("name"), "%" + orderCriteria.getTradeMerchant().getName() + "%"));
                }
                if (orderCriteria.getTradePartner() != null) {
                    predicate.getExpressions().add(cb.like(r.get("tradePartner").get("name"), "%" + orderCriteria.getTradePartner().getName() + "%"));
                }
                if (orderCriteria.getLockPartner() != null) {
                    predicate.getExpressions().add(cb.like(r.get("lockPartner").get("name"), "%" + orderCriteria.getLockPartner().getName() + "%"));
                }
                return predicate;
            }
        };
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Map findOtherData(OrderShareCriteria managerCriteria) {
        Date start = null;
        Date end = null;
        if (managerCriteria.getStartDate() == null) {
            start = new Date();
            end = new Date();
        } else {
            start = new Date(managerCriteria.getStartDate());
            end = new Date(managerCriteria.getEndDate());
        }
        List<Object[]> otherData = offLineOrderShareRepository.findOtherData(managerCriteria.getPartnerManager().getId(), start, end);
        Map map = new HashMap();
        map.put("bind_merchants", otherData.get(0)[0] == null ? 0L : otherData.get(0)[0]);
        map.put("bind_partner_manager", otherData.get(0)[1] == null ? 0L : otherData.get(0)[1]);
        map.put("total_price", otherData.get(0)[2] == null ? 0L : otherData.get(0)[2]);
        return map;
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
     * 查看门店每日订单总额(线下)
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Long countOffLineOrder(List<Merchant> merchants) {
        Long offLineDailyCount = 0L;
        for (Merchant merchant : merchants) {
            Long totalCount = offLineOrderRepository.countDailyOrderNum(merchant.getId());
            offLineDailyCount += totalCount == null ? 0L : totalCount;
        }
        return offLineDailyCount;
    }


    /**
     * 根据商户ID查找旗下门店 扫码订单信息
     *
     * @param codeOrderCriteria 商户查询信息
     * @return 商户旗下门店 扫码订单信息
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public CodeOrderCriteria findCodeOrderByMerchantUser222(CodeOrderCriteria codeOrderCriteria) {

        if (codeOrderCriteria.getPayWay() != null && codeOrderCriteria.getPayWay() == 0) {
            codeOrderCriteria.setPayWay(1);//微信
        } else if (codeOrderCriteria.getPayWay() != null && codeOrderCriteria.getPayWay() == 1) {
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
        if (codeOrderCriteria.getState() != null) {
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
        if (codeOrderCriteria.getState() != null) {
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
        if (listCount <= pageSize) {
            codeOrderCriteria.setTotalPages(1);
        } else {
            codeOrderCriteria.setTotalPages(listCount % pageSize == 0 ? listCount / pageSize : (listCount / pageSize) + 1);
        }

        return codeOrderCriteria;
    }

    /**
     * 根据门店ID查询二维码收款金额
     *
     * @param merchantId
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Object[]> findMyCodePriceByMerchantid(Long merchantId) {
        return offLineOrderRepository.findMyCodePriceByMerchantid(merchantId);
    }



    /***
     *  乐加结算详情 - 数据概览
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Object[]> findOfflineOrderStatistic(OLOrderCriteria olOrderCriteria) {
         /***
         *  根据条件查询订单流水和入账 - 交易记录【到账详情】
         */
            String base = " select count(1),IFNULL(sum(ol.total_price),0),IFNULL(sum(ol.transfer_money),0),IFNULL(sum(ol.lj_commission),0)  from off_line_order ol,pay_way p where ol.pay_way_id = p.id  ";
            StringBuffer sql = new StringBuffer(base);
            if(olOrderCriteria.getMerchant()!=null && olOrderCriteria.getMerchant().getId()!=null) {
                sql.append(" and ol.merchant_id = "+olOrderCriteria.getMerchant().getId());
            }else {
                return null;
            }
            if(olOrderCriteria.getOrderType()!=null && olOrderCriteria.getOrderType()==0) {        // 订单类型
                sql.append(" and (ol.rebate_way=0 or ol.rebate_way=2 or ol.rebate_way=4 or ol.rebate_way=5 or ol.rebate_way=6) ");
            }else if(olOrderCriteria.getOrderType()!=null && olOrderCriteria.getOrderType()==1){
                sql.append(" and (ol.rebate_way=1 or ol.rebate_way=3) ");
            }
            if(olOrderCriteria.getPayWay()!=null) {                 // 支付类型
                if(olOrderCriteria.getPayWay()==0) {
                    sql.append(" and ol.true_score = 0 ");
                }else if(olOrderCriteria.getPayWay()==1) {
                    sql.append(" and ol.true_pay = 0 ");
                }else {
                    sql.append(" and ol.true_score!=0 and ol.true_pay!=0 ");
                }
            }
            if(olOrderCriteria.getPayType()!=null) {                 // 支付类型
                sql.append(" and ol.pay_type = "+olOrderCriteria.getPayType());
            }
            if(olOrderCriteria.getOrderSid()!=null) {               // SID
                sql.append(" and ol.order_sid = "+olOrderCriteria.getOrderSid());
            }
            if(olOrderCriteria.getState()!=null) {                  // state
                sql.append(" and ol.state = "+olOrderCriteria.getState());
            }
            if(olOrderCriteria.getStartDate()!=null && olOrderCriteria.getEndDate()!=null) {
                sql.append(" and ol.complete_date between '"+olOrderCriteria.getStartDate()+"' and '"+olOrderCriteria.getEndDate()+"'");
            }
            Query query_count = em.createNativeQuery(sql.toString());
            List<Object[]> details = query_count.getResultList();
            return details;
    }

    /***
     *  根据Sid查找
     */
    @Transactional(readOnly = true,propagation = Propagation.REQUIRED)
    public OffLineOrder findByOrderSid(String orderSid) {
        return offLineOrderRepository.findByOrderSid(orderSid);
    }


    public Map<String, Object> getRefundInfo(String orderSid) {
        Map<String, Object> result = new HashMap<>();
        OffLineOrder order = offLineOrderRepository.findOneByOrderSid(orderSid);
        if (order == null) {
            result.put("status", 1001);
            result.put("msg", "未找到该订单");
            return result;
        }
        if (order.getState() != 1) {
            result.put("status", 1003);
            result.put("msg", "该订单未支付或已退款！state=" + order.getState());
            return result;
        }
        //订单信息
        result.put("orderSid", order.getOrderSid());
        result.put("merchantName", order.getMerchant().getName());
        LeJiaUser leJiaUser = order.getLeJiaUser();
        ScoreA scoreA = scoreAService.findScoreAByWeiXinUser(leJiaUser);
        ScoreC scoreC = scoreCService.findScoreCByWeiXinUser(leJiaUser);
        //分润信息
        result.put("userInfo", leJiaUser.getPhoneNumber());
        result.put("totalPrice", order.getTotalPrice());
        result.put("truePay", order.getTruePay());
        result.put("trueScore", order.getTrueScore());
        result.put("backScoreC", order.getScoreC());
        result.put("backScoreA", order.getRebate());
        result.put("userScoreA", scoreA.getScore());
        result.put("userScoreC", scoreC.getScore());
        result.put("orderFrom", 0);
        result.put("status", 200);
        return result;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public OffLineOrder findOffLineOrderByOrderSid(String orderSid) {
        return offLineOrderRepository.findByOrderSid(orderSid);
    }
}
