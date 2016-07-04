package com.jifenke.lepluslive.order.repository;

import com.jifenke.lepluslive.order.domain.entities.OffLineOrder;
import com.jifenke.lepluslive.order.domain.entities.OffLineOrderShare;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by wcg on 16/5/5.
 */
public interface OffLineOrderShareRepository extends JpaRepository<OffLineOrderShare,Long> {

  OffLineOrder findByOrderSid(String orderSid);
}
