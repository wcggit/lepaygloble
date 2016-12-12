package com.jifenke.lepluslive.order.service;

import com.jifenke.lepluslive.order.controller.view.LejiaOrderDTO;
import com.jifenke.lepluslive.order.domain.criteria.DailyOrderCriteria;
import com.jifenke.lepluslive.order.repository.OffLineOrderRepository;
import com.jifenke.lepluslive.order.repository.PosOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xf on 16-12-8.
 */
@Service
@Transactional(readOnly = true)
public class LejiaOrderService {

    @Inject
    private PosOrderRepository posOrderRepository;
    @Inject
    private OffLineOrderRepository offLineOrderRepository;

    @Transactional(readOnly = true,propagation = Propagation.REQUIRED)
    public List<LejiaOrderDTO> findDailyOrderByMerchant(DailyOrderCriteria dailyOrderCriteria) {
        String start = dailyOrderCriteria.getStartDate();
        Date startDate = new Date(start);                           // 开始时间
        Long dayMills = 86400000L;                                  // 一天的毫秒值
        Long merchantId = dailyOrderCriteria.getMerchant().getId(); // 门店id

        //  统计出订单数据
        List<Object[]> offOrders = offLineOrderRepository.countWeekOfflineOrder(merchantId, startDate);
        List<Object[]> posOrders = posOrderRepository.countWeekPosOrder(merchantId, startDate);
        List<Object[]> wxOrders = offLineOrderRepository.countWeekOfflineWx(merchantId, startDate);
        List<Object[]> offScores = offLineOrderRepository.countWeekOffScore(merchantId, startDate);
        List<Object[]> posCards = posOrderRepository.countWeekPosCard(merchantId, startDate);
        List<Object[]> posMobile = posOrderRepository.countWeekPosMobile(merchantId, startDate);
        List<Object[]> posScores = posOrderRepository.countWeekPosScore(merchantId, startDate);
        //  创建报表数据列表ww6
        List<LejiaOrderDTO> lejiaOrders = new ArrayList<>();        // 存放最近七天的订单数据
        for (int i=0;i<7;i++) {
            String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(startDate);
            startDate.setTime(startDate.getTime()-dayMills);
            LejiaOrderDTO lejiaOrderDTO = new LejiaOrderDTO();
            lejiaOrderDTO.setStartDate(dateStr);                    // 设置时间和金额默认值
            lejiaOrderDTO.setTotalTransfer(0L);
            lejiaOrderDTO.setPosCardTransfer(0L);
            lejiaOrderDTO.setPosMobileTransfer(0L);
            lejiaOrderDTO.setScoreTransfer(0L);
            lejiaOrderDTO.setWxTransfer(0L);
            lejiaOrders.add(lejiaOrderDTO);
        }
        //  封装数据
        for (LejiaOrderDTO lejiaOrder : lejiaOrders) {
            String startTime = lejiaOrder.getStartDate();
            //  总入账金额 =  Pos总金额 +  扫码总金额
            for(Object[] offOrder:offOrders) {
                if(startTime.equals(offOrder[0].toString())) {
                    lejiaOrder.setTotalTransfer(lejiaOrder.getTotalTransfer()+new Long(offOrder[1].toString()));
                    break;
                }
            }
            for(Object[] posOrder:posOrders) {
                if(startTime.equals(posOrder[0].toString())) {
                    lejiaOrder.setTotalTransfer(lejiaOrder.getTotalTransfer()+new Long(posOrder[1].toString()));
                    break;
                }
            }
            // 扫码牌微信入账
            for(Object[] wxOrder:wxOrders) {
                if(startTime.equals(wxOrder[0].toString())) {
                    lejiaOrder.setWxTransfer(lejiaOrder.getWxTransfer()+new Long(wxOrder[1].toString()));
                    break;
                }
            }
            //  pos 刷卡
            for(Object[] posCard:posCards) {
                if(startTime.equals(posCard[0].toString())) {
                    lejiaOrder.setPosCardTransfer(lejiaOrder.getPosCardTransfer()+new Long(posCard[1].toString()));
                    break;
                }
            }
            //  pos 移动支付
            for(Object[] posMob:posMobile) {
                if(startTime.equals(posMob[0].toString())) {
                    lejiaOrder.setPosMobileTransfer(lejiaOrder.getPosMobileTransfer()+new Long(posMob[1].toString()));
                    break;
                }
            }
            //  红包 =  pos 红包 + 扫码红包
            for(Object[] posScore:posScores) {
                if(startTime.equals(posScore[0].toString())) {
                    lejiaOrder.setScoreTransfer(lejiaOrder.getScoreTransfer()+new Long(posScore[1].toString()));
                    break;
                }
            }
            for(Object[] offScore:offScores) {
                if(startTime.equals(offScore[0].toString())) {
                    lejiaOrder.setScoreTransfer(lejiaOrder.getScoreTransfer()+new Long(offScore[1].toString()));
                    break;
                }
            }
        }
        return lejiaOrders;
    }
}
