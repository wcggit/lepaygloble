package com.jifenke.lepluslive.merchant.service;

import com.jifenke.lepluslive.global.util.MD5Util;
import com.jifenke.lepluslive.lejiauser.repository.LeJiaUserRepository;
import com.jifenke.lepluslive.merchant.domain.criteria.MerchantUserCriteria;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUserResource;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUserShop;
import com.jifenke.lepluslive.merchant.repository.MerchantRepository;
import com.jifenke.lepluslive.merchant.repository.MerchantUserRepository;
import com.jifenke.lepluslive.merchant.repository.MerchantUserResourceRepository;
import com.jifenke.lepluslive.merchant.repository.MerchantUserShopRepository;
import com.jifenke.lepluslive.security.SecurityUtils;
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
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wcg on 16/6/27.
 */
@Service
@Transactional(readOnly = true)
public class MerchantUserService {

    @Inject
    private MerchantUserRepository merchantUserRepository;
    @Inject
    private MerchantUserResourceRepository merchantUserResourceRepository;
    @Inject
    private MerchantRepository merchantRepository;
    @Inject
    private LeJiaUserRepository lejiaUserRepository;
    @Inject
    private MerchantUserShopRepository merchantUserShopRepository;

    public MerchantUser getUserWithAuthorities() {
        return  merchantUserRepository.findMerchantUserByMerchantSid(SecurityUtils.getCurrentUserLogin()).get();
    }


    @Transactional(propagation = Propagation.REQUIRED,readOnly = true)
    public Map getLockerInfoByMerchant(String merchantName) {
        //1.根据商户loginName查询旗下所有门店id
        List<Object[]> list = merchantUserResourceRepository.findByMerchantInfoUser(merchantName);
        List<Object> mlist = new ArrayList<Object>();
        for (int i = 0; i < list.size(); i++) {
            mlist.add(list.get(i)[0]);
        }
        //2.统计锁定会员总数
        Integer totalCount = merchantRepository.findMerchantTotalMember(mlist);
        //3.统计今日锁定会员数
        Integer dailyCount = merchantRepository.findMerchantDailyMember(mlist);
        // 4.统计总共发放的红包和积分
        Long totalScorea = 0L;
        Long totalScoreb = 0L;
        List members = merchantRepository.findMerchantMemberIds(mlist);
        for (Object m : members) {
            Long id = new Long(m.toString());
            Long scorea = lejiaUserRepository.findTotalScorea(id);
            Long scoreb = lejiaUserRepository.findTotalScoreb(id);
            totalScorea += (scorea==null?0:scorea);
            totalScoreb += (scoreb==null?0:scoreb);
        }
        Map map = new HashMap();
        map.put("totalCount",totalCount);
        map.put("dailyCount",dailyCount);
        map.put("totalScorea",totalScorea);
        map.put("totalScoreb",totalScoreb);
        return map;
    }

    /**
     *  获取商户下的店主账号
     */
    @Transactional(propagation = Propagation.REQUIRED,readOnly = true)
    public List<MerchantUser> findOwerAccount(Long merchnatUserId) {
        return merchantUserRepository.findOwerAccount(merchnatUserId);
    }

    /**
     *  获取商户下的收银员账号
     */
    @Transactional(propagation = Propagation.REQUIRED,readOnly = true)
    public List<MerchantUser> findCashierAccount(Long merchantUserId) {
        return merchantUserRepository.findCashierAccount(merchantUserId);
    }

    /**
     *  获取商户下所有账号
     */
    @Transactional(propagation = Propagation.REQUIRED,readOnly = true)
    public List<MerchantUser> findUserAccount(Long merchantUserId) {
        return merchantUserRepository.findUserAccount(merchantUserId);
    }

    /**
     *  根据用户名查询商户
     */
    @Transactional(propagation = Propagation.REQUIRED,readOnly = true)
    public MerchantUser findByName(String username) {
        return merchantUserRepository.findByUserName(username);
    }
    @Transactional(propagation = Propagation.REQUIRED,readOnly = true)
    public MerchantUser findByMerchantSid(String sid) {
        return merchantUserRepository.findByMerchantSid(sid);
    }

