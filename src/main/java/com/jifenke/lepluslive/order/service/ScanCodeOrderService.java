package com.jifenke.lepluslive.order.service;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.merchant.domain.criteria.CodeOrderCriteria;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.order.domain.entities.ScanCodeOrder;
import com.jifenke.lepluslive.order.domain.entities.ScanCodeOrderCriteria;
import com.jifenke.lepluslive.order.repository.ScanCodeOrderRepository;
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
import java.util.List;

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

    /**
     *  查询 扫码订单 富友结算
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
//            for(int i =0;i<codeOrderCriteria.getStoreIds().length;i++){
//                sql.append(codeOrderCriteria.getStoreIds()[i]+",");
//            }
//            sql.deleteCharAt(sql.length()-1);

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
//            for(int i =0;i<codeOrderCriteria.getStoreIds().length;i++){
//                sb.append(codeOrderCriteria.getStoreIds()[i]+",");
//            }
//            sb.deleteCharAt(sb.length()-1);

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
        if(codeOrderCriteria.getState()!=null){
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
        if (listCount <= pageSize){
            codeOrderCriteria.setTotalPages(1);
        }else {
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
                if (orderCriteria.getState() != null && !""
                    .equals(orderCriteria.getState())) { //门店ID
                    predicate.getExpressions().add(
                        cb.equal(r.<Merchant>get("merchant").get("id"), orderCriteria.getMerchantId()));
                }
                if (orderCriteria.getOrderType() != null) { //订单类型
                    if (orderCriteria.getOrderType() == 2) {
                        predicate.getExpressions().add(
                            cb.or(cb.equal(r.<Category>get("orderType"), "12004"),
                                cb.equal(r.<Category>get("orderType"), "12005"))
                        );
                    }
                    if (orderCriteria.getOrderType() == 1) {
                        predicate.getExpressions().add(
                            cb.or(cb.equal(r.<Category>get("orderType"), "12001"),
                                cb.equal(r.<Category>get("orderType"), "12002"),
                                cb.equal(r.<Category>get("orderType"), "12003"),
                                cb.equal(r.<Category>get("orderType"), "12006"))
                        );
                    }
                }
                if(orderCriteria.getPayType()!=null && !"".equals(orderCriteria.getPayType())) {
                    predicate.getExpressions().add(cb.equal(r.<Category>get("scanCodeOrderExt").get("payType"),orderCriteria.getPayType()));
                }
                return predicate;
            }
        };
    }

    /***
     *  根据条件查询订单流水和入账 - 交易记录【到账详情】
     */
    @Transactional(readOnly = true,propagation = Propagation.REQUIRED)
    public List<Object[]> findTotalDailyTransferAndIncome(ScanCodeOrderCriteria scanCodeOrderCriteria) {
        String base = "select count(1),IFNULL(sum(so.transfer_money),0),IFNULL(sum(so.transfer_money_from_true_pay),0) from scan_code_order so,scan_code_order_ext se where so.scan_code_order_ext_id = se.id ";
        StringBuffer sql = new StringBuffer(base);
        if(scanCodeOrderCriteria.getMerchantId()!=null) {
            sql.append(" and so.merchant_id = "+scanCodeOrderCriteria.getMerchantId());
        }else {
            return null;
        }
        if(scanCodeOrderCriteria.getOrderType()!=null && scanCodeOrderCriteria.getOrderType()==0) {        // 订单类型
            sql.append(" and (so.order_type = 12001 or so.order_type = 12002 or so.order_type = 12003 or so.order_type = 12006) ");
        }else {
            sql.append(" and (so.order_type = 12004 or so.order_type = 12005) ");
        }
        if(scanCodeOrderCriteria.getPayType()!=null) {          // 支付类型
            sql.append(" and se.pay_type = "+scanCodeOrderCriteria.getPayType());
        }
        if(scanCodeOrderCriteria.getStartDate()!=null && scanCodeOrderCriteria.getEndDate()!=null) {
            sql.append(" and so.complete_date between "+scanCodeOrderCriteria.getStartDate()+" and "+scanCodeOrderCriteria.getEndDate());
        }else {
            return null;
        }
        Query query_count = em.createNativeQuery(sql.toString());
        List<Object[]> details = query_count.getResultList();
        return details;
    }

}
