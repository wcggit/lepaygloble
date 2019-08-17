package com.jifenke.lepluslive.cmbc.repository;

import com.jifenke.lepluslive.cmbc.domain.entities.CmbcSubMerchant;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ZM.Wang
 */
public interface CmbcSubMerchantRepository extends JpaRepository<CmbcSubMerchant, Long> {

    CmbcSubMerchant findBySubMerchantNo(String subMerchantNo);
}