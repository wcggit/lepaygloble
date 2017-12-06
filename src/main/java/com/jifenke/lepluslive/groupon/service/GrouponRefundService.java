package com.jifenke.lepluslive.groupon.service;

import com.jifenke.lepluslive.groupon.domain.entities.GrouponCode;
import com.jifenke.lepluslive.groupon.domain.entities.GrouponOrder;
import com.jifenke.lepluslive.groupon.domain.entities.GrouponRefund;
import com.jifenke.lepluslive.groupon.repository.GrouponCodeRepository;
import com.jifenke.lepluslive.groupon.repository.GrouponOrderRepository;
import com.jifenke.lepluslive.groupon.repository.GrouponRefundRepository;
import com.jifenke.lepluslive.score.domain.entities.ScoreA;
import com.jifenke.lepluslive.score.domain.entities.ScoreADetail;
import com.jifenke.lepluslive.score.domain.entities.ScoreC;
import com.jifenke.lepluslive.score.domain.entities.ScoreCDetail;
import com.jifenke.lepluslive.score.repository.ScoreADetailRepository;
import com.jifenke.lepluslive.score.repository.ScoreCDetailRepository;
import com.jifenke.lepluslive.score.service.ScoreAService;
import com.jifenke.lepluslive.score.service.ScoreCService;
import com.jifenke.lepluslive.user.domain.entities.LeJiaUser;
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
import java.util.List;

/**
 * 退款管理 Service
 *
 * @author XF
 * @date 2017/6/20
 */
@Service
@Transactional(readOnly = true)
public class GrouponRefundService {
    @Inject
    private GrouponRefundRepository grouponRefundRepository;
    @Inject
    private GrouponOrderRepository grouponOrderRepository;
    @Inject
    private GrouponCodeRepository grouponCodeRepository;
    @Inject
    private ScoreAService scoreAService;
    @Inject
    private ScoreCService scoreCService;
    @Inject
    private ScoreCDetailRepository scoreCDetailRepository;
    @Inject
    private ScoreADetailRepository scoreADetailRepository;
    /***
     *  根据条件查询退款单
     *  Created by xf on 2017-06-22.
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Page<GrouponRefund> findByCriteria(GrouponRefundCriteria criteria, Integer limit) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        return grouponRefundRepository.findAll(getWhereClause(criteria), new PageRequest(criteria.getOffset() - 1, limit, sort));
    }

    public static Specification<GrouponRefund> getWhereClause(GrouponRefundCriteria criteria) {
        return new Specification<GrouponRefund>() {
            @Override
            public Predicate toPredicate(Root<GrouponRefund> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                //  订单编号
                if(criteria.getOrderSid()!=null&&!"".equals(criteria.getOrderSid())) {
                    predicate.getExpressions().add(
                            cb.like(root.get("grouponOrder").get("orderSid"), "%" + criteria.getOrderSid() + "%"));
                }
                // 团购SID
                if (criteria.getProductSid() != null&&!"".equals(criteria.getProductSid())) {
                    predicate.getExpressions().add(
                            cb.like(root.get("grouponOrder").get("grouponProduct").get("sid"), "%" + criteria.getProductSid() + "%"));
                }
                // 团购名称
                if (criteria.getProductName() != null&&!"".equals(criteria.getProductSid())) {
                    predicate.getExpressions().add(
                            cb.like(root.get("grouponOrder").get("grouponProduct").get("name"), "%" + criteria.getProductName() + "%"));
                }
                //  订单类型  0 普通订单 1 乐加订单
                if(criteria.getOrderType()!=null) {
                    predicate.getExpressions().add(
                            cb.equal(root.get("grouponOrder").get("orderType"), criteria.getOrderType()));
                }
                // 订单状态  0 退款中 1 退款完成 2 退款驳回
                if (criteria.getState()!= null) {
                    predicate.getExpressions().add(
                            cb.equal(root.get("state"), criteria.getState()));
                }
                // 创建时间
                if(criteria.getStartDate()!=null && !"".equals(criteria.getStartDate())) {
                    Date startDate = new Date(criteria.getStartDate());
                    Date endDate = new Date(criteria.getEndDate());
                    predicate.getExpressions().add(
                            cb.between(root.get("createDate"),startDate,endDate));
                }
                return predicate;
            }
        };
    }

    /***
     *  设为已退款
     *  Created by xf on 2017-09-17.
     */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public boolean updateRefundOrder(GrouponRefund grouponRefund) {
        try{
            GrouponRefund refund = grouponRefundRepository.findOne(grouponRefund.getId());
            Integer num = refund.getRefundNum();              // 退款数量
            Integer index = 0;
            GrouponOrder grouponOrder = refund.getGrouponOrder();
            List<GrouponCode> grouponCodes = grouponOrder.getGrouponCodes();
            for (GrouponCode grouponCode : grouponCodes) {
                Integer state = grouponCode.getState();
                if(index<num) {
                    if(state==0||state==2) {
                        grouponCode.setState(3);         // 将券码设为已退款
                        grouponCodeRepository.save(grouponCode);
                        index+=1;
                    }
                }
            }
            //  重新计算鼓励金、金币
            LeJiaUser ljUser = grouponOrder.getLeJiaUser();
            Date changeDate = new Date();
            ScoreA scoreA = scoreAService.findScoreAByWeiXinUser(ljUser);
            ScoreC scoreC = scoreCService.findScoreCByWeiXinUser(ljUser);
            Long scorea = scoreA.getScore();
            Long scorec = scoreC.getScore();
            scorea += grouponOrder.getScorea();         // 退回购买时使用的鼓励金
            scorea -= grouponOrder.getRebateScorea();   // 收回购买时获得的鼓励金
            scorec -= grouponOrder.getRebateScorec();   // 收回购买时获得的金币
            scoreA.setScore(scorea);
            scoreA.setLastUpdateDate(changeDate);
            scoreC.setScore(scorec);
            scoreC.setLastUpdateDate(changeDate);
            ScoreADetail scoreADetail = new ScoreADetail();        // 鼓励金变更日志
            scoreADetail.setDateCreated(changeDate);
            scoreADetail.setOrderSid(grouponOrder.getOrderSid());
            scoreADetail.setScoreA(scoreA);
            scoreADetail.setNumber(grouponOrder.getScorea()-grouponOrder.getRebateScorea());
            scoreADetail.setOrigin(19015);
            scoreADetail.setOperate("团购退款");
            ScoreCDetail scoreCDetail = new ScoreCDetail();       // 金币变更日志
            scoreCDetail.setDateCreated(changeDate);
            scoreCDetail.setOrderSid(grouponOrder.getOrderSid());
            scoreCDetail.setScoreC(scoreC);
            scoreCDetail.setNumber(0L-grouponOrder.getRebateScorec());
            scoreCDetail.setOrigin(20016);
            scoreCDetail.setOperate("团购退款");
            scoreAService.saveScore(scoreA);
            scoreCService.saveScore(scoreC);
            scoreCDetailRepository.save(scoreCDetail);
            scoreADetailRepository.save(scoreADetail);
            if(num==grouponCodes.size()) {
                grouponOrder.setOrderState(2);          // 将订单设为已退款
                grouponOrderRepository.save(grouponOrder);
            }
            refund.setState(1);                         // 将退款单状态设为退款完成
            refund.setCompleteDate(new Date());
            grouponRefundRepository.save(refund);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
