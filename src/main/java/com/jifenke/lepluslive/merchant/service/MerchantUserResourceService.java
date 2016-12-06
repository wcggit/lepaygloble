package com.jifenke.lepluslive.merchant.service;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUserResource;
import com.jifenke.lepluslive.merchant.repository.MerchantRepository;
import com.jifenke.lepluslive.merchant.repository.MerchantUserResourceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xf on 16-12-2.
 */
@Service
@Transactional(readOnly = true)
public class MerchantUserResourceService {

    @Inject
    private MerchantRepository merchantRepository;
    @Inject
    private MerchantUserResourceRepository merchantUserResourceRepository;

    /***
     *  根据商户找旗下的门店
     */
    @Transactional(propagation = Propagation.REQUIRED,readOnly = true)
    public List<Merchant> findMerchantsByMerchantUser(MerchantUser merchantUser) {
        List<Merchant> merchants =  new ArrayList<>();
        List<MerchantUserResource> merchantUserResource = merchantUserResourceRepository.findByMerchantUser(merchantUser.getId());
        if(merchantUserResource!=null && merchantUserResource.size()>0) {
            for (MerchantUserResource userResource : merchantUserResource) {
                Long merchantId = userResource.getLeJiaResource().getResourceId();
                Merchant merchant = merchantRepository.findOne(merchantId);
                merchants.add(merchant);
            }
        }
        return merchants;
    }

}
