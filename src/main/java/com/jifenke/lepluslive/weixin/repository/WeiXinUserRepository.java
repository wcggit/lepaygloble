package com.jifenke.lepluslive.weixin.repository;

import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by wcg on 16/3/18.
 */
public interface WeiXinUserRepository extends JpaRepository<WeiXinUser,Long> {

  WeiXinUser findByOpenId(String openId);
}
