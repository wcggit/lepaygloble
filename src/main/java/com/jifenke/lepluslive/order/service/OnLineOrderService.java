package com.jifenke.lepluslive.order.service;

import com.jifenke.lepluslive.merchant.domain.criteria.CommissionDetailsCriteria;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Created by tqy on 2016/12/19.
 */
@Service
@Transactional(readOnly = true)
public class OnLineOrderService {

    @Inject
    private EntityManager em;

    /**
     *  查询 线上 佣金明细
     * @param commissionDetailsCriteria
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List getCommissionDetailsList_on_line(CommissionDetailsCriteria commissionDetailsCriteria) {

        int start = commissionDetailsCriteria.getPageSize() * (commissionDetailsCriteria.getCurrentPage() - 1);

        StringBuffer sql = new StringBuffer();
        sql.append("select  olo_olos.complete_date, wxu.nickname, wxu.head_image_url, merchant.name, olo_olos.total_price, olo_olos.true_pay, olo_olos.share_ from "
                   + " (select olo.confirm_date as complete_date,  olo.le_jia_user_id as user_id,  olos.lock_merchant_id as merchant_id, "
                   + " olo.total_price as total_price,  olo.true_price as true_pay,  olos.to_lock_merchant as share_ "
                   + " from on_line_order olo, on_line_order_share olos where olo.id = olos.on_line_order_id "
                   + " and olo.state in (3) " //3已收货
                   + " and olo.pay_state = 1 "
                   + "");
        if (commissionDetailsCriteria.getStoreIds() != null) {
            sql.append(" and olos.lock_merchant_id in (");
            for(int i =0;i<commissionDetailsCriteria.getStoreIds().length;i++){
                sql.append(commissionDetailsCriteria.getStoreIds()[i]+",");
            }
            sql.deleteCharAt(sql.length()-1);
            sql.append(")");
        }
        if (commissionDetailsCriteria.getStartDate() != null && commissionDetailsCriteria.getStartDate() != "") {
            sql.append(" and olo.confirm_date between '");
            sql.append(commissionDetailsCriteria.getStartDate());
            sql.append("' and '");
            sql.append(commissionDetailsCriteria.getEndDate());
        }
        sql.append(" order by olo.confirm_date desc limit ");
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
     *  查询 线上 佣金明细count
     * @param commissionDetailsCriteria
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List getCommissionDetails_on_line_count(CommissionDetailsCriteria commissionDetailsCriteria) {

        StringBuffer sql = new StringBuffer();
        sql.append(" select count(olo.id), sum(olos.to_lock_merchant) "
                   + " from on_line_order olo, on_line_order_share olos where olo.id = olos.on_line_order_id "
                   + " and olo.state in (3) " //3已收货
                   + " and olo.pay_state = 1 ");
        if (commissionDetailsCriteria.getStoreIds() != null) {
            sql.append(" and olos.lock_merchant_id in (");
            for(int i =0;i<commissionDetailsCriteria.getStoreIds().length;i++){
                sql.append(commissionDetailsCriteria.getStoreIds()[i]+",");
            }
            sql.deleteCharAt(sql.length()-1);
            sql.append(")");
        }
        if (commissionDetailsCriteria.getStartDate() != null && commissionDetailsCriteria.getStartDate() != "") {
            sql.append(" and olo.confirm_date between '");
            sql.append(commissionDetailsCriteria.getStartDate());
            sql.append("' and '");
            sql.append(commissionDetailsCriteria.getEndDate());
        }

        Query query = em.createNativeQuery(sql.toString());
        List<Object[]> details = query.getResultList();
        return details;
    }

}
