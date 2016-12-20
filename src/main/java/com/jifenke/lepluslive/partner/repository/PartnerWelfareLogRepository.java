package com.jifenke.lepluslive.partner.repository;

import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerWelfareLog;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

/**
 * Created by wcg on 16/10/10.
 */
public interface PartnerWelfareLogRepository extends JpaRepository<PartnerWelfareLog, Long> {

    Integer countByPartnerAndCreateDateBetween(Partner partner, Date startOfDay, Date endOfDay);

}
