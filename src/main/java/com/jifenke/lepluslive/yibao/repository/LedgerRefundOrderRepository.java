package com.jifenke.lepluslive.yibao.repository;

import com.jifenke.lepluslive.yibao.domain.entities.LedgerRefundOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by zhangwen on 2017/7/12.
 */
public interface LedgerRefundOrderRepository extends JpaRepository<LedgerRefundOrder, Long> {

    Page findAll(Specification<LedgerRefundOrder> whereClause, Pageable pageRequest);

    List<LedgerRefundOrder> findByMerchantIdAndTradeDate(String merchantId, String tradeDate);

    @Query(value="select sum(transfer_money)  from yb_ledger_refund_order where merchant_id=?1 and trade_date = ?2 and state=2 and order_from=1",nativeQuery=true)
    Long sumDailyTotalRefund(String merchantId, String tradeDate);

    
}
