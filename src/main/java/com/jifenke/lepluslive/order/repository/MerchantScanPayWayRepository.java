package com.jifenke.lepluslive.order.repository;

import com.jifenke.lepluslive.merchant.domain.entities.MerchantScanPayWay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
/**
 * Created by xf on 16-12-28.
 */
public interface MerchantScanPayWayRepository extends JpaRepository<MerchantScanPayWay,Long> {
    /**
     *  根据门店 ID 查找结算方式:  如果没有记录,说明门店用的是乐加扫码,否则为掌富扫码
     */
    MerchantScanPayWay findByMerchantId(Long merchantID);

    /**
     * 查询扫码支付 结算方式
     * @param obj  门店id: merchantId
     * @return
     */
    @Query(value = "SELECT mspw.id,  mspw.merchant_id,  mspw.type  from  merchant_scan_pay_way mspw  where mspw.merchant_id =?1" , nativeQuery = true)
    List<Object[]> findMerchantScanPayWayByMerchantId(String obj);

}
