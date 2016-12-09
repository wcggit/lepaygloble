package com.jifenke.lepluslive.order.repository;

import com.jifenke.lepluslive.order.domain.entities.PosOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by xf on 16-12-5.
 */
public interface PosOrderRepository extends JpaRepository<PosOrder,Long>,JpaSpecificationExecutor<PosOrder> {
    /**
     *  门店下:  今日订单流水(线下)
     */
    @Query(value = "select sum(total_price) from pos_order where merchant_id = ?1 and to_days(complete_date) = to_days(now())",nativeQuery = true)
    Long countTotalPrice(Long merchantId);
}
