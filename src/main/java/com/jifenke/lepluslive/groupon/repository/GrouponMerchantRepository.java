package com.jifenke.lepluslive.groupon.repository;

import com.jifenke.lepluslive.groupon.domain.entities.GrouponMerchant;
import com.jifenke.lepluslive.groupon.domain.entities.GrouponProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * GrouponMerchantRepository
 *  团购产品对应门店
 * @author XF
 * @date 2017/6/20
 */
public interface GrouponMerchantRepository extends JpaRepository<GrouponMerchant,Long> {
    /**
     *  根据门店统计
     */
    Long countGrouponMerchantByGrouponProduct(GrouponProduct grouponProduct);

    List<GrouponMerchant> findByGrouponProduct(GrouponProduct grouponProduct);
}
