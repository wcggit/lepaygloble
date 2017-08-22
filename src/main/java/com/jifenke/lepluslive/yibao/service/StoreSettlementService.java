package com.jifenke.lepluslive.yibao.service;


import com.jifenke.lepluslive.yibao.domain.criteria.StoreSettlementCriteria;
import com.jifenke.lepluslive.yibao.domain.entities.StoreSettlement;
import com.jifenke.lepluslive.yibao.repository.StoreSettlementRepository;
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
public class StoreSettlementService {

    @Inject
    private StoreSettlementRepository storeSettlementRepository;


    /***
     *  根据条件查询门店结算单
     *  Created by xf on 2017-07-13.
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Page<StoreSettlement> findByCriteria(StoreSettlementCriteria criteria, Integer limit) {
        Sort sort = new Sort(Sort.Direction.DESC, "dateCreated");
        return storeSettlementRepository.findAll(getWhereClause(criteria), new PageRequest(criteria.getOffset() - 1, limit, sort));
    }

    public static Specification<StoreSettlement> getWhereClause(StoreSettlementCriteria criteria) {
        return new Specification<StoreSettlement>() {
            @Override
            public Predicate toPredicate(Root<StoreSettlement> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                // 易宝商户号
                if (criteria.getLedgerNo() != null&&!"".equals(criteria.getLedgerNo())) {
                    predicate.getExpressions().add(
                            cb.equal(root.get("ledgerNo"), criteria.getLedgerNo()));
                }
                //  清算日期 T-1
                if(criteria.getTradeDate()!=null&&!"".equals(criteria.getTradeDate())) {
                    predicate.getExpressions().add(
                            cb.equal(root.get("tradeDate"),criteria.getTradeDate()));
                }
                return predicate;
            }
        };
    }

    /***
     *  易宝结算累计入账
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<Object[]> findTotalAndNumberFromDailyOrderYiBao(Long merchantId) {
        return storeSettlementRepository.findTotalAndNumberFromDailyOrderYiBao(merchantId);
    }

    /***
     *  易宝结算累计入账 - 月
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public  Long sumTotalPriceByMonth( String startStr,String endStr,Long merchantId){
        return storeSettlementRepository.sumTotalPriceByMonth(startStr,endStr,merchantId);
    }

    /**
     *  根据OrderSid 查询
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public StoreSettlement findByOrderSid(String orderSid) {
        return storeSettlementRepository.findByOrderSid(orderSid);
    }
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public StoreSettlement findById(Long id) {
        return storeSettlementRepository.findOne(id);
    }

    /**
     *  根据时间和易宝商户号查询门店结算单
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<StoreSettlement> findByTradeDateAndLedgerNo(String tradeDate,String ledgerNo) {
        return storeSettlementRepository.findByTradeDateAndLedgerNo(tradeDate,ledgerNo);
    }
}
