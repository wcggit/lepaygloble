package com.jifenke.lepluslive.order.controller.view;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantScanPayWay;
import com.jifenke.lepluslive.order.domain.criteria.DailyOrderCriteria;
import com.jifenke.lepluslive.order.domain.entities.OffLineOrder;
import com.jifenke.lepluslive.order.domain.entities.ScanCodeOrder;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by xf on 17-1-18.
 */
@Configuration
public class MerchantOrderExcel extends AbstractExcelView {

    @Override
    protected void buildExcelDocument(Map<String, Object> map, HSSFWorkbook hssfWorkbook,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
        // 获取数据
        String[] titles1 = (String[]) map.get("titles1");
        String[] titles2 = (String[]) map.get("titles2");
        String[] titles3 = (String[]) map.get("titles3");
        DailyOrderCriteria dailyOrderCriteria = (DailyOrderCriteria) map.get("dailyOrderCriteria");
        MerchantScanPayWay payWay = (MerchantScanPayWay) map.get("payWay");
        List<OffLineOrder> offLineOrders = (List<OffLineOrder>) map.get("offLineOrders");
        List<ScanCodeOrder> scanCodeOrders = (List<ScanCodeOrder>) map.get("scanCodeOrders");
        /*List<ScanCodeRefundOrder> refoundOrder = (List<ScanCodeRefundOrder>) map.get("refoundOrder");*/
        Long custScore = (Long) map.get("custScore");
        Long custPay = (Long) map.get("custPay");
        Long trueScore =(Long) map.get("trueScore");
        Long truePay = (Long) map.get("truePay");
        List<Object[]> refound = (List<Object[]>) map.get("refound");
        Long offCustScore = (Long) map.get("offCustScore");
        Long offCustPay = (Long) map.get("offCustPay");
        Long offtrueScore = (Long) map.get("offtrueScore");
        Long offtruePay = (Long) map.get("offtruePay");
        //  设置
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String filename = sdf.format(new Date()) + ".xls";//设置下载时客户端Excel的名称
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + filename);
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
        if (payWay == null||(payWay!=null&&payWay.getType()==1)) {                          // 乐加订单结算
            if (offLineOrders != null && offLineOrders.size() > 0) {
                for (OffLineOrder offLineOrder : offLineOrders) {
                    HSSFRow contentRow = sheet.createRow(sheet.getLastRowNum() + 1);
                    // 订单编号
                    HSSFCell contentCell0 = contentRow.createCell(0);
                    contentCell0.setCellValue(offLineOrder.getOrderSid());
                    // 交易完成时间
                    HSSFCell contentCell1 = contentRow.createCell(1);
                    contentCell1.setCellValue(sdf.format(offLineOrder.getCompleteDate()));
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
                    contentCell6.setCellValue(new Double((offLineOrder.getTransferMoneyFromTruePay()) * 0.01));
                    // 订单类型
                    HSSFCell contentCell7 = contentRow.createCell(7);
                    if (offLineOrder.getBasicType() == 0) {
                        contentCell7.setCellValue("普通订单");
                    } else if (offLineOrder.getBasicType() == 1) {
                        contentCell7.setCellValue("乐加订单");
                    }
                    //  导流订单和会员订单（佣金费率） 显示折扣
                    HSSFCell contentCell8 = contentRow.createCell(8);
                    HSSFCell contentCell9 = contentRow.createCell(9);
                    if (offLineOrder.getBasicType() == 1) {
                        Long wxCommission = offLineOrder.getWxCommission();
                        String wxDiscount = new Double((100L - wxCommission) * 0.1).toString();
                        contentCell8.setCellValue(wxDiscount);
                        Long scoreCommission = (offLineOrder.getLjCommission() - offLineOrder.getWxCommission());
                        String scoreDiscount = new Double((100L - scoreCommission) * 0.1).toString();
                        contentCell9.setCellValue(scoreDiscount);
                    } else {
                        contentCell8.setCellValue(offLineOrder.getWxCommission());
                        String scoreCommission = new Double(offLineOrder.getLjCommission() - offLineOrder.getWxCommission()).toString();
                        contentCell9.setCellValue(scoreCommission);
                    }
                    // 总入账金额
                    HSSFCell contentCell10 = contentRow.createCell(10);
                    contentCell10.setCellValue(offLineOrder.getTransferMoney() * 0.01);
                    //  微信支付入账
                    HSSFCell contentCell11 = contentRow.createCell(11);
                    contentCell11.setCellValue(offLineOrder.getTransferMoneyFromTruePay() * 0.01);
                    //  红包支付入账
                    HSSFCell contentCell12 = contentRow.createCell(12);
                    contentCell12.setCellValue((offLineOrder.getTransferMoney() - offLineOrder.getTransferMoneyFromTruePay() * 0.01));
                    //  退款时间
                    HSSFCell contentCell13 = contentRow.createCell(13);
                    if (offLineOrder.getState() != 2) {
                        contentCell13.setCellValue(" ~ ");
                    } else {
                        contentCell13.setCellValue(offLineOrder.getCompleteDate());
                    }
                }
            }
        } else {                                                                        // 易宝通道结算
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
                // 付款方式
                HSSFCell contentCell3 = contentRow.createCell(3);
                if (scanCodeOrder.getScanCodeOrderExt().getPayment() == 0) {
                    contentCell3.setCellValue("纯现金");
                } else if (scanCodeOrder.getScanCodeOrderExt().getPayment() == 1) {
                    contentCell3.setCellValue("纯鼓励金");
                } else if (scanCodeOrder.getScanCodeOrderExt().getPayment() == 2) {
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
                contentCell6.setCellValue(new Double((scanCodeOrder.getTransferMoneyFromTruePay()) * 0.01));
                // 订单类型
                HSSFCell contentCell7 = contentRow.createCell(7);
                contentCell7.setCellValue(scanCodeOrder.getOrderType());
                //  导流订单和会员订单（佣金费率） 显示折扣
                HSSFCell contentCell8 = contentRow.createCell(8);
                HSSFCell contentCell9 = contentRow.createCell(9);
                if (scanCodeOrder.getOrderType() == 12005 || scanCodeOrder.getOrderType() == 12003) {
                    Long wxCommission = scanCodeOrder.getWxCommission();
                    String wxDiscount = new Double((100L - wxCommission) * 0.1).toString();
                    contentCell8.setCellValue(wxDiscount);
                    Long scoreCommission = (scanCodeOrder.getCommission() - scanCodeOrder.getWxCommission());
                    String scoreDiscount = new Double((100L - scoreCommission) * 0.1).toString();
                    contentCell9.setCellValue(scoreDiscount);
                } else {
                    contentCell8.setCellValue(scanCodeOrder.getWxCommission());
                    String scoreCommission = new Double(scanCodeOrder.getCommission() - scanCodeOrder.getWxCommission()).toString();
                    contentCell9.setCellValue(scoreCommission);
                }
                // 总入账金额
                HSSFCell contentCell10 = contentRow.createCell(10);
                contentCell10.setCellValue(scanCodeOrder.getTransferMoney() * 0.01);
                //  微信支付入账
                HSSFCell contentCell11 = contentRow.createCell(11);
                contentCell11.setCellValue(scanCodeOrder.getTransferMoneyFromTruePay() * 0.01);
                //  红包支付入账
                HSSFCell contentCell12 = contentRow.createCell(12);
                contentCell12.setCellValue((scanCodeOrder.getTransferMoney() - scanCodeOrder.getTransferMoneyFromTruePay() * 0.01));
                //  退款时间
                HSSFCell contentCell13 = contentRow.createCell(13);
                if (scanCodeOrder.getState() != 2) {
                    contentCell13.setCellValue(" ~ ");
                } else {
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
          /*  if (refoundOrder != null && refoundOrder.size() > 0) {
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
                    contentCell3.setCellValue(scanCodeOrder.getOrderType());
                    // 订单完成时间
                    HSSFCell contentCell4 = contentRow.createCell(4);
                    contentCell4.setCellValue(scanCodeOrder.getCompleteDate());
                    // 微信渠道退款
                    HSSFCell contentCell5 = contentRow.createCell(5);
                    contentCell5.setCellValue(scanCodeOrder.getTransferMoney() * 0.01);
                    // 红包渠道退款
                    HSSFCell contentCell6 = contentRow.createCell(6);
                    contentCell6.setCellValue(scanCodeOrder.getTrueScore() * 0.01);
                    // 微信支付
                    HSSFCell contentCell7 = contentRow.createCell(7);
                    contentCell7.setCellValue(scanCodeOrder.getTransferMoneyFromTruePay() * 0.01);
                    // 红包支付少转账
                    HSSFCell contentCell8 = contentRow.createCell(8);
                    contentCell8.setCellValue(scanCodeOrder.getTransferMoneyFromScore() * 0.01);
                }
            }*/
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
        HSSFCell sumCell0 = cotentSum.createCell(0);
        HSSFCell sumCell1 = cotentSum.createCell(1);
        HSSFCell sumCell2 = cotentSum.createCell(2);
        HSSFCell sumCell3 = cotentSum.createCell(3);
        HSSFCell sumCell4 = cotentSum.createCell(4);
        HSSFCell sumCell5 = cotentSum.createCell(5);
        if (payWay != null) {
            sumCell0.setCellValue(custScore * 0.01);
            sumCell1.setCellValue(custPay * 0.01);
            if (refound != null) {
                Object[] obj = refound.get(0);
                Long refundMoney = new Long(obj[0].toString());
                Long refundScore = new Long(obj[1].toString());
                sumCell2.setCellValue(refundMoney * 0.01);
                sumCell3.setCellValue(refundScore * 0.01);
            } else {
                sumCell2.setCellValue(0L);
                sumCell3.setCellValue(0L);
            }
            sumCell4.setCellValue(trueScore * 0.01);
            sumCell5.setCellValue(truePay * 0.01);
        } else {
            sumCell0.setCellValue(offCustScore * 0.01);
            sumCell1.setCellValue(offCustPay * 0.01);
            sumCell2.setCellValue(0L);
            sumCell3.setCellValue(0L);
            sumCell4.setCellValue(offtrueScore * 0.01);
            sumCell5.setCellValue(offtruePay * 0.01);
        }
        OutputStream ouputStream = response.getOutputStream();
        workbook.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }
}
