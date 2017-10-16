package com.jifenke.lepluslive.partner.repository;

import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerWalletOnline;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by zhangwen on 16/11/05.
 */
public interface PartnerWalletOnlineRepository extends JpaRepository<PartnerWalletOnline, Long> {

  PartnerWalletOnline findByPartner(Partner partner);
}
