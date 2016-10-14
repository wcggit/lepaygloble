package com.jifenke.lepluslive.weixin.service;

import com.jifenke.lepluslive.lejiauser.repository.LeJiaUserRepository;
import com.jifenke.lepluslive.partner.domain.criteria.WeiXinUserInfoCriteria;
import com.jifenke.lepluslive.partner.domain.entities.PartnerScoreLog;
import com.jifenke.lepluslive.partner.repository.PartnerScoreLogRepository;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.repository.WeiXinUserRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created by wcg on 16/3/18.
 */
@Service
@Transactional(readOnly = true)
public class WeiXinUserService {

    @Value("${bucket.ossBarCodeReadRoot}")
    private String barCodeRootUrl;


    @Inject
    private WeiXinUserRepository weiXinUserRepository;

    @Inject
    private LeJiaUserRepository leJiaUserRepository;


    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public WeiXinUser findWeiXinUserByOpenId(String openId) {
        return weiXinUserRepository.findByOpenId(openId);
    }

    /**
     * 查询合伙人-邀请关注用户-关注红包/积分
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Map findPageByInfoCriteria(WeiXinUserInfoCriteria infoCriteria, Integer limit) {
        //  分页查询数据
        Sort sort = new Sort(Sort.Direction.DESC, "dateCreated");
        Page page = weiXinUserRepository.findAll(getWhereClause(infoCriteria),
                                                 new PageRequest(infoCriteria.getOffset() - 1,
                                                                 limit, sort));
        List<WeiXinUser> weixinUsers = page.getContent();
        List<Long> wxScoreas = new ArrayList<>();
        List<Long> wxScorebs = new ArrayList<>();
        for (WeiXinUser weixinUser : weixinUsers) {
            Long scorea = leJiaUserRepository.findTotalScorea(weixinUser.getLeJiaUser().getId());
            wxScoreas.add(scorea);
            Long scoreb = leJiaUserRepository.findTotalScoreb(weixinUser.getLeJiaUser().getId());
            wxScorebs.add(scoreb);
        }
        //  封装数据
        Map map = new HashMap();
        map.put("page", page);
        map.put("wxScoreas", wxScoreas);
        map.put("wxScorebs", wxScorebs);
        return map;
    }

    public static Specification<WeiXinUser> getWhereClause(WeiXinUserInfoCriteria infoCriteria) {
        return new Specification<WeiXinUser>() {
            @Override
            public Predicate toPredicate(Root<WeiXinUser> root, CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (infoCriteria.getSubSource() != null) {
                    predicate.getExpressions()
                        .add(cb.equal(root.get("subSource"), infoCriteria.getSubSource()));
                }
                return predicate;
            }
        };
    }

}
