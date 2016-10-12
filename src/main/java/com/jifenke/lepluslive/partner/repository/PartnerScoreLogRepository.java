package com.jifenke.lepluslive.partner.repository;

import com.jifenke.lepluslive.partner.domain.entities.PartnerScoreLog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

/**
 * Created by wcg on 16/9/28.
 */
public interface PartnerScoreLogRepository extends JpaRepository<PartnerScoreLog, Long> {

    Page findAll(Specification<PartnerScoreLog> whereClause,Pageable pageRequest);
    Integer countByPartnerIdAndCreateDateBetween(Long id, Date startOfDay, Date endOfDay);
}
