package com.jifenke.lepluslive.lejiauser.service;


import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.repository.LeJiaUserRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Created by wcg on 16/4/21.
 */
@Service
@Transactional(readOnly = true)
public class LeJiaUserService {

    @Value("${bucket.ossBarCodeReadRoot}")
    private String barCodeRootUrl;

    @Inject
    private LeJiaUserRepository leJiaUserRepository;


    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public LeJiaUser findUserByUserSid(String userSid) {
        return leJiaUserRepository.findByUserSid(userSid);
    }


}
