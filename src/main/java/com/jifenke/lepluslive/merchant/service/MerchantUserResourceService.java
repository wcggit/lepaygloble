package com.jifenke.lepluslive.merchant.service;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUserResource;
import com.jifenke.lepluslive.merchant.repository.MerchantRepository;
import com.jifenke.lepluslive.merchant.repository.MerchantUserResourceRepository;
import com.jifenke.lepluslive.order.domain.entities.PosOrder;
import com.jifenke.lepluslive.order.repository.PosOrderRepository;
import net.sf.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.jifenke.lepluslive.merchant.domain.criteria.PosOrderCriteria;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xf on 16-12-2.
 */
@Service
@Transactional(readOnly = true)
public class MerchantUserResourceService {

    @Inject
    private MerchantRepository merchantRepository;
    @Inject
    private MerchantUserResourceRepository merchantUserResourceRepository;
    @Inject
    private PosOrderRepository posOrderRepository;
    @Inject
    private EntityManager em;

    /***
     *  根据商户找旗下的门店
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Merchant> findMerchantsByMerchantUser(MerchantUser merchantUser) {
        List<Merchant> merchants = new ArrayList<>();
        List<MerchantUserResource> merchantUserResource = merchantUserResourceRepository.findByMerchantUser(merchantUser.getId());
        if (merchantUserResource != null && merchantUserResource.size() > 0) {
            for (MerchantUserResource userResource : merchantUserResource) {
                Long merchantId = userResource.getLeJiaResource().getResourceId();
                Merchant merchant = merchantRepository.findOne(merchantId);
                merchants.add(merchant);
            }
        }
        return merchants;
    }

    /**
     * 根据商户ID查找旗下门店订单信息
     *
     * @param posOrderCriteria 商户信息
     * @return 商户旗下门店订单信息
     * <p>
     * <p>
     * 万俊
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PosOrderCriteria findPosOrderByMerchantUser(PosOrderCriteria posOrderCriteria) {
        int pageSize = 10;
        Sort.Order order =  new Sort.Order(Sort.Direction.DESC,"completeDate");
        Sort sort =  new Sort(order);
        PageRequest pagerequest = new PageRequest(posOrderCriteria.getCurrentPage()-1, pageSize,sort);
        Specification<PosOrder> specification = new Specification<PosOrder>() {
            @Override
            public Predicate toPredicate(Root<PosOrder> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (posOrderCriteria.getStoreIds() != null) {
                    predicate.getExpressions().add(
                        root.get("merchant").get("id").in(posOrderCriteria.getStoreIds()));
                }
                    if (posOrderCriteria.getRebateWay() != null) {
                        predicate.getExpressions().add(
                            cb.equal(root.get("rebateWay"), posOrderCriteria.getRebateWay()));
                    }
                    if (posOrderCriteria.getTradeFlag() != null) {
                        predicate.getExpressions().add(
                            cb.equal(root.get("tradeFlag"), posOrderCriteria.getTradeFlag()));
                    }
                    if (posOrderCriteria.getMerchantPosId() != null) {
                        predicate.getExpressions().add(
                            cb.equal(root.get("merchantPos").get("id"), posOrderCriteria.getMerchantPosId()));
                    }
                    if (posOrderCriteria.getOrderSid() != null && !"".equals(posOrderCriteria.getOrderSid())) {
                        predicate.getExpressions().add(
                            cb.equal(root.get("orderSid"), posOrderCriteria.getOrderSid()));
                    }
                    if (posOrderCriteria.getStartDate() != null && !"".equals(posOrderCriteria.getStartDate())
                        && posOrderCriteria.getEndDate() != null && !"".equals(posOrderCriteria.getEndDate())) {
                        predicate.getExpressions().add(
                            cb.between(root.<Date>get("completeDate"),
                                new Date(posOrderCriteria.getStartDate()),
                                new Date(posOrderCriteria.getEndDate()))
                        );
                    }
                    if(posOrderCriteria.getState()!=null){
                        predicate.getExpressions().add(cb.equal(root.get("state"),posOrderCriteria.getState()));
                    }
                    return predicate;
            }
        };
        /**
         * 查询条件后的统计数据
         */
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT count(o.total_price) as totalPrice,count(o.true_score) as trueScore, count(o.transfer_by_bank) as transferByBank , (count(o.transfer_money)-count(o.transfer_by_bank)) as transferByHB ,\n" +
            "(SELECT count(a.true_pay) from pos_order a where a.trade_flag=0) as zhifubao,\n" +
            "(SELECT count(b.true_pay) from pos_order b where b.trade_flag=3) as shuaka,\n" +
            "(SELECT count(c.true_pay) from pos_order c where c.trade_flag=4) as weixin\n" +
            "from pos_order as o where");
        if (posOrderCriteria.getStoreIds() != null) {
            sb.append(" o.merchant_id in (");
            for(int i =0;i<posOrderCriteria.getStoreIds().length;i++){
                sb.append(posOrderCriteria.getStoreIds()[i]+",");
            }
            sb.deleteCharAt(sb.length()-1);
            sb.append(")");
        }
        if (posOrderCriteria.getRebateWay() != null) {
            sb.append(" and o.rebate_way = ");
            sb.append(posOrderCriteria.getRebateWay());
        }
        if (posOrderCriteria.getTradeFlag() != null) {
            sb.append(" and o.trade_flag = ");
            sb.append(posOrderCriteria.getTradeFlag());
        }
        if (posOrderCriteria.getMerchantPosId() != null) {
            sb.append(" and o.merchant_pos_id = ");
            sb.append(posOrderCriteria.getMerchantPosId());
        }
        if (posOrderCriteria.getOrderSid() != null && !"".equals(posOrderCriteria.getOrderSid())) {
            sb.append(" and o.order_sid=");
            sb.append(posOrderCriteria.getOrderSid());
        }
        if (posOrderCriteria.getStartDate() != null && !"".equals(posOrderCriteria.getStartDate())
            && posOrderCriteria.getEndDate() != null && !"".equals(posOrderCriteria.getEndDate())) {
            sb.append(" and o.complete_date between '");
            sb.append(posOrderCriteria.getStartDate());
            sb.append("' and '");
            sb.append(posOrderCriteria.getEndDate());
            sb.append("'");
        }
        if(posOrderCriteria.getState()!=null){
            sb.append(" and o.state=");
            sb.append(posOrderCriteria.getState());
        }
        Query query = em.createNativeQuery(sb.toString());

        List<Object[]> details = query.getResultList();
        posOrderCriteria.setTotalPrice(((BigInteger)details.get(0)[0]).doubleValue());
        posOrderCriteria.setTrueScore(((BigInteger)details.get(0)[1]).doubleValue());
        posOrderCriteria.setTransferByBank(((BigInteger)details.get(0)[2]).doubleValue());
        posOrderCriteria.setTransferByHB(((BigInteger)details.get(0)[3]).doubleValue());
        posOrderCriteria.setZhifubao(((BigInteger)details.get(0)[4]).doubleValue());
        posOrderCriteria.setShuaka(((BigInteger)details.get(0)[5]).doubleValue());
        posOrderCriteria.setWeixin(((BigInteger)details.get(0)[6]).doubleValue());
        Page<PosOrder> page = posOrderRepository.findAll(specification,pagerequest);
        posOrderCriteria.setPage(page);
        return posOrderCriteria;
        }
        /**
         * 根据商户ID查询商户旗下门店信息   sql语句查询
         * @param merchantName 商户id
         * @return 商户旗下门店信息
         *
         *
         * 万俊
         */
        @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        public List<Object> findMerchantsByMerchantUserSql (String merchantName){
            return merchantUserResourceRepository.findByMerchantInfoUser(merchantName);
        }

    /**
     * 根据商户ID查询商户旗下所有门店的所有pos机
     * @param merchantName
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        public  List<Object> findByMerchantPosUser(String merchantName){
            return merchantUserResourceRepository.findByMerchantPosUser(merchantName);
        }
}
