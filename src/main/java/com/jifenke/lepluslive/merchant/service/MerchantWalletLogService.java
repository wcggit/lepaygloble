package com.jifenke.lepluslive.merchant.service;


import com.jifenke.lepluslive.merchant.domain.entities.MerchantWalletLog;
import com.jifenke.lepluslive.merchant.repository.MerchantWalletLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Created by lss on 2016/8/30.
 */
@Service
@Transactional(readOnly = true)
public class MerchantWalletLogService {

  @Inject
  private MerchantWalletLogRepository merchantWalletLogRepository;


  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void saveMerchantWalletLog(MerchantWalletLog merchantWalletLog) {
    merchantWalletLogRepository.saveAndFlush(merchantWalletLog);
  }

}
