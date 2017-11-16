package com.jifenke.lepluslive.partner.service;

import com.jifenke.lepluslive.partner.repository.PartnerWalletLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Created by
 * @author zxf
 * @date  2017/11/15.
 */
@Service
public class PartnerWalletLogService {
    @Inject
    private PartnerWalletLogRepository partnerWalletLogRepository;


    /***
     * 获取合伙人当日佣金收入
     * @param partnerId
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Long countDailyIncomeCommissionByPartner(Long partnerId) {
       return partnerWalletLogRepository.findIncomeCommissionByPartnerDaily(partnerId);
    }


    /***
     * 获取合伙人当日佣金支出
     * @param partnerId
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Long countDailyExpendCommissionByPartner(Long partnerId) {
        return partnerWalletLogRepository.findExpendCommissionByPartnerDaily(partnerId);
    }

}
