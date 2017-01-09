package com.jifenke.lepluslive.withdraw.repository;

import com.jifenke.lepluslive.withdraw.domain.entities.WithdrawBill;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by xf on 2016/9/18.
 */
public interface WithdrawRepository extends JpaRepository<WithdrawBill, Long> {

    Page findAll(Specification<WithdrawBill> specification,Pageable pageRequest);
    @Query(value = "SELECT * from withdraw_bill WHERE merchant_id=?1 and state=0", nativeQuery = true)
    List<WithdrawBill> findByMerchantId(Long id);

    @Query(value = "SELECT * from withdraw_bill WHERE partner_id=?1 and state=0", nativeQuery = true)
    List<WithdrawBill> findByPartnerId(Long id);
}
