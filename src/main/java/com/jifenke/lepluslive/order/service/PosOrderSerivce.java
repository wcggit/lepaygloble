package com.jifenke.lepluslive.order.service;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.order.repository.PosOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by xf on 16-12-5.
 */
@Service
@Transactional(readOnly = true)
public class PosOrderSerivce {
    @Inject
    private PosOrderRepository posOrderRepository;

    /**
     *  查看门店每日订单总额(POS)
     */
    @Transactional(propagation = Propagation.REQUIRED,readOnly = true)
    public Long countPosOrder(List<Merchant> merchants) {
        Long posDailyCount = 0L;
        for (Merchant merchant : merchants) {
            Long totalPrice = posOrderRepository.countTotalPrice(merchant.getId());
            posDailyCount += totalPrice==null?0L:totalPrice;
        }
        return posDailyCount;
    }
}

