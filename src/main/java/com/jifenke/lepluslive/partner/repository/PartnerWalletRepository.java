package com.jifenke.lepluslive.partner.repository;

import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerWallet;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by wcg on 16/6/3.
 */
public interface PartnerWalletRepository extends JpaRepository<PartnerWallet, Long> {
  PartnerWallet findByPartner(Partner partner);
}
