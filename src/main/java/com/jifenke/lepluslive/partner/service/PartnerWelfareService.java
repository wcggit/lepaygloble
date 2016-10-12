package com.jifenke.lepluslive.partner.service;

import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.repository.PartnerScoreLogRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 * Created by wcg on 16/10/12.
 */
@Service
@Transactional(readOnly = true)
public class PartnerWelfareService {

    @Inject
    private EntityManager em;

    @Inject
    private PartnerScoreLogRepository partnerScoreLogRepository;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Boolean checkUserWelfareLimit(Partner partner, Long id) {
        Date date = new Date();
        Date startOfDay = MvUtil.getStartOfDay(date);
        Date endOfDay = MvUtil.getEndOfDay(date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String start = sdf.format(startOfDay);
        String end = sdf.format(endOfDay);
        StringBuffer sql = new StringBuffer();
        sql.append("select count(*) from scorea,scorea_detail where scorea.le_jia_user_id = ");
        sql.append(id);
        sql.append(
            " and scorea_detail.scorea_id = scorea.id and scorea_detail.origin = 10 and scorea_detail.date_created between '");
        sql.append(start);
        sql.append("' and '");
        sql.append(end);
        sql.append("'");
        List<BigInteger> resultList = em.createNativeQuery(sql.toString()).getResultList();
        return resultList.get(0).intValue() >= partner.getBenefitTime();
    }

    public Integer checkUserArrayWelfareLimit(Partner partner, Long[] ids) {
        Date date = new Date();
        Date startOfDay = MvUtil.getStartOfDay(date);
        Date endOfDay = MvUtil.getEndOfDay(date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String start = sdf.format(startOfDay);
        String end = sdf.format(endOfDay);
        Integer
            dayTimes =
            partnerScoreLogRepository
                .countByPartnerIdAndCreateDateBetween(partner.getId(), startOfDay, endOfDay);
        if (dayTimes.intValue() >= partner.getBenefitTime()) {
            String idstr = Arrays.toString(ids).replaceAll("[\\,\\[\\]\\ ]", "");
            StringBuffer sql = new StringBuffer();
            sql.append(
                "select id from (select count(*)count,scorea.le_jia_user_id id from scorea,scorea_detail where   scorea_detail.scorea_id = scorea.id and scorea_detail.origin = 10 and scorea_detail.date_created between '");
            sql.append(start);
            sql.append("' and '");
            sql.append(end);
            sql.append("' and scorea.le_jia_user_id in (");
            sql.append(idstr);
            sql.append(") group by scorea.le_jia_user_id having count>31)statistic");
            List<BigInteger> resultList = em.createNativeQuery(sql.toString()).getResultList();

            return 0;
        } else {
            return 0;
        }
    }
}
