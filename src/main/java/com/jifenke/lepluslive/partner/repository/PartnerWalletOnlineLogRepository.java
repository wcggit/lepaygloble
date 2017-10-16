package com.jifenke.lepluslive.partner.repository;

import com.jifenke.lepluslive.partner.domain.entities.PartnerWalletOnlineLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by wcg on 16/6/3.
 */
public interface PartnerWalletOnlineLogRepository extends JpaRepository<PartnerWalletOnlineLog, Long> {
    Page findAll(Specification<PartnerWalletOnlineLog> whereClause, Pageable pageRequest);
}
