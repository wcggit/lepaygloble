package com.jifenke.lepluslive.order.repository;


import com.jifenke.lepluslive.merchant.domain.criteria.DataOverviewCriteria;
import com.jifenke.lepluslive.order.domain.entities.OnLineOrderShare;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by wanjun on 2016/12/15.
 */
public interface OnLineOrderShareRepository extends JpaRepository<OnLineOrderShare,Long> {
    Page findAll(Specification<OnLineOrderShare> whereClause, Pageable pageRequest);



    /**
     * 查询今日 佣金收入、今日佣金单数
     * @param obj
     * @return
     */
    @Query(value = "SELECT ifnull(sum(a.to_lock_merchant)*0.01,0) as onTodayToalCommission , count(a.id) as onTodayToalNumber from on_line_order_share a WHERE a.lock_merchant_id in (?1) and TO_DAYS(create_date) =TO_DAYS(NOW())",nativeQuery = true)
    List<Object []> findTodayCommissionAndTodayNumber(List<Object> obj);

    /**
     * 查询人均佣金收入、累计佣金单数
     * @param totalMember
     * @param obj
     * @return
     */
    @Query(value = "SELECT count(a.id) as onTotalNumber,ifnull(sum(a.to_lock_merchant)/?1*0.01,0) as onPerCapita from on_line_order_share a where a.lock_merchant_id in (?2)" ,nativeQuery = true)
    List<Object []> findPerCapitaAndTotalNumber(Integer totalMember, List<Object> obj);


}
