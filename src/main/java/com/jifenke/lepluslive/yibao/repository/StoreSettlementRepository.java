package com.jifenke.lepluslive.yibao.repository;

import com.jifenke.lepluslive.yibao.domain.entities.StoreSettlement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by zhangwen on 2017/7/12.
 */
public interface StoreSettlementRepository extends JpaRepository<StoreSettlement, Long> {

    Page findAll(Specification<StoreSettlement> whereClause, Pageable pageRequest);

    /**
     *  累计转账，次数
     */
    @Query(value = "SELECT SUM(actual_transfer),count(*) from yb_ledger_settlement where merchant_user_id=?1 and state=1", nativeQuery = true)
    List<Object[]> findTotalAndNumberFromDailyOrderYiBao(Long merchantUserId);

    /**
     *  月累计转账
     */
    @Query(value = "SELECT sum(actual_transfer+refund_expend) from yb_ledger_settlement  where date_format(trade_date,'%Y-%c-%d') BETWEEN date_format(?1,'%Y-%c-%d') and date_format(?2,'%Y-%c-%d') and merchant_user_id=?3", nativeQuery = true)
    Long sumTotalPriceByMonth(String startStr, String endStr, Long merchantUserId);

    StoreSettlement findByOrderSid(String orderSid);

    @Query(value = "SELECT * from yb_store_settlement  where trade_date=?1 and ledger_no=?2 ", nativeQuery = true)
    List<StoreSettlement>  findByTradeDateAndLedgerNo(String tradeDate, String ledgerNo);
}
