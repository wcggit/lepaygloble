package com.jifenke.lepluslive.groupon.repository;

import com.jifenke.lepluslive.groupon.domain.entities.GrouponProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 团购产品 Repository
 * Created by xf on 17-6-16.
 */
public interface GrouponProductRepository extends JpaRepository<GrouponProduct, Long> {

    Page<GrouponProduct> findAll(Specification<GrouponProduct> whereClause, Pageable pageRequest);

    GrouponProduct findBySid(String sid);
}
