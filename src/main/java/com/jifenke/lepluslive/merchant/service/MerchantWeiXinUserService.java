package com.jifenke.lepluslive.merchant.service;

import com.jifenke.lepluslive.lejiauser.domain.entities.RegisterOrigin;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantWeiXinUser;
import com.jifenke.lepluslive.merchant.repository.MerchantWeiXinUserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by wcg on 16/5/17.
 */
@Service
@Transactional(readOnly = true)
public class MerchantWeiXinUserService {

    @Inject
    private MerchantWeiXinUserRepository merchantWeiXinUserRepository;

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
}
