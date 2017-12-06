package com.jifenke.lepluslive.groupon.service;

import com.jifenke.lepluslive.groupon.domain.entities.GrouponMerchant;
import com.jifenke.lepluslive.groupon.domain.entities.GrouponProduct;
import com.jifenke.lepluslive.groupon.repository.GrouponMerchantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

/**
 * GrouponMerchantService
 *   产品和商户绑定
 * @author XF
 * @date 2017/6/27
 */
@Service
@Transactional(readOnly = true)
public class GrouponMerchantService {
    @Inject
    private GrouponMerchantRepository merchantRepository;
    @Transactional(readOnly = true,propagation = Propagation.REQUIRED)
    public List<GrouponMerchant> findByGrouponProduct(GrouponProduct grouponProduct) {
        return merchantRepository.findByGrouponProduct(grouponProduct);
    }
}
