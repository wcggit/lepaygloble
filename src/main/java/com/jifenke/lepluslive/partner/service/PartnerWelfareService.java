package com.jifenke.lepluslive.partner.service;

import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.lejiauser.domain.criteria.LeJiaUserCriteria;
import com.jifenke.lepluslive.partner.controller.dto.ExclusiveArrayDto;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.repository.PartnerScoreLogRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.LongStream;

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

    public Map checkInclusiveArrayWelfareLimit(Partner partner, Long[] ids) {
        Map map = new HashMap<String, Object>();
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
            sql.append(") group by scorea.le_jia_user_id having count>=");
            sql.append(partner.getBenefitTime());
            sql.append(")statistic");
            List<BigInteger> resultList = em.createNativeQuery(sql.toString()).getResultList();
            if (resultList.size() > 0) {
                map.put("conflict", resultList.size());
                map.put("filterArray", Arrays.stream(ids).filter(id -> !resultList.contains(id))
                    .toArray(Long[]::new));
            } else {
                map.put("conflict", 0);
            }
            return map;
        } else {
            map.put("conflict", 0);
            return map;
        }
    }

    public Object checkExclusiveArrayWelfareLimit(Partner partner,
                                                  ExclusiveArrayDto exclusiveArrayDto) {
        Map map = new HashMap<String, Object>();
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
            LeJiaUserCriteria leJiaUserCriteria = exclusiveArrayDto.getLeJiaUserCriteria();
            Long[] ids = exclusiveArrayDto.getIds();
            StringBuffer
                sql = new StringBuffer();
            sql.append(
                "select user.id id ,(select count(*) from scorea,scorea_detail where scorea.le_jia_user_id = user.id and scorea.id = scorea_detail.scorea_id and scorea_detail.date_created between '");
            sql.append(start);
            sql.append("' and '");
            sql.append(end);
            sql.append("'");
            sql.append(
                " and scorea_detail.origin = 10  )count from (select le_jia_user.id id,wei_xin_user.nickname name,wei_xin_user.head_image_url image,le_jia_user.bind_partner_date date,merchant.name merchant_name,le_jia_user.bind_partner_id partner_id ,le_jia_user.phone_number phone,scorea.score scorea,scoreb.score scoreb  from le_jia_user,wei_xin_user,merchant,scorea,scoreb where scorea.le_jia_user_id = le_jia_user.id and scoreb.le_jia_user_id = le_jia_user.id and wei_xin_user.le_jia_user_id= le_jia_user.id and le_jia_user.bind_merchant_id = merchant.id and  le_jia_user.bind_partner_id =");
            sql.append(leJiaUserCriteria.getPartner().getId());
            if (leJiaUserCriteria.getPartnerStartDate() != null
                && leJiaUserCriteria.getPartnerStartDate() != "") {
                sql.append(" and le_jia_user.bind_partner_date between '");
                sql.append(leJiaUserCriteria.getPartnerStartDate());
                sql.append("' and '");
                sql.append(leJiaUserCriteria.getPartnerEndDate());
                sql.append("'");
            }
            if (leJiaUserCriteria.getMerchantName() != null
                && leJiaUserCriteria.getMerchantName() != "") {
                sql.append(" and merchant.name like '%");
                sql.append(leJiaUserCriteria.getMerchantName());
                sql.append("%'");
            }
            if (leJiaUserCriteria.getPhone() != null && leJiaUserCriteria.getPhone() != "") {
                sql.append(" and le_jia_user.phone_number like '%");
                sql.append(leJiaUserCriteria.getPhone());
                sql.append("%'");
            }

            sql.append(
                ") user left join (select count(*) s_count,sum(off_line_order_share.to_lock_merchant) to_merchant,sum(off_line_order_share.to_lock_partner) to_partner,off_line_order.le_jia_user_id id from off_line_order_share,merchant,off_line_order where off_line_order_share.lock_merchant_id = merchant.id  and merchant.partner_id = ");
            sql.append(leJiaUserCriteria.getPartner().getId());
            sql.append(
                " and off_line_order.id = off_line_order_share.off_line_order_id group by off_line_order.le_jia_user_id) audit on user.id = audit.id where 1=1 ");

            if (leJiaUserCriteria.getConsumptionTimes() != null) {
                sql.append(" and ifnull(audit.s_count,0)");
                if (leJiaUserCriteria.getTimeSelect() == 0) {
                    sql.append("<");
                }
                if (leJiaUserCriteria.getTimeSelect() == 1) {
                    sql.append("=");
                }
                if (leJiaUserCriteria.getTimeSelect() == 2) {
                    sql.append(">");
                }
                sql.append(leJiaUserCriteria.getConsumptionTimes());
            }

            if (leJiaUserCriteria.getConsumptionCount() != null
                && leJiaUserCriteria.getConsumptionCount() != "") {
                sql.append(" and ifnull(audit.to_partner,0)");
                if (leJiaUserCriteria.getCountSelect() == 0) {
                    sql.append("<");
                }
                if (leJiaUserCriteria.getCountSelect() == 1) {
                    sql.append("=");
                }
                if (leJiaUserCriteria.getCountSelect() == 2) {
                    sql.append(">");
                }
                sql.append(new BigDecimal(leJiaUserCriteria.getConsumptionCount()).multiply(
                    new BigDecimal(100))
                               .longValue());
            }

            if (leJiaUserCriteria.getNickname() != null && leJiaUserCriteria.getNickname() != "") {
                sql.append(" and user.name like '%");
                sql.append(leJiaUserCriteria.getNickname());
                sql.append("%'");
            }
            sql.append("having count>=");
            sql.append(partner.getBenefitTime());
            if (ids.length > 0) {
                sql.append(" and user.id not in (");
                sql.append(Arrays.toString(ids).replaceAll("[\\,\\[\\]\\ ]", ""));
                sql.append(")");
            }
            List<BigInteger> resultList = em.createNativeQuery(sql.toString()).getResultList();
            if (resultList.size() > 0) {
                map.put("conflict", resultList.size());
                map.put("conflictList", resultList);
                return map;
            } else {
                map.put("conflict", 0);
                return map;
            }

        } else {
            map.put("conflict", 0);
            return map;
        }
    }
}
