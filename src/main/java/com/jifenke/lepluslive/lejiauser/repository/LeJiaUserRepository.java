package com.jifenke.lepluslive.lejiauser.repository;

import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by wcg on 16/3/24.
 */
public interface LeJiaUserRepository extends JpaRepository<LeJiaUser, Long> {
    LeJiaUser findByUserSid(String userSid);

    LeJiaUser findByPhoneNumber(String phoneNumber);
}
