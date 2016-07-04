package com.jifenke.lepluslive.merchant.repository;

import com.jifenke.lepluslive.merchant.domain.entities.Area;
import com.jifenke.lepluslive.merchant.domain.entities.City;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.List;

import javax.persistence.QueryHint;

/**
 * Created by wcg on 16/3/30.
 */
public interface AreaRepository extends JpaRepository<Area,Long> {

    /**
     * 获取某个城市的所有地区列表
     */
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    List<Area> findAllByCity(City city);

}
