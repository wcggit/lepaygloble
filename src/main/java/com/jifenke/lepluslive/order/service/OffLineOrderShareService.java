package com.jifenke.lepluslive.order.service;

import com.jifenke.lepluslive.order.repository.OffLineOrderShareRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by wanjun on 2016/12/15.
 */
@Service
@Transactional(readOnly = true)
public class OffLineOrderShareService {
    @Inject
    private OffLineOrderShareRepository offLineOrderShareRepository;

    /**
     * 查询今日 佣金收入、今日佣金单数
     *
     * @param objects 门店ids
     * @return
     */
    public Object findTodayCommissionAndTodayNumber(List<Object> objects) {
        Object obj = offLineOrderShareRepository.findTodayCommissionAndTodayNumber(objects);
        System.out.print(obj);
        return obj;
    }
}
