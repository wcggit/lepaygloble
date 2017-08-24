package com.jifenke.lepluslive.yibao.service;


import com.jifenke.lepluslive.yibao.domain.criteria.LedgerRefundOrderCriteria;
import com.jifenke.lepluslive.yibao.domain.entities.LedgerRefundOrder;
import com.jifenke.lepluslive.yibao.repository.LedgerRefundOrderRepository;
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
import java.util.Date;
import java.util.List;


/**
 * Created by zhangwen on 2017/7/12.
 */
@Service
@Transactional(readOnly = true)
public class LedgerRefundOrderService {

  @Inject
  private LedgerRefundOrderRepository ledgerRefundOrderRepository;

  /**
   *  每日退款单 - 记录
   */
  @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
  public List<LedgerRefundOrder> findDailyRefundOrderByMerchant(String merchantId, String tradeDate) {
    return ledgerRefundOrderRepository.findByMerchantIdAndTradeDate(merchantId,tradeDate);
  }
  /**
   *  每日退款单 - 退款总额
   */
  @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
  public Long sumDailyTotalRefund(String merchantId,String tradeDate) {
    return ledgerRefundOrderRepository.sumDailyTotalRefund(merchantId,tradeDate);
  }

    /***
     *  根据条件查询退款单记录
     *  Created by xf on 2017-07-14.
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Page<LedgerRefundOrder> findByCriteria(LedgerRefundOrderCriteria criteria, Integer limit) {
        Sort sort = new Sort(Sort.Direction.DESC, "dateCreated");
        return ledgerRefundOrderRepository
            .findAll(getWhereClause(criteria), new PageRequest(criteria.getOffset() - 1, limit, sort));
    }

    public static Specification<LedgerRefundOrder> getWhereClause(
        LedgerRefundOrderCriteria criteria) {
        return new Specification<LedgerRefundOrder>() {
            @Override
            public Predicate toPredicate(Root<LedgerRefundOrder> root, CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                // 门店ID
                if (criteria.getMerchantId() != null && !"".equals(criteria.getMerchantId())) {
                    predicate.getExpressions().add(
                        cb.equal(root.get("merchantId"), criteria.getMerchantId()));
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
