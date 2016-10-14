package com.jifenke.lepluslive.partner.service;

import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.lejiauser.domain.criteria.LeJiaUserCriteria;
import com.jifenke.lepluslive.partner.controller.dto.ExclusiveArrayDto;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerScoreLog;
import com.jifenke.lepluslive.partner.domain.entities.PartnerWallet;
import com.jifenke.lepluslive.partner.domain.entities.PartnerWelfareLog;
import com.jifenke.lepluslive.partner.repository.PartnerScoreLogRepository;
import com.jifenke.lepluslive.partner.repository.PartnerWelfareLogRepository;
import com.jifenke.lepluslive.weixin.service.WxTemMsgService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 * Created by wcg on 16/10/12.
 */
@Service
public class PartnerWelfareService {

    private static Logger log = LoggerFactory.getLogger(PartnerWelfareService.class);


    @Inject
    private WxTemMsgService wxTemMsgService;

    @Inject
    private EntityManager em;

    @Inject
    private PartnerScoreLogRepository partnerScoreLogRepository;

    @Inject
    private PartnerWelfareLogRepository partnerWelfareLogRepository;

    @Inject
    private PartnerService partnerService;


    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Boolean checkUserWelfareLimit(Partner partner, Long id) {
        Date date = new Date();
        Date startOfDay = MvUtil.getStartOfDay(date);
        Date endOfDay = MvUtil.getEndOfDay(date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Map checkExclusiveArrayWelfareLimit(Partner partner,
                                               ExclusiveArrayDto exclusiveArrayDto) {
        Map map = new HashMap<String, Object>();
        Date date = new Date();
        Date startOfDay = MvUtil.getStartOfDay(date);
        Date endOfDay = MvUtil.getEndOfDay(date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
            sql.append(" having count>=");
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


    /**
     * 给会员送红包和积分
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Boolean welFareToOneUser(String userId, PartnerWelfareLog partnerWelfareLog) {
        Partner partner = partnerWelfareLog.getPartner();
        StringBuffer sql = new StringBuffer();
        Date date = new Date();
        if (updatePartnerWalletByWelfare(partner, partnerWelfareLog.getScoreA(),
                                         partnerWelfareLog.getScoreB(), partnerWelfareLog)) {
            try {
                partnerService.welfareToUser(userId, partnerWelfareLog);
            } catch (Exception e) {
                throw e;
            }
            wxTemMsgService.sendToClient(partnerWelfareLog, findOpenIdByLeJiaUserId(userId));
            return true;
        } else {
            return false;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    private String findOpenIdByLeJiaUserId(String userid) {
        return (String) em.createNativeQuery(
            "select wei_xin_user.open_id from wei_xin_user,le_jia_user where wei_xin_user.le_jia_user_id = le_jia_user.id and le_jia_user.id = "
            + userid).getResultList().get(0);
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void savePartnerWelfareLog(PartnerWelfareLog partnerWelfareLog) {
        partnerWelfareLogRepository.save(partnerWelfareLog);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Object batchWelfareExclusive(Partner partner, ExclusiveArrayDto exclusiveArrayDto) {
        PartnerWelfareLog partnerWelfareLog = exclusiveArrayDto.getPartnerWelfareLog();
        Long scoreA = partnerWelfareLog.getScoreA() * partnerWelfareLog.getUserCount();
        Long scoreB = partnerWelfareLog.getScoreB() * partnerWelfareLog.getUserCount();
        if (updatePartnerWalletByWelfare(partner, scoreA, scoreB, partnerWelfareLog)) {
            Long length = partnerWelfareLog.getUserCount();
            int threads = (int) Math.ceil(length / 10.0);
            ExecutorService executor;
            if (threads <= 100) {
                executor = Executors.newFixedThreadPool(threads);
            } else {
                executor = Executors.newFixedThreadPool(100);
            }
            for (int i = 0; i < threads; i++) {
                int n = i;
                executor.execute(new Thread(() -> {
                    List<BigInteger>
                        userIds =
                        getExclusiveWelfareUserListByPage(exclusiveArrayDto, partner, n);
                    userIds.forEach(userId -> {
                        try {
                            partnerService.welfareToUser(userId.intValue(), partnerWelfareLog);
                        } catch (Exception e) {

                        }
//                        wxTemMsgService.sendToClient(partnerWelfareLog,
//                                                     findOpenIdByLeJiaUserId(userId.toString()));
                    });
                }));
            }
            executor.shutdown();
            return true;
        } else {
            return false;
        }
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Boolean updatePartnerWalletByWelfare(Partner partner,
                                                Long scoreA, Long scoreB,
                                                PartnerWelfareLog partnerWelfareLog) {
        Date date = new Date();
        PartnerWallet
            partnerWallet =
            partnerService.findPartnerWalletByPartner(partner);
        if (partnerWallet.getAvailableScoreA() >= scoreA
            && partnerWallet.getAvailableScoreB() >= scoreB) {
            if (scoreA > 0) {
                PartnerScoreLog partnerScoreLog = new PartnerScoreLog();
                partnerScoreLog.setCreateDate(date);
                partnerScoreLog.setSid(partnerWelfareLog.getSid());
                partnerScoreLog.setType(1);
                partnerScoreLog.setScoreAOrigin(2);
                partnerScoreLog.setDescription("送红包给会员");
                partnerScoreLog.setPartnerId(partner.getId());
                partnerScoreLog.setNumber(-partnerWelfareLog.getScoreA());
                partnerScoreLogRepository.save(partnerScoreLog);
            }
            if (scoreB > 0) {
                PartnerScoreLog partnerScoreLog = new PartnerScoreLog();
                partnerScoreLog.setCreateDate(date);
                partnerScoreLog.setSid(partnerWelfareLog.getSid());
                partnerScoreLog.setType(0);
                partnerScoreLog.setScoreBOrigin(2);
                partnerScoreLog.setDescription("送积分给会员");
                partnerScoreLog.setPartnerId(partner.getId());
                partnerScoreLog.setNumber(-partnerWelfareLog.getScoreB());
                partnerScoreLogRepository.save(partnerScoreLog);
            }
            partnerWallet.setAvailableScoreA(
                partnerWallet.getAvailableScoreA() - scoreA);
            partnerWallet
                .setAvailableScoreB(
                    partnerWallet.getAvailableScoreB() - scoreB);
            return true;
        } else {
            return false;
        }
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public List<BigInteger> getExclusiveWelfareUserListByPage(ExclusiveArrayDto exclusiveArrayDto,
                                                              Partner partner, Integer offset) {
        LeJiaUserCriteria leJiaUserCriteria = exclusiveArrayDto.getLeJiaUserCriteria();
        Long[] ids = exclusiveArrayDto.getIds();
        StringBuffer
            sql = new StringBuffer();
        sql.append(
            "select user.id id ");
        sql.append(
            " from (select le_jia_user.id id,wei_xin_user.nickname name,wei_xin_user.head_image_url image,le_jia_user.bind_partner_date date,merchant.name merchant_name,le_jia_user.bind_partner_id partner_id ,le_jia_user.phone_number phone,scorea.score scorea,scoreb.score scoreb  from le_jia_user,wei_xin_user,merchant,scorea,scoreb where scorea.le_jia_user_id = le_jia_user.id and scoreb.le_jia_user_id = le_jia_user.id and wei_xin_user.le_jia_user_id= le_jia_user.id and le_jia_user.bind_merchant_id = merchant.id and  le_jia_user.bind_partner_id =");
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
        if (ids.length > 0) {
            sql.append(" and user.id not in (");
            sql.append(Arrays.toString(ids).replaceAll("[\\,\\[\\]\\ ]", ""));
            sql.append(")");
        }
        sql.append(" limit ");
        sql.append(offset * 10);
        sql.append(",10");
        return em.createNativeQuery(sql.toString()).getResultList();
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Boolean batchWelfareInclusive(Partner partner, ExclusiveArrayDto exclusiveArrayDto) {
        PartnerWelfareLog partnerWelfareLog = exclusiveArrayDto.getPartnerWelfareLog();
        Long scoreA = partnerWelfareLog.getScoreA() * partnerWelfareLog.getUserCount();
        Long scoreB = partnerWelfareLog.getScoreB() * partnerWelfareLog.getUserCount();
        if (updatePartnerWalletByWelfare(partner, scoreA, scoreB, partnerWelfareLog)) {

            return true;
        } else {
            return false;
        }
    }
}
