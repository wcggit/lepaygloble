package com.jifenke.lepluslive.order.repository;

import com.jifenke.lepluslive.order.domain.entities.OffLineOrder;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by wcg on 16/5/5.
 */
public interface OffLineOrderRepository extends JpaRepository<OffLineOrder, Long> {

    Page findAll(Specification<OffLineOrder> whereClause, Pageable pageRequest);

    @Query(value = "select merchant_id,sum(transfer_money) from off_line_order where state = 1 and complete_date between ?1 and ?2 group by `merchant_id`  ", nativeQuery = true)
    List<Object[]> countTransferMoney(Date start, Date end);

    @Query(value = "select count(*),sum(total_price),sum(lj_commission),sum(transfer_money) from off_line_order where merchant_id = ?1 and complete_date between ?2 and ?3", nativeQuery = true)
    List<Object[]> countTodayOrderDetail(Long id, Date start, Date end);

    @Query(value = "select count(*),sum(total_price),sum(lj_commission),sum(transfer_money) from off_line_order where merchant_id =?1", nativeQuery = true)
    List<Object[]> countOrderDetail(Long id);

    /**
     *  门店下:  会员消费次数 / 消费总金额 / 红包总额  (全部)
     */
    @Query(value = "select count(*),sum(total_price),sum(rebate) from off_line_order where merchant_id =?1 and rebate_way = 1", nativeQuery = true)
    List<Object[]> countMemberOrderDetail(Long merchantId);

    /**
     *  门店下:  会员消费次数 / 消费总金额 / 红包总额  (今日)
     */
    @Query(value = "select count(*),sum(total_price),sum(rebate) from off_line_order where merchant_id =?1 and rebate_way = 1 and to_days(complete_date) = to_days(now())", nativeQuery = true)
    List<Object[]> countMemberDailyOrderDetail(Long merchantId);


    /**
     *  门店下:  今日转账总金额
     */
    @Query(value = "select sum(transfer_money) from off_line_order where merchant_id =?1  and to_days(complete_date) = to_days(now())", nativeQuery = true)
    Long countDailyTransferMoney(Long merchantId);

    /**
     *  门店下:  今日订单流水(线下)
     */
    @Query(value = "select sum(total_price) from off_line_order where merchant_id = ?1 and to_days(complete_date) = to_days(now())",nativeQuery = true)
    Long countDailyTotalPrice(Long merchantId);

    @Query(value = "select count(*) from off_line_order where merchant_id = ?1 and to_days(complete_date) = to_days(now())",nativeQuery = true)
    Long countDailyOrderNum(Long merchantId);


    /**
     *  指定商户扫码订单最近七天入账总金额
     */
    @Query(value="select DATE_FORMAT(complete_date,'%Y-%m-%d'),sum(transfer_money) from off_line_order " +
                " where merchant_id = ?1 AND DATE_SUB(date(?2), INTERVAL 7 DAY) <= date(complete_date) group by DATE_FORMAT(complete_date,'%Y-%m-%d')",nativeQuery = true)
    List<Object[]> countWeekOfflineOrder(Long merchantId, Date startDate);

    /**
     *  扫码牌微信最近七天入账
     */
    @Query(value="select DATE_FORMAT(complete_date,'%Y-%m-%d'),IFNULL(sum(transfer_money_from_true_pay),0) from off_line_order " +
          " where merchant_id = ?1 AND DATE_SUB(date(?2), INTERVAL 7 DAY) <= date(complete_date) group by DATE_FORMAT(complete_date,'%Y-%m-%d')",nativeQuery = true)
    List<Object[]> countWeekOfflineWx(Long merchantId, Date startDate);

    /**
     * 指定商户线下订单红包支付入账
     */
    @Query(value="select DATE_FORMAT(complete_date,'%Y-%m-%d'),IFNULL(sum(transfer_money-transfer_money_from_true_pay),0) from off_line_order " +
        "where merchant_id = ?1 AND DATE_SUB(date(?2), INTERVAL 7 DAY) <= date(complete_date) " +
        "group by DATE_FORMAT(complete_date,'%Y-%m-%d')",nativeQuery = true)
    List<Object[]> countWeekOffScore(Long merchantId, Date completeDate);

