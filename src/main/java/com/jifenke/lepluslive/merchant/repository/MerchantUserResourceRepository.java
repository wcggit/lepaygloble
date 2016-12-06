package com.jifenke.lepluslive.merchant.repository;

import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUserResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by xf on 16-12-2.
 */
public interface MerchantUserResourceRepository extends JpaRepository<MerchantUserResource,Long> {
    /**
     * 根据商户获取门店资源id列表
     * @param merchantUserId
     * @return
     */
    @Query(value="SELECT * FROM merchant_user_resource where resource_type  = 0 and  merchant_user_id = ?1",nativeQuery = true)
    List<MerchantUserResource> findByMerchantUser(Long merchantUserId);
}
