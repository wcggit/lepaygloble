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
}
