package com.jifenke.lepluslive.groupon.service;

import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.groupon.controller.dto.GrouponProductDto;
import com.jifenke.lepluslive.groupon.domain.entities.GrouponMerchant;
import com.jifenke.lepluslive.groupon.domain.entities.GrouponProduct;
import com.jifenke.lepluslive.groupon.domain.entities.GrouponProductDetail;
import com.jifenke.lepluslive.groupon.domain.entities.GrouponScrollPicture;
import com.jifenke.lepluslive.groupon.repository.GrouponMerchantRepository;
import com.jifenke.lepluslive.groupon.repository.GrouponProductDetailRepository;
import com.jifenke.lepluslive.groupon.repository.GrouponProductRepository;
import com.jifenke.lepluslive.groupon.repository.GrouponScrollPictureRepository;
import com.jifenke.lepluslive.groupon.util.GrouponParameter;
import com.jifenke.lepluslive.groupon.util.MathUtil;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.repository.MerchantRepository;
import com.jifenke.lepluslive.merchant.repository.MerchantUserRepository;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 团购产品 Service
 * Created by xf on 17-6-16.
 */
@Service
@Transactional(readOnly = true)
public class GrouponProductService {
    @Inject
    private GrouponProductRepository grouponProductRepository;
    @Inject
    private MerchantUserRepository merchantUserRepository;
    @Inject
    private MerchantRepository merchantRepository;
    @Inject
    private GrouponMerchantRepository grouponMerchantRepository;
    @Inject
    private GrouponProductDetailRepository grouponProductDetailRepository;
    @Inject
    private GrouponScrollPictureRepository grouponScrollPictureRepository;

