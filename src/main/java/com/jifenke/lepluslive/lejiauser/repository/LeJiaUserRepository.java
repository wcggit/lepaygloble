package com.jifenke.lepluslive.lejiauser.repository;

import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.partner.domain.entities.Partner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

/**
 * Created by wcg on 16/3/24.
 */
public interface LeJiaUserRepository extends JpaRepository<LeJiaUser, Long> {

    LeJiaUser findByUserSid(String userSid);

    LeJiaUser findByPhoneNumber(String phoneNumber);

    @Query(value = "select count(*) from le_jia_user where bind_merchant_id = ?1", nativeQuery = true)
    Long countBindMerchant(Long id);

    @Query(value = "select count(*) from le_jia_user where bind_partner_id = ?1", nativeQuery = true)
    Long countPartnerBindLeJiaUser(Long partnerId);

    Long countByBindPartnerAndBindPartnerDateBetween(Partner partner, Date start, Date end);
}
