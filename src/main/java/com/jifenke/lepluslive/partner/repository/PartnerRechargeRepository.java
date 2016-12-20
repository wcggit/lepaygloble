package com.jifenke.lepluslive.partner.repository;

import com.jifenke.lepluslive.partner.domain.entities.PartnerRecharge;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Created by xf on 2016/10/9. 合伙人充值持久层
 */
public interface PartnerRechargeRepository extends JpaRepository<PartnerRecharge, Long> {

    Page findAll(Specification<PartnerRecharge> specification, Pageable pageRequest);
}
