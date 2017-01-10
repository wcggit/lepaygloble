package com.jifenke.lepluslive.merchant.service;

import com.jifenke.lepluslive.lejiauser.repository.LeJiaUserRepository;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.repository.MerchantRepository;
import com.jifenke.lepluslive.merchant.repository.MerchantUserRepository;
import com.jifenke.lepluslive.merchant.repository.MerchantUserResourceRepository;
import com.jifenke.lepluslive.security.SecurityUtils;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.*;

/**
 * Created by wcg on 16/6/27.
 */
@Service
@Transactional(readOnly = true)
public class MerchantUserService {

    @Inject
    private MerchantUserRepository merchantUserRepository;
    @Inject
    private MerchantUserResourceRepository merchantUserResourceRepository;
    @Inject
    private MerchantRepository merchantRepository;
    @Inject
    private LeJiaUserRepository lejiaUserRepository;

    public MerchantUser getUserWithAuthorities() {
        return  merchantUserRepository.findByName(SecurityUtils.getCurrentUserLogin()).get();
    }


    @Transactional(propagation = Propagation.REQUIRED,readOnly = true)
    public Map getLockerInfoByMerchant(String merchantName) {
        //1.根据商户loginName查询旗下所有门店id
        List<Object[]> list = merchantUserResourceRepository.findByMerchantInfoUser(merchantName);
        List<Object> mlist = new ArrayList<Object>();
        for (int i = 0; i < list.size(); i++) {
            mlist.add(list.get(i)[0]);
        }
        //2.统计锁定会员总数
        Integer totalCount = merchantRepository.findMerchantTotalMember(mlist);
        //3.统计今日锁定会员数
        Integer dailyCount = merchantRepository.findMerchantDailyMember(mlist);
        // 4.统计总共发放的红包和积分
        Long totalScorea = 0L;
        Long totalScoreb = 0L;
        List members = merchantRepository.findMerchantMemberIds(mlist);
        for (Object m : members) {
            Long id = new Long(m.toString());
            Long scorea = lejiaUserRepository.findTotalScorea(id);
            Long scoreb = lejiaUserRepository.findTotalScoreb(id);
            totalScorea += (scorea==null?0:scorea);
            totalScoreb += (scoreb==null?0:scoreb);
        }
        Map map = new HashMap();
        map.put("totalCount",totalCount);
        map.put("dailyCount",dailyCount);
        map.put("totalScorea",totalScorea);
        map.put("totalScoreb",totalScoreb);
        return map;
    }

    /**
     *  获取商户下的店主账号
     */
    @Transactional(propagation = Propagation.REQUIRED,readOnly = true)
    public List<MerchantUser> findOwerAccount(Long merchnatUserId) {
        return merchantUserRepository.findOwerAccount(merchnatUserId);
    }

    /**
     *  获取商户下的收银员账号
     */
    @Transactional(propagation = Propagation.REQUIRED,readOnly = true)
    public List<MerchantUser> findCashierAccount(Long merchantUserId) {
        return merchantUserRepository.findCashierAccount(merchantUserId);
    }

    /**
     *  根据用户名查询商户
     */
    @Transactional(propagation = Propagation.REQUIRED,readOnly = true)
    public MerchantUser findByName(String username) {
        return merchantUserRepository.findByUserName(username);
    }
}
