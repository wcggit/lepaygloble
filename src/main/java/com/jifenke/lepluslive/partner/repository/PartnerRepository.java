package com.jifenke.lepluslive.partner.repository;

import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.partner.domain.entities.Partner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;
import java.util.Optional;

import javax.persistence.LockModeType;

/**
 * Created by wcg on 16/6/3.
 */
public interface PartnerRepository extends JpaRepository<Partner, Long> {

    Optional<Partner> findByPartnerSid(String userName);

    @Query(value = "select count(*) from merchant where partner_id = ?1 and partnerShip != 2", nativeQuery = true)
    Long countParnterBindMerchant(Long id);

    Optional<Partner> findByName(String lowercaseLogin);

    @Modifying
    @Query(value = "select id from scorea where scorea.le_jia_user_id = ?1 for update ; update scorea set score = score+?2,total_score=total_score+?2,last_update_date=now() where scorea.le_jia_user_id = ?1,;insert into  scorea_detail (date_created,number,operate,scorea_id,order_sid,origin) values (now(),?1,'合伙人发福利',(select id from scorea where scorea.le_jia_user_id = ?1),?3,10)", nativeQuery = true)
    void welfareToUser(Serializable userId, Long score, String sid);
}
