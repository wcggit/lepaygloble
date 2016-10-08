package com.jifenke.lepluslive.partner.repository;

import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.partner.domain.entities.Partner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * Created by wcg on 16/6/3.
 */
public interface PartnerRepository extends JpaRepository<Partner, Long> {

    Optional<Partner> findByPartnerSid(String userName);

    @Query(value = "select count(*) from merchant where partner_id = ?1", nativeQuery = true)
    Long countParnterBindMerchant(Long id);

    Optional<Partner> findByName(String lowercaseLogin);
}
