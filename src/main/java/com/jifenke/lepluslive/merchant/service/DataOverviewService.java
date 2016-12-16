package com.jifenke.lepluslive.merchant.service;

import com.jifenke.lepluslive.merchant.domain.criteria.DataOverviewCriteria;
import com.jifenke.lepluslive.merchant.repository.MerchantRepository;
import com.jifenke.lepluslive.merchant.repository.MerchantUserRepository;
import com.jifenke.lepluslive.merchant.repository.MerchantUserResourceRepository;
import com.jifenke.lepluslive.order.repository.OffLineOrderShareRepository;
import com.jifenke.lepluslive.order.repository.OnLineOrderShareRepository;
import com.jifenke.lepluslive.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanjun on 2016/12/15.
 */
@Service
@Transactional(readOnly = true)
public class DataOverviewService {

    @Inject
    private OnLineOrderShareRepository onLineOrderShareRepository;
    @Inject
    private OffLineOrderShareRepository offLineOrderShareRepository;
    @Inject
    private MerchantRepository merchantRepository;
    @Inject
    private MerchantUserResourceRepository merchantUserResourceRepository;
    @Inject
    private MerchantUserRepository merchantUserRepository;


    /**
     * 查询数据概览、线上佣金收入、线下佣金收入信息。
     *
     * @return
     */
    public DataOverviewCriteria findCommissionAndLockNumber() {
        DataOverviewCriteria doc = new DataOverviewCriteria();
        //1.根据商户loginName查询旗下所有门店id
        List<Object[]> list = merchantUserResourceRepository.findByMerchantInfoUser(SecurityUtils.getCurrentUserLogin());
        List<Object> mlist = new ArrayList<Object>();
        for (int i = 0; i < list.size(); i++) {
            mlist.add(list.get(i)[0]);
        }
        doc.setMerchantIds(mlist);
        //2.查询商户锁定会员上限
        Integer merchantUserTotalLockLimit = merchantUserRepository.findLockLimitByName(SecurityUtils.getCurrentUserLogin());
        doc.setMerchantUserTotalLockLimit(merchantUserTotalLockLimit);
        //3.查询商户旗下所有门店锁定会员的总数
        Integer merchantUserLockLimit = merchantRepository.findMerchantTotalMember(mlist);
        doc.setMerchantUserLockLimit(merchantUserLockLimit);
        //4.查询线下今日佣金收入、今日佣金单数
        List<Object[]> offToday = offLineOrderShareRepository.findTodayCommissionAndTodayNumber(mlist);
        doc.setOffTodayTotalCommission(Double.valueOf(offToday.get(0)[0].toString()));
        doc.setOffTodayTotalNumber(Integer.parseInt(offToday.get(0)[1].toString()));
        //5.查询线上今日佣金收入、今日佣金单数
        List<Object[]> onToday = onLineOrderShareRepository.findTodayCommissionAndTodayNumber(mlist);
        doc.setOnTodayTotalCommission(Double.valueOf(onToday.get(0)[0].toString()));
        doc.setOnTodayTotalNumber(Integer.parseInt(onToday.get(0)[1].toString()));
        //6.查询线下人均佣金收入、累计佣金单数
        List<Object[]> offPer = offLineOrderShareRepository.findPerCapitaAndTotalNumber(merchantUserLockLimit, mlist);
        doc.setOffTotalNumber(Integer.parseInt(offPer.get(0)[0].toString()));
        doc.setOffPerCapita(Double.valueOf(offPer.get(0)[1].toString()));
        //7.查询线上人均佣金收入、累计佣金单数
        List<Object[]> onPer = onLineOrderShareRepository.findPerCapitaAndTotalNumber(merchantUserLockLimit, mlist);
        doc.setOnTotalNumber(Integer.parseInt(onPer.get(0)[0].toString()));
        doc.setOnPerCapita(Double.valueOf(onPer.get(0)[1].toString()));
        return doc;
    }

    /**
     * 分页查询商户旗下门店会员锁定信息
     *
     * @param dataOverviewCriteria
     * @return
     */
    public List<Object[]> findPageMerchantMemberLockNumber(DataOverviewCriteria dataOverviewCriteria) {
        Integer offSet = dataOverviewCriteria.getOffset() * 6;
        List<Object[]> list = merchantRepository.findPageMerchantMemberLockNumber(dataOverviewCriteria.getMerchantIds(), offSet);
        return list;
    }

}
