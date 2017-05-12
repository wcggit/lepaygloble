package com.jifenke.lepluslive.partner.repository;

import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerManager;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by wcg on 16/6/3.
 */
public interface PartnerManagerRepository extends JpaRepository<PartnerManager, Long> {

    PartnerManager findByPartnerId(Long accountId);

    Optional<PartnerManager> findByName(String lowercaseLogin);

    PartnerManager findByPartnerManagerSid(String partnerManagerSid);
}
