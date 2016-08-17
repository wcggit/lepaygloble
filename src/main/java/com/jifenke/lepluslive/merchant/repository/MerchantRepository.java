package com.jifenke.lepluslive.merchant.repository;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.partner.domain.entities.Partner;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by wcg on 16/3/17.
 */
public interface MerchantRepository extends JpaRepository<Merchant, Long> {
    Page<Merchant> findAll(Pageable pageable);

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

    Long countByPartner(Partner partner);

    Merchant findByMerchantSid(String sid);
}
