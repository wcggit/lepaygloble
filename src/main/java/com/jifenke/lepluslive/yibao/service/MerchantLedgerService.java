package com.jifenke.lepluslive.yibao.service;


import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.yibao.domain.entities.MerchantLedger;
import com.jifenke.lepluslive.yibao.domain.entities.MerchantUserLedger;
import com.jifenke.lepluslive.yibao.repository.MerchantLedgerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;


/**
 * Created by zhangwen on 2017/7/12.
 */
@Service
@Transactional(readOnly = true)
public class MerchantLedgerService {

  @Inject
  private MerchantLedgerRepository merchantLedgerRepository;

  /**
   *  根据商户账号获取Yibao 子商户
   * @param
   * @return
   */
  @Transactional(readOnly = true)
  public MerchantLedger findMerchantLedgerByMerchant(Merchant merchant) {
    return merchantLedgerRepository.findByMerchant(merchant);
  }

  /**
  *  根据子商户号获取统一子商户门店
  */
  @Transactional(readOnly = true)
  public List<Object[]> findMerchantLedgerByMerchantUserLedger(MerchantUserLedger merchantUserLedger) {
      return merchantLedgerRepository.findMerchantIdsByMerchantUserLedger(merchantUserLedger.getId());
  }

  /**
   *  根据ID统计个数
   */
  @Transactional(readOnly = true)
  public Long countByMerchantLedger(MerchantUserLedger merchantUserLedger) {
    return merchantLedgerRepository.countByMerchantLedger(merchantUserLedger.getId());
  }

}
