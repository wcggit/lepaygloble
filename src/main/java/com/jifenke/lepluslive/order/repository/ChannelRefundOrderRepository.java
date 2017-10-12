package com.jifenke.lepluslive.order.repository;

import com.jifenke.lepluslive.order.domain.entities.ChannelRefundOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 通道退款单
 * Created by zhangwen on 2017/7/12.
 */
public interface ChannelRefundOrderRepository extends JpaRepository<ChannelRefundOrder, Long> {

    /**
     * 某门店某日通道退款记录 2017/9/18
     *
     * @param merchantId 门店ID
     * @param tradeDate  清算日期
     * @return
     */
    List<ChannelRefundOrder> findByMerchantIdAndTradeDateAndState(String merchantId, String tradeDate, int state);

    /**
     * 某门店某日通道退款总额 2017/9/18
     *
     * @param merchantId 门店ID
     * @param tradeDate  清算日期
     * @return
     */
    @Query(value = "select sum(transfer_money)  from channel_refund_order where merchant_id=?1 and trade_date = ?2 and state=2 and order_from=1", nativeQuery = true)
    Long sumDailyTotalRefund(String merchantId, String tradeDate);

    @Query(value = "select count(*)  from channel_refund_order where order_form=2  and trade_date = ?1 and real_mer_num=?2   and order_type=?3", nativeQuery = true)
    Long countRefundByDateAndMerchantNum(String settleDate,String realMerNum,Integer orderType);

    Page findAll(Specification<ChannelRefundOrder> whereClause, Pageable pageRequest);
}
