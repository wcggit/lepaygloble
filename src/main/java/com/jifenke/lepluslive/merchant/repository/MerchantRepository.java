package com.jifenke.lepluslive.merchant.repository;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.partner.domain.entities.Partner;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by wcg on 16/3/17.
 */
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    Page<Merchant> findAll(Pageable pageable);

    /**
     * 查询合伙人虚拟商户
     * @param partnerId
     * @return
     */
    @Query(value="SELECT * FROM merchant WHERE partner_id = ?1 AND partnership = 2",nativeQuery = true)
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

    Merchant findByMerchantSid(String sid);

    /***
     * 根据门店查询数据
     */
    @Query(value="select * from (" +
        "        SELECT o.order_sid,o.complete_date,lepay_code,true_pay,true_score,o.rebate_way,0 order_type,1 merchant_name FROM off_line_order o where o.state=1 and  o.merchant_id=?1" +
        "        UNION " +
        "        SELECT p.order_sid,p.complete_date,null,true_pay,true_score,p.rebate_way,1 order_type,1 merchant_name FROM pos_order p where  p.state=1 and p.merchant_id =?1" +
        "    ) r order by r.complete_date desc limit ?2,10",nativeQuery = true)
    List<Object[]> findOrderListByMerchant(Long merchantId,Long offSet);





}
