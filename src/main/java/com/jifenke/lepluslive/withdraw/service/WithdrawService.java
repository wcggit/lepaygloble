package com.jifenke.lepluslive.withdraw.service;

import com.jifenke.lepluslive.withdraw.domain.entities.WithdrawBill;
import com.jifenke.lepluslive.withdraw.repository.WithdrawRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Created by xf on 2016/9/18.
 */
@Service
@Transactional
public class WithdrawService {

    @Inject
    private WithdrawRepository withdrawRepository;

    @Transactional(propagation = Propagation.REQUIRED,readOnly = false)
    public void saveWithdrawBill(WithdrawBill withdrawBill) {
        withdrawRepository.save(withdrawBill);
    }
}
