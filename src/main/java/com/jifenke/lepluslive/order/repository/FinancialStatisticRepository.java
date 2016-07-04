package com.jifenke.lepluslive.order.repository;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.order.domain.entities.FinancialStatistic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;


/**
 * Created by wcg on 16/5/9.
 */
public interface FinancialStatisticRepository extends JpaRepository<FinancialStatistic, Long> {


    Page findAll(Specification<FinancialStatistic> financialClause, Pageable pageRequest);

    @Query(value = "select sum(transfer_price) from financial_statistic where merchant_id = ?1",nativeQuery = true)
    Long countTransfering(Long id);

    List<FinancialStatistic> findAllByMerchantAndBalanceDateBetweenOrderByBalanceDate(
        Merchant merchant, Date start, Date end);
}
