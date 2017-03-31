package com.jifenke.lepluslive.merchant.repository;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by wcg on 16/3/17.
 */
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    Page<Merchant> findAll(Pageable pageable);

    /**
     * 查询合伙人虚拟商户
     *
     * @param partnerId
     * @return
     */
    @Query(value = "SELECT * FROM merchant WHERE partner_id = ?1 AND partnership = 2", nativeQuery = true)
    Merchant findVtMerchantByPartner(Long partnerId);

    @Query(value = "select count(*) from merchant group by ?1", nativeQuery = true)
    int getMerchantSid(String location);

    /**
     * 按照距离远近对商家排序
     *
     * @param latitude  经度
     * @param longitude 纬度
     * @param startNum  开始记录
     * @param pageSize  每页显示个数
     * @return
     */
    @Query(value = "SELECT m.id,m.sid,m.location,m.phone_number,m.`name`,m.picture,m.discount,m.rebate,m.lng,m.lat, ROUND( 6378.138 * 2 * ASIN(SQRT(POW(SIN((?1 * PI() / 180 - m.lat * PI() / 180) / 2),2) + COS(?1 * PI() / 180) * COS(m.lat * PI() / 180) * POW(SIN((?2 * PI() / 180 - m.lng * PI() / 180) / 2),2))) * 1000) AS distance FROM merchant m ORDER BY distance LIMIT ?3,?4", nativeQuery = true)
    List<Object[]> findOrderByDistance(Double latitude, Double longitude, Integer startNum,
                                       Integer pageSize);

    Long countByPartnerAndPartnershipNot(Partner partner, Integer partnerShip);

    Merchant findMerchantUserBySid(String sid);

    /***
     * 根据门店查询数据 (乐加)
     */
    @Query(value = "select * from (" +
        "        SELECT o.order_sid,o.complete_date,lepay_code,true_pay,true_score,o.rebate_way,0 order_type,1 merchant_name FROM off_line_order o where o.state=1 and  o.merchant_id=?1" +
        "        UNION " +
        "        SELECT p.order_sid,p.complete_date,null,true_pay,true_score,p.rebate_way,1 order_type,1 merchant_name FROM pos_order p where  p.state=1 and p.merchant_id =?1" +
        "    ) r order by r.complete_date desc limit ?2,10", nativeQuery = true)
    List<Object[]> findOrderListByMerchant(Long merchantId, Long offSet);

    /***
     * 根据门店查询数据 (掌富)
     */
    @Query(value = "select * from (" +
        "SELECT o.order_sid,o.complete_date,le_pay_code ,true_pay,true_score,o.order_type_id rebate_way,0 order_type,1 merchant_name FROM scan_code_order o where o.state=1 and  o.merchant_id=?1 " +
        "UNION  " +
        "SELECT p.order_sid,p.complete_date,null le_pay_code,true_pay,true_score,p.rebate_way rebate_way,1 order_type,1 merchant_name FROM pos_order p where  p.state=1 and p.merchant_id =?1) r order by r.complete_date desc limit ?2,10", nativeQuery = true)
    List<Object[]> findScanOrderListByMerchant(Long merchantId, Long offSet);


    /**
     * 分页查询商户旗下各门店锁定会数量和总数
     *
     * @param obj
     * @param offSet
     * @return
     */
    @Query(value = "SELECT a.`name`,count(b.id) number, a.user_limit from merchant a " +
        "LEFT JOIN le_jia_user b on a.id = b.bind_merchant_id " +
        "where a.id in (?1) GROUP BY a.id ORDER BY number DESC LIMIT ?2,6", nativeQuery = true)
    List<Object[]> findPageMerchantMemberLockNumber(List<Object> obj, Integer offSet);

    /**
     * 查询商户旗下所有的会员锁定总数
     *
     * @param obj
     * @return
     */
    @Query(value = "SELECT count(a.id) from le_jia_user a where a.bind_merchant_id in (?1)", nativeQuery = true)
    Integer findMerchantTotalMember(List<Object> obj);

    /**
     * 查询商户旗下门店今日锁定总数
     */
    @Query(value = "SELECT count(id) from le_jia_user  where bind_merchant_id in (?1) and to_days(bind_merchant_date) = to_days(now())", nativeQuery = true)
    Integer findMerchantDailyMember(List<Object> obj);


    /**
     * 查询商户旗下锁定会员ID
     */
    @Query(value = "SELECT id from le_jia_user a where a.bind_merchant_id in (?1)", nativeQuery = true)
    List findMerchantMemberIds(List<Object> obj);

    @Query(value = "select DATE_FORMAT(create_date,\"%Y-%m-%d\"),count(*) from merchant where partner_id = ?1 and create_date between ?2 and ?3 GROUP BY DATE_FORMAT(create_date,\"%Y-%m-%d\")", nativeQuery = true)
    List<Object[]> countMerchantByPartnerAndDate(Long partnerId, Date startDate, Date endDate);

    /**
     *  统计指定合伙人 7 天内锁定的商户数量
     */
    @Query(value = "select DATE_FORMAT(create_date,'%Y-%m-%d'),IFNULL(count(*),0) from merchant " +
        " where  partner_id = ?1 AND DATE_SUB(date(?2), INTERVAL 7 DAY) <= date(create_date) group by DATE_FORMAT(create_date,'%Y-%m-%d')",nativeQuery = true)
    List<Object[]> countMerchantByPartnerWeek(Long partnerId,Date start);
}
