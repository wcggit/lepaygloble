package com.jifenke.lepluslive.partner.repository;

import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerInfo;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by wcg on 16/6/3.
 */
public interface PartnerInfoRepository extends JpaRepository<PartnerInfo, Long> {

  PartnerInfo findByPartner(Partner partner);
}
