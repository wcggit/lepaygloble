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
    Long countBindMerchant(Long id);

    @Query(value = "select count(*) from le_jia_user where bind_partner_id = ?1", nativeQuery = true)
    Long countPartnerBindLeJiaUser(Long partnerId);

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
     * @param subSource 关注来源
     * @return 数量
     */
    @Query(value = "SELECT SUM(a.total_score) FROM wei_xin_user u,scorea a WHERE u.le_jia_user_id=a.le_jia_user_id AND sub_source=?1 AND state=1", nativeQuery = true)
    List countScoreAByMerchant(String subSource);


    /**
     * 某个商户邀请会员的会员累计积分 16/09/06
     *
     * @param subSource 关注来源
     * @return 数量
     */
    @Query(value = "SELECT SUM(b.total_score) FROM wei_xin_user u,scoreb b WHERE u.le_jia_user_id=b.le_jia_user_id AND sub_source=?1 AND state=1", nativeQuery = true)
    List countScoreBByMerchant(String subSource);

    /**
     * 某个微信用户的首次关注红包
     * @param leJiaUserId
     * @return
     */
    @Query(value="select sum(number) from scorea a,scorea_detail sd where sd.scorea_id = a.id and le_jia_user_id = ?1 and origin = 0 ",nativeQuery=true)
    Long findTotalScorea(Long leJiaUserId);

    /**
     *  某个微信用户的首次关注积分
     * @param leJiaUserId
     * @return
     */
    @Query(value="select sum(number) from scoreb b,scoreb_detail sd where sd.scoreb_id = b.id and le_jia_user_id = ?1 and origin = 0 ",nativeQuery=true)
    Long findTotalScoreb(Long leJiaUserId);

}
