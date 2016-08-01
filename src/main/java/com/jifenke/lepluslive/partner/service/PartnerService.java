package com.jifenke.lepluslive.partner.service;

import com.jifenke.lepluslive.global.util.MD5Util;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerWallet;
import com.jifenke.lepluslive.partner.repository.PartnerManagerRepository;
import com.jifenke.lepluslive.partner.repository.PartnerManagerWalletRepository;
import com.jifenke.lepluslive.partner.repository.PartnerRepository;
import com.jifenke.lepluslive.partner.repository.PartnerWalletRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Created by wcg on 16/6/3.
 */
@Service
@Transactional(readOnly = true)
public class PartnerService {



  @Inject
  private PartnerRepository partnerRepository;



    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Partner findPartnerByName(String currentUserLogin) {
        return partnerRepository.findByName(currentUserLogin).get();
    }
}
