package com.jifenke.lepluslive.merchant.repository;

import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by wcg on 16/5/18.
 */
public interface MerchantUserRepository extends JpaRepository<MerchantUser,Long> {

  Optional<MerchantUser> findByName(String userName);

}
