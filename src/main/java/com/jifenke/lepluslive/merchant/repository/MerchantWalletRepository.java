package com.jifenke.lepluslive.merchant.repository;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantWallet;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by wcg on 16/5/13.
 */
public interface MerchantWalletRepository extends JpaRepository<MerchantWallet, Long> {

  MerchantWallet findByMerchant(Merchant merchant);

}
