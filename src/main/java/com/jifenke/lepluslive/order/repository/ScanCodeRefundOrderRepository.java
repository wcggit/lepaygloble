package com.jifenke.lepluslive.order.repository;

import com.jifenke.lepluslive.order.domain.entities.ScanCodeRefundOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by xf on 17-1-17.
 */
public interface ScanCodeRefundOrderRepository extends JpaRepository<ScanCodeRefundOrder,Long> {
    /**
     *  查找指定门店下：  已完成的退款单 （时间段内）
     */
    @Query(value="SELECT * FROM scan_code_refund_order ro,scan_code_order o where ro.scan_code_order_id = o.id and o.merchant_id = ?1 and ro.state=1 and ro.complete_date between ?2 and ?3 ",nativeQuery = true)
    List<ScanCodeRefundOrder> findScanOrderByMerchant(Long merchantId, String startDate, String endDate);

    /**
     *  查找指定门店下：  已完成的退款总金额和总红包
     */
    @Query(value=" select ifnull(sum(o.transfer_money_from_true_pay),0),ifnull(sum(o.transfer_money_from_score),0) " +
                " from scan_code_refund_order ro,scan_code_order o " +
                " where ro.scan_code_order_id = o.id " +
                " and o.merchant_id = ?1 and ro.state=1 and ro.complete_date between ?2 and ?3 ",nativeQuery = true)
    List<Object[]> countRefundMoneyAndScore(Long merchantId, String startDate, String endDate);

}
