package com.jifenke.lepluslive.merchant.service;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUserShop;
import com.jifenke.lepluslive.merchant.repository.MerchantUserShopRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * 账号对应的门店列表 Created by zhangwen on 2017/2/7.
 */
@Service
@Transactional(readOnly = true)
public class MerchantUserShopService {

    @Inject
    private MerchantUserShopRepository repository;

    /**
     * 获取每个账户的管理门店信息  2017/02/07
     */
    public List countByMerchantUserList(List<MerchantUser> list) {
        List<Object> result = new ArrayList<>();
        for (MerchantUser user : list) {
            List<MerchantUserShop> shops = repository.findByMerchantUser(user);
            result.add(shops);
        }
        return result;
    }

    /**
     * 获取某个账户的管理门店信息  2017/02/08
     */
    public List<MerchantUserShop> findByMerchantUser(MerchantUser merchantUser) {

        return repository.findByMerchantUser(merchantUser);
    }

    /**
     * 保存某个账号对应的门店 2017/02/09
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void saveShop(MerchantUserShop shop) {
        repository.save(shop);
    }

    /**
     * 获取某个账号和门店对应的记录 2017/05/04
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public MerchantUserShop findByMerchantAndUser(MerchantUser merchantUser, Merchant merchant) {
        return repository.findByMerchantUserAndMerchant(merchantUser, merchant);
    }

    /**
     * 根据门店查找对应记录 2017/09/06
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<MerchantUserShop> findByMerchant(Merchant merchant) {
        return repository.findByMerchant(merchant);
    }
}
