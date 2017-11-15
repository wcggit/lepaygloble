package com.jifenke.lepluslive.merchant.repository;


import com.jifenke.lepluslive.merchant.domain.entities.StatsMerDailyData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 商户每日交易等信息统计
 *
 * @author zhangwen at 2017-11-06 11:22
 **/
public interface StatsMerDailyDataRepository extends JpaRepository<StatsMerDailyData, Long>, JpaSpecificationExecutor<StatsMerDailyData> {

}
