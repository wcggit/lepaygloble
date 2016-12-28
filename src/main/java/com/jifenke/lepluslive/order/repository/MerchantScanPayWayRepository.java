package com.jifenke.lepluslive.order.repository;

import com.jifenke.lepluslive.order.domain.entities.MerchantScanPayWay;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by xf on 16-12-28.
 */
public interface MerchantScanPayWayRepository extends JpaRepository<MerchantScanPayWay,Long> {
    /**
     *  根据门店 ID 查找结算方式:  如果没有记录,说明门店用的是乐加扫码,否则为掌富扫码
     */
    MerchantScanPayWay findByMerchantId(Long merchantID);
}
