package com.jifenke.lepluslive.groupon.service;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.groupon.domain.criteria.GrouponCodeCriteria;
import com.jifenke.lepluslive.groupon.domain.entities.GrouponCode;
import com.jifenke.lepluslive.groupon.domain.entities.GrouponOrder;
import com.jifenke.lepluslive.groupon.repository.GrouponCodeRepository;
import com.jifenke.lepluslive.groupon.repository.GrouponOrderRepository;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.order.service.OnLineOrderShareService;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
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
 * 券码管理 Service
 *
 * @author XF
 * @date 2017/6/19
 */
@Service
@Transactional(readOnly = true)
public class GrouponCodeService {
    @Inject
    private GrouponCodeRepository grouponCodeRepository;
    @Inject
    private GrouponOrderRepository grouponOrderRepository;
    @Inject
    private OnLineOrderShareService onLineOrderShareService;

    @Inject
    private EntityManager entityManager;

    /***
     *  根据条件查询团购券码
     *  Created by xf on 2017-06-19.
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Page<GrouponCode> findByCriteria(GrouponCodeCriteria criteria, Integer limit) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        return grouponCodeRepository.findAll(getWhereClause(criteria), new PageRequest(criteria.getOffset() - 1, limit, sort));
    }

    public static Specification<GrouponCode> getWhereClause(GrouponCodeCriteria criteria) {
        return new Specification<GrouponCode>() {
            @Override
            public Predicate toPredicate(Root<GrouponCode> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                // 商户ID
                if (criteria.getMerchantId() != null) {
                    predicate.getExpressions().add(
                        cb.equal(root.get("merchant").get("id"), criteria.getMerchantId()));
                }
                // 券码状态  0 未付款 1 已完成
                if (criteria.getState() != null) {
                    predicate.getExpressions().add(
                        cb.equal(root.get("state"), criteria.getState()));
                }
                //  电话
                if (criteria.getPhoneNumber() != null && !"".equals(criteria.getPhoneNumber())) {
                    predicate.getExpressions().add(
                        cb.like(root.get("grouponOrder").get("leJiaUser").get("phoneNumber"), "%" + criteria.getPhoneNumber() + "%"));
                }
                //  订单金额
                if (criteria.getOrderPrice() != null) {
                    predicate.getExpressions().add(
                        cb.equal(root.get("grouponOrder").get("totalPrice"), criteria.getOrderPrice()));
                }
                // 核销日期
                if (criteria.getStartDate() != null && !"".equals(criteria.getStartDate())) {
                    Date startDate = new Date(criteria.getStartDate());
                    Date endDate = new Date(criteria.getEndDate());
                    predicate.getExpressions().add(
                        cb.between(root.get("checkDate"), startDate, endDate));
                }
                return predicate;
            }
        };
    }

    public GrouponCode findBySid(String sid) {
        return grouponCodeRepository.findFirstBySid(sid);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public GrouponCode findGrouponCodeBySid(String sid) {
        return grouponCodeRepository.findOneBySid(sid);
    }


    /**
     * 核销团购和分润
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void chargeOffGrouponCode(GrouponCode grouponCode, Merchant merchant, String name) {
        grouponCode.setState(1);
        grouponCode.setMerchant(merchant);
        grouponCode.setCheckDate(new Date());
        grouponCode.setMerchantUser(name);
        GrouponOrder grouponOrder = grouponCode.getGrouponOrder();
        Integer orderstate = 1;
        for (GrouponCode code : grouponOrder.getGrouponCodes()) {
            if (!code.getId().equals(grouponCode.getId())) {
                if (grouponCode.getState() == 0) {
                    orderstate = 0;
                    break;
                }
            }
        }
        grouponOrder.setOrderState(orderstate);
        onLineOrderShareService.share(grouponCode);
        grouponOrderRepository.save(grouponOrder);
        grouponCodeRepository.save(grouponCode);
    }

    /**
     * 团购核销数据统计
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<Map<String, Object>> findDailyStatistics(Long merchantId) {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" SELECT IFNULL(sum(c.total_price),0) daliyPrice,count(*) dailyCount from groupon_code c ");
            sql.append(" WHERE c.state=1 ");
            sql.append(" AND c.merchant_id =  " + merchantId);
            sql.append(" AND to_days(c.check_date) = to_days(now());  ");
            Query query = entityManager.createNativeQuery(sql.toString());
            query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            List<Map<String, Object>> resultList = query.getResultList();
            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<Map<String, Object>> findTotalStatistics(Long merchantId) {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" SELECT IFNULL(sum(c.total_price),0) totalPrice,count(*) totalCount from groupon_code c ");
            sql.append(" WHERE c.state=1 ");
            sql.append(" AND c.merchant_id =  " + merchantId + " ;");
            Query query = entityManager.createNativeQuery(sql.toString());
            query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            List<Map<String, Object>> resultList = query.getResultList();
            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<Map<String, Object>> findDetailStatistics(Long merchantId) {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" SELECT p.id productId,p.`name` productName,p.normal_price productPrice,COUNT(*) checkCount,IFNULL(SUM(c.total_price),0) totalPrice FROM groupon_code c ");
            sql.append(" LEFT JOIN groupon_product p ON c.groupon_product_id = p.id ");
            sql.append(" WHERE c.state = 1 ");
            sql.append(" AND c.merchant_id =  " + merchantId);
            sql.append(" GROUP BY c.groupon_product_id ;  ");
            Query query = entityManager.createNativeQuery(sql.toString());
            query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            List<Map<String, Object>> resultList = query.getResultList();
            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public LejiaResult checkUseDate(GrouponCode grouponCode) {
        Date date = new Date();
        if (grouponCode.getStartDate() != null) {
            if (date.getTime() < grouponCode.getStartDate().getTime()) {
                return LejiaResult.build(506, "未到使用时间，无法核销");
            }
        }
        if (grouponCode.getExpiredDate() != null) {
            if (date.getTime() > grouponCode.getExpiredDate().getTime()) {
                grouponCode.setState(4);
                grouponCodeRepository.save(grouponCode);
                return LejiaResult.build(507, "改券码已过期，无法核销");
            }
        }
        return null;
    }

}
