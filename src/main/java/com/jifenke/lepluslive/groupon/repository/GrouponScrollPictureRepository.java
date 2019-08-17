package com.jifenke.lepluslive.groupon.repository;

import com.jifenke.lepluslive.groupon.domain.entities.GrouponProduct;
import com.jifenke.lepluslive.groupon.domain.entities.GrouponScrollPicture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * GrouponScrollPictureRepository
 * 商品轮播图
 * @author XF
 * @date 2017/6/20
 */
public interface GrouponScrollPictureRepository extends JpaRepository<GrouponScrollPicture,Integer> {
    List<GrouponScrollPicture> findByGrouponProduct(GrouponProduct grouponProduct);
    Long countGrouponScrollPictureByGrouponProduct(GrouponProduct grouponProduct);
}
