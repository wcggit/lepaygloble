package com.jifenke.lepluslive.groupon.repository;

import com.jifenke.lepluslive.groupon.domain.entities.GrouponCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * 团购券码 Repository
 * Created by xf on 17-6-16.
 */
public interface GrouponCodeRepository extends JpaRepository<GrouponCode,Long> {
    Page findAll(Specification<GrouponCode> whereClause, Pageable pageRequest);

    GrouponCode findFirstBySid(String sid);

    GrouponCode findOneBySid(String sid);

}
