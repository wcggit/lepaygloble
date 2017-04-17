package com.jifenke.lepluslive.order.repository;

import com.jifenke.lepluslive.order.domain.entities.PosOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by xf on 16-12-5.
 */
public interface PosOrderRepository extends JpaRepository<PosOrder, Long>, JpaSpecificationExecutor<PosOrder> {
    /**
     * 门店下:  今日订单流水(线下)
     */
    @Query(value = "select sum(total_price) from pos_order where merchant_id = ?1 and to_days(complete_date) = to_days(now())", nativeQuery = true)
    Long countTotalPrice(Long merchantId);

    /**
     * 指定商户pos订单最近七天入账总金额 (不包括现金结算)
     */
    @Query(value = "select DATE_FORMAT(complete_date,'%Y-%m-%d'),sum(transfer_money) from pos_order where merchant_id = ?1" +
        " AND trade_flag!=6 AND DATE_SUB(date(?2), INTERVAL 7 DAY) <= date(complete_date) " +
        " group by DATE_FORMAT(complete_date,'%Y-%m-%d')", nativeQuery = true)
    List<Object[]> countWeekPosOrder(Long merchantId, Date completeDate);

    /***
     * 指定商户pos订单最近七天 Pos 刷卡入账
     */
    @Query(value = "select DATE_FORMAT(complete_date,'%Y-%m-%d'),IFNULL(sum(transfer_money),0) from pos_order " +
        "where merchant_id = ?1 AND trade_flag=3 AND DATE_SUB(date(?2), INTERVAL 7 DAY) <= date(complete_date) " +
        "group by DATE_FORMAT(complete_date,'%Y-%m-%d')", nativeQuery = true)
    List<Object[]> countWeekPosCard(Long merchantId, Date completeDate);

    /**
     * 指定商户pos订单最近七天 Pos 移动支付入账 （支付宝）
     */
    @Query(value = "select DATE_FORMAT(complete_date,'%Y-%m-%d'),IFNULL(sum(transfer_money),0) from pos_order " +
        "where merchant_id = ?1 AND trade_flag=0  AND DATE_SUB(date(?2), INTERVAL 7 DAY) <= date(complete_date) " +
        "group by DATE_FORMAT(complete_date,'%Y-%m-%d')", nativeQuery = true)
    List<Object[]> countWeekPosAli(Long merchantId, Date completeDate);

    /**
     * 指定商户pos订单最近七天 Pos 移动支付入账 （微信）
     */
    @Query(value = "select DATE_FORMAT(complete_date,'%Y-%m-%d'),IFNULL(sum(transfer_money),0) from pos_order " +
        "where merchant_id = ?1 AND trade_flag=4  AND DATE_SUB(date(?2), INTERVAL 7 DAY) <= date(complete_date) " +
        "group by DATE_FORMAT(complete_date,'%Y-%m-%d')", nativeQuery = true)
    List<Object[]> countWeekPosWx(Long merchantId, Date completeDate);


    /**
     * 指定商户pos订单红包支付入账
     */
    @Query(value = "select DATE_FORMAT(complete_date,'%Y-%m-%d'),IFNULL(sum(transfer_money-transfer_by_bank),0) from pos_order " +
        "where merchant_id = ?1 AND DATE_SUB(date(?2), INTERVAL 7 DAY) <= date(complete_date)" +
        "group by DATE_FORMAT(complete_date,'%Y-%m-%d')", nativeQuery = true)
    List<Object[]> countWeekPosScore(Long merchantId, Date completeDate);

    /**
     * 查询指定门店 时间段内总入账 (Pos订单)
     */
    @Query(value="select IFNULL(sum(transfer_money),0) from  pos_order where merchant_id = ?1 and complete_date between ?2 and ?3",nativeQuery =true)
    Long countMerchantPosTotal(Long merchantId,String startDate,String endDate);

    /**
     *  查询指定门店 pos 刷卡入账
     */
    @Query(value="select IFNULL(sum(transfer_money),0) from  pos_order where merchant_id = ?1 and trade_flag=3 and complete_date between ?2 and ?3",nativeQuery=true)
    Long countMerchantPosCardTransfer(Long id,String startDate,String endDate);

    /**
     *  查询指定门店 pos移动- 微信入账
     */
    @Query(value="select IFNULL(sum(transfer_money),0) from  pos_order where merchant_id =?1 and trade_flag=3 and complete_date between  ?2 and ?3",nativeQuery = true)
    Long countWxMobileTranfer(Long id,String startDate,String endDate);
    /**
     *  查询指定门店 pos移动- 支付宝入账
     */
    @Query(value="select IFNULL(sum(transfer_money),0) from  pos_order where merchant_id =?1 and trade_flag=0 and complete_date between  ?2 and ?3",nativeQuery = true)
    Long countAliMobileTranfer(Long id,String startDate,String endDate);

    /**
     * 查询指定门店 红包入账(Pos)
     */
    @Query(value="select IFNULL(sum(transfer_money-transfer_by_bank),0) from  pos_order where merchant_id =?1 and complete_date between  ?2 and ?3",nativeQuery = true)
    Long countMobilePosScore(Long id,String startDate,String endDate);
}
