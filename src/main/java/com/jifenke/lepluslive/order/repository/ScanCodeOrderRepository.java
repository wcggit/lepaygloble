package com.jifenke.lepluslive.order.repository;

import com.jifenke.lepluslive.order.domain.entities.ScanCodeOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by xf on 16-12-28.
 */
public interface ScanCodeOrderRepository extends JpaRepository<ScanCodeOrder,String> {
    /**
     *  门店下:  今日转账总金额
     */
    @Query(value = "select sum(transfer_money) from scan_code_order where merchant_id =?1  and to_days(complete_date) = to_days(now()) ", nativeQuery = true)
    Long countDailyScanTransferMoney(Long merchantId);

    /**
     *  门店下:  统计订单详情
     */
    @Query(value = "select count(*),sum(total_price),sum(commission),sum(transfer_money) from scan_code_order where merchant_id =?1", nativeQuery = true)
    List<Object[]> countScanOrderDetail(Long id);

}
