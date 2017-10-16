package com.jifenke.lepluslive.partner.service;

import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerWalletOnline;
import com.jifenke.lepluslive.partner.repository.PartnerWalletOnlineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Created by lss on 2017/5/12.
 */
@Service
@Transactional(readOnly = true)
public class PartnerWalletOnlineService {
    @Inject
    private PartnerWalletOnlineRepository partnerWalletOnlineRepository;


    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PartnerWalletOnline findByPartner(Partner partner) {
        return partnerWalletOnlineRepository.findByPartner(partner);
    }


}
