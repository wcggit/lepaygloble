package com.jifenke.lepluslive.groupon.repository;

import com.jifenke.lepluslive.groupon.domain.entities.GrouponStatistic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by wcg on 2017/6/27.
 */
public interface GrouponStatisticRepository extends JpaRepository<GrouponStatistic, Long> {

  Page findAll(Specification<GrouponStatistic> financialClause, Pageable pageRequest);

  List<GrouponStatistic> findAllByState(int i);
}
