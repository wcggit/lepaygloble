package com.jifenke.lepluslive.yibao.service;

import com.jifenke.lepluslive.yibao.domain.entities.MerchantUserLedger;
import com.jifenke.lepluslive.yibao.repository.MerchantUserLedgerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Created by zhangwen on 2017/7/12.
 */
@Service
@Transactional(readOnly = true)
public class MerchantUserLedgerService {

  @Inject
  private MerchantUserLedgerRepository repository;

  /**
   *  根据易宝的子商户号
   */
  public MerchantUserLedger findByLedgerNo(String ledgerNo) {
    return repository.findByLedgerNo(ledgerNo);
  }
}
