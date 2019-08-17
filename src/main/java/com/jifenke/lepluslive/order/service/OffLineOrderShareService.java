package com.jifenke.lepluslive.order.service;

import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.order.controller.dto.OffShareDto;
import com.jifenke.lepluslive.order.domain.criteria.ShareCriteria;
import com.jifenke.lepluslive.order.domain.entities.OffLineOrderShare;
import com.jifenke.lepluslive.order.repository.OffLineOrderShareRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.Predicate;
import java.util.*;

/**
 * Created by wanjun on 2016/12/15.
 */
@Service
@Transactional(readOnly = true)
public class OffLineOrderShareService {
    @Inject
    private OffLineOrderShareRepository repository;

    @Inject
    private LeJiaUserService userService;

    @Inject
    private EntityManager entityManager;

    public Map<String, Object> findShareByPage(ShareCriteria criteria) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Page<OffLineOrderShare> page = repository.findAll(getWhereClause(criteria), new PageRequest(criteria.getOffset() - 1, criteria.getSize(), sort));
        Map<String, Object> result = new HashMap<>(4);
        result.put("totalElements", page.getTotalElements());
        result.put("totalPages", page.getTotalPages());
        result.put("content", convert(page.getContent()));
        result.put("count", countWhereClause(criteria));
        return result;
    }

    private List<OffShareDto> convert(List<OffLineOrderShare> list) {
        List<OffShareDto> result = new ArrayList<>();
        for (OffLineOrderShare share : list) {
            OffShareDto dto = new OffShareDto();
            dto.parseShareInfo(share);
            dto.parseOrderNumber(share);
            LeJiaUser leJiaUser = userService.findUserById(share.getUserId());
            if (share.getUserId() != null && leJiaUser != null) {
                dto.setUser(OffShareDto.userParse(userService.findUserById(share.getUserId())));
                if (leJiaUser.getWeiXinUser() != null) {
                    dto.setImgUrl(leJiaUser.getWeiXinUser().getHeadImageUrl());
                }
                if (leJiaUser.getWeiXinUser() != null) {
                    dto.setNickName(leJiaUser.getWeiXinUser().getNickname());
                }
            }
            result.add(dto);
        }
        return result;
    }


    /**
     * 查询 线下 佣金明细count
     *
     * @param
     * @return
     */
    private Object countWhereClause(ShareCriteria criteria) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(*),IFNULL(SUM(s.to_lock_merchant),0) FROM off_line_order_share s WHERE 1 = 1");
        if (StringUtils.isNotEmpty(criteria.getStartDate()) && StringUtils.isNotEmpty(criteria.getEndDate())) {
            sql.append(" AND s.create_date BETWEEN '" + criteria.getStartDate().replaceAll("/","-") + "' AND '" + criteria.getEndDate().replaceAll("/","-") + "'");
        }
        if (criteria.getLockMerchantId() != null) {
            sql.append(" AND s.lock_merchant_id = ").append(criteria.getLockMerchantId());
        }
        return entityManager.createNativeQuery(sql.toString()).getSingleResult();
    }


    /**
     * 查询 线下 佣金明细
     *
     * @param
     * @return
     */
    private Specification<OffLineOrderShare> getWhereClause(ShareCriteria criteria) {
        return (root, query, cb) -> {
            Predicate p = cb.conjunction();
            if (StringUtils.isNotEmpty(criteria.getStartDate()) && StringUtils.isNotEmpty(criteria.getEndDate())) {
                    Date start = new Date(criteria.getStartDate());
                    Date end = new Date(criteria.getEndDate());
                    p.getExpressions().add(cb.between(root.get("createDate"),start,end));
            }
            if (criteria.getLockMerchantId() != null) {
                p.getExpressions().add(cb.equal(root.<Merchant>get("lockMerchant").get("id"), criteria.getLockMerchantId()));
            }
            return p;
        };
    }

    /**
     * 查询今日 佣金收入、今日佣金单数
     *
     * @param objects 门店ids
     * @return
     */
    public Object findTodayCommissionAndTodayNumber(List<Object> objects) {
        Object obj = repository.findTodayCommissionAndTodayNumber(objects);
        System.out.print(obj);
        return obj;
    }
}
