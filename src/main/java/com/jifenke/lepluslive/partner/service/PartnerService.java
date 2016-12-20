package com.jifenke.lepluslive.partner.service;

import com.jifenke.lepluslive.global.util.MD5Util;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.partner.domain.criteria.MerchantCriteria;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerInfo;
import com.jifenke.lepluslive.partner.domain.entities.PartnerWallet;
import com.jifenke.lepluslive.partner.domain.entities.PartnerWelfareLog;
import com.jifenke.lepluslive.partner.repository.PartnerInfoRepository;
import com.jifenke.lepluslive.partner.repository.PartnerRepository;
import com.jifenke.lepluslive.partner.repository.PartnerWalletRepository;
import com.jifenke.lepluslive.partner.repository.PartnerWelfareLogRepository;

import org.hibernate.LockMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Created by wcg on 16/6/3.
 */
@Service
@Transactional(readOnly = true)
public class PartnerService {


    @Inject
    private PartnerRepository partnerRepository;

    @Inject
    private PartnerWalletRepository partnerWalletRepository;

    @Inject
    private EntityManager em;

    @Inject
    private PartnerInfoRepository partnerInfoRepository;

    @Inject
    private PartnerWelfareLogRepository partnerWelfareLogRepository;


    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Partner findByPartnerSid(String currentUserLogin) {
        return partnerRepository.findByPartnerSid(currentUserLogin).get();
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PartnerWallet findPartnerWalletByPartner(Partner partner) {
        return partnerWalletRepository.findByPartner(partner);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Long countPartnerDayCommission(Partner partner) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        String start = simpleDateFormat.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.SECOND, -1);
        String end = simpleDateFormat.format(calendar.getTime());
        StringBuffer sql = new StringBuffer();
        sql.append(
            "select ifnull(a.sum+b.sum,0) from (select sum(to_lock_partner) sum from off_line_order_share where lock_partner_id = ");
        sql.append(partner.getId());
        sql.append(" and create_date between '");
        sql.append(start);
        sql.append("' and '");
        sql.append(end);
        sql.append(
            "')a,(select sum(to_trade_partner) sum from off_line_order_share where trade_partner_id =");
        sql.append(partner.getId());
        sql.append(" and create_date between '");
        sql.append(start);
        sql.append(" 'and '");
        sql.append(end);
        sql.append("' ) b");
        Query query = em.createNativeQuery(sql.toString());

        List<BigDecimal> details = query.getResultList();
        return details.get(0).longValue();
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List findMerchantTop5(Integer sortType, Partner partner) {
        StringBuffer sql = new StringBuffer();
        if (sortType == 0) {
            sql.append(
                "select a.name,a.total,merchant_wallet.total_money from (select merchant.name,statistic.total,merchant.id  from merchant,(select * from (select (ifnull(trade_merchant.total,0)+ifnull(lock_merchant.total,0)) total,lock_merchant.lock_merchant_id,trade_merchant.trade_merchant_id from (select sum(to_lock_partner) total, lock_merchant_id  from off_line_order_share where lock_partner_id = ");
            sql.append(partner.getId());
            sql.append(
                " group by lock_merchant_id ) lock_merchant left join (select sum(to_trade_partner) total, trade_merchant_id  from off_line_order_share where trade_partner_id = ");
            sql.append(partner.getId());
            sql.append(
                "  group by trade_merchant_id ) trade_merchant on trade_merchant.trade_merchant_id = lock_merchant.lock_merchant_id union select (ifnull(trade_merchant.total,0)+ifnull(lock_merchant.total,0)) total,lock_merchant.lock_merchant_id,trade_merchant.trade_merchant_id from (select sum(to_lock_partner) total, lock_merchant_id  from off_line_order_share where lock_partner_id = ");
            sql.append(partner.getId());
            sql.append(
                "  group by lock_merchant_id ) lock_merchant right join (select sum(to_trade_partner) total, trade_merchant_id  from off_line_order_share where trade_partner_id = ");
            sql.append(partner.getId());
            sql.append(
                "  group by trade_merchant_id ) trade_merchant on trade_merchant.trade_merchant_id = lock_merchant.lock_merchant_id ) statistic  limit 0,5 )statistic where merchant.id=statistic.lock_merchant_id or  merchant.id=statistic.trade_merchant_id ) a ,merchant_wallet where a.id =merchant_wallet.merchant_id order by a.total desc");
        }
        if (sortType == 1) {
            sql.append(
                "select merchant.name,statistic.bind_user,merchant.user_limit from merchant,(select count(*) bind_user,bind_merchant_id from le_jia_user where bind_partner_id = ");
            sql.append(partner.getId());
            sql.append(
                " group by bind_merchant_id order by bind_user desc limit 0,5) statistic where statistic.bind_merchant_id = merchant.id");
        }
        if (sortType == 2) {
            sql.append(
                "select merchant.name,sum(off_line_order.total_price) total_money from off_line_order,merchant where merchant.id = off_line_order.merchant_id and off_line_order.state = 1 and merchant.partner_id = ");
            sql.append(partner.getId());
            sql.append(" group by off_line_order.merchant_id order by total_money desc limit 0,5");
        }
        Query query = em.createNativeQuery(sql.toString());

        return query.getResultList();

    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List getMerchantList(MerchantCriteria merchantCriteria) {
        int start = 10 * (merchantCriteria.getOffset() - 1);
        StringBuffer sql = new StringBuffer();
        sql.append(
            "select merchant_sid,name,create_date,partnership,location,(select count(*) from le_jia_user where bind_merchant_id = merchant.id)bind_user,(select total_money from merchant_wallet where merchant_id = merchant.id),ifNull((SELECT sum(to_trade_partner) total FROM off_line_order_share WHERE trade_merchant_id = merchant.id),0)+ifNull((SELECT sum(to_lock_partner) total FROM off_line_order_share WHERE  lock_merchant_id = merchant.id),0)commission,user_limit from merchant where partner_id =");
        sql.append(merchantCriteria.getPartner().getId());
        if (merchantCriteria.getStartDate() != null && merchantCriteria.getStartDate() != "") {
            sql.append(" and merchant.create_date  between '");
            sql.append(merchantCriteria.getStartDate());
            sql.append("' and '");
            sql.append(merchantCriteria.getEndDate());
            sql.append("'");
        }
        if (merchantCriteria.getMerchantName() != null
            && merchantCriteria.getMerchantName() != "") {
            sql.append(" and merchant.name like '%");
            sql.append(merchantCriteria.getMerchantName());
            sql.append("%'");
        }
        if (merchantCriteria.getPartnerShip() != null) {
            sql.append(" and merchant.partnership =");
            sql.append(merchantCriteria.getPartnerShip());
        }
        sql.append(" and merchant.partnership !=2");
        if (merchantCriteria.getUserBindState() != null) {
            if (merchantCriteria.getUserBindState() == 0) {
                sql.append(
                    "  and (case when (merchant.user_limit=0 ) then 0 else  (select count(*) from le_jia_user where bind_merchant_id = merchant.id)/merchant.user_limit end )=0");
            }
            if (merchantCriteria.getUserBindState() == 1) {
                sql.append(
                    " and (case when (merchant.user_limit=0 ) then 0 else  (select count(*) from le_jia_user where bind_merchant_id = merchant.id)/merchant.user_limit end )>=0.85 and  (case when (merchant.user_limit=0 ) then 0 else  (select count(*) from le_jia_user where bind_merchant_id = merchant.id)/merchant.user_limit end )<1");
            }
            if (merchantCriteria.getUserBindState() == 2) {
                sql.append(
                    "  and (case when (merchant.user_limit=0 ) then 0 else  (select count(*) from le_jia_user where bind_merchant_id = merchant.id)/merchant.user_limit end )=1");
            }
        }
        sql.append(" order by merchant.create_date desc limit ");
        sql.append(start);
        sql.append(",10");
        Query nativeQuery = em.createNativeQuery(sql.toString());

        return nativeQuery.getResultList();
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Long getMerchantListPage(MerchantCriteria merchantCriteria) {
        int start = 10 * (merchantCriteria.getOffset() - 1);
        StringBuffer sql = new StringBuffer();
        sql.append(
            "select count(*)from merchant where partner_id =");
        sql.append(merchantCriteria.getPartner().getId());
        if (merchantCriteria.getStartDate() != null && merchantCriteria.getStartDate() != "") {
            sql.append(" and merchant.create_date  between '");
            sql.append(merchantCriteria.getStartDate());
            sql.append("' and '");
            sql.append(merchantCriteria.getEndDate());
            sql.append("'");
        }
        if (merchantCriteria.getMerchantName() != null
            && merchantCriteria.getMerchantName() != "") {
            sql.append(" and merchant.name like '%");
            sql.append(merchantCriteria.getMerchantName());
            sql.append("%'");
        }
        if (merchantCriteria.getPartnerShip() != null) {
            sql.append(" and merchant.partnership =");
            sql.append(merchantCriteria.getPartnerShip());
        }

        if (merchantCriteria.getUserBindState() != null) {
            if (merchantCriteria.getUserBindState() == 0) {
                sql.append(
                    "  and (case when (merchant.user_limit=0 ) then 0 else  (select count(*) from le_jia_user where bind_merchant_id = merchant.id)/merchant.user_limit end )=0");
            }
            if (merchantCriteria.getUserBindState() == 1) {
                sql.append(
                    " and (case when (merchant.user_limit=0 ) then 0 else  (select count(*) from le_jia_user where bind_merchant_id = merchant.id)/merchant.user_limit end )>=0.85 and  (case when (merchant.user_limit=0 ) then 0 else  (select count(*) from le_jia_user where bind_merchant_id = merchant.id)/merchant.user_limit end )<1");
            }
            if (merchantCriteria.getUserBindState() == 2) {
                sql.append(
                    "  and (case when (merchant.user_limit=0 ) then 0 else  (select count(*) from le_jia_user where bind_merchant_id = merchant.id)/merchant.user_limit end )=1");
            }
        }
        sql.append(" order by merchant.create_date desc limit ");
        sql.append(start);
        sql.append(",10");
        Query nativeQuery = em.createNativeQuery(sql.toString());
        List<BigInteger> details = nativeQuery.getResultList();
        return (long) Math.ceil(details.get(0).doubleValue() / 10.0);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Long getMerchantListCount(MerchantCriteria merchantCriteria) {
        int start = 10 * (merchantCriteria.getOffset() - 1);
        StringBuffer sql = new StringBuffer();
        sql.append(
            "select count(*)from merchant where partner_id =");
        sql.append(merchantCriteria.getPartner().getId());
        if (merchantCriteria.getStartDate() != null && merchantCriteria.getStartDate() != "") {
            sql.append(" and merchant.create_date  between '");
            sql.append(merchantCriteria.getStartDate());
            sql.append("' and '");
            sql.append(merchantCriteria.getEndDate());
            sql.append("'");
        }
        if (merchantCriteria.getMerchantName() != null
            && merchantCriteria.getMerchantName() != "") {
            sql.append(" and merchant.name like '%");
            sql.append(merchantCriteria.getMerchantName());
            sql.append("%'");
        }
        if (merchantCriteria.getPartnerShip() != null) {
            sql.append(" and merchant.partnership =");
            sql.append(merchantCriteria.getPartnerShip());
        }

        if (merchantCriteria.getUserBindState() != null) {
            if (merchantCriteria.getUserBindState() == 0) {
                sql.append(
                    "  and (case when (merchant.user_limit=0 ) then 0 else  (select count(*) from le_jia_user where bind_merchant_id = merchant.id)/merchant.user_limit end )=0");
            }
            if (merchantCriteria.getUserBindState() == 1) {
                sql.append(
                    " and (case when (merchant.user_limit=0 ) then 0 else  (select count(*) from le_jia_user where bind_merchant_id = merchant.id)/merchant.user_limit end )>=0.85 and  (case when (merchant.user_limit=0 ) then 0 else  (select count(*) from le_jia_user where bind_merchant_id = merchant.id)/merchant.user_limit end )<1");
            }
            if (merchantCriteria.getUserBindState() == 2) {
                sql.append(
                    "  and (case when (merchant.user_limit=0 ) then 0 else  (select count(*) from le_jia_user where bind_merchant_id = merchant.id)/merchant.user_limit end )=1");
            }
        }
        sql.append(" order by merchant.create_date desc limit ");
        sql.append(start);
        sql.append(",10");
        Query nativeQuery = em.createNativeQuery(sql.toString());
        List<BigInteger> details = nativeQuery.getResultList();
        return (long) Math.ceil(details.get(0).intValue());
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Long countPartnerBindFullMerchant(Partner partner) {
        StringBuffer sql = new StringBuffer();
        sql.append(
            "select count(*) from merchant where partner_id = ");
        sql.append(partner.getId());
        sql.append(
            " and user_limit = (select count(*) from le_jia_user where bind_merchant_id = merchant.id) and user_limit != 0");
        Query nativeQuery = em.createNativeQuery(sql.toString());
        List<BigInteger> details = nativeQuery.getResultList();
        return details.get(0).longValue();
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void resetPassword(Partner partner, String reset, String password) {

        String origin = MD5Util.MD5Encode(password, "utf-8");

        if (partner.getPassword().equals(origin)) {
            partner.setPassword(MD5Util.MD5Encode(reset, "utf-8"));
            partnerRepository.save(partner);
        } else {
            throw new RuntimeException();
        }

    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Boolean checkPartnerBindMerchantLimit(Partner partner) {
        long currentBind = partnerRepository.countParnterBindMerchant(partner.getId());
        return partner.getMerchantLimit() > currentBind;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Partner findPartnerById(Long id) {
        return partnerRepository.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PartnerInfo findPartnerInfoByPartnerSid(String currentUserLogin) {
        return partnerInfoRepository.findByPartner(findByPartnerSid(currentUserLogin));
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void unbindWeiXinUser(Partner partner) {
        partner.setWeiXinUser(null);
        partnerRepository.save(partner);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void insertPartnerWelfareLog(PartnerWelfareLog partnerWelfareLog) {
        partnerWelfareLogRepository.save(partnerWelfareLog);
    }

    /**
     * 发福利给用户
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void sendWelfareToUser(String[] userSids, AtomicLong count,
                                  PartnerWelfareLog partnerWelfareLog) {
        for (String userSid : userSids) {
            StringBuffer sql = new StringBuffer();
            sql.append(
                "select id from scorea where scorea.le_jia_user_id = (select id from le_jia_user where le_jia_user.user_sid = ");
            sql.append(userSid);
            sql.append(")");
            Query nativeQuery = em.createNativeQuery(sql.toString());
            int aid = nativeQuery.getFirstResult();
            sql.setLength(0);
            sql.append("update scorea set score = score+");
            sql.append(partnerWelfareLog.getScoreA());
            sql.append(",total_score=total_score+");
            sql.append(partnerWelfareLog.getScoreA());
            sql.append(" ,last_update_date=now() where id = ");
            sql.append(aid);
            em.createNativeQuery(sql.toString());
            sql.setLength(0);
            sql.append(
                "insert into  scorea_detail (date_created,number,operate,scorea_id,order_sid,origin) values (now(),");
            sql.append(partnerWelfareLog.getScoreA());
            sql.append(",");
            sql.append("'合伙人发福利',");
            sql.append(aid);
            sql.append(",");
            sql.append(partnerWelfareLog.getSid());
            sql.append(",");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void welfareToUser(Serializable userId,
                              PartnerWelfareLog partnerWelfareLog) {

        if (partnerWelfareLog.getScoreA() > 0) {
            StringBuffer sql = new StringBuffer();
            sql.append("select id from scorea where scorea.le_jia_user_id = ");
            sql.append(userId);
            sql.append("  for update ;");
            em.createNativeQuery(sql.toString()).getResultList();
            sql.setLength(0);
            sql.append("update scorea set score = score+");
            sql.append(partnerWelfareLog.getScoreA());
            sql.append(",total_score=total_score+");
            sql.append(partnerWelfareLog.getScoreA());
            sql.append(" ,last_update_date=now() where scorea.le_jia_user_id =  ");
            sql.append(userId);
            em.createNativeQuery(sql.toString()).executeUpdate();
            sql.setLength(0);
            sql.append(
                "insert into  scorea_detail (date_created,number,operate,scorea_id,order_sid,origin) values (now(),");
            sql.append(partnerWelfareLog.getScoreA());
            sql.append(",");
            sql.append("'合伙人发福利',");
            sql.append("(select id from scorea where scorea.le_jia_user_id = ");
            sql.append(userId);
            sql.append("),");
            sql.append(partnerWelfareLog.getSid());
            sql.append(",");
            sql.append("10)");
            em.createNativeQuery(sql.toString()).executeUpdate();
        }
        if (partnerWelfareLog.getScoreB() > 0) {
            StringBuffer sql = new StringBuffer();
            sql.append("select id from scoreb where scoreb.le_jia_user_id = ");
            sql.append(userId);
            sql.append("  for update ;");
            em.createNativeQuery(sql.toString()).getResultList();
            sql.setLength(0);
            sql.append("update scoreb set score = score+");
            sql.append(partnerWelfareLog.getScoreB());
            sql.append(",total_score=total_score+");
            sql.append(partnerWelfareLog.getScoreB());
            sql.append(" ,last_update_date=now() where scoreb.le_jia_user_id =  ");
            sql.append(userId);
            em.createNativeQuery(sql.toString()).executeUpdate();
            sql.setLength(0);
            sql.append(
                "insert into  scoreb_detail (date_created,number,operate,scoreb_id,order_sid,origin) values (now(),");
            sql.append(partnerWelfareLog.getScoreB());
            sql.append(",");
            sql.append("'合伙人发福利',");
            sql.append("(select id from scoreb where scoreb.le_jia_user_id = ");
            sql.append(userId);
            sql.append("),");
            sql.append(partnerWelfareLog.getSid());
            sql.append(",");
            sql.append("10)");
            em.createNativeQuery(sql.toString()).executeUpdate();
        }
    }
}
