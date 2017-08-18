package com.jifenke.lepluslive.partner.service;

import com.jifenke.lepluslive.lejiauser.repository.LeJiaUserRepository;
import com.jifenke.lepluslive.merchant.repository.MerchantRepository;
import com.jifenke.lepluslive.partner.controller.dto.PartnerManagerDto;
import com.jifenke.lepluslive.partner.domain.criteria.PartnerManagerCriteria;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerManager;
import com.jifenke.lepluslive.partner.domain.entities.PartnerManagerWallet;
import com.jifenke.lepluslive.partner.repository.*;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by xf on 17-3-17.
 */
@Service
@Transactional(readOnly = true)
public class PartnerManagerService {
    @Inject
    private PartnerManagerRepository partnerManagerRepository;

    @Inject
    private PartnerManagerWalletRepository partnerManagerWalletRepository;

    @Inject
    private LeJiaUserRepository leJiaUserRepository;

    @Inject
    private MerchantRepository merchantRepository;

    @Inject
    private PartnerManagerWalletLogRepository partnerManagerWalletLogRepository;

    @Inject
    private PartnerRepository partnerRepository;

    @Inject
    private PartnerWalletLogRepository partnerWalletLogRepository;

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public PartnerManager findByPartnerAccountId(Long accountId) {
        return partnerManagerRepository.findByPartnerId(accountId);
    }

    @Transactional(readOnly = true,propagation = Propagation.REQUIRED)
    public PartnerManagerWallet findWalletByPartnerManager(PartnerManager partnerManager) {
        return partnerManagerWalletRepository.findByPartnerManager(partnerManager);
    }

    /**
     *  城市合伙人每日佣金收入
     */
    @Transactional(readOnly = true,propagation = Propagation.REQUIRED)
    public Long findDailyCommissionByPartnerManager(Long partnerManagerId) {
        return partnerManagerWalletLogRepository.findDailyCommissionByPartnerManager(partnerManagerId);
    }

    /**
     *  城市合伙人 - 会员、门店、合伙人  上限
     */
    @Transactional(readOnly = true,propagation = Propagation.REQUIRED)
    public List<PartnerManagerDto> getBindsByCriteria(PartnerManagerCriteria partnerManagerCriteria) {
        List<Partner> partners = partnerManagerCriteria.getPartners();
        List<PartnerManagerDto> list = new ArrayList<>();
        //  统计绑定会员数量
        if(partnerManagerCriteria.getType()==0) {
            for (Partner partner : partners) {
                Date startDate = new Date(partnerManagerCriteria.getStartDate());
                Date endDate = new Date(partnerManagerCriteria.getEndDate());
                List<Object[]> counts = leJiaUserRepository.countBindUserByPartnerAndDate(partner.getId(), startDate, endDate);
                for (Object[] count : counts) {
                    if(list.size()>0) {
                        for(PartnerManagerDto dto : list) {
                            if(dto.getDate().equals(count[0].toString())) {
                                dto.setCount(dto.getCount()+new Long(count[1].toString()));
                            }
                        }
                    }else {
                        PartnerManagerDto nDto = new PartnerManagerDto();
                        nDto.setDate(new String(count[0].toString()));
                        nDto.setCount(new Long(count[1].toString()));
                        list.add(nDto);
                    }
                }
            }
        }
        // 统计绑定商户数量
        if(partnerManagerCriteria.getType()==1) {
            for (Partner partner : partners) {
                Date startDate = new Date(partnerManagerCriteria.getStartDate());
                Date endDate = new Date(partnerManagerCriteria.getEndDate());
                List<Object[]> merchantCounts = merchantRepository.countMerchantByPartnerAndDate(partner.getId(), startDate, endDate);
                if(merchantCounts==null||merchantCounts.size()==0) {
                    continue;
                }
                for (Object[] merchantCount : merchantCounts) {
                    if(list.size()>0) {
                        for(PartnerManagerDto dto : list) {
                            if(dto.getDate().equals(merchantCount[0].toString())) {
                                dto.setCount(dto.getCount()+new Long(merchantCount[1].toString()));
                            }
                        }
                    }else {
                        PartnerManagerDto nDto = new PartnerManagerDto();
                        nDto.setDate(new String(merchantCount[0].toString()));
                        nDto.setCount(new Long(merchantCount[1].toString()));
                        list.add(nDto);
                    }
                }
            }
        }
        // 统计佣金收入
        if(partnerManagerCriteria.getType()==1) {
                Date startDate = new Date(partnerManagerCriteria.getStartDate());
                Date endDate = new Date(partnerManagerCriteria.getEndDate());
                List<Object[]> commissions = partnerManagerWalletLogRepository.findDailyCommissionByPartnerManager(partnerManagerCriteria.getPartnerManager().getId(), startDate, endDate);
                if(commissions==null) {
                    PartnerManagerDto partnerManagerDto = new PartnerManagerDto();
                    partnerManagerDto.setCommission(0L);
                    partnerManagerDto.setDate(new SimpleDateFormat("MM-dd").format(new Date()));
                    list.add(partnerManagerDto);
                    return list;
                }
                for (Object[] commission : commissions) {
                    if(list.size()>0) {
                        for(PartnerManagerDto dto : list) {
                            if(dto.getDate().equals(commission[0].toString())) {
                                dto.setCount(dto.getCount()+new Long(commission[1].toString()));
                            }
                        }
                    }else {
                        PartnerManagerDto nDto = new PartnerManagerDto();
                        nDto.setDate(new String(commission[0].toString()));
                        nDto.setCount(new Long(commission[1].toString()));
                        list.add(nDto);
                    }
                }
        }

        return list;
    }

