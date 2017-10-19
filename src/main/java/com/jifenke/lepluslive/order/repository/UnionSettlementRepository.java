package com.jifenke.lepluslive.order.repository;

import com.jifenke.lepluslive.order.domain.entities.UnionSettlement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by xf on 17-10-18.
 */
public interface UnionSettlementRepository extends JpaRepository<UnionSettlement,Long> {
    Page findAll(Specification<UnionSettlement> whereClause, Pageable pageRequest);
}
