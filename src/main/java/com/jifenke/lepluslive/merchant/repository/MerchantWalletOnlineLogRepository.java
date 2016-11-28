package com.jifenke.lepluslive.merchant.repository;


import com.jifenke.lepluslive.merchant.domain.entities.MerchantWalletOnlineLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by root on 16-11-28.
 */
public interface MerchantWalletOnlineLogRepository extends JpaRepository<MerchantWalletOnlineLog, Long> {

    @Query(value="SELECT count(*),sum(change_money) from merchant_wallet_online_log WHERE merchant_id=?1 AND create_date  between ?2 and ?3 and type in (1,2)",nativeQuery = true)
    List<Object[]> findOneDayLogCountAndSum(Long id, Date start, Date end);

    @Query(value="SELECT count(*) FROM merchant_wallet_online_log WHERE merchant_id=?1 and type in (1,2) ",nativeQuery = true)
    Integer findTotalCount(Long id);
}
