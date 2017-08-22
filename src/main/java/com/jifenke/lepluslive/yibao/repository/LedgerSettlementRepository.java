package com.jifenke.lepluslive.yibao.repository;

import com.jifenke.lepluslive.yibao.domain.entities.LedgerSettlement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by zhangwen on 2017/7/12.
 */
public interface LedgerSettlementRepository extends JpaRepository<LedgerSettlement, Long> {

    Page findAll(Specification<LedgerSettlement> whereClause, Pageable pageRequest);

    /**
     *  累计转账，次数
     */
    @Query(value = "SELECT SUM(actual_transfer),count(*) from yb_store_settlement where merchant_id=?1 and state=1", nativeQuery = true)
    List<Object[]> findTotalAndNumberFromDailyOrderYiBao(Long merchantId);

    /**
     *  月累计转账
     */
    @Query(value = "SELECT sum(actual_transfer+refund_expend) from yb_store_settlement  where date_format(trade_date,'%Y-%c-%d') BETWEEN date_format(?1,'%Y-%c-%d') and date_format(?2,'%Y-%c-%d') and merchant_id=?3", nativeQuery = true)
    Long sumTotalPriceByMonth(String startStr, String endStr, Long merchantId);

    LedgerSettlement findByOrderSid(String sid);
}
