package com.jifenke.lepluslive.merchant.repository;

import com.jifenke.lepluslive.merchant.domain.entities.MerchantWeiXinUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;

/**
 * Created by wcg on 16/5/17.
 */
public interface MerchantWeiXinUserRepository extends JpaRepository<MerchantWeiXinUser,Long> {

  @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
  MerchantWeiXinUser findByOpenId(String openid);
}
