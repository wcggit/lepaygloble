package com.jifenke.lepluslive.merchant.service;

import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.repository.MerchantUserRepository;
import com.jifenke.lepluslive.security.SecurityUtils;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Created by wcg on 16/6/27.
 */
@Service
@Transactional(readOnly = true)
public class MerchantUserService {

    @Inject
    private MerchantUserRepository merchantUserRepository;


    public MerchantUser getUserWithAuthorities() {
        return  merchantUserRepository.findByName(SecurityUtils.getCurrentUserLogin()).get();
    }
}
