package com.jifenke.lepluslive.cmbc.service;

import com.jifenke.lepluslive.cmbc.domain.entities.CmbcSubMerNexus;
import com.jifenke.lepluslive.cmbc.domain.entities.CmbcSubMerchant;
import com.jifenke.lepluslive.cmbc.repository.CmbcSubMerNexusRepository;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhangwen on 2017/9/21.
 */
@Service
public class CmbcSubMerNexusService {

    @Autowired
    private CmbcSubMerNexusRepository repository;

    public CmbcSubMerNexus findByMerchant(Merchant merchant) {
        return repository.findByMerchant(merchant);
    }

    /**
     * 使用某商户号的门店个数
     *
     * @param cmbcSubMerchant 商户号
     * @return
     */
    public Long countByCmbcSubMerchant(CmbcSubMerchant cmbcSubMerchant) {
        return repository.countByCmbcSubMerchant(cmbcSubMerchant);
    }

    /**
     * 使用某商户号的门店列表
     *
     * @param cmbcSubMerchant 商户号
     */
    public List<CmbcSubMerNexus> findAllByCmbcSubMerchant(CmbcSubMerchant cmbcSubMerchant) {
        return repository.findAllByCmbcSubMerchant(cmbcSubMerchant);
    }
}
