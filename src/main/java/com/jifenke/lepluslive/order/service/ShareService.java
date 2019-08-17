package com.jifenke.lepluslive.order.service;

import com.jifenke.lepluslive.order.domain.criteria.ShareCriteria;
import com.jifenke.lepluslive.order.domain.entities.OffLineOrderShare;
import com.jifenke.lepluslive.order.repository.OffLineOrderShareRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Created by wcg on 16/7/19.
 */
@Service
@Transactional(readOnly = true)
public class ShareService {

  @Inject
  private OffLineOrderShareRepository offLineOrderShareRepository;

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public Page findShareByPage(ShareCriteria orderCriteria, Integer limit) {
    Sort sort = new Sort(Sort.Direction.DESC, "createDate");
    return null;

  }


  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public OffLineOrderShare findOneByOrderId(Long id) {
    return offLineOrderShareRepository.findOneByOrderId(id);

  }

  /**
   * 根据富友订单查询分润单  2016/12/22
   *
   * @param orderId 订单ID
   */
  public OffLineOrderShare findByScanCodeOrder(Long orderId) {
    return offLineOrderShareRepository.findByScanCodeOrder(orderId);
  }
}
