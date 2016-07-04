package com.jifenke.lepluslive.merchant.service;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantWallet;
import com.jifenke.lepluslive.merchant.repository.MerchantRepository;
import com.jifenke.lepluslive.merchant.repository.MerchantUserRepository;
import com.jifenke.lepluslive.merchant.repository.MerchantWalletRepository;
import com.jifenke.lepluslive.order.domain.entities.OffLineOrder;

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
}
