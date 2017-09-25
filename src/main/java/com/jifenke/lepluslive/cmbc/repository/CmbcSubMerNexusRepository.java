package com.jifenke.lepluslive.cmbc.repository;

import com.jifenke.lepluslive.cmbc.domain.entities.CmbcSubMerNexus;
import com.jifenke.lepluslive.cmbc.domain.entities.CmbcSubMerchant;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CmbcSubMerNexusRepository extends JpaRepository<CmbcSubMerNexus, Long> {

    CmbcSubMerNexus findByMerchant(Merchant merchant);

    Long countByCmbcSubMerchant(CmbcSubMerchant cmbcSubMerchant);

    List<CmbcSubMerNexus> findAllByCmbcSubMerchant(CmbcSubMerchant cmbcSubMerchant);

}
