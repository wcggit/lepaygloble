package com.jifenke.lepluslive.merchant.service;

import com.jifenke.lepluslive.merchant.repository.MerchantWalletOnlineLogRepository;
import com.jifenke.lepluslive.merchant.repository.MerchantWalletOnlineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

/**
 * Created by lss on 16-11-28.
 */
@Service
@Transactional(readOnly = true)
public class MerchantWalletOnlineService {
    @Inject
    private MerchantWalletOnlineRepository merchantWalletOnlineRepository;

    @Inject
    private MerchantWalletOnlineLogRepository merchantWalletOnlineLogRepository;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Long findTotalMoney(Long id) {
        return  merchantWalletOnlineRepository.findTotalMoney(id);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Object[]> findOneDayLogCountAndSum(Long id, Date start, Date end) {
        return  merchantWalletOnlineLogRepository.findOneDayLogCountAndSum(id,start,end);
    }
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public  Integer findTotalCount(Long id) {
        return  merchantWalletOnlineLogRepository.findTotalCount(id);
    }

}
