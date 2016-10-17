package com.jifenke.lepluslive.partner.service;


import com.jifenke.lepluslive.partner.domain.entities.PartnerInfo;
import com.jifenke.lepluslive.partner.repository.PartnerInfoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Created by xf on 2016/10/14.
 */
@Service
@Transactional(readOnly=true)
public class PartnerInfoService {

    @Inject
    private PartnerInfoRepository partnerInfoRepository;


    @Transactional(readOnly = false)
    public void saveOrUpdatePartnerInfo(PartnerInfo partnerInfo) {
        PartnerInfo existInfo = partnerInfoRepository.findByPartner(partnerInfo.getPartner());
        if(existInfo==null) {                       // 如果不存在,新增信息
            partnerInfoRepository.save(partnerInfo);
        }else {                                     // 如果已存在,更新信息
            existInfo.setMaxScoreA(partnerInfo.getMaxScoreA()*100);
            existInfo.setMaxScoreB(partnerInfo.getMaxScoreB());
            existInfo.setMinScoreA(partnerInfo.getMinScoreA()*100);
            existInfo.setMinScoreB(partnerInfo.getMinScoreB());
            existInfo.setScoreAType(partnerInfo.getScoreAType());
            existInfo.setScoreBType(partnerInfo.getScoreBType());
            partnerInfoRepository.save(existInfo);
        }
    }
}