    /**
     *  修改商户账号密码 17/05/08
     *  merchantUser 当前登录商户
     *  oldPwd 原密码
     *  newPwd 新密码
     */
    @Transactional(readOnly = false,propagation = Propagation.REQUIRED)
    public boolean updatePwd(MerchantUser merchantUser,String oldPwd, String newPwd) {
        MerchantUser manager = merchantUserRepository.findOne(merchantUser.getCreateUserId());
        String managerPwd = manager.getPassword();
        if(managerPwd.equals(MD5Util.MD5Encode(oldPwd,null))) {
            merchantUser.setPassword(MD5Util.MD5Encode(newPwd,null));
            merchantUserRepository.save(merchantUser);
            return true;
        }else {
            return false;
        }
    }

    /**
     *  账号解绑
     */
    @Transactional(readOnly = false,propagation = Propagation.REQUIRED)
    public boolean unbindAccount(MerchantUser merchantUser) {
        if(merchantUser.getType()==8) {
            return false;
        }
        // 清空PC平台关联
        List<MerchantUserResource> merchantUserResources = merchantUserResourceRepository.findByMerchantUser(merchantUser.getId());
        if(merchantUserResources!=null&&merchantUserResources.size()>0) {
            for (MerchantUserResource merchantUserResource : merchantUserResources) {
                merchantUserResourceRepository.delete(merchantUserResource);
            }
        }
        // 清空公众号关联
        List<MerchantUserShop> userShops = merchantUserShopRepository.findByMerchantUser(merchantUser);
        if(userShops!=null&&userShops.size()>0) {
            for (MerchantUserShop userShop : userShops) {
                merchantUserShopRepository.delete(userShop);
            }
        }
        // 解除商户关系
        merchantUser.setCreateUserId(merchantUser.getId());
        merchantUserRepository.save(merchantUser);
        return true;
    }
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Page findMerchantUserByPage(MerchantUserCriteria criteria, Integer limit) {
        Sort sort = new Sort(Sort.Direction.DESC, "createdDate");
        return merchantUserRepository
            .findAll(getWhereClause(criteria),
                new PageRequest(criteria.getOffset() - 1, limit, sort));
    }

    public static Specification<MerchantUser> getWhereClause(final MerchantUserCriteria criteria) {
        return new Specification<MerchantUser>() {
            @Override
            public Predicate toPredicate(Root<MerchantUser> r, CriteriaQuery<?> q,
                                         CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (criteria.getMerchantName() != null&&!"".equals(criteria.getMerchantName())) {
                    predicate.getExpressions().add(
                        cb.like(r.get("merchantName"), "%"+criteria.getMerchantName()+"%"));
                }
                if(criteria.getPhoneNum()!=null&&!"".equals(criteria.getPhoneNum())){
                    predicate.getExpressions().add(
                        cb.like(r.get("phoneNum"), "%"+criteria.getPhoneNum()+"%"));
                }
                if(criteria.getLinkMan()!=null&&!"".equals(criteria.getLinkMan())){
                    predicate.getExpressions().add(
                        cb.like(r.get("linkMan"), "%"+criteria.getLinkMan()+"%"));
                }
                if(criteria.getType()!=null) {
                    predicate.getExpressions().add(
                        cb.equal(r.get("type"),criteria.getType()));
                }
                if(criteria.getPartner()!=null) {
                    predicate.getExpressions().add(
                        cb.equal(r.get("partner"),criteria.getPartner()));
                }
                if(criteria.getStartDate()!=null&&!"".equals(criteria.getStartDate())) {
                    try{
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date start =  sdf.parse(criteria.getStartDate());
                        Date end = sdf.parse(criteria.getEndDate());
                        predicate.getExpressions().add(cb.between(r.get("createdDate"),start,end));
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return predicate;
            }
        };
    }
}