    /**
     *  根据 Sid 查询PartnerManager
     */
    @Transactional(readOnly = true,propagation = Propagation.REQUIRED)
    public PartnerManager findByPartnerManagerSid(String partnerManagerSid) {
        return partnerManagerRepository.findByPartnerManagerSid(partnerManagerSid);
    }


    /***
     *  获取折线图数据 - PartnerManager
     */
    @Transactional(readOnly = true,propagation = Propagation.REQUIRED)
    public Map findManagerWeekNumberChartData(PartnerManagerCriteria managerCriteria) {
        String start = managerCriteria.getStartDate();
        Date startDate = null;                           // 开始时间
        if(start==null||"".equals(start)) {
            startDate = new Date();
        }else  {
            startDate = new Date(start);
        }
        Long dayMills = 86400000L;                                  // 一天的毫秒值
        List<String> dates = new ArrayList<>();                     // 统计时间
        List<Long> userNumbers = new ArrayList<>();                 // 合伙人管理员锁定会员;
        List<Long> merchantNumbers = new ArrayList<>();             // 合伙人管理员锁定门店;
        List<Double> totalCommission = new ArrayList<>();             // 合伙人管理员佣金收入;
        for (int i = 0; i < 7; i++) {
            String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(startDate);
            startDate.setTime(startDate.getTime() - dayMills);
            dates.add(dateStr);                                  // 设置时间和金额默认值
            userNumbers.add(0L);
            merchantNumbers.add(0L);
            totalCommission.add(0.0);
        }
        Collections.reverse(dates);                               // 翻转
        PartnerManager partnerManager = managerCriteria.getPartnerManager();
        //  数据计算和封装
        List<Partner> partners = partnerRepository.findByPartnerManager(partnerManager);
        for (Partner partner : partners) {
            List<Object[]> userNumer = leJiaUserRepository.countPartnerBindLeJiaUserByWeek(partner.getId(), startDate);
            List<Object[]> merchantNumber = merchantRepository.countMerchantByPartnerWeek(partner.getId(), startDate);
            for (int i = 0; i < dates.size(); i++) {
                String startTime = dates.get(i);
                for (Object[] merchantNum : merchantNumber) {
                    if (startTime.equals(merchantNum[0].toString())) {
                        merchantNumbers.set(i, (merchantNumbers.get(i) + new Long(merchantNum[1].toString())));
                        break;
                    }
                }
                for (Object[] userNum : userNumer) {
                    if (startTime.equals(userNum[0].toString())) {
                        userNumbers.set(i, (userNumbers.get(i) + new Long(userNum[1].toString())));
                        break;
                    }
                }
            }
        }
        List<Object[]> commissions = partnerManagerWalletLogRepository.findCommissionByPartnerWeek(partnerManager.getId(), startDate);
        for (int i = 0; i < dates.size(); i++) {
            String startTime = dates.get(i);
            for (Object[] commission : commissions) {
                if (startTime.equals(commission[0].toString())) {
                    totalCommission.set(i, (totalCommission.get(i) + new Double(commission[1].toString())* 0.01));
                    break;
                }
            }
        }
        Map map = new HashMap();
        map.put("merchantNumbers",merchantNumbers);
        map.put("userNumbers",userNumbers);
        map.put("totalCommission",totalCommission);
        map.put("dates",dates);
        return map;
    }

