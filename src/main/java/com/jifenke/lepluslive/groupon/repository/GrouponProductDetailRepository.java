package com.jifenke.lepluslive.groupon.repository;

import com.jifenke.lepluslive.groupon.domain.entities.GrouponProduct;
import com.jifenke.lepluslive.groupon.domain.entities.GrouponProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * GrouponProductDetailRepository
 *  商品详情图
 * @author XF
 * @date 2017/6/20
 */
public interface GrouponProductDetailRepository extends JpaRepository<GrouponProductDetail,Integer> {
    List<GrouponProductDetail> findByGrouponProduct(GrouponProduct grouponProduct);
    Long countGrouponProductDetailByGrouponProduct(GrouponProduct grouponProduct);
}
