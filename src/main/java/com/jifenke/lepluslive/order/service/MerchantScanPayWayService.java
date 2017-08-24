package com.jifenke.lepluslive.order.service;

import com.jifenke.lepluslive.merchant.domain.entities.MerchantScanPayWay;
import com.jifenke.lepluslive.order.repository.MerchantScanPayWayRepository;
import com.jifenke.lepluslive.order.repository.OffLineOrderShareRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by tqy on 2016/12/27.
 */
@Service
@Transactional(readOnly = true)
public class MerchantScanPayWayService {
    @Inject
    private MerchantScanPayWayRepository merchantScanPayWayRepository;

    /**
     * 查询扫码支付 结算方式
     * @param obj  门店id: merchantId
     * @return
     */
    public List<Object[]> findMerchantScanPayWayByMerchantId(String obj) {
        List<Object[]> object = merchantScanPayWayRepository.findMerchantScanPayWayByMerchantId(obj);
        return object;
    }

    @Transactional(readOnly = true,propagation = Propagation.REQUIRED)
    public MerchantScanPayWay findByMerchant(Long merchantId) {
        return merchantScanPayWayRepository.findByMerchantId(merchantId);
    }
}
