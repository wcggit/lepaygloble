package com.jifenke.lepluslive.merchant.repository;

import com.jifenke.lepluslive.merchant.domain.entities.MerchantRebatePolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by wcg on 2016/11/7.
 */
@Repository
public interface MerchantRebatePolicyRepository extends JpaRepository<MerchantRebatePolicy,Long> {
    MerchantRebatePolicy findByMerchantId(Long merchnatId);
}
