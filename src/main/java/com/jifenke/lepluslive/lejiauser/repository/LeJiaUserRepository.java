package com.jifenke.lepluslive.lejiauser.repository;

import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.partner.domain.entities.Partner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by wcg on 16/3/24.
 */
public interface LeJiaUserRepository extends JpaRepository<LeJiaUser, Long> {

    LeJiaUser findByUserSid(String userSid);

    LeJiaUser findByPhoneNumber(String phoneNumber);

    @Query(value = "select count(*) from le_jia_user where bind_merchant_id = ?1", nativeQuery = true)
    Long countBindMerchantBindLeJiaUser(Long id);

    @Query(value = "select count(*) from le_jia_user where bind_partner_id = ?1", nativeQuery = true)
    Long countPartnerBindLeJiaUser(Long partnerId);

    /**
     * 根据时间统计最近七天绑定数量
     * @return
     */
    @Query(value = "select DATE_FORMAT(bind_partner_date,'%Y-%m-%d'),IFNULL(count(*),0) from le_jia_user " +
        " where  bind_partner_id = ?1 AND DATE_SUB(date(?2), INTERVAL 7 DAY) <= date(bind_partner_date) group by DATE_FORMAT(bind_partner_date,'%Y-%m-%d') ", nativeQuery = true)
    List<Object[]> countPartnerBindLeJiaUserByWeek(Long partnerId, Date start);



    Long countByBindPartnerAndBindPartnerDateBetween(Partner partner, Date start, Date end);

    /**
     * 统计某个注册来源的数量 16/09/06
     *
     * @param subSource 关注来源
     * @return 数量
     */
    @Query(value = "SELECT COUNT(*) FROM wei_xin_user WHERE sub_source=?1", nativeQuery = true)
    Integer countBySubSource(String subSource);

    /**
     * 某个商户邀请会员数 16/09/06
     *
     * @param subSource 关注来源
     * @return 数量
     */
    @Query(value = "SELECT COUNT(*) FROM wei_xin_user WHERE sub_source=?1 AND state=1", nativeQuery = true)
    Integer countBySubSourceAndState(String subSource);

    /**
     * 某个商户邀请会员的会员累计红包16/09/06
     *
     * @return 数量
     */
    @Query(value = "select ifnull(-sum(number),0) from partner_score_log where partner_id = ?1 and scoreaorigin = 1 and type = 1 ", nativeQuery = true)
    Long countScoreAByMerchant(Long partnerId);


    /**
     * 某个商户邀请会员的会员累计积分 16/09/06
     *
     * @return 数量
     */
    @Query(value = "select ifnull(-sum(number),0) from partner_score_log where partner_id = ?1 and scoreborigin = 1 and type = 0 ", nativeQuery = true)
    Long countScoreBByMerchant(Long partnerId);

    /**
     * 某个微信用户的首次关注红包
     */
    @Query(value = "select ifnull(sum(number),0) from scorea a,scorea_detail sd where sd.scorea_id = a.id and le_jia_user_id = ?1 and origin = 0 ", nativeQuery = true)
    Long findTotalScorea(Long leJiaUserId);

    /**
     * 某个微信用户的首次关注积分
     */
    @Query(value = "select ifnull(sum(number),0) from scoreb b,scoreb_detail sd where sd.scoreb_id = b.id and le_jia_user_id = ?1 and origin = 0 ", nativeQuery = true)
    Long findTotalScoreb(Long leJiaUserId);


    @Query(value = "SELECT ifnull(SUM(detail.number),0) FROM wei_xin_user u,scorea a,scorea_detail detail WHERE u.le_jia_user_id=a.le_jia_user_id AND u.sub_source=?1 AND u.state=1 and detail.scorea_id = a.id and detail.origin = 0", nativeQuery = true)
    Long countScoreABySubSource(String subSource);

    @Query(value = "SELECT ifnull(SUM(detail.number),0) FROM wei_xin_user u,scoreb b,scoreb_detail detail WHERE u.le_jia_user_id=b.le_jia_user_id AND u.sub_source=?1 AND u.state=1 and detail.scoreb_id = b.id and detail.origin = 0", nativeQuery = true)
    Long countScoreBBySubSource(String subSource);

    /**
     * 根据日期查询合伙人绑定会员数
     *
     * @return
     */
    @Query(value = "select DATE_FORMAT(create_date,\"%Y-%m-%d\"),count(*) from le_jia_user where bind_partner_id = ?1 and create_date between ?2 and ?3 GROUP BY DATE_FORMAT(create_date,\"%Y-%m-%d\")", nativeQuery = true)
    List<Object[]> countBindUserByPartnerAndDate(Long partnerId, Date startDate, Date endDate);
}
