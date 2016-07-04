package com.jifenke.lepluslive.weixin.service;

import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.repository.WeiXinUserRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Created by wcg on 16/3/18.
 */
@Service
@Transactional(readOnly = true)
public class WeiXinUserService {

  @Value("${bucket.ossBarCodeReadRoot}")
  private String barCodeRootUrl;



  @Inject
  private WeiXinUserRepository weiXinUserRepository;


  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public WeiXinUser findWeiXinUserByOpenId(String openId) {
    return weiXinUserRepository.findByOpenId(openId);
  }


}
