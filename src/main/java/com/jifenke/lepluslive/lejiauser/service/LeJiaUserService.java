package com.jifenke.lepluslive.lejiauser.service;


import com.jifenke.lepluslive.lejiauser.domain.criteria.LeJiaUserCriteria;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.repository.LeJiaUserRepository;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.partner.domain.entities.Partner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 * Created by wcg on 16/4/21.
 */
@Service
@Transactional(readOnly = true)
public class LeJiaUserService {


    @Inject
    private LeJiaUserRepository leJiaUserRepository;

    @Inject
    private EntityManager em;


    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public LeJiaUser findUserByUserSid(String userSid) {
        return leJiaUserRepository.findByUserSid(userSid);
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Long countBindMerchant(Merchant merchant) {

        return leJiaUserRepository.countBindMerchant(merchant.getId());
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List getMerchantBindUserList(LeJiaUserCriteria leJiaUserCriteria) {

        int start = 10 * (leJiaUserCriteria.getOffset() - 1);

        StringBuffer
            sql = new StringBuffer();
        sql.append(
            "select  le_jia_user.id,le_jia_user.bind_date,le_jia_user.phone,ifnull(counts.total,0) as count,le_jia_user.nickname,le_jia_user.image from (select le_jia_user.id,le_jia_user.bind_merchant_date  bind_date,le_jia_user.phone_number phone,wei_xin_user.nickname nickname,wei_xin_user.head_image_url image from le_jia_user,wei_xin_user where le_jia_user.bind_merchant_id = ");
        sql.append(leJiaUserCriteria.getMerchant().getId());
        if (leJiaUserCriteria.getStartDate() != null && leJiaUserCriteria.getStartDate() != "") {
            sql.append(" and le_jia_user.bind_merchant_date between '");
            sql.append(leJiaUserCriteria.getStartDate());
            sql.append("' and '");
            sql.append(leJiaUserCriteria.getEndDate());
            sql.append("'");
        }
        sql.append(
            " and le_jia_user.id = wei_xin_user.le_jia_user_id order by bind_merchant_date desc limit ");
        sql.append(start);
        sql.append(",10");
        sql.append(
            ") as le_jia_user left join (select sum(off_line_order_share.to_lock_merchant) as total,off_line_order.le_jia_user_id as id from off_line_order,off_line_order_share where off_line_order.id = off_line_order_share.off_line_order_id and off_line_order_share.lock_merchant_id = ");
        sql.append(leJiaUserCriteria.getMerchant().getId());
        sql.append(
            " group by off_line_order.le_jia_user_id) as counts  on   le_jia_user.id = counts.id ");

        Query query = em.createNativeQuery(sql.toString());

        List<Object[]> details = query.getResultList();

        return details;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Long getTotalPages(LeJiaUserCriteria leJiaUserCriteria) {
        StringBuffer
            sql = new StringBuffer();
        sql.append("select count(*) from le_jia_user where le_jia_user.bind_merchant_id =");
        sql.append(leJiaUserCriteria.getMerchant().getId());
        if (leJiaUserCriteria.getStartDate() != null && leJiaUserCriteria.getStartDate() != "") {
            sql.append(" and le_jia_user.bind_merchant_date between '");
            sql.append(leJiaUserCriteria.getStartDate());
            sql.append("' and '");
            sql.append(leJiaUserCriteria.getEndDate());
            sql.append("'");
        }

        Query query = em.createNativeQuery(sql.toString());
        List<BigInteger> details = query.getResultList();
        return (long) Math.ceil(details.get(0).doubleValue() / 10.0);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List getUserByBindPartner(LeJiaUserCriteria leJiaUserCriteria) {
        int start = 10 * (leJiaUserCriteria.getOffset() - 1);

        StringBuffer
            sql = new StringBuffer();
        sql.append(
            "select user.id id,user.name,user.image,user.date,user.merchant_name,user.phone,ifnull(audit.to_merchant,0),ifnull(audit.to_partner,0) from (select le_jia_user.id id,wei_xin_user.nickname name,wei_xin_user.head_image_url image,le_jia_user.bind_partner_date date,merchant.name merchant_name,le_jia_user.bind_partner_id partner_id ,le_jia_user.phone_number phone  from le_jia_user,wei_xin_user,merchant where wei_xin_user.le_jia_user_id= le_jia_user.id and le_jia_user.bind_merchant_id = merchant.id and  le_jia_user.bind_partner_id =");
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
        sql.append(" order by le_jia_user.bind_partner_date desc limit  ");
        sql.append(start);
        sql.append(",10");

        sql.append(
            ") user left join (select sum(off_line_order_share.to_lock_merchant) to_merchant,sum(off_line_order_share.to_lock_partner) to_partner,off_line_order.le_jia_user_id id from off_line_order_share,merchant,off_line_order where off_line_order_share.lock_merchant_id = merchant.id  and merchant.partner_id = ");
        sql.append(leJiaUserCriteria.getPartner().getId());
        sql.append(
            " and off_line_order.id = off_line_order_share.off_line_order_id group by off_line_order.le_jia_user_id) audit on user.id = audit.id ");

        Query query = em.createNativeQuery(sql.toString());

        List<Object[]> details = query.getResultList();

        return details;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Long getTotalPagesByBindPartner(LeJiaUserCriteria leJiaUserCriteria) {
        int start = 10 * (leJiaUserCriteria.getOffset() - 1);

        StringBuffer
            countPage = new StringBuffer();
        countPage.append(
            "select count(*) from le_jia_user,wei_xin_user,merchant where wei_xin_user.le_jia_user_id= le_jia_user.id and le_jia_user.bind_merchant_id = merchant.id and  le_jia_user.bind_partner_id =");
        countPage.append(leJiaUserCriteria.getPartner().getId());
        if (leJiaUserCriteria.getPartnerStartDate() != null
            && leJiaUserCriteria.getPartnerStartDate() != "") {
            countPage.append(" and le_jia_user.bind_partner_date between '");
            countPage.append(leJiaUserCriteria.getPartnerStartDate());
            countPage.append("' and '");
            countPage.append(leJiaUserCriteria.getPartnerEndDate());
            countPage.append("'");
        }
        if (leJiaUserCriteria.getMerchantName() != null
            && leJiaUserCriteria.getMerchantName() != "") {
            countPage.append(" and merchant.name like '%");
            countPage.append(leJiaUserCriteria.getMerchantName());
            countPage.append("%'");
        }
        if (leJiaUserCriteria.getPhone() != null && leJiaUserCriteria.getPhone() != "") {
            countPage.append(" and le_jia_user.phone_number like '%");
            countPage.append(leJiaUserCriteria.getPhone());
            countPage.append("%'");
        }

        countPage.append(" order by le_jia_user.bind_partner_date desc limit  ");
        countPage.append(start);
        countPage.append(",10");

        List<BigInteger> details = em.createNativeQuery(countPage.toString()).getResultList();

        return (long) Math.ceil(details.get(0).doubleValue() / 10.0);

    }

    public Long countPartnerBindLeJiaUser(Partner partner) {
        return leJiaUserRepository.countPartnerBindLeJiaUser(partner.getId());
    }

    public Long countPartnerBindLeJiaUserByDate(Partner partner) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Date start =calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.SECOND, -1);
        Date end =calendar.getTime();
        return leJiaUserRepository.countByBindPartnerAndBindPartnerDateBetween(partner,start,end);
    }
}
