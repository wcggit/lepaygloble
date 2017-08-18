package com.jifenke.lepluslive.merchant.service;

import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.merchant.domain.criteria.MerchantCriteria;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUserResource;
import com.jifenke.lepluslive.merchant.repository.MerchantPosRepository;
import com.jifenke.lepluslive.merchant.repository.MerchantRepository;
import com.jifenke.lepluslive.merchant.repository.MerchantUserRepository;
import com.jifenke.lepluslive.merchant.repository.MerchantUserResourceRepository;
import com.jifenke.lepluslive.order.domain.entities.MerchantPos;
import com.jifenke.lepluslive.order.domain.entities.PosOrder;
import com.jifenke.lepluslive.order.repository.PosOrderRepository;
import com.jifenke.lepluslive.security.SecurityUtils;
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
    @Inject
    private MerchantPosRepository merchantPosRepository;
    @Inject
    private MerchantUserRepository merchantUserRepository;

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
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "completeDate");
        Sort sort = new Sort(order);
        PageRequest pagerequest = new PageRequest(posOrderCriteria.getCurrentPage() - 1, pageSize, sort);
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
                        cb.like(root.get("orderSid"),"%"+ posOrderCriteria.getOrderSid()+"%"));
                }
                if (posOrderCriteria.getStartDate() != null && !"".equals(posOrderCriteria.getStartDate())
                    && posOrderCriteria.getEndDate() != null && !"".equals(posOrderCriteria.getEndDate())) {
                    predicate.getExpressions().add(
                        cb.between(root.<Date>get("completeDate"),
                            new Date(posOrderCriteria.getStartDate()),
                            new Date(posOrderCriteria.getEndDate()))
                    );
                }
                if (posOrderCriteria.getState() != null) {
                    predicate.getExpressions().add(cb.equal(root.get("state"), posOrderCriteria.getState()));
                }
                return predicate;
            }
        };
        findPosOrderSum(posOrderCriteria);
        Page<PosOrder> page = posOrderRepository.findAll(specification, pagerequest);
        posOrderCriteria.setPage(page);
        return posOrderCriteria;
    }

    /**
     * 统计所选条件下的订单总金额、使用红包、刷卡实付、微信实付、支付宝实付、实际入账、红包
     *
     * @param posOrderCriteria
     */
    public void findPosOrderSum(PosOrderCriteria posOrderCriteria) {
        /**
         * 查询条件后的统计数据
         */
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT sum(o.total_price) as totalPrice,sum(o.true_score) as trueScore, sum(o.transfer_by_bank) as transferByBank , (sum(o.transfer_money)-sum(o.transfer_by_bank)) as transferByHB ,\n" +
            "(SELECT sum(a.true_pay) from pos_order a where a.trade_flag=0 and" + returnSqlFindCondition(posOrderCriteria, "a") + ") as zhifubao,\n" +
            "(SELECT sum(b.true_pay) from pos_order b where b.trade_flag=3 and" + returnSqlFindCondition(posOrderCriteria, "b") + ") as shuaka,\n" +
            "(SELECT sum(c.true_pay) from pos_order c where c.trade_flag=4 and" + returnSqlFindCondition(posOrderCriteria, "c") + ") as weixin\n" +
            "from pos_order as o where" + returnSqlFindCondition(posOrderCriteria, "o"));
        Query query = em.createNativeQuery(sql.toString());
        List<Object[]> details = query.getResultList();
        posOrderCriteria.setTotalPrice(Double.valueOf(details.get(0)[0] == null ? "0.00" : details.get(0)[0].toString()));
        posOrderCriteria.setTrueScore(Double.valueOf(details.get(0)[1] == null ? "0.00" : details.get(0)[1].toString()));
        posOrderCriteria.setTransferByBank(Double.valueOf(details.get(0)[2] == null ? "0.00" : details.get(0)[2].toString()));
        posOrderCriteria.setTransferByHB(Double.valueOf(details.get(0)[3] == null ? "0.00" : details.get(0)[3].toString()));
        posOrderCriteria.setZhifubao(Double.valueOf(details.get(0)[4] == null ? "0.00" : details.get(0)[4].toString()));
        posOrderCriteria.setShuaka(Double.valueOf(details.get(0)[5] == null ? "0.00" : details.get(0)[5].toString()));
        posOrderCriteria.setWeixin(Double.valueOf(details.get(0)[6] == null ? "0.00" : details.get(0)[6].toString()));
    }

    /**
     * 根据传送进来的别名不同，生成不同的别名的查询条件
     *
     * @param posOrderCriteria
     * @param asType           sql别名
     * @return
     */
    public String returnSqlFindCondition(PosOrderCriteria posOrderCriteria, String asType) {
        StringBuffer sb = new StringBuffer();
        if (posOrderCriteria.getStoreIds() != null) {
            sb.append(" " + asType + ".merchant_id in (");
            for (int i = 0; i < posOrderCriteria.getStoreIds().length; i++) {
                sb.append(posOrderCriteria.getStoreIds()[i] + ",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(")");
        }
        if (posOrderCriteria.getRebateWay() != null) {
            sb.append(" and " + asType + ".rebate_way = ");
            sb.append(posOrderCriteria.getRebateWay());
        }
        if (posOrderCriteria.getTradeFlag() != null) {
            sb.append(" and " + asType + ".trade_flag = ");
            sb.append(posOrderCriteria.getTradeFlag());
        }
        if (posOrderCriteria.getMerchantPosId() != null) {
            sb.append(" and " + asType + ".merchant_pos_id = ");
            sb.append(posOrderCriteria.getMerchantPosId());
        }
        if (posOrderCriteria.getOrderSid() != null && !"".equals(posOrderCriteria.getOrderSid())) {
            sb.append(" and " + asType + ".order_sid=");
            sb.append(posOrderCriteria.getOrderSid());
        }
        if (posOrderCriteria.getStartDate() != null && !"".equals(posOrderCriteria.getStartDate())
            && posOrderCriteria.getEndDate() != null && !"".equals(posOrderCriteria.getEndDate())) {
            sb.append(" and " + asType + ".complete_date between '");
            sb.append(posOrderCriteria.getStartDate());
            sb.append("' and '");
            sb.append(posOrderCriteria.getEndDate());
            sb.append("'");
        }
        if (posOrderCriteria.getState() != null) {
            sb.append(" and " + asType + ".state=");
            sb.append(posOrderCriteria.getState());
        }
        return sb.toString();
    }

    /**
     * 根据商户ID查询商户旗下门店信息   sql语句查询
     *
     * @param merchantName 商户id
     * @return 商户旗下门店信息
     * <p>
     * <p>
     * 万俊
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Object[]> findMerchantsByMerchantUserSql(String merchantName) {
        return merchantUserResourceRepository.findByMerchantInfoUser(merchantName);
    }

    /**
     * 根据商户ID查询商户旗下所有门店的所有pos机
     *
     * @param merchantName
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Object> findByMerchantPosUser(String merchantName) {
        return merchantUserResourceRepository.findByMerchantPosUser(merchantName);
    }

    /**
     * 根据商户信息查询商户旗下门店所有pos机信息
     *
     * @param posOrderCriteria
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PosOrderCriteria findPosInfoByMerchantUser(PosOrderCriteria posOrderCriteria) {
        int pageSize = 10;
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "createdDate");
        Sort sort = new Sort(order);
        PageRequest pagerequest = new PageRequest(posOrderCriteria.getCurrentPage() - 1, pageSize, sort);
        Specification<MerchantPos> specification = new Specification<MerchantPos>() {

            @Override
            public Predicate toPredicate(Root<MerchantPos> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                predicate.getExpressions().add(root.get("id").in(posOrderCriteria.getMerchantPosIds()));
                return predicate;
            }
        };
        Page<MerchantPos> page = merchantPosRepository.findAll(specification, pagerequest);
        posOrderCriteria.setPosPage(page);
        return posOrderCriteria;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public MerchantCriteria pageFindMerchantInfoByMerchantUser(MerchantCriteria merchantCriteria) {
        MerchantUser merchantUser = merchantUserRepository.findMerchantUserByMerchantSid(SecurityUtils.getCurrentUserLogin()).get();
        List<Object[]> list = findMerchantsByMerchantUserSql(merchantUser.getName());
        List<Object> mlist = new ArrayList<Object>();
        for (int i = 0; i < list.size(); i++) {
            mlist.add(list.get(i)[0]);
        }
        List<Object[]> aa = merchantUserResourceRepository.pageFindMerchantInfoByMerchantUser(mlist, (merchantCriteria.getCurrentPage() - 1) * 10);
        merchantCriteria.setmList(aa);
        merchantCriteria.setTotalPages(mlist.size() / 10);
        return merchantCriteria;
    }
}
