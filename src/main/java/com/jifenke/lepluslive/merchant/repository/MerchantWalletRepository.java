package com.jifenke.lepluslive.merchant.repository;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantWallet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by wcg on 16/5/13.
 */
public interface MerchantWalletRepository extends JpaRepository<MerchantWallet, Long> {

    MerchantWallet findByMerchant(Merchant merchant);

    @Query(value = "SELECT * FROM  merchant_wallet where merchant_user_id = ?1 ", nativeQuery = true)
    MerchantWallet findByMerchantUser(Long merchantUserId);

    /**
     * 根据门店ID获取门店钱包  17/01/03
     *
     * @param merchantId 门店ID
     */
    @Query(value = "SELECT * FROM merchant_wallet WHERE merchant_id = ?1", nativeQuery = true)
    MerchantWallet findByMerchantId(Long merchantId);

    @Query(value = "SELECT IFNULL(sum(w.total_money),0) FROM merchant m " +
        " LEFT JOIN merchant_wallet w ON m.id=w.merchant_id " +
        " WHERE m.merchant_user_id = ?1", nativeQuery = true)
    Long countTotalMoneyByMerchantUserId(Long merchantUserId);

    @Query(value = "SELECT IFNULL(sum(w.total_money),0) FROM merchant m " +
        " LEFT JOIN merchant_wallet_online w ON m.id=w.merchant_id " +
        " WHERE m.merchant_user_id = ?1", nativeQuery = true)
    Long countTotalMoneyOnlineByMerchantUserId(Long merchantUserId);

}
