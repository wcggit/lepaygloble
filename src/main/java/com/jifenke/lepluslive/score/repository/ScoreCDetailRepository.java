package com.jifenke.lepluslive.score.repository;

import com.jifenke.lepluslive.score.domain.entities.ScoreCDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by xf on 17-2-21.
 */
public interface ScoreCDetailRepository extends JpaRepository<ScoreCDetail, Long> {
    Page findAll(Specification<ScoreCDetail> specification, Pageable pageRequest);
}
