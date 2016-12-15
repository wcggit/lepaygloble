package com.jifenke.lepluslive.order.repository;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.order.domain.entities.OffLineOrder;
import com.jifenke.lepluslive.order.domain.entities.OffLineOrderShare;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by wcg on 16/5/5.
 */
public interface OffLineOrderShareRepository extends JpaRepository<OffLineOrderShare, Long> {

    @Query(value = "select count(*),ifnull(sum(to_lock_merchant),0) from off_line_order_share where lock_merchant_id = ?1 and create_date between ?2 and ?3",nativeQuery = true)
    List<Object[]> countTodayCommission(Long merchantId, Date start, Date end);

    Long countByLockMerchant(Merchant merchant);

    Page findAll(Specification<OffLineOrderShare> orderShareClause, Pageable pageRequest);

    /**
     * 查询今日 佣金收入、今日佣金单数
     * @param obj
     * @return
     */
    @Query(value = "SELECT sum(a.to_lock_merchant)*0.01 as offTodayToalCommission , count(a.id) as offTodayToalNumber from off_line_order_share a WHERE a.lock_merchant_id in (?1) and TO_DAYS(create_date) =TO_DAYS(NOW())",nativeQuery = true)
    Object findTodayCommissionAndTodayNumber(List<Object> obj);

    /**
     * 查询人均佣金收入、累计佣金单数
     * @param totalMember
     * @param obj
     * @return
     */
    @Query(value = "SELECT count(a.id) as offTotalNumber,sum(a.to_lock_merchant)/?1 as offPerCapita from off_line_order_share a where a.lock_merchant_id in (?2)" ,nativeQuery = true)
    Object findPerCapitaAndTotalNumber(Integer totalMember,List<Object> obj);
}
