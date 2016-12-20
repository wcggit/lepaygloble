package com.jifenke.lepluslive.merchant.repository;

import com.jifenke.lepluslive.merchant.domain.entities.MerchantWalletOnline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


/**
 * Created by lss on 16-11-28.
 */
public interface MerchantWalletOnlineRepository extends JpaRepository<MerchantWalletOnline, Long> {

    @Query(value="SELECT total_money from merchant_wallet_online where merchant_id=?1",nativeQuery = true)
    Long findTotalMoney(Long id);


}
