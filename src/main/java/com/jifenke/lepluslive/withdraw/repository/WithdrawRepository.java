package com.jifenke.lepluslive.withdraw.repository;

import com.jifenke.lepluslive.withdraw.domain.entities.WithdrawBill;

import org.omg.CORBA.Object;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by xf on 2016/9/18.
 */
public interface WithdrawRepository extends JpaRepository<WithdrawBill, Long> {

    Page findAll(Specification<WithdrawBill> specification, Pageable pageRequest);

}
