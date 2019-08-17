package com.jifenke.lepluslive.partner.repository;

import com.jifenke.lepluslive.partner.domain.entities.PartnerWalletOnlineLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by wcg on 16/6/3.
 */
public interface PartnerWalletOnlineLogRepository extends JpaRepository<PartnerWalletOnlineLog, Long> {
    Page findAll(Specification<PartnerWalletOnlineLog> whereClause, Pageable pageRequest);

    @Query(value = "select IFNULL(SUM(after_change_money-before_change_money),0) from partner_wallet_online_log " +
        "where  partner_id = ?1 AND to_days(create_date) = to_days(now()) AND after_change_money>before_change_money ", nativeQuery = true)
    Long findIncomeCommissionByPartnerDaily(Long partnerId);

    @Query(value = "select IFNULL(SUM(after_change_money-before_change_money),0) from partner_wallet_online_log " +
        "where  partner_id = ?1 AND to_days(create_date) = to_days(now()) AND after_change_money<before_change_money ", nativeQuery = true)
    Long findExpendCommissionByPartnerDaily(Long partnerId);
}
