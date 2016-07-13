package com.jifenke.lepluslive.lejiauser.service;


import com.jifenke.lepluslive.lejiauser.domain.criteria.LeJiaUserCriteria;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.repository.LeJiaUserRepository;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
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
        sql.append(" and le_jia_user.id = wei_xin_user.le_jia_user_id order by bind_merchant_date desc limit ");
        sql.append(start);
        sql.append(",10");
        sql.append(
            ") as le_jia_user left join (select sum(off_line_order_share.to_lock_merchant) as total,off_line_order.le_jia_user_id as id from off_line_order,off_line_order_share where off_line_order.id = off_line_order_share.off_line_order_id and off_line_order_share.lock_merchant_id = ");
        sql.append(leJiaUserCriteria.getMerchant().getId());
        sql.append(
            " group by off_line_order.le_jia_user_id) as counts  on   le_jia_user.id = counts.id ");

        Date end = new Date();
        Query query = em.createNativeQuery(sql.toString());

        List<Object[]> details = query.getResultList();



        Date end1 = new Date();



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
}
