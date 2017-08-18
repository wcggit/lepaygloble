package com.jifenke.lepluslive.partner.repository;

import com.jifenke.lepluslive.partner.domain.entities.PartnerManagerWalletLog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.UsesJava7;

import java.util.Date;
import java.util.List;

/**
 * Created by wcg on 16/6/3.
 */
public interface PartnerManagerWalletLogRepository extends JpaRepository<PartnerManagerWalletLog, Long> {
    @Query(value = "SELECT IFNULL(sum(after_change_money-before_change_money),0) FROM partner_manager_wallet_log  where to_days(create_date) = to_days(now()) and partner_manager_id = ?1 ",nativeQuery = true)
    Long findDailyCommissionByPartnerManager(Long partnerManagerId);

    @Query(value="select DATE_FORMAT(create_date,\"%Y-%m-%d\"),count(after_change_money-before_change_money) from partner_manager_wallet_log where partner_manager_id = ?1 and create_date between ?2 and ?3  GROUP BY DATE_FORMAT(create_date,\"%Y-%m-%d\")",nativeQuery = true)
    List<Object[]> findDailyCommissionByPartnerManager(Long partnerId, Date startDate, Date endDate);

    @Query(value = "select DATE_FORMAT(create_date,'%Y-%m-%d'),IFNULL(SUM(after_change_money-before_change_money),0) from partner_manager_wallet_log where  partner_manager_id = ?1 AND DATE_SUB(date(?2), INTERVAL 7 DAY) <= date(create_date) group by DATE_FORMAT(create_date,'%Y-%m-%d')",nativeQuery = true)
    List<Object[]> findCommissionByPartnerWeek(Long partnerId, Date startDate);
}
