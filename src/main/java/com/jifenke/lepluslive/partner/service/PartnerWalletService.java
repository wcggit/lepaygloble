package com.jifenke.lepluslive.partner.service;

import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerWallet;
import com.jifenke.lepluslive.partner.repository.PartnerWalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Created by xf on 17-3-22.
 */
@Service
@Transactional(readOnly = true)
public class PartnerWalletService {
    @Inject
    private PartnerWalletRepository partnerWalletRepository;

    @Transactional(propagation = Propagation.REQUIRED,readOnly = true)
    public PartnerWallet findByPartner(Partner partner) {
        return partnerWalletRepository.findByPartner(partner);
    }
}


