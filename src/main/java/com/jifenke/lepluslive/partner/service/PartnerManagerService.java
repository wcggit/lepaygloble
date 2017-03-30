package com.jifenke.lepluslive.partner.service;

import com.jifenke.lepluslive.lejiauser.repository.LeJiaUserRepository;
import com.jifenke.lepluslive.merchant.repository.MerchantRepository;
import com.jifenke.lepluslive.partner.controller.dto.PartnerManagerDto;
import com.jifenke.lepluslive.partner.domain.criteria.PartnerManagerCriteria;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerManager;
import com.jifenke.lepluslive.partner.domain.entities.PartnerManagerWallet;
import com.jifenke.lepluslive.partner.repository.PartnerManagerRepository;
import com.jifenke.lepluslive.partner.repository.PartnerManagerWalletLogRepository;
import com.jifenke.lepluslive.partner.repository.PartnerManagerWalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
}
