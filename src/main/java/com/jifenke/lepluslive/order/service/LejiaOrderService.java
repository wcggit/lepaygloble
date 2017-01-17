package com.jifenke.lepluslive.order.service;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantScroll;
import com.jifenke.lepluslive.order.controller.view.LejiaOrderDTO;
import com.jifenke.lepluslive.order.controller.view.MerchantOrderDto;
import com.jifenke.lepluslive.order.domain.criteria.DailyOrderCriteria;
import com.jifenke.lepluslive.order.domain.entities.MerchantScanPayWay;
import com.jifenke.lepluslive.order.domain.entities.OffLineOrder;
import com.jifenke.lepluslive.order.domain.entities.ScanCodeOrder;
import com.jifenke.lepluslive.order.domain.entities.ScanCodeRefundOrder;
import com.jifenke.lepluslive.order.repository.*;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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
            merchantOrderDtos.add(merchantOrderDto);
        }
        return merchantOrderDtos;
    }


    /**
     * 导出订单 Excel
     */
    public void exportOrderExcel(ServletOutputStream outputStream, String[] titles1, String[] titles2, String[] titles3, DailyOrderCriteria dailyOrderCriteria) {
        //  创建表格对象
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        //  创建第一个表头
        HSSFRow headRow1 = sheet.createRow(0);
        HSSFCell cell = null;
        for (int i = 0; i < titles1.length; i++) {
            cell = headRow1.createCell(i);
            cell.setCellValue(titles1[i]);
        }
        Merchant merchant = dailyOrderCriteria.getMerchant();
        MerchantScanPayWay payWay = merchantScanPayWayRepository.findByMerchantId(merchant.getId());
        if (payWay == null) {                          // 乐加订单结算
            List<OffLineOrder> offLineOrders = offLineOrderRepository.findByMerchantAndDate(merchant.getId(), dailyOrderCriteria.getStartDate(), dailyOrderCriteria.getEndDate());
            if (offLineOrders != null && offLineOrders.size() > 0) {
                for (OffLineOrder offLineOrder : offLineOrders) {
                    HSSFRow contentRow = sheet.createRow(sheet.getLastRowNum() + 1);
                    // 订单编号
                    HSSFCell contentCell0 = contentRow.createCell(0);
                    contentCell0.setCellValue(offLineOrder.getOrderSid());
                    // 交易完成时间
                    HSSFCell contentCell1 = contentRow.createCell(1);
                    contentCell1.setCellValue(offLineOrder.getCompleteDate());
                    // 订单状态
                    HSSFCell contentCell2 = contentRow.createCell(2);
                    if (offLineOrder.getState() == 1) {
                        contentCell2.setCellValue("已付款");
                    } else if (offLineOrder.getState() == 2) {
                        contentCell2.setCellValue("已退款");
                    }
                    // 支付渠道
                    HSSFCell contentCell3 = contentRow.createCell(3);
                    contentCell3.setCellValue(offLineOrder.getPayWay() == null ? "未记录" : offLineOrder.getPayWay().getPayWay());
                    // 消费金额
                    HSSFCell contentCell4 = contentRow.createCell(4);
                    contentCell4.setCellValue(new Double(offLineOrder.getTotalPrice() * 0.01));
                    // 使用红包
                    HSSFCell contentCell5 = contentRow.createCell(5);
                    contentCell5.setCellValue(new Double(offLineOrder.getTrueScore() * 0.01));
                    // 实际支付
                    HSSFCell contentCell6 = contentRow.createCell(6);
                    contentCell6.setCellValue(new Double((offLineOrder.getTransferMoneyFromTruePay())*0.01));
                    // 订单类型
                    HSSFCell contentCell7 = contentRow.createCell(7);
                    if (offLineOrder.getRebateWay() == 0 || offLineOrder.getRebateWay() == 2) {
                        contentCell7.setCellValue("普通订单");
                    } else if (offLineOrder.getRebateWay() == 1) {
                        contentCell7.setCellValue("导流订单");
                    } else if (offLineOrder.getRebateWay() == 3 || offLineOrder.getRebateWay() == 6) {
                        contentCell7.setCellValue("会员订单");
                    } else {
                        contentCell7.setCellValue("扫码订单");
                    }
                    //  导流订单和会员订单（佣金费率） 显示折扣
                    HSSFCell contentCell8 = contentRow.createCell(8);
                    HSSFCell contentCell9 = contentRow.createCell(9);
                    if (offLineOrder.getRebateWay() == 1 || offLineOrder.getRebateWay() == 3) {
                        Long wxCommission = offLineOrder.getWxCommission();
                        String wxDiscount = new Double((100L - wxCommission) * 0.1).toString();
                        contentCell8.setCellValue(wxDiscount);
                        Long scoreCommission = (offLineOrder.getLjCommission()-offLineOrder.getWxCommission());
                        String scoreDiscount = new Double((100L-scoreCommission)*0.1).toString();
                        contentCell9.setCellValue(scoreDiscount);
                    } else {
                        contentCell8.setCellValue(offLineOrder.getWxCommission());
                        String scoreCommission = new Double(offLineOrder.getLjCommission() - offLineOrder.getWxCommission()).toString();
                        contentCell9.setCellValue(scoreCommission);
                    }
                    // 总入账金额
                    HSSFCell contentCell10 = contentRow.createCell(10);
                    contentCell10.setCellValue(offLineOrder.getTransferMoney()*0.01);
                    //  微信支付入账
                    HSSFCell contentCell11 = contentRow.createCell(11);
                    contentCell11.setCellValue(offLineOrder.getTransferMoneyFromTruePay()*0.01);
                    //  红包支付入账
                    HSSFCell contentCell12 = contentRow.createCell(12);
                    contentCell12.setCellValue((offLineOrder.getTransferMoney()-offLineOrder.getTransferMoneyFromTruePay()*0.01));
                    //  退款时间
                    HSSFCell contentCell13 = contentRow.createCell(13);
                    if(offLineOrder.getState()!=2) {
                        contentCell13.setCellValue(" ~ ");
                    }else {
                        contentCell13.setCellValue(offLineOrder.getCompleteDate());
                    }
                }
            }
        } else {                                     // 富有通道结算
            List<ScanCodeOrder> scanCodeOrders = scanCodeOrderRepository.findByMerchantAndDate(merchant.getId(), dailyOrderCriteria.getStartDate(), dailyOrderCriteria.getEndDate());
            for (ScanCodeOrder scanCodeOrder : scanCodeOrders) {
                HSSFRow contentRow = sheet.createRow(sheet.getLastRowNum() + 1);
                // 订单编号
                HSSFCell contentCell0 = contentRow.createCell(0);
                contentCell0.setCellValue(scanCodeOrder.getOrderSid());
                // 交易完成时间
                HSSFCell contentCell1 = contentRow.createCell(1);
                contentCell1.setCellValue(scanCodeOrder.getCompleteDate());
                // 订单状态
                HSSFCell contentCell2 = contentRow.createCell(2);
                if (scanCodeOrder.getState() == 1) {
                    contentCell2.setCellValue("已付款");
                } else if (scanCodeOrder.getState() == 2) {
                    contentCell2.setCellValue("已退款");
                }
                // 支付渠道
                HSSFCell contentCell3 = contentRow.createCell(3);
                if(scanCodeOrder.getPayment()==0) {
                    contentCell3.setCellValue("纯现金");
                }else if(scanCodeOrder.getPayment()==1) {
                    contentCell3.setCellValue("纯红包");
                }else if(scanCodeOrder.getPayment()==2) {
                    contentCell3.setCellValue("混合");
                }
                // 消费金额
                HSSFCell contentCell4 = contentRow.createCell(4);
                contentCell4.setCellValue(new Double(scanCodeOrder.getTotalPrice() * 0.01));
                // 使用红包
                HSSFCell contentCell5 = contentRow.createCell(5);
                contentCell5.setCellValue(new Double(scanCodeOrder.getTrueScore() * 0.01));
                // 实际支付
                HSSFCell contentCell6 = contentRow.createCell(6);
                contentCell6.setCellValue(new Double((scanCodeOrder.getTransferMoneyFromTruePay())*0.01));
                // 订单类型
                HSSFCell contentCell7 = contentRow.createCell(7);
                contentCell7.setCellValue(scanCodeOrder.getOrderType().getValue());
                //  导流订单和会员订单（佣金费率） 显示折扣
                HSSFCell contentCell8 = contentRow.createCell(8);
                HSSFCell contentCell9 = contentRow.createCell(9);
                if (scanCodeOrder.getOrderType().getId() == 12005 || scanCodeOrder.getOrderType().getId() == 12003) {
                    Long wxCommission = scanCodeOrder.getWxCommission();
                    String wxDiscount = new Double((100L - wxCommission) * 0.1).toString();
                    contentCell8.setCellValue(wxDiscount);
                    Long scoreCommission = (scanCodeOrder.getCommission()-scanCodeOrder.getWxCommission());
                    String scoreDiscount = new Double((100L-scoreCommission)*0.1).toString();
                    contentCell9.setCellValue(scoreDiscount);
                } else {
                    contentCell8.setCellValue(scanCodeOrder.getWxCommission());
                    String scoreCommission = new Double(scanCodeOrder.getCommission() - scanCodeOrder.getWxCommission()).toString();
                    contentCell9.setCellValue(scoreCommission);
                }
                // 总入账金额
                HSSFCell contentCell10 = contentRow.createCell(10);
                contentCell10.setCellValue(scanCodeOrder.getTransferMoney()*0.01);
                //  微信支付入账
                HSSFCell contentCell11 = contentRow.createCell(11);
                contentCell11.setCellValue(scanCodeOrder.getTransferMoneyFromTruePay()*0.01);
                //  红包支付入账
                HSSFCell contentCell12 = contentRow.createCell(12);
                contentCell12.setCellValue((scanCodeOrder.getTransferMoney()-scanCodeOrder.getTransferMoneyFromTruePay()*0.01));
                //  退款时间
                HSSFCell contentCell13 = contentRow.createCell(13);
                if(scanCodeOrder.getState()!=2) {
                    contentCell13.setCellValue(" ~ ");
                }else {
                    contentCell13.setCellValue(scanCodeOrder.getCompleteDate());
                }
            }
        }
        //  *  粗体样式
        HSSFFont font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        //  创建第二个表头
        HSSFRow headRow2 = sheet.createRow(sheet.getLastRowNum() + 1);
        HSSFCell row2Cell = headRow2.createCell(0);
        row2Cell.setCellStyle(cellStyle);
        row2Cell.setCellValue("退款单");
        HSSFRow headRow2Title = sheet.createRow(sheet.getLastRowNum() + 1);
        for (int i = 0; i < titles2.length; i++) {
            cell = headRow2Title.createCell(i);
            cell.setCellValue(titles2[i]);
        }
        if (payWay != null) {
            List<ScanCodeRefundOrder> refoundOrder =  scanCodeRefundOrderRepository.findScanOrderByMerchant(merchant.getId(), dailyOrderCriteria.getStartDate(), dailyOrderCriteria.getEndDate());
            if(refoundOrder!=null && refoundOrder.size()>0) {
                for (ScanCodeRefundOrder scanCodeRefundOrder : refoundOrder) {
                    HSSFRow contentRow = sheet.createRow(sheet.getLastRowNum() + 1);
                    ScanCodeOrder scanCodeOrder = scanCodeRefundOrder.getScanCodeOrder();
                    // 退款单号
                    HSSFCell contentCell0 = contentRow.createCell(0);
                    contentCell0.setCellValue(scanCodeRefundOrder.getRefundOrderSid());
                    // 退款完成时间
                    HSSFCell contentCell1 = contentRow.createCell(1);
                    contentCell1.setCellValue(scanCodeRefundOrder.getCompleteDate());
                    // 订单编号
                    HSSFCell contentCell2 = contentRow.createCell(2);
                    contentCell2.setCellValue(scanCodeOrder.getId());
                    // 订单类型
                    HSSFCell contentCell3 = contentRow.createCell(3);
                    contentCell3.setCellValue(scanCodeOrder.getOrderType().getValue());
                    // 订单完成时间
                    HSSFCell contentCell4 = contentRow.createCell(4);
                    contentCell4.setCellValue(scanCodeOrder.getCompleteDate());
                    // 微信渠道退款
                    HSSFCell contentCell5 = contentRow.createCell(5);
                    contentCell5.setCellValue(scanCodeOrder.getTransferMoney()*0.01);
                    // 红包渠道退款
                    HSSFCell contentCell6 = contentRow.createCell(6);
                    contentCell6.setCellValue(scanCodeOrder.getTrueScore()*0.01);
                    // 微信支付
                    HSSFCell contentCell7 = contentRow.createCell(7);
                    contentCell7.setCellValue(scanCodeOrder.getTransferMoneyFromTruePay()*0.01);
                    // 红包支付少转账
                    HSSFCell contentCell8 = contentRow.createCell(8);
                    contentCell8.setCellValue(scanCodeOrder.getTransferMoneyFromScore()*0.01);
                }
            }
        }
        //  创建第三个表头
        HSSFRow headRow3 = sheet.createRow(sheet.getLastRowNum() + 1);
        HSSFCell row3Cell = headRow3.createCell(0);
        row3Cell.setCellStyle(cellStyle);
        row3Cell.setCellValue("数据汇总");
        HSSFRow headRow3Title = sheet.createRow(sheet.getLastRowNum() + 1);
        for (int i = 0; i < titles3.length; i++) {
            cell = headRow3Title.createCell(i);
            cell.setCellValue(titles3[i]);
        }
        // 数据汇总
        HSSFRow cotentSum = sheet.createRow(sheet.getLastRowNum() + 1);
        HSSFCell sumCell0 =cotentSum.createCell(0);
        HSSFCell sumCell1 =cotentSum.createCell(1);
        HSSFCell sumCell2 =cotentSum.createCell(2);
        HSSFCell sumCell3 =cotentSum.createCell(3);
        HSSFCell sumCell4 =cotentSum.createCell(4);
        HSSFCell sumCell5 =cotentSum.createCell(5);
        if(payWay!=null) {
            Long custScore = scanCodeOrderRepository.countMerchantCustScore(merchant.getId(), dailyOrderCriteria.getStartDate(), dailyOrderCriteria.getEndDate());
            Long custPay = scanCodeOrderRepository.countMerchantWxCustTransfer(merchant.getId(), dailyOrderCriteria.getStartDate(), dailyOrderCriteria.getEndDate());
            Long trueScore = scanCodeOrderRepository.countMerchantOffScore(merchant.getId(), dailyOrderCriteria.getStartDate(), dailyOrderCriteria.getEndDate());
            Long truePay = scanCodeOrderRepository.countMerchantWxTransfer(merchant.getId(), dailyOrderCriteria.getStartDate(), dailyOrderCriteria.getEndDate());
            List<Object[]> refound = scanCodeRefundOrderRepository.countRefundMoneyAndScore(merchant.getId(), dailyOrderCriteria.getStartDate(), dailyOrderCriteria.getEndDate());
            sumCell0.setCellValue(custScore*0.01);
            sumCell1.setCellValue(custPay*0.01);
            if(refound!=null) {
                Object [] obj = refound.get(0);
                Long refundMoney = new Long(obj[0].toString());
                Long refundScore = new Long(obj[1].toString());
                sumCell2.setCellValue(refundMoney*0.01);
                sumCell3.setCellValue(refundScore*0.01);
            }else{
                sumCell2.setCellValue(0L);
                sumCell3.setCellValue(0L);
            }
            sumCell4.setCellValue(trueScore*0.01);
            sumCell5.setCellValue(truePay*0.01);
        }else {
            Long offCustScore = offLineOrderRepository.findCustScoreByMerchantAndDate(merchant.getId(), dailyOrderCriteria.getStartDate(), dailyOrderCriteria.getEndDate());
            Long offCustPay = offLineOrderRepository.findCustPayByMerchantAndDate(merchant.getId(), dailyOrderCriteria.getStartDate(), dailyOrderCriteria.getEndDate());
            Long trueScore = offLineOrderRepository.countMerchantOffScore(merchant.getId(), dailyOrderCriteria.getStartDate(), dailyOrderCriteria.getEndDate());
            Long truePay = offLineOrderRepository.countMerchantWxTransfer(merchant.getId(), dailyOrderCriteria.getStartDate(), dailyOrderCriteria.getEndDate());
            sumCell0.setCellValue(offCustScore*0.01);
            sumCell1.setCellValue(offCustPay*0.01);
            sumCell2.setCellValue(0L);
            sumCell3.setCellValue(0L);
            sumCell4.setCellValue(trueScore*0.01);
            sumCell5.setCellValue(truePay*0.01);
        }

    }
}
