package com.jifenke.lepluslive.yibao.repository;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.yibao.domain.entities.MerchantLedger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by zhangwen on 2017/7/12.
 */
public interface MerchantLedgerRepository extends JpaRepository<MerchantLedger, Long> {

    MerchantLedger findByMerchant(Merchant merchant);

    @Query(value="select count(*) from yb_merchant_ledger where merchant_user_ledger_id=?1",nativeQuery = true)
    Long countByMerchantLedger(Long merchantUserLedgerId);
}
