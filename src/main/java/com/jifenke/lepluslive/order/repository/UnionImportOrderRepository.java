package com.jifenke.lepluslive.order.repository;

import com.jifenke.lepluslive.order.domain.entities.UnionImportOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by xf on 17-10-18.
 */
public interface UnionImportOrderRepository extends JpaRepository<UnionImportOrder,Long> {
    Page findAll(Specification<UnionImportOrder> whereClause, Pageable pageRequest);
}
