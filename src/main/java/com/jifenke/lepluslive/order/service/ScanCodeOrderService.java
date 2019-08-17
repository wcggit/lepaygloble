package com.jifenke.lepluslive.order.service;

import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.merchant.domain.criteria.CodeOrderCriteria;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantWallet;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.order.domain.entities.OffLineOrderShare;
import com.jifenke.lepluslive.order.domain.entities.ScanCodeOrder;
import com.jifenke.lepluslive.order.domain.criteria.ScanCodeOrderCriteria;
import com.jifenke.lepluslive.order.repository.ScanCodeOrderRepository;
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
import com.jifenke.lepluslive.weixin.domain.entities.Category;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tqy on 2016/12/27.
 */
@Service
@Transactional(readOnly = true)
public class ScanCodeOrderService {

    @Inject
    private ScanCodeOrderRepository repository;


    @Inject
    private EntityManager em;

    @Inject
    private PartnerService partnerService;

    @Inject
    private MerchantService merchantService;

    @Inject
    private PartnerWalletService partnerWalletService;

    @Inject
    private ShareService shareService;

    @Inject
    private ScoreCService scoreCService;

    @Inject
    private ScoreAService scoreAService;

    /**
     * 查询 扫码订单 富友结算
     *
     * @param codeOrderCriteria
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public CodeOrderCriteria getScanCodeOrderList(CodeOrderCriteria codeOrderCriteria) {

        int start = codeOrderCriteria.getPageSize() * (codeOrderCriteria.getCurrentPage() - 1);

        StringBuffer sql = new StringBuffer();
        sql.append("select olo.order_sid,  m.name,  olo.payment,  olo.le_pay_code,  olo.complete_date,  olo.total_price,  olo.true_score,  c.type, "
            + " olo.transfer_money_from_true_pay,  olo.transfer_money_from_score,  ifnull(m.lj_commission,0),  olo.commission,  111  "
            + " from scan_code_order olo INNER JOIN merchant m ON olo.merchant_id = m.id INNER JOIN category c ON olo.order_type_id = c.id  where  ");
        if (codeOrderCriteria.getStoreIds() != null) {
            sql.append(" olo.merchant_id in (");
            sql.append(codeOrderCriteria.getStoreIds()[0]);//改为单门店查询
            sql.append(")");
        }
        if (codeOrderCriteria.getRebateWay() != null && !"".equals(codeOrderCriteria.getRebateWay())) {//订单类型 3种
            sql.append(" and c.type in ");
            sql.append(codeOrderCriteria.getRebateWay());
        }
        if (codeOrderCriteria.getPayWay() != null) {
            sql.append(" and olo.payment = ");
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
        List<Object[]> scan_code_order = query.getResultList();
        codeOrderCriteria.setListCodeOrder(scan_code_order);


        /**
         * 查询条件后的统计数据
         */
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT sum(o.total_price) as totalPrice , sum(o.true_pay) as truePay , sum(o.true_score) as trueScore , \n" +
            " sum(o.transfer_money_from_true_pay) as transferMoneyFromTruePay , \n" +
            " sum(o.transfer_money_from_score) as transferMoneyHB, count(o.id) \n" +
            " from scan_code_order as o  INNER JOIN category c ON o.order_type_id = c.id where ");
        if (codeOrderCriteria.getStoreIds() != null) {
            sb.append(" o.merchant_id in (");
            sb.append(codeOrderCriteria.getStoreIds()[0]);//改为单门店查询
            sb.append(")");
        }
        if (codeOrderCriteria.getRebateWay() != null && !"".equals(codeOrderCriteria.getRebateWay())) {//订单类型 3种
            sb.append(" and c.type in ");
            sb.append(codeOrderCriteria.getRebateWay());
        }
        if (codeOrderCriteria.getPayWay() != null) {
            sb.append(" and o.payment = ");
            sb.append(codeOrderCriteria.getPayWay());
        }
        if (codeOrderCriteria.getOrderSid() != null && !"".equals(codeOrderCriteria.getOrderSid())) {
            sb.append(" and o.order_sid=");
            sb.append(codeOrderCriteria.getOrderSid());
        }
        if (codeOrderCriteria.getState() != null) {
            sb.append(" and o.state=");
            sb.append(codeOrderCriteria.getState());
        }
        if (codeOrderCriteria.getStartDate() != null && !"".equals(codeOrderCriteria.getStartDate())
            && codeOrderCriteria.getEndDate() != null && !"".equals(codeOrderCriteria.getEndDate())) {
            sb.append(" and o.complete_date between '");
            sb.append(codeOrderCriteria.getStartDate());
            sb.append("' and '");
            sb.append(codeOrderCriteria.getEndDate());
            sb.append("'");
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


    /***
     *   根据条件查询订单数据
     * @param criteria
     * @param limit
     * @return
     */
    public Page findOrderByPage(ScanCodeOrderCriteria criteria, Integer limit) {
        Sort sort = new Sort(Sort.Direction.DESC, "createdDate");
        return repository
            .findAll(getWhereClause(criteria),
                new PageRequest(criteria.getOffset() - 1, limit, sort));
    }

    private static Specification<ScanCodeOrder> getWhereClause(ScanCodeOrderCriteria orderCriteria) {
        return new Specification<ScanCodeOrder>() {
            @Override
            public Predicate toPredicate(Root<ScanCodeOrder> r, CriteriaQuery<?> q,
                                         CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (orderCriteria.getState() != null) { //订单状态
                    predicate.getExpressions().add(cb.equal(r.get("state"), orderCriteria.getState()));
                }
                if(orderCriteria.getSettleDate()!=null&&!"".equals(orderCriteria.getSettleDate())) {    // 结算时间
                    predicate.getExpressions().add(cb.equal(r.get("settleDate"),orderCriteria.getSettleDate()));
                }
                if (orderCriteria.getStartDate() != null && !""
                    .equals(orderCriteria.getStartDate())) {//交易完成时间
                    predicate.getExpressions().add(
                        cb.between(r.get("completeDate"), new Date(orderCriteria.getStartDate()),
                            new Date(orderCriteria.getEndDate())));
                }
                if (orderCriteria.getMerchantId() != null && !""
                    .equals(orderCriteria.getMerchantName())) { //门店ID
                    predicate.getExpressions().add(
                        cb.equal(r.<Merchant>get("merchant").get("id"), orderCriteria.getMerchantId()));
                }
                if(orderCriteria.getGatewayType()!=null) {  // 通道方
                    predicate.getExpressions().add(cb.equal(r.<Merchant>get("scanCodeOrderExt").get("gatewayType"), orderCriteria.getGatewayType()));
                }
                if (orderCriteria.getOrderSid() != null && !""
                    .equals(orderCriteria.getOrderSid())) { //OrderSID
                    predicate.getExpressions().add(
                        cb.equal(r.<Merchant>get("orderSid"), orderCriteria.getOrderSid()));
                }
                if (orderCriteria.getOrderType() != null) {                     //订单类型
                    if (orderCriteria.getOrderType() == 1 || orderCriteria.getOrderType() == 0) {
                        predicate.getExpressions().add(cb.equal(r.<Category>get("scanCodeOrderExt").get("basicType"), orderCriteria.getOrderType()));
                    }
                }
                if (orderCriteria.getPayment() != null && !"".equals(orderCriteria.getPayment())) {
                    predicate.getExpressions().add(
                        cb.equal(r.<Merchant>get("scanCodeOrderExt").get("payment"), orderCriteria.getPayment()));
                }
                if (orderCriteria.getPayType() != null && !"".equals(orderCriteria.getPayType())) {
                    predicate.getExpressions().add(cb.equal(r.<Category>get("scanCodeOrderExt").get("payType"), orderCriteria.getPayType()));
                }
                return predicate;
            }
        };
    }

    /***
     *  根据条件查询订单流水和入账 - 交易记录【到账详情】
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<Object[]> findTotalDailyTransferAndIncome(ScanCodeOrderCriteria scanCodeOrderCriteria) {
        String base = "select count(1),IFNULL(sum(so.total_price),0),IFNULL(sum(so.transfer_money),0),IFNULL(sum(so.commission),0) from scan_code_order so,scan_code_order_ext se where so.scan_code_order_ext_id = se.id ";
        StringBuffer sql = new StringBuffer(base);
        if (scanCodeOrderCriteria.getMerchantId() != null) {
            sql.append(" and so.merchant_id = " + scanCodeOrderCriteria.getMerchantId());
        } else {
            return null;
        }
        if (scanCodeOrderCriteria.getOrderType() != null && scanCodeOrderCriteria.getOrderType() == 0) {        // 订单类型
            sql.append(" and se.basic_type = 0 ");
        } else if (scanCodeOrderCriteria.getOrderType() != null && scanCodeOrderCriteria.getOrderType() == 1) {
            sql.append(" and se.basic_type = 1 ");
        }
        if (scanCodeOrderCriteria.getPayType() != null) {          // 支付类型
            sql.append(" and se.pay_type = " + scanCodeOrderCriteria.getPayType());
        }
        if (scanCodeOrderCriteria.getState() != null) {
            sql.append(" and so.state = " + scanCodeOrderCriteria.getState());
        }
        if (scanCodeOrderCriteria.getPayment() != null) {          // 支付类型
            sql.append(" and se.payment = " + scanCodeOrderCriteria.getPayment());
        }
        if (scanCodeOrderCriteria.getStartDate() != null && scanCodeOrderCriteria.getEndDate() != null) {
            sql.append(" and so.complete_date between '" + scanCodeOrderCriteria.getStartDate() + "' and '" + scanCodeOrderCriteria.getEndDate() + "'");
        }
        Query query_count = em.createNativeQuery(sql.toString());
        List<Object[]> details = query_count.getResultList();
        return details;
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public ScanCodeOrder findByOrderSid(String orderSid) {
        return repository.findByOrderSid(orderSid);
    }

    /**
     * 根据门店统计订单金额
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Long countScanOrder(List<Merchant> merchants) {
        Long ledgerDailyCount = 0L;
        for (Merchant merchant : merchants) {
            ledgerDailyCount += repository.countDailyCount(merchant.getId());
        }
        return ledgerDailyCount;
    }

    /**
     * 根据日期和子商户号统计订单数量
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Long countOrderByDateAndMerchantNo(String settleDate, String realMerNum, Integer basicType) {
        settleDate.replaceAll("-", "");
        return repository.countOrderByDateAndMerchantNo(settleDate, realMerNum, basicType);
    }


    /**
     * 易宝 点击退款，获取退款信息  2017/8/3
     *
     * @param orderSid 订单号
     */
    public Map<String, Object> getRefundInfo(String orderSid) {
        Map<String, Object> result = new HashMap<>();
        ScanCodeOrder order = findByOrderSid(orderSid);
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
        //用户信息
        LeJiaUser leJiaUser = order.getLeJiaUser();
        ScoreA scoreA = scoreAService.findScoreAByWeiXinUser(leJiaUser);
        ScoreC scoreC = scoreCService.findScoreCByWeiXinUser(leJiaUser);
        result.put("userInfo", leJiaUser.getPhoneNumber());
        result.put("totalPrice", order.getTotalPrice());
        result.put("truePay", order.getTruePay());
        result.put("trueScore", order.getTrueScore());
        result.put("backScoreC", order.getScoreC());
        result.put("backScoreA", order.getRebate());
        result.put("userScoreA", scoreA.getScore());
        result.put("userScoreC", scoreC.getScore());
        result.put("orderFrom", 1);
        result.put("status", 200);
        return result;
    }
}
