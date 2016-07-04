package com.jifenke.lepluslive.merchant.repository;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.List;

import javax.persistence.QueryHint;

/**
 * Created by zw on 16/04/27.
 */
public interface MerchantDetailRepository extends JpaRepository<MerchantDetail, Long> {

    /**
     * 商家轮播图
     * @param merchant
     * @return
     */
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    List<MerchantDetail> findAllByMerchant(Merchant merchant);

}