    /**
     * 查询指定门店 时间段内总入账 (扫码订单)
     */
    @Query(value="select IFNULL(sum(transfer_money),0) from off_line_order where merchant_id = ?1 and complete_date between ?2  and ?3",nativeQuery = true)
    Long countMerchantTotal(Long id, String startDate, String endDate);

    /**
     * 查询指定门店 微信总入账
     */
    @Query(value="select IFNULL(sum(transfer_money_from_true_pay),0) from off_line_order where merchant_id = ?1 and complete_date between ?2 and ?3",nativeQuery = true)
    Long countMerchantWxTransfer(Long id, String startDate, String endDate);

    /**
     * 查询指定门店 红包入账(扫码)
     */
    @Query(value="select IFNULL(sum(transfer_money-transfer_money_from_true_pay),0) from off_line_order where merchant_id =?1 and complete_date between  ?2 and ?3",nativeQuery = true)
    Long countMerchantOffScore(Long id, String startDate, String endDate);

    /**
     * 指定商户线下订单红包支付入账
     */
    @Query(value = "select merchant_id,sum(true_pay),sum(total_price) from off_line_order where state = 1 and merchant_id = ?1 ", nativeQuery = true)
    List<Object[]> findMyCodePriceByMerchantid(Long merchantId);


    /**
     *  查询指定门店的订单记录 （指定时间段）
     */
    @Query(value=" select * from off_line_order where merchant_id = ?1 and complete_date between ?2 and ?3 and (state = 1 or  state = 2) ",nativeQuery = true)
    List<OffLineOrder> findByMerchantAndDate(Long merchantId, String startTime, String endTime);

    /**
     *  查询指定门店下用户消费金额 （时间段内）
     */
    @Query(value="select IFNULL(sum(true_pay),0) from off_line_order where merchant_id = ?1 and complete_date between ?2 and ?3",nativeQuery = true)
    Long findCustPayByMerchantAndDate(Long merchantId, String startTime, String endTime);

    /**
     *  查询指定门店下用户消费红包 （时间段内）
     */
    @Query(value="select IFNULL(sum(true_score),0) from off_line_order where merchant_id = ?1 and complete_date between ?2 and ?3",nativeQuery = true)
    Long findCustScoreByMerchantAndDate(Long merchantId, String startTime, String endTime);


    /***
     *  查询指定日期总流水、总入账
     */
    @Query(value="select IFNULL(sum(total_price),0),IFNULL(sum(transfer_money),0) from off_line_order where merchant_id = ?1 and complete_date between ?2 and ?3",nativeQuery = true)
    List<Object[]> countTotalTransferByMerchantAndDate(Long merchantId, String startTime, String endTime);


    @Query(value="select count(1),IFNULL(sum(total_price),0),IFNULL(sum(transfer_money),0) from off_line_order where merchant_id = ?1 and complete_date between ?2 and ?3 and (rebate_way=1 or rebate_way=3)",nativeQuery = true)
    List<Object[]> countLejiaTransferByMerchantAndDate(Long merchantId, String startTime, String endTime);

    @Query(value="select count(1),IFNULL(sum(total_price),0),IFNULL(sum(transfer_money),0) from off_line_order where merchant_id = ?1 and complete_date between ?2 and ?3 and (rebate_way=0 or rebate_way=2 or rebate_way=4 or rebate_way=6) ",nativeQuery = true)
    List<Object[]> countCommonTransferByMerchantAndDate(Long merchantId, String startTime, String endTime);


    OffLineOrder findByOrderSid(String orderSid);


    @Query(value = "SELECT * FROM off_line_order WHERE order_sid=?1", nativeQuery = true)
    OffLineOrder findOneByOrderSid(String orderSid);
}

