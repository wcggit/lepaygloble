package com.jifenke.lepluslive.partner.service;

import com.jifenke.lepluslive.partner.domain.criteria.PartnerWalletLogCriteria;
import com.jifenke.lepluslive.partner.repository.PartnerWalletOnlineLogRepository;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by
 * @Azxf on 2017/11/15.
 */
@Service
public class PartnerWalletOnlineLogService {
    @Inject
    private PartnerWalletOnlineLogRepository partnerWalletOnlineLogRepository;

    @Autowired
    private EntityManager entityManager;

    /***
     * 获取合伙人当日佣金收入
     * @param partnerId
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Long countDailyIncomeCommissionByPartner(Long partnerId) {
        return partnerWalletOnlineLogRepository.findIncomeCommissionByPartnerDaily(partnerId);
    }

    /***
     * 获取合伙人当日佣金支出
     * @param partnerId
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Long countDailyExpendCommissionByPartner(Long partnerId) {
        return partnerWalletOnlineLogRepository.findExpendCommissionByPartnerDaily(partnerId);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public Map<String, Object> listByCriteria(PartnerWalletLogCriteria criteria) {
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
    public List<Map<String, Object>> list(PartnerWalletLogCriteria criteria, int pageSize) {
        String sql = " SELECT log.id logId,IFNULL(log.change_money,0) changeMoney,log.create_date changeDate,c.type_explain changeOrigin,log.order_sid orderSid,log.before_change_money beforeChange,log.after_change_money afterChange FROM partner_wallet_online_log log " +
            " LEFT JOIN category c ON log.type = c.id " +
            " WHERE 1=1 ";
        if(criteria.getPartnerId()!=null) {
            sql+= " AND  log.partner_id= "+criteria.getPartnerId();
        }
        if(criteria.getType()!=null) {
            sql+= " AND  log.type= "+criteria.getType();
        }
        if (StringUtils.isNotEmpty(criteria.getStartDate()) && StringUtils.isNotEmpty(criteria.getEndDate())) {
            sql += " AND   log.create_date  BETWEEN '" + criteria.getStartDate().trim() + "' AND '" + criteria.getEndDate().trim() + "'";
        }
        sql += " ORDER BY log.create_date DESC LIMIT " + (criteria.getOffset() - 1) * 10 + "," + pageSize;
        Query query = entityManager.createNativeQuery(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    /**
     * 按筛选条件下门店数量
     *
     * @param criteria 筛选条件
     */
    private long count(PartnerWalletLogCriteria criteria) {
        String sql = " SELECT count(*) totalElements FROM partner_wallet_online_log log " +
            " LEFT JOIN category c ON log.type = c.id " +
            " WHERE 1=1 ";
        if(criteria.getPartnerId()!=null) {
            sql+= " AND  log.partner_id= "+criteria.getPartnerId();
        }
        if(criteria.getType()!=null) {
            sql+= " AND  log.type= "+criteria.getType();
        }
        if (StringUtils.isNotEmpty(criteria.getStartDate()) && StringUtils.isNotEmpty(criteria.getEndDate())) {
            sql += " AND log.create_date  BETWEEN '" + criteria.getStartDate().trim() + "' AND '" + criteria.getEndDate().trim() + "'";
        }
        Query query = entityManager.createNativeQuery(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        Map<String, Object> result = (Map) query.getSingleResult();
        return Long.valueOf(String.valueOf(result.get("totalElements")));
    }


}