    /***
     *  获取折线图数据 - Partner
     */
    @Transactional(readOnly = true,propagation = Propagation.REQUIRED)
    public Map findPartnerData(PartnerManagerCriteria managerCriteria,Partner partner) {
        String start = managerCriteria.getStartDate();
        Date startDate = null;                           // 开始时间
        if(start==null||"".equals(start)) {
            startDate = new Date();
        }else  {
            startDate = new Date(start);
        }
        List<Object[]> userNumer = leJiaUserRepository.countPartnerBindLeJiaUserByWeek(partner.getId(), startDate);
        List<Object[]> merchantNumber = merchantRepository.countMerchantByPartnerWeek(partner.getId(), startDate);
        Long dayMills = 86400000L;                                  // 一天的毫秒值
        List<String> dates = new ArrayList<>();                     // 统计时间
        List<Long> userNumbers = new ArrayList<>();                 // 合伙人管理员锁定会员;
        List<Long> merchantNumbers = new ArrayList<>();             // 合伙人管理员锁定门店;
        List<Double> totalCommission = new ArrayList<>();             // 合伙人管理员佣金收入;
        for (int i = 0; i < 7; i++) {
            String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(startDate);
            startDate.setTime(startDate.getTime() - dayMills);
            dates.add(dateStr);                                  // 设置时间和金额默认值
            userNumbers.add(0L);
            merchantNumbers.add(0L);
            totalCommission.add(0.0);
        }

        for (int i = 0; i < dates.size(); i++) {
            String startTime = dates.get(i);
            for (Object[] merchantNum : merchantNumber) {
                if (startTime.equals(merchantNum[0].toString())) {
                    merchantNumbers.set(i, (merchantNumbers.get(i) + new Long(merchantNum[1].toString())));
                    break;
                }
            }
            for (Object[] userNum : userNumer) {
                if (startTime.equals(userNum[0].toString())) {
                    userNumbers.set(i, (userNumbers.get(i) + new Long(userNum[1].toString())));
                    break;
                }
            }
        }
        List<Object[]> commissions = partnerWalletLogRepository.findCommissionByPartnerWeek(partner.getId(), startDate);
        for (int i = 0; i < dates.size(); i++) {
            String startTime = dates.get(i);
            for (Object[] commission : commissions) {
                if (startTime.equals(commission[0].toString())) {
                    totalCommission.set(i, (totalCommission.get(i) + new Double(commission[1].toString())* 0.01));
                    break;
                }
            }
        }
        Map map = new HashMap();
        map.put("merchantNumbers",merchantNumbers);
        map.put("userNumbers",userNumbers);
        map.put("totalCommission",totalCommission);
        map.put("dates",dates);
        return map;
    }

    @Transactional(readOnly = true,propagation = Propagation.REQUIRED)
    public Optional<Partner> findByWeiXinUser(WeiXinUser weiXinUser) {
        return partnerManagerRepository.findByWeiXinUser(weiXinUser);
    }
}
