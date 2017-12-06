package com.jifenke.lepluslive.groupon.service;

import com.jifenke.lepluslive.groupon.domain.entities.GrouponProduct;
import com.jifenke.lepluslive.groupon.domain.entities.GrouponProductDetail;
import com.jifenke.lepluslive.groupon.repository.GrouponProductDetailRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

/**
 * GrouponProductDetailService
 *  商品详情图
 * @author XF
 * @date 2017/6/27
 */
@Service
@Transactional(readOnly = true)
public class GrouponProductDetailService {
    @Inject
    private GrouponProductDetailRepository productDetailRepository;
    @Transactional(readOnly = true,propagation = Propagation.REQUIRED)
    public List<GrouponProductDetail> findByGrouponProduct(GrouponProduct grouponProduct) {
        return productDetailRepository.findByGrouponProduct(grouponProduct);
    }
}
