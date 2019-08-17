package com.jifenke.lepluslive.groupon.repository;

import com.jifenke.lepluslive.groupon.domain.entities.GrouponRefund;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * GrouponRefundRepository
 * @author XF
 * @date 2017/6/20
 */
public interface GrouponRefundRepository extends JpaRepository<GrouponRefund,Long> {
    Page findAll(Specification<GrouponRefund> whereClause, Pageable pageRequest);
}
