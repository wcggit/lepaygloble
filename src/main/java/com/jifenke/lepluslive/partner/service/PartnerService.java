package com.jifenke.lepluslive.partner.service;

import com.jifenke.lepluslive.global.util.MD5Util;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerWallet;
import com.jifenke.lepluslive.partner.repository.PartnerManagerRepository;
import com.jifenke.lepluslive.partner.repository.PartnerManagerWalletRepository;
import com.jifenke.lepluslive.partner.repository.PartnerRepository;
import com.jifenke.lepluslive.partner.repository.PartnerWalletRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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


    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Partner findPartnerByName(String currentUserLogin) {
        return partnerRepository.findByName(currentUserLogin).get();
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
        return  details.get(0).longValue();
    }
}
