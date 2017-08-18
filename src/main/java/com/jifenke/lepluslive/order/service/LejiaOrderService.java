package com.jifenke.lepluslive.order.service;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantScanPayWay;
import com.jifenke.lepluslive.order.controller.view.LejiaOrderDTO;
import com.jifenke.lepluslive.order.controller.view.MerchantOrderDto;
import com.jifenke.lepluslive.order.domain.criteria.DailyOrderCriteria;
import com.jifenke.lepluslive.order.domain.entities.OffLineOrder;
import com.jifenke.lepluslive.order.domain.entities.ScanCodeOrder;
import com.jifenke.lepluslive.order.domain.entities.ScanCodeRefundOrder;
import com.jifenke.lepluslive.order.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.*;

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
    @Inject
    private MerchantScanPayWayRepository merchantScanPayWayRepository;
    @Inject
    private ScanCodeOrderRepository scanCodeOrderRepository;
    @Inject
    private ScanCodeRefundOrderRepository scanCodeRefundOrderRepository;

    /**
     * 每日账单
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public LejiaOrderDTO findDailyOrderByMerchant(DailyOrderCriteria dailyOrderCriteria) {
        String start = dailyOrderCriteria.getStartDate();
        Date startDate = new Date(start);                           // 开始时间
        Long dayMills = 86400000L;                                  // 一天的毫秒值
        Long merchantId = dailyOrderCriteria.getMerchant().getId(); // 门店id

        //  统计出订单数据
        List<Object[]> offOrders = null;
        List<Object[]> wxOrders = null;
        List<Object[]> offScores = null;
        //  判断当前门店是否接入了新通道
        MerchantScanPayWay payWay = merchantScanPayWayRepository.findByMerchantId(dailyOrderCriteria.getMerchant().getId());
        if (payWay == null) {
            offOrders = offLineOrderRepository.countWeekOfflineOrder(merchantId, startDate);
            wxOrders = offLineOrderRepository.countWeekOfflineWx(merchantId, startDate);
            offScores = offLineOrderRepository.countWeekOffScore(merchantId, startDate);
        } else {
            offOrders = scanCodeOrderRepository.countWeekScanCodeOrder(merchantId, startDate);
            wxOrders = scanCodeOrderRepository.countWeekScanCodeWx(merchantId, startDate);
            offScores = scanCodeOrderRepository.countWeekScanCodeScore(merchantId, startDate);
        }
        List<Object[]> posOrders = posOrderRepository.countWeekPosOrder(merchantId, startDate);
        List<Object[]> posCards = posOrderRepository.countWeekPosCard(merchantId, startDate);
        List<Object[]> posWxMobile = posOrderRepository.countWeekPosWx(merchantId, startDate);
        List<Object[]> posAliMobile = posOrderRepository.countWeekPosAli(merchantId, startDate);
        List<Object[]> posScores = posOrderRepository.countWeekPosScore(merchantId, startDate);
        //  创建报表数据列表ww6
        LejiaOrderDTO lejiaOrderDTO = new LejiaOrderDTO();        // 存放最近七天的订单数据
        List<String> dates = new ArrayList<>();                   //  统计时间
        List<Double> totalTransfer = new ArrayList<>();           // 商户入账总金额;
        List<Double> wxTransfer = new ArrayList<>();              // 微信扫码牌;
        List<Double> posCardTransfer = new ArrayList<>();         // pos 刷卡入账
        List<Double> posMobileTransfer = new ArrayList<>();       // pos 移动入账
        List<Double> scoreTransfer = new ArrayList<>();           // 红包支付入账

        List<Double> wxMobileTransfer = new ArrayList<>();              // 微信移动支付
        List<Double> aliMobileTransfer = new ArrayList<>();             // 支付宝移动支付
        List<Double> posScoreb = new ArrayList<>();                    // POS积分
        List<Double> offScoreb = new ArrayList<>();                     // 扫码积分


        for (int i = 0; i < 7; i++) {
            String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(startDate);
            startDate.setTime(startDate.getTime() - dayMills);
            dates.add(dateStr);                                  // 设置时间和金额默认值
            totalTransfer.add(0.0);
            wxTransfer.add(0.0);
            posCardTransfer.add(0.0);
            posMobileTransfer.add(0.0);
            scoreTransfer.add(0.0);
            // 具体数据
            wxMobileTransfer.add(0.0);
            aliMobileTransfer.add(0.0);
            posScoreb.add(0.0);
            offScoreb.add(0.0);
        }
        Collections.reverse(dates);                               // 翻转
        //  封装数据
        for (int i = 0; i < dates.size(); i++) {
            String startTime = dates.get(i);
            //  总入账金额 =  Pos总金额 +  扫码总金额
            for (Object[] offOrder : offOrders) {
                if (startTime.equals(offOrder[0].toString())) {
                    totalTransfer.set(i, (totalTransfer.get(i) + new Double(offOrder[1].toString()) * 0.01));
                    break;
                }
            }
            for (Object[] posOrder : posOrders) {
                if (startTime.equals(posOrder[0].toString())) {
                    totalTransfer.set(i, (totalTransfer.get(i) + new Double(posOrder[1].toString()) * 0.01));
                    break;
                }
            }
            // 扫码牌微信入账
            for (Object[] wxOrder : wxOrders) {
                if (startTime.equals(wxOrder[0].toString())) {
                    wxTransfer.set(i, wxTransfer.get(i) + new Double(wxOrder[1].toString()) * 0.01);
                    break;
                }
            }
            //  pos 刷卡
            for (Object[] posCard : posCards) {
                if (startTime.equals(posCard[0].toString())) {
                    posCardTransfer.set(i, posCardTransfer.get(i) + new Double(posCard[1].toString()) * 0.01);
                    break;
                }
            }
            //  pos 移动支付
            for (Object[] aliMob : posAliMobile) {
                if (startTime.equals(aliMob[0].toString())) {
                    Long aliCount = new Long(aliMob[1].toString());
                    posMobileTransfer.set(i, posMobileTransfer.get(i) + aliCount * 0.01);
                    aliMobileTransfer.set(i, aliMobileTransfer.get(i) + aliCount * 0.01);
                    break;
                }
            }
            for (Object[] wxMob : posWxMobile) {
                if (startTime.equals(wxMob[0].toString())) {
                    posMobileTransfer.set(i, posMobileTransfer.get(i) + new Double(wxMob[1].toString()) * 0.01);
                    wxMobileTransfer.set(i, wxMobileTransfer.get(i) + new Double(wxMob[1].toString()) * 0.01);
                    break;
                }
            }

            //  红包 =  pos 红包 + 扫码红包
            for (Object[] posScore : posScores) {
                if (startTime.equals(posScore[0].toString())) {
                    scoreTransfer.set(i, scoreTransfer.get(i) + new Double(posScore[1].toString()) * 0.01);
                    posScoreb.set(i, posScoreb.get(i) + new Double(posScore[1].toString()) * 0.01);
                    break;
                }
            }
            for (Object[] offScore : offScores) {
                if (startTime.equals(offScore[0].toString())) {
                    scoreTransfer.set(i, scoreTransfer.get(i) + new Double(offScore[1].toString()) * 0.01);
                    offScoreb.set(i, offScoreb.get(i) + new Double(offScore[1].toString()) * 0.01);
                    break;
                }
            }
        }
        lejiaOrderDTO.setDates(dates);
        lejiaOrderDTO.setPosCardTransfer(posCardTransfer);
        lejiaOrderDTO.setScoreTransfer(scoreTransfer);
        lejiaOrderDTO.setPosMobileTransfer(posMobileTransfer);
        lejiaOrderDTO.setTotalTransfer(totalTransfer);
        lejiaOrderDTO.setWxTransfer(wxTransfer);
        lejiaOrderDTO.setWxMobileTransfer(wxMobileTransfer);
        lejiaOrderDTO.setAliMobileTransfer(aliMobileTransfer);
        lejiaOrderDTO.setOffScores(offScoreb);
        lejiaOrderDTO.setPosScores(posScoreb);
        return lejiaOrderDTO;
    }


    /**
     * 门店账单
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<MerchantOrderDto> findMerchantOrderData(DailyOrderCriteria dailyOrderCriteria, List<Merchant> merchants) {
        List<MerchantOrderDto> merchantOrderDtos = new ArrayList<>();
        for (Merchant merchant : merchants) {
            MerchantScanPayWay payWay = merchantScanPayWayRepository.findByMerchantId(merchant.getId());
            Long offTotal = null;
            Long wxTransfer = null;
            Long offScore = null;
            if (payWay == null) {
                offTotal = offLineOrderRepository.countMerchantTotal(merchant.getId(), dailyOrderCriteria.getStartDate(), dailyOrderCriteria.getEndDate());
                wxTransfer = offLineOrderRepository.countMerchantWxTransfer(merchant.getId(), dailyOrderCriteria.getStartDate(), dailyOrderCriteria.getEndDate());            // 微信扫码总入账
                offScore = offLineOrderRepository.countMerchantOffScore(merchant.getId(), dailyOrderCriteria.getStartDate(), dailyOrderCriteria.getEndDate());
            } else {
                offTotal = scanCodeOrderRepository.countMerchantTotal(merchant.getId(), dailyOrderCriteria.getStartDate(), dailyOrderCriteria.getEndDate());
                wxTransfer = scanCodeOrderRepository.countMerchantWxTransfer(merchant.getId(), dailyOrderCriteria.getStartDate(), dailyOrderCriteria.getEndDate());
                offScore = scanCodeOrderRepository.countMerchantOffScore(merchant.getId(), dailyOrderCriteria.getStartDate(), dailyOrderCriteria.getEndDate());
            }
            Long posTotal = posOrderRepository.countMerchantPosTotal(merchant.getId(), dailyOrderCriteria.getStartDate(), dailyOrderCriteria.getEndDate());
            Double totalTransfer = (offTotal + posTotal) * 0.01;                      // 总入账
            Long posCardTransfer = posOrderRepository.countMerchantPosCardTransfer(merchant.getId(), dailyOrderCriteria.getStartDate(), dailyOrderCriteria.getEndDate());      // pos 刷卡
            Long wxMobileTransfer = posOrderRepository.countWxMobileTranfer(merchant.getId(), dailyOrderCriteria.getStartDate(), dailyOrderCriteria.getEndDate());             // pos 移动 - 微信
            Long aliMobileTransfer = posOrderRepository.countAliMobileTranfer(merchant.getId(), dailyOrderCriteria.getStartDate(), dailyOrderCriteria.getEndDate());           // pos 移动 - 阿里
            Double mobileTransfer = (wxMobileTransfer + aliMobileTransfer) * 0.01;
            Long posScore = posOrderRepository.countAliMobileTranfer(merchant.getId(), dailyOrderCriteria.getStartDate(), dailyOrderCriteria.getEndDate());
            Double totalScore = (offScore + posScore) * 0.01;
            MerchantOrderDto merchantOrderDto = new MerchantOrderDto();
            merchantOrderDto.setTotalTransfer(totalTransfer);
            merchantOrderDto.setMobileTransfer(mobileTransfer);
            merchantOrderDto.setTotalScore(totalScore);
            merchantOrderDto.setOffTransferFromTruePay(wxTransfer * 0.01);
            merchantOrderDto.setPosCardTransfer(posCardTransfer * 0.01);
            merchantOrderDto.setMobileWxTransfer(wxMobileTransfer * 0.01);
            merchantOrderDto.setMobileAliTransfer(aliMobileTransfer * 0.01);
            merchantOrderDto.setOffScore(offScore * 0.01);
            merchantOrderDto.setPosScore(posScore * 0.01);
            merchantOrderDto.setMerchantName(merchant.getName());
            merchantOrderDto.setMerchantId(merchant.getId());
            merchantOrderDtos.add(merchantOrderDto);
        }
        return merchantOrderDtos;
    }


    /**
     * 导出订单 Excel
     */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Map findOrderExcelData(DailyOrderCriteria dailyOrderCriteria) {
        Map map = new HashMap();
        Merchant merchant = dailyOrderCriteria.getMerchant();
        MerchantScanPayWay payWay = merchantScanPayWayRepository.findByMerchantId(merchant.getId());
        map.put("payWay",payWay);
        List<OffLineOrder> offLineOrders = offLineOrderRepository.findByMerchantAndDate(merchant.getId(), dailyOrderCriteria.getStartDate(), dailyOrderCriteria.getEndDate());
        map.put("offLineOrders",offLineOrders);
        List<ScanCodeOrder> scanCodeOrders = scanCodeOrderRepository.findByMerchantAndDate(merchant.getId(), dailyOrderCriteria.getStartDate(), dailyOrderCriteria.getEndDate());
        map.put("scanCodeOrders",scanCodeOrders);
        List<ScanCodeRefundOrder> refoundOrder = scanCodeRefundOrderRepository.findScanOrderByMerchant(merchant.getId(), dailyOrderCriteria.getStartDate(), dailyOrderCriteria.getEndDate());
        map.put("refoundOrder",refoundOrder);
        Long custScore = scanCodeOrderRepository.countMerchantCustScore(merchant.getId(), dailyOrderCriteria.getStartDate(), dailyOrderCriteria.getEndDate());
        map.put("custScore",custScore);
        Long custPay = scanCodeOrderRepository.countMerchantWxCustTransfer(merchant.getId(), dailyOrderCriteria.getStartDate(), dailyOrderCriteria.getEndDate());
        map.put("custPay",custPay);
        Long trueScore = scanCodeOrderRepository.countMerchantOffScore(merchant.getId(), dailyOrderCriteria.getStartDate(), dailyOrderCriteria.getEndDate());
        map.put("trueScore",trueScore);
        Long truePay = scanCodeOrderRepository.countMerchantWxTransfer(merchant.getId(), dailyOrderCriteria.getStartDate(), dailyOrderCriteria.getEndDate());
        map.put("truePay",truePay);
        List<Object[]> refound = scanCodeRefundOrderRepository.countRefundMoneyAndScore(merchant.getId(), dailyOrderCriteria.getStartDate(), dailyOrderCriteria.getEndDate());
        map.put("refound",refound);
        Long offCustScore = offLineOrderRepository.findCustScoreByMerchantAndDate(merchant.getId(), dailyOrderCriteria.getStartDate(), dailyOrderCriteria.getEndDate());
        map.put("offCustScore",offCustScore);
        Long offCustPay = offLineOrderRepository.findCustPayByMerchantAndDate(merchant.getId(), dailyOrderCriteria.getStartDate(), dailyOrderCriteria.getEndDate());
        map.put("offCustPay",offCustPay);
        Long offtrueScore = offLineOrderRepository.countMerchantOffScore(merchant.getId(), dailyOrderCriteria.getStartDate(), dailyOrderCriteria.getEndDate());
        map.put("offtrueScore",offtrueScore);
        Long offtruePay = offLineOrderRepository.countMerchantWxTransfer(merchant.getId(), dailyOrderCriteria.getStartDate(), dailyOrderCriteria.getEndDate());
        map.put("offtruePay",offtruePay);
        return map;
    }
}
