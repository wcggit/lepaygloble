package com.jifenke.lepluslive.merchant.service;

import com.jifenke.lepluslive.global.util.MD5Util;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantWallet;
import com.jifenke.lepluslive.merchant.domain.entities.OpenRequest;
import com.jifenke.lepluslive.merchant.repository.MerchantRepository;
import com.jifenke.lepluslive.merchant.repository.MerchantUserRepository;
import com.jifenke.lepluslive.merchant.repository.MerchantWalletRepository;
import com.jifenke.lepluslive.merchant.repository.OpenRequestRepository;
import com.jifenke.lepluslive.order.domain.entities.OffLineOrder;
import com.jifenke.lepluslive.partner.domain.entities.Partner;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Created by wcg on 16/3/17.
 */
@Service
@Transactional(readOnly = true)
public class MerchantService {

    @Inject
    private MerchantRepository merchantRepository;


    @Inject
    private MerchantWalletRepository merchantWalletRepository;

    @Inject
    private MerchantUserRepository merchantUserRepository;

    @Inject
    private OpenRequestRepository openRequestRepository;


    /**
     * 获取商家详情
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Merchant findMerchantById(Long id) {
        return merchantRepository.findOne(id);
    }


    public MerchantWallet findMerchantWalletByMerchant(Merchant merchant) {
        return merchantWalletRepository.findByMerchant(merchant);
    }


    public MerchantUser findMerchantUserByName(String name) {
        return merchantUserRepository.findByName(name).get();
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void createOpenRequest(Merchant merchant) {
        OpenRequest openRequest = openRequestRepository.findByMerchant(merchant);
        if (openRequest == null) {
            openRequest = new OpenRequest();
            openRequest.setState(0);
            openRequest.setMerchant(merchant);
        }
        openRequestRepository.save(openRequest);

    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void resetPasswword(MerchantUser merchantUser, String reset, String password) {
        String origin = MD5Util.MD5Encode(password, "utf-8");

        if (merchantUser.getPassword().equals(origin)) {
            merchantUser.setPassword(MD5Util.MD5Encode(reset, "utf-8"));
            merchantUserRepository.save(merchantUser);
        } else {
            throw new RuntimeException();
        }

    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
      public Long countPartnerBindMerchant(Partner partner) {
        return merchantRepository.countByPartner(partner);
    }
}