    /***
     *  根据条件查询团购产品
     *  Created by xf on 2017-06-16.
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Page<GrouponProduct> findByCriteria(GrouponProductCriteria criteria, Integer limit) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        return grouponProductRepository.findAll(getWhereClause(criteria), new PageRequest(criteria.getOffset() - 1, limit, sort));
    }

    public static Specification<GrouponProduct> getWhereClause(GrouponProductCriteria criteria) {
        return new Specification<GrouponProduct>() {
            @Override
            public Predicate toPredicate(Root<GrouponProduct> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (criteria.getMerchantUser() != null) {
                    // 商户
                    if (criteria.getMerchantUser().matches("^\\d{1,6}$")) {
                        predicate.getExpressions().add(
                                cb.equal(root.<Merchant>get("merchantUser").get("id"), criteria.getMerchantUser()));
                    } else {
                        predicate.getExpressions().add(
                                cb.like(root.<Merchant>get("merchantUser").get("name"),
                                        "%" + criteria.getMerchantUser() + "%"));
                    }
                }
                // 团购SID
                if (criteria.getSid() != null) {
                    predicate.getExpressions().add(
                            cb.equal(root.get("sid"), criteria.getSid()));
                }
                // 团购名称
                if (criteria.getName() != null) {
                    predicate.getExpressions().add(
                            cb.like(root.get("name"), "%" + criteria.getName() + "%"));
                }
                // 团购全部状态
                if (criteria.getState() != null) {
                    predicate.getExpressions().add(
                            cb.equal(root.get("state"), criteria.getState()));
                }

                return predicate;
            }
        };
    }

    /**
     * 统计团购产品下绑定门店数
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<Long> countMerchantByProducts(List<GrouponProduct> products) {
        List<Long> bindMerchants = new ArrayList<>();
        for (GrouponProduct product : products) {
            Long count = grouponMerchantRepository.countGrouponMerchantByGrouponProduct(product);
            bindMerchants.add(count);
        }
        return bindMerchants;
    }

    /***
     *  新建保存团购产品
     *  Created by xf on 2017-06-20.
     */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public boolean saveProduct(GrouponProductDto grouponProductDto) {
        try {
            //   保存产品
            GrouponProduct product = grouponProductDto.getGrouponProduct();
            product.setState(1);
            product.setCreateDate(new Date());
            product.setSid(MvUtil.getOrderNumber());
            MerchantUser merchantUser = merchantUserRepository.findOne(product.getMerchantUser().getId());
            product.setMerchantUser(merchantUser);
            //计算分润和返金币鼓励金
            GrouponParameter parameter = MathUtil.share(product.getLjCommission());
            product.setRebateScorea(parameter.getScoreA());
            product.setRebateScorec(parameter.getScoreC());
            product.setShareToLockMerchant(parameter.getLockMerchant());
            product.setShareToLockPartner(parameter.getLockPartner());
            product.setShareToLockPartnerManager(parameter.getLockPartnerManager());
            product.setShareToTradePartner(parameter.getTradePartner());
            product.setShareToTradePartnerManager(parameter.getTradePartnerManager());
            grouponProductRepository.save(product);
            //   保存产品门店对应关系
            List<Merchant> merchantList = grouponProductDto.getMerchantList();
            if (merchantList != null || merchantList.size() > 0) {
                for (Merchant merchant : merchantList) {
                    GrouponMerchant grouponMerchant = new GrouponMerchant();
                    Merchant existMerchant = merchantRepository.findOne(merchant.getId());
                    grouponMerchant.setMerchant(existMerchant);
                    grouponMerchant.setGrouponProduct(product);
                    grouponMerchantRepository.save(grouponMerchant);
                }
            }
            //   保存产品详情图
            List<GrouponProductDetail> detailList = grouponProductDto.getDelDetailList();
            for (int j = 0; j < detailList.size(); j++) {
                GrouponProductDetail grouponProductDetail = detailList.get(j);
                grouponProductDetail.setDescription(product.getDescription());
                grouponProductDetail.setSid(new Integer(j + 1));
                grouponProductDetail.setGrouponProduct(product);
                grouponProductDetailRepository.save(grouponProductDetail);
            }
            //   保存商品轮播图
            List<GrouponScrollPicture> scorePictureList = grouponProductDto.getDelScrollList();
            for (int i = 0; i < scorePictureList.size(); i++) {
                GrouponScrollPicture grouponScrollPicture = scorePictureList.get(i);
                grouponScrollPicture.setDescription(product.getDescription());
                grouponScrollPicture.setSid(new Integer(i + 1));
                grouponScrollPicture.setGrouponProduct(product);
                grouponScrollPictureRepository.save(grouponScrollPicture);
            }
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    /**
     * 根据 ID 查找商品
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public GrouponProduct findById(Long productId) {
        return grouponProductRepository.findOne(productId);
    }

    /***
     *  修改保存团购产品
     *  Created by xf on 2017-06-20.
     */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public boolean saveEdit(GrouponProductDto grouponProductDto) {
        try {
            //   修改产品
            GrouponProduct product = grouponProductDto.getGrouponProduct();
            GrouponProduct existProduct = grouponProductRepository.findOne(product.getId());
            if (product.getCharge() != null) {
                existProduct.setCharge(product.getCharge());
            }
            if (product.getDescription() != null) {
                existProduct.setDescription(product.getDescription());
            }
            if (product.getLjStorage() != null) {
                existProduct.setLjStorage(product.getLjStorage());
            }
            if (product.getNormalStorage() != null) {
                existProduct.setNormalStorage(product.getNormalStorage());
            }
            if (product.getLjPrice() != null) {
                existProduct.setLjPrice(product.getLjPrice());
            }
            if (product.getNormalPrice() != null) {
                existProduct.setNormalPrice(product.getNormalPrice());
            }
            if (product.getOriginPrice() != null) {
                existProduct.setOriginPrice(product.getOriginPrice());
            }
            if (product.getLjCommission() != null) {
                existProduct.setLjCommission(product.getLjCommission());
                //计算分润和返金币鼓励金
                GrouponParameter parameter = MathUtil.share(product.getLjCommission());
                existProduct.setRebateScorea(parameter.getScoreA());
                existProduct.setRebateScorec(parameter.getScoreC());
                existProduct.setShareToLockMerchant(parameter.getLockMerchant());
                existProduct.setShareToLockPartner(parameter.getLockPartner());
                existProduct.setShareToLockPartnerManager(parameter.getLockPartnerManager());
                existProduct.setShareToTradePartner(parameter.getTradePartner());
                existProduct.setShareToTradePartnerManager(parameter.getTradePartnerManager());
            }
            if (product.getDisplayPicture() != null) {
                existProduct.setDisplayPicture(product.getDisplayPicture());
            }
            if (product.getExplainPicture() != null) {
                existProduct.setExplainPicture(product.getExplainPicture());
            }
            if (product.getInstruction() != null) {
                existProduct.setInstruction(product.getInstruction());
            }
            if (product.getName() != null) {
                existProduct.setName(product.getName());
            }
            if (product.getRefundType() != null) {
                existProduct.setRefundType(product.getRefundType());
            }
            if (product.getReservation() != null) {
                existProduct.setReservation(product.getReservation());
            }
            existProduct.setPayNumType(product.getPayNumType());
            if (product.getPayNumType() == 1) {
                existProduct.setPayNumMax(product.getPayNumMax());
            }
            if (product.getValidity() != null) {
                existProduct.setValidity(product.getValidity());
            }
            if (product.getValidityType() != null) {
                existProduct.setValidityType(product.getValidityType());
            }
            if (product.getMerchantUser() != null) {
                MerchantUser merchantUser = merchantUserRepository.findOne(product.getMerchantUser().getId());
                existProduct.setMerchantUser(merchantUser);
            }
            if (product.getPhoneType() != null) {
                existProduct.setPhoneType(product.getPhoneType());
            }
            grouponProductRepository.save(existProduct);
            //   修改产品门店对应关系
            List<Merchant> merchantList = grouponProductDto.getMerchantList();
            if (merchantList != null || merchantList.size() > 0) {
                // 删除原有的对应关系
                List<GrouponMerchant> existMerchants = grouponMerchantRepository.findByGrouponProduct(existProduct);
                for (int i = 0; i < existMerchants.size(); i++) {
                    GrouponMerchant existMerchant = existMerchants.get(i);
                    grouponMerchantRepository.delete(existMerchant.getId());
                }
                //  建立新的对应关系
                for (Merchant merchant : merchantList) {
                    GrouponMerchant grouponMerchant = new GrouponMerchant();
                    Merchant existMerchant = merchantRepository.findOne(merchant.getId());
                    grouponMerchant.setMerchant(existMerchant);
                    grouponMerchant.setGrouponProduct(existProduct);
                    grouponMerchantRepository.save(grouponMerchant);
                }
            }
            //   保存产品详情图
            List<GrouponProductDetail> detailList = grouponProductDto.getDelDetailList();
            Long detailSid = grouponProductDetailRepository.countGrouponProductDetailByGrouponProduct(existProduct);
            Long scrollSid = grouponScrollPictureRepository.countGrouponScrollPictureByGrouponProduct(existProduct);
            for (int j = 0; j < detailList.size(); j++) {
                GrouponProductDetail grouponProductDetail = detailList.get(j);
                //  如果详情已存在 ， 修改图片路径
                if (grouponProductDetail.getId() != null) {
                    GrouponProductDetail existDetial = grouponProductDetailRepository.findOne(grouponProductDetail.getId());
                    existDetial.setPicture(grouponProductDetail.getPicture());
                    grouponProductDetailRepository.save(existDetial);
                } else {
                    //  如果图片不存在，新增图片
                    GrouponProductDetail newDetial = new GrouponProductDetail();
                    newDetial.setGrouponProduct(existProduct);
                    newDetial.setPicture(grouponProductDetail.getPicture());
                    newDetial.setDescription(existProduct.getDescription());
                    detailSid++;
                    newDetial.setSid(detailSid.intValue());
                    grouponProductDetailRepository.save(newDetial);
                }
            }
            //   保存商品轮播图
            List<GrouponScrollPicture> scorePictureList = grouponProductDto.getDelScrollList();
            for (int i = 0; i < scorePictureList.size(); i++) {
                GrouponScrollPicture grouponScrollPicture = scorePictureList.get(i);
                if (grouponScrollPicture.getId() != null) {
                    GrouponScrollPicture existScroll = grouponScrollPictureRepository.findOne(grouponScrollPicture.getId());
                    existScroll.setPicture(grouponScrollPicture.getPicture());
                    grouponScrollPictureRepository.save(existScroll);
                } else {
                    GrouponScrollPicture newScroll = new GrouponScrollPicture();
                    scrollSid++;
                    newScroll.setDescription(existProduct.getDescription());
                    newScroll.setSid(scrollSid.intValue());
                    newScroll.setGrouponProduct(existProduct);
                    grouponScrollPictureRepository.save(newScroll);
                }
            }
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    /**
     * 根据sid查找商品
     * Created by xf on 2017-06-28.
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public GrouponProduct findBySid(String sid) {
        return grouponProductRepository.findBySid(sid);
    }

    /**
     * 修改商品状态
     * Created by xf on 2017-06-28.
     */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public boolean changeState(GrouponProduct grouponProduct, Integer state) {
        grouponProduct.setState(state);
        grouponProductRepository.save(grouponProduct);
        return true;
    }
}
