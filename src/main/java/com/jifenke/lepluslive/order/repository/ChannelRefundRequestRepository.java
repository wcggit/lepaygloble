package com.jifenke.lepluslive.order.repository;

import com.jifenke.lepluslive.order.domain.entities.ChannelRefundRequest;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 通道退款单
 * Created by zhangwen on 2017/7/12.
 */
public interface ChannelRefundRequestRepository extends JpaRepository<ChannelRefundRequest, Long> {
    ChannelRefundRequest findByOrderSid(String orderSid);
}
