package com.jifenke.lepluslive.merchant.repository;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUserResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
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

    /**
     * 根据商户ID查询商户旗下所有的门店
     * @param merchantName
     * @return 商户旗下所有门店信息
     *
     *
     * 万俊
     */
    @Query(value = "SELECT c.id,c.name from merchant c where c.id in" +
        "(SELECT a.resource_id from le_jia_resource a where a.id in " +
        "(SELECT b.le_jia_resource_id from merchant_user_resource b where b.merchant_user_id=" +
        "(SELECT d.id from merchant_user d WHERE d.name=?1)))",nativeQuery = true)
    List<Object> findByMerchantInfoUser(String merchantName);

    /**
     * 根据商户ID查询商户旗下所有门店的所有pos机
     * @param merchantName
     * @return
     */
    @Query(value = "SELECT c.id,c.pos_id from merchant_pos c where c.merchant_id in" +
        "(SELECT a.resource_id from le_jia_resource a where a.id in " +
        "(SELECT b.le_jia_resource_id from merchant_user_resource b where b.merchant_user_id=" +
        "(SELECT d.id from merchant_user d WHERE d.name=?1)))",nativeQuery = true)
    List<Object> findByMerchantPosUser(String merchantName);
}
