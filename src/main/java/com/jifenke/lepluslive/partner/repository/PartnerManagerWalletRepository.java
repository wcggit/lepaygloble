package com.jifenke.lepluslive.partner.repository;

import com.jifenke.lepluslive.partner.domain.entities.PartnerManager;
import com.jifenke.lepluslive.partner.domain.entities.PartnerManagerWallet;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by wcg on 16/6/3.
 */
public interface PartnerManagerWalletRepository extends JpaRepository<PartnerManagerWallet, Long> {

  PartnerManagerWallet findByPartnerManager(PartnerManager partnerManager);

}
