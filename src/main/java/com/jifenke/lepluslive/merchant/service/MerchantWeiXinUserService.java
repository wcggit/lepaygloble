package com.jifenke.lepluslive.merchant.service;

import com.jifenke.lepluslive.lejiauser.domain.entities.RegisterOrigin;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantWeiXinUser;
import com.jifenke.lepluslive.merchant.repository.MerchantWeiXinUserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

/**
 * Created by wcg on 16/5/17.
 */
@Service
@Transactional(readOnly = true)
public class MerchantWeiXinUserService {

    @Inject
    private MerchantWeiXinUserRepository merchantWeiXinUserRepository;

    @Inject
    private MerchantUserService merchantUserService;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void unBindMerchantUser(MerchantUser merchantUser) {
        List<MerchantWeiXinUser>
            merchantWeiXinUsers =
            merchantWeiXinUserRepository.findAllByMerchantUser(merchantUser);
        for (MerchantWeiXinUser merchantWeiXinUser : merchantWeiXinUsers) {
            merchantWeiXinUser.setMerchantUser(null);
            merchantWeiXinUserRepository.save(merchantWeiXinUser);
        }

    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void unBindMerchantWeiXinUser(Long id) {
        MerchantWeiXinUser merchantWeiXinUser = merchantWeiXinUserRepository.findOne(id);
        merchantWeiXinUser.setMerchantUser(null);
        merchantWeiXinUserRepository.save(merchantWeiXinUser);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<MerchantWeiXinUser> findMerchantWeixinUserByMerchanUsers(
        List<MerchantUser> merchantUsers) {
        List<MerchantWeiXinUser> results = new ArrayList<>();
        merchantUsers.stream().map(merchantUser -> {
            List<MerchantWeiXinUser> merchantWeiXinUsers = merchantWeiXinUserRepository.findAllByMerchantUser(merchantUser);
            results.addAll(merchantWeiXinUsers);
            return merchantUser;
        }).collect(Collectors.toList());
        return results;
    }

}
