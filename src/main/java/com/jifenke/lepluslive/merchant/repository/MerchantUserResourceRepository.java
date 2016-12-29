package com.jifenke.lepluslive.merchant.repository;

import com.jifenke.lepluslive.merchant.domain.criteria.MerchantCriteria;
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
    List<Object []> findByMerchantInfoUser(String merchantName);

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

    /**
     * 根据门店ID统计，门店锁定会员数
     * @param merchantId
     * @return
     */
    @Query(value = "SELECT count(a.id) from le_jia_user a where a.bind_merchant_id=?1",nativeQuery = true)
    Long findByLeJiaUser(Long merchantId);


    @Query(value = "SELECT\n" +
        "\ta.`name`,\n" +
        "\ta.location,\n" +
        "\ta.contact,\n" +
        "\ta.merchant_phone as merchantPhone,\n" +
        "\ta.phone_number as phoneNumber,\n" +
        "\ta.receipt_auth as receiptAuth,\n" +
        "\ta.state,\n" +
        "\tcount(b.id) as countUser,\n" +
        "c.total_money as totalMoney\n" +
        "FROM\n" +
        "\tmerchant a\n" +
        "LEFT JOIN le_jia_user b on a.id  = b.bind_merchant_id LEFT JOIN merchant_wallet c on c.merchant_id = a.id\n" +
        "WHERE\n" +
        "\ta.id IN (?1)\n" +
        "GROUP BY a.id\n" +
        "LIMIT ?2,\n" +
        "10",nativeQuery = true)
    List<Object []> pageFindMerchantInfoByMerchantUser(List<Object> obj, Integer offSet);
}
