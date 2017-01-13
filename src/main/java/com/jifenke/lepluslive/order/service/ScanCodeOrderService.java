package com.jifenke.lepluslive.order.service;

import com.jifenke.lepluslive.merchant.domain.criteria.CodeOrderCriteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by tqy on 2016/12/27.
 */
@Service
@Transactional(readOnly = true)
public class ScanCodeOrderService {

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

}
