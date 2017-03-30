package com.jifenke.lepluslive.merchant.repository;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.order.domain.entities.MerchantPos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by sunxingfei on 2016/12/12.
 */
public interface MerchantPosRepository extends JpaRepository<MerchantPos, Long>,JpaSpecificationExecutor<MerchantPos> {

}
