package com.jifenke.lepluslive.merchant.service;

import com.jifenke.lepluslive.merchant.domain.criteria.StatsMerDailyDataCriteria;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.repository.MerchantUserRepository;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商户每日交易等信息统计
 *
 * @author zhangwen at 2017-11-07 14:55
 **/
@Service
public class StatsMerDailyDataService {

    @Autowired
    private EntityManager entityManager;

    @Inject
    private MerchantUserRepository merchantUserRepository;

    public Map<String, Object> listByCriteria(StatsMerDailyDataCriteria criteria) {
        Map<String, Object> result = new HashMap<>(3);
        long totalElements = count(criteria);
        result.put("totalElements", totalElements);
        result.put("data", list(criteria, criteria.getLimit()));
        result.put("totalPages", totalElements / 10 + 1);
        return result;
    }

    /**
     * 按筛选条件下门店交易金额倒序统计
     *
     * @param criteria 筛选条件
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> list(StatsMerDailyDataCriteria criteria, int pageSize) {
        String sql = "SELECT m.id mid,m.user_limit bindLimit,m.`name` AS merchantName,mu.`merchant_name` AS ownerMerchant,m.partnership AS partnership,m.lj_commission AS ljCommission," +
                "m.lj_brokerage AS ljBrokerage,c.`name` AS cityName,e.* FROM merchant m LEFT JOIN (SELECT s.merchant_id AS merchantId,SUM(s.lock_num) AS lockNum,SUM(s.order_num) AS orderNum,"
                + "SUM(s.order_amount) AS orderAmount,SUM(s.com_order_num) AS comOrderNum," +
                "SUM(s.com_order_amount) AS comOrderAmount,SUM(s.le_order_num) AS leOrderNum," +
                "SUM(s.le_order_amount) AS leOrderAmount,SUM(s.le_order_comm) AS leOrderComm," +
                "SUM(s.use_score) AS useScore FROM stats_mer_daily_data s" +
                " WHERE 1 = 1";
        //不影响总条数的筛选条件
        if (StringUtils.isNotEmpty(criteria.getStartDate()) && StringUtils.isNotEmpty(criteria.getEndDate())) {
            sql += " AND s.trade_date BETWEEN '" + criteria.getStartDate().trim() + "' AND '" + criteria.getEndDate().trim() + "'";
        }
        //group
        sql += " GROUP BY s.merchant_id) e ON m.id = e.merchantId  ";
        sql += " LEFT JOIN city c ON m.city_id = c.id ";
        sql += " LEFT JOIN merchant_user mu ON m.merchant_user_id = mu.id ";
        sql += " WHERE 1 = 1";
        //影响记录总条数的筛选条件
        if (criteria.getPartner() != null) {
            sql += " AND m.partner_id = " + criteria.getPartner();
        }
        if (StringUtils.isNotBlank(criteria.getMerchant())) {
            sql += " AND m.`name` like '%" + criteria.getMerchant()+"%'";
        }
        if (StringUtils.isNotBlank(criteria.getMerchantUserName())) {
            MerchantUser merchantUser = merchantUserRepository.findByMerchantUserName(criteria.getMerchantUserName());
            if(merchantUser!=null) {
                sql += " AND m.merchant_user_id = " + merchantUser.getId();
            }else {
                sql += " AND m.merchant_user_id = " + 0;
            }
        }
        if(criteria.getPartnership()!=null) {
            sql += " AND m.partnership = " + criteria.getPartnership() + "";
        }
        //orderBy and limit
        Integer currPage = criteria.getOffset() == null ? 1 : criteria.getOffset();
        sql += " ORDER BY e.orderAmount DESC LIMIT " + (currPage - 1) * 10 + "," + pageSize;
        Query query = entityManager.createNativeQuery(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    /**
     * 按筛选条件下门店数量
     *
     * @param criteria 筛选条件
     */
    @SuppressWarnings("unchecked")
    private long count(StatsMerDailyDataCriteria criteria) {
        String sql = "SELECT COUNT(1) AS totalElements FROM merchant m LEFT JOIN (SELECT s.merchant_id AS merchantId"
                + " FROM stats_mer_daily_data s WHERE 1 = 1";
        //不影响总条数的筛选条件
        if (StringUtils.isNotEmpty(criteria.getStartDate()) && StringUtils.isNotEmpty(criteria.getEndDate())) {
            sql += " AND s.trade_date BETWEEN '" + criteria.getStartDate() + "' AND '" + criteria.getEndDate() + "'";
        }
        //group
        sql += " GROUP BY s.merchant_id) e ON m.id = e.merchantId ";

        sql += " LEFT JOIN city c ON m.city_id = c.id ";
        sql += " LEFT JOIN merchant_user mu ON m.merchant_user_id = mu.id ";
        //影响记录总条数的筛选条件
        if (criteria.getPartner() != null) {
            sql += " AND m.partner_id = " + criteria.getPartner();
        }
        if (StringUtils.isNotBlank(criteria.getMerchant())) {
            sql += " AND m.`name` like '%" + criteria.getMerchant()+"%'";
        }
        if (StringUtils.isNotBlank(criteria.getMerchantUserName())) {
            MerchantUser merchantUser = merchantUserRepository.findByMerchantUserName(criteria.getMerchantUserName());
            if(merchantUser!=null) {
                sql += " AND m.merchant_user_id = " + merchantUser.getId();
            }else {
                sql += " AND m.merchant_user_id = " + 0;
            }
        }
        if(criteria.getPartnership()!=null) {
            sql += " AND m.partnership = " + criteria.getPartnership() + "";
        }
        Query query = entityManager.createNativeQuery(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        Map<String, Object> result = (Map) query.getSingleResult();
        return Long.valueOf(String.valueOf(result.get("totalElements")));
    }

}
