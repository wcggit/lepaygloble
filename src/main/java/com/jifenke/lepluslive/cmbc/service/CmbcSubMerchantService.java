package com.jifenke.lepluslive.cmbc.service;

import com.jifenke.lepluslive.cmbc.domain.entities.CmbcSubMerchant;
import com.jifenke.lepluslive.cmbc.repository.CmbcSubMerchantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhangwen on 2017/9/21.
 */
@Service
public class CmbcSubMerchantService {

    @Autowired
    private CmbcSubMerchantRepository repository;

    public CmbcSubMerchant findBySubMerchantNo(String subMerchantNo) {
        return repository.findBySubMerchantNo(subMerchantNo);
    }


}
