package com.jifenke.lepluslive.order.repository;

import com.jifenke.lepluslive.order.domain.entities.ScanCodeOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by xf on 16-12-28.
 * 掌富通道线下订单数据
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


    /**
     *   门店下:  一周内扫码订单数据
     */
    @Query(value="select DATE_FORMAT(complete_date,'%Y-%m-%d'),sum(transfer_money) from scan_code_order " +
        " where merchant_id = ?1 AND DATE_SUB(date(?2), INTERVAL 7 DAY) <= date(complete_date) group by DATE_FORMAT(complete_date,'%Y-%m-%d')",nativeQuery = true)
    List<Object[]> countWeekScanCodeOrder(Long merchantId, Date startDate);

    /**
     *  门店下: 扫码牌微信最近七天入账
     */
    @Query(value="select DATE_FORMAT(complete_date,'%Y-%m-%d'),IFNULL(sum(transfer_money_from_true_pay),0) from scan_code_order " +
        " where merchant_id = ?1 AND DATE_SUB(date(?2), INTERVAL 7 DAY) <= date(complete_date) group by DATE_FORMAT(complete_date,'%Y-%m-%d')",nativeQuery = true)
    List<Object[]> countWeekScanCodeWx(Long merchantId, Date startDate);

    /**
     *  门店下线下订单红包支付入账
     */
    @Query(value="select DATE_FORMAT(complete_date,'%Y-%m-%d'),IFNULL(sum(transfer_money-transfer_money_from_true_pay),0) from scan_code_order " +
        "where merchant_id = ?1 AND DATE_SUB(date(?2), INTERVAL 7 DAY) <= date(complete_date) " +
        "group by DATE_FORMAT(complete_date,'%Y-%m-%d')",nativeQuery = true)
    List<Object[]> countWeekScanCodeScore(Long merchantId, Date completeDate);

    /**
     * 查询指定门店 时间段内总入账 (扫码订单)
     */
    @Query(value="select IFNULL(sum(transfer_money),0) from scan_code_order where merchant_id = ?1 and complete_date between ?2  and ?3",nativeQuery = true)
    Long countMerchantTotal(Long id, String startDate, String endDate);

    /**
     * 查询指定门店 微信总入账
     */
    @Query(value="select IFNULL(sum(transfer_money_from_true_pay),0) from scan_code_order where merchant_id = ?1 and complete_date between ?2 and ?3",nativeQuery = true)
    Long countMerchantWxTransfer(Long id, String startDate, String endDate);

    /**
     * 查询指定门店 红包入账(扫码)
     */
    @Query(value="select IFNULL(sum(transfer_money-transfer_money_from_true_pay),0) from scan_code_order where merchant_id =?1 and complete_date between  ?2 and ?3",nativeQuery = true)
    Long countMerchantOffScore(Long id, String startDate, String endDate);

    /**
     * 查询指定门店 红包实际支付
     */
    @Query(value="select IFNULL(sum(true_score),0) from scan_code_order where merchant_id =?1 and complete_date between  ?2 and ?3",nativeQuery = true)
    Long countMerchantCustScore(Long id, String startDate, String endDate);


    /**
     * 查询指定门店 微信实际支付
     */
    @Query(value="select IFNULL(sum(true_pay),0) from scan_code_order where merchant_id = ?1 and complete_date between ?2 and ?3",nativeQuery = true)
    Long countMerchantWxCustTransfer(Long id, String startDate, String endDate);


    /**
     *  查询指定门店 订单记录(指定时间段)
     */
    @Query(value="select * from scan_code_order where merchant_id =?1 and complete_date between  ?2 and ?3",nativeQuery = true)
    List<ScanCodeOrder> findByMerchantAndDate(Long merchantId, String startDate, String endDate);


    Page findAll(Specification<ScanCodeOrder> whereClause, Pageable pageRequest);

}
