package com.jifenke.lepluslive.order.service;


import com.jifenke.lepluslive.order.domain.entities.ChannelRefundOrder;
import com.jifenke.lepluslive.order.domain.entities.ChannelRefundRequest;
import com.jifenke.lepluslive.order.domain.entities.OffLineOrder;
import com.jifenke.lepluslive.order.domain.entities.ScanCodeOrder;
import com.jifenke.lepluslive.order.repository.ChannelRefundOrderRepository;
import com.jifenke.lepluslive.order.repository.ChannelRefundRequestRepository;
import com.jifenke.lepluslive.yibao.domain.criteria.LedgerRefundOrderCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by zhangwen on 2017/7/12.
 */
@Service
@Transactional(readOnly = true)
public class ChannelRefundOrderService {

    @Inject
    private ChannelRefundOrderRepository repository;
    @Inject
    private ScanCodeOrderService scanCodeOrderService;
    @Inject
    private OffLineOrderService offLineOrderService;
    @Inject
    private ChannelRefundRequestRepository requestRepository;

    /**
     * 某商户某日退款单 - 记录
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<ChannelRefundOrder> findDailyRefundOrderByMerchant(String merchantId, String tradeDate) {
        return repository.findByMerchantIdAndTradeDateAndState(merchantId, tradeDate, 2);
    }

    /**
     * 某商户某日退款单 - 退款总额
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Long sumDailyTotalRefund(String merchantId, String tradeDate) {
        return repository.sumDailyTotalRefund(merchantId, tradeDate);
    }

    /**
     * 根据类型和时间统计商户退款单数
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Long countRefundByDateAndMerchantNum(String settleDate, String realMerNum, Integer orderType) {
        return repository.countRefundByDateAndMerchantNum(settleDate, realMerNum, orderType);
    }

    /***
     *  根据条件查询退款单记录
     *  Created by xf on 2017-07-14.
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Page<ChannelRefundOrder> findByCriteria(LedgerRefundOrderCriteria criteria, Integer limit) {
        Sort sort = new Sort(Sort.Direction.DESC, "dateCreated");
        return repository
            .findAll(getWhereClause(criteria), new PageRequest(criteria.getOffset() - 1, limit, sort));
    }

    public static Specification<ChannelRefundOrder> getWhereClause(
        LedgerRefundOrderCriteria criteria) {
        return new Specification<ChannelRefundOrder>() {
            @Override
            public Predicate toPredicate(Root<ChannelRefundOrder> root, CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                // 门店ID
                if (criteria.getMerchantId() != null && !"".equals(criteria.getMerchantId())) {
                    predicate.getExpressions().add(
                        cb.equal(root.get("merchantId"), criteria.getMerchantId()));
                }
                if (criteria.getState() != null) {
                    predicate.getExpressions().add(
                        cb.equal(root.get("state"), criteria.getState()));
                }
                if (criteria.getOrderType() != null) {
                    predicate.getExpressions().add(
                        cb.equal(root.get("orderType"), criteria.getOrderType()));
                }
                if (criteria.getPayType() != null) {
                    predicate.getExpressions().add(
                        cb.equal(root.get("payType"), criteria.getPayType()));
                }
                // 退款完成时间
                if (criteria.getTradeDate() != null && criteria.getTradeDate() != null) {
                    predicate.getExpressions().add(
                        cb.equal(root.get("tradeDate"), criteria.getTradeDate()));
                }
                return predicate;
            }
        };
    }


    /**
     * 发起退款申请
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Map<String, Object> createRefundRequest(String orderSid, Integer orderFrom) {
        ChannelRefundRequest refundRequest = requestRepository.findByOrderSid(orderSid);
        Map<String, Object> map = new HashMap<>();
        /*通道订单*/
        if (orderFrom == 1) {
            ScanCodeOrder codeOrder = scanCodeOrderService.findByOrderSid(orderSid);
            //  订单不存在
            if (codeOrder == null) {
                map.put("status", 400);
                map.put("msg", "此类订单暂不支持退款，请联系客服！");
                return map;
            }
            //  支付宝订单 (暂不支持)
            if (codeOrder.getScanCodeOrderExt().getPayType() == 1) {
                map.put("status", 400);
                map.put("msg", "此类订单暂不支持退款，请联系客服！");
                return map;
            }
            //  订单已申请退款
            if (refundRequest != null) {
                map.put("status", 400);
                map.put("msg", "此订单已申请退款，请勿重复申请 ^_^ ！");
                return map;
            } else {
                refundRequest = new ChannelRefundRequest();
                refundRequest.setOrderSid(orderSid);
                refundRequest.setOrderFrom(orderFrom);
                refundRequest.setOrderType(codeOrder.getScanCodeOrderExt().getBasicType());
                refundRequest.setLepayCode(codeOrder.getLePayCode());
                refundRequest.setMerchantId(codeOrder.getMerchant().getId());
                refundRequest.setMerhcantName(codeOrder.getMerchant().getName());
                refundRequest.setState(0);
                refundRequest.setDateCreated(new Date());
            }
        /*非通道订单*/
        } else if (orderFrom == 0) {
            OffLineOrder offLineOrder = offLineOrderService.findOffLineOrderByOrderSid(orderSid);
            //  订单不存在
            if (offLineOrder == null) {
                map.put("status", 400);
                map.put("msg", "此类订单暂不支持退款，请联系客服！");
                return map;
            }
            //  支付宝订单 (暂不支持)
            if (offLineOrder.getPayType() == 1) {
                map.put("status", 400);
                map.put("msg", "此类订单暂不支持退款，请联系客服！");
                return map;
            }
            //  订单已申请退款

            if (refundRequest != null) {
                map.put("status", 400);
                map.put("msg", "此订单已申请退款，请勿重复申请 ^_^ ！");
                return map;
            } else {
                refundRequest = new ChannelRefundRequest();
                refundRequest.setOrderSid(orderSid);
                refundRequest.setOrderFrom(orderFrom);
                refundRequest.setOrderType(offLineOrder.getBasicType());
                refundRequest.setLepayCode(offLineOrder.getLepayCode());
                refundRequest.setMerchantId(offLineOrder.getMerchant().getId());
                refundRequest.setMerhcantName(offLineOrder.getMerchant().getName());
                refundRequest.setState(0);
                refundRequest.setDateCreated(new Date());
            }
        } else {
            map.put("status", 400);
            map.put("msg", "此类订单暂不支持退款，请联系客服！");
            return map;
        }
        requestRepository.save(refundRequest);
        map.put("status", 200);
        map.put("msg", "退款已提交 ！");
        return map;
    }

}
