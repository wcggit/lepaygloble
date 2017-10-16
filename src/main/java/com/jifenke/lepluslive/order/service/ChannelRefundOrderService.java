package com.jifenke.lepluslive.order.service;


import com.jifenke.lepluslive.order.domain.entities.ChannelRefundOrder;
import com.jifenke.lepluslive.order.repository.ChannelRefundOrderRepository;
import com.jifenke.lepluslive.yibao.domain.criteria.LedgerRefundOrderCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;


/**
 * Created by zhangwen on 2017/7/12.
 */
@Service
@Transactional(readOnly = true)
public class ChannelRefundOrderService {

    @Inject
    private ChannelRefundOrderRepository repository;

    /**
     * 某商户某日退款单 - 记录
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<ChannelRefundOrder> findDailyRefundOrderByMerchant(String merchantId, String tradeDate) {
        return repository.findByMerchantIdAndTradeDateAndState(merchantId, tradeDate,2);
    }

    /**
     * 某商户某日退款单 - 退款总额
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Long sumDailyTotalRefund(String merchantId, String tradeDate) {
        return repository.sumDailyTotalRefund(merchantId, tradeDate);
    }

    /**
     *  根据类型和时间统计商户退款单数
     */
    @Transactional(readOnly = true,propagation = Propagation.REQUIRED)
    public Long countRefundByDateAndMerchantNum(String settleDate,String realMerNum,Integer orderType) {
        return repository.countRefundByDateAndMerchantNum(settleDate,realMerNum,orderType);
    }

    /***
     *  根据条件查询退款单记录
     *  Created by xf on 2017-07-14.
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Page<ChannelRefundOrder> findByCriteria(LedgerRefundOrderCriteria criteria, Integer limit) {
        Sort sort = new Sort(Sort.Direction.DESC, "dateCreated");
        return repository
            .findAll(getWhereClause(criteria), new PageRequest(criteria.getOffset() - 1, limit, sort));
    }

    public static Specification<ChannelRefundOrder> getWhereClause(
        LedgerRefundOrderCriteria criteria) {
        return new Specification<ChannelRefundOrder>() {
            @Override
            public Predicate toPredicate(Root<ChannelRefundOrder> root, CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                // 门店ID
                if (criteria.getMerchantId() != null && !"".equals(criteria.getMerchantId())) {
                    predicate.getExpressions().add(
                        cb.equal(root.get("merchantId"), criteria.getMerchantId()));
                }
                if (criteria.getState() != null) {
                    predicate.getExpressions().add(
                        cb.equal(root.get("state"), criteria.getState()));
                }
                if(criteria.getOrderType()!=null) {
                    predicate.getExpressions().add(
                        cb.equal(root.get("orderType"), criteria.getOrderType()));
                }
                if(criteria.getPayType()!=null) {
                    predicate.getExpressions().add(
                        cb.equal(root.get("payType"), criteria.getPayType()));
                }
                // 退款完成时间
                if (criteria.getTradeDate() != null && criteria.getTradeDate() != null) {
                    predicate.getExpressions().add(
                        cb.equal(root.get("tradeDate"), criteria.getTradeDate()));
                }
                return predicate;
            }
        };
    }
}
