package com.jifenke.lepluslive.order.service;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantWallet;
import com.jifenke.lepluslive.merchant.repository.MerchantWalletRepository;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.order.domain.entities.FinancialStatistic;
import com.jifenke.lepluslive.order.repository.FinancialStatisticRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import sun.util.resources.cldr.guz.LocaleNames_guz;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by wcg on 16/5/21.
 */
@Service
@Transactional(readOnly = true)
public class FinanicalStatisticService {

  @Inject
  private MerchantService merchantService;

  @Inject
  private FinancialStatisticRepository financialStatisticRepository;

  @Inject
  private MerchantWalletRepository merchantWalletRepository;

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void createFinancialstatistic(Object[] object,Date date) {
    Merchant merchant = merchantService.findMerchantById(Long.parseLong(object[0].toString()));
    if (merchant != null) {
      FinancialStatistic financialStatistic = new FinancialStatistic();
      financialStatistic.setMerchant(merchant);
      financialStatistic.setBalanceDate(date);
      financialStatistic.setTransferPrice(Long.parseLong(object[1].toString()));
      financialStatisticRepository.save(financialStatistic);
    }
  }

    //  旧版本 - 昨日交易金额
    public Long countDailyTransfering(Merchant merchant) {
       return financialStatisticRepository.countTransfering(merchant.getId());
    }

    /**
     *  多门店 - 总入账金额
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED,readOnly = true)
    public Long countTotalTransfering(List<Merchant> merchants) {
        Long totalTransfering = 0L;
        for (Merchant merchant : merchants) {
            MerchantWallet wallet = merchantWalletRepository.findByMerchant(merchant);
            totalTransfering += (wallet==null||wallet.getTotalTransferMoney()==null)?0L:wallet.getTotalTransferMoney();
        }
        return totalTransfering;
    }

    public List<FinancialStatistic> findByMerchantAndBalanceDate(Merchant merchant, Date start,
                                                                 Date end) {
        return financialStatisticRepository.findAllByMerchantAndBalanceDateBetweenOrderByBalanceDate(
            merchant, start, end);
    }
}
