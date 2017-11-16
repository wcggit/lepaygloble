package com.jifenke.lepluslive.partner.repository;

import com.jifenke.lepluslive.partner.domain.entities.PartnerWalletLog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by wcg on 16/6/3.
 */
public interface PartnerWalletLogRepository extends JpaRepository<PartnerWalletLog, Long> {
    @Query(value = "select DATE_FORMAT(create_date,'%Y-%m-%d'),IFNULL(SUM(after_change_money-before_change_money),0) from partner_wallet_log " +
        "where  partner_id = ?1 AND DATE_SUB(date(?2), INTERVAL 7 DAY) <= date(create_date) group by DATE_FORMAT(create_date,'%Y-%m-%d')", nativeQuery = true)
    List<Object[]> findCommissionByPartnerWeek(Long partnerId, Date startDate);

    @Query(value = "select IFNULL(SUM(after_change_money-before_change_money),0) from partner_wallet_log " +
        "where  partner_id = ?1 AND to_days(create_date) = to_days(now()) AND  after_change_money>before_change_money  ", nativeQuery = true)
    Long findIncomeCommissionByPartnerDaily(Long partnerId);

    @Query(value = "select IFNULL(SUM(after_change_money-before_change_money),0) from partner_wallet_log " +
        "where  partner_id = ?1 AND to_days(create_date) = to_days(now()) AND  after_change_money>before_change_money  ", nativeQuery = true)
    Long findExpendCommissionByPartnerDaily(Long partnerId);
}
