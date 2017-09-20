package com.jifenke.lepluslive.order.controller.view;

import com.jifenke.lepluslive.order.domain.entities.OffLineOrder;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
 * Created by xf on 17-8-28.
 */
@Configuration
public class OffLineOrderExcel extends AbstractExcelView {
    @Override
    protected void buildExcelDocument(Map<String, Object> map, HSSFWorkbook hssfWorkbook,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
        HSSFSheet sheet = hssfWorkbook.createSheet("list");
        setExcelHeader(sheet);

        List<OffLineOrder> orderList = (List<OffLineOrder>) map.get("olOrderList");
        setExcelRows(sheet, orderList);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String filename = sdf.format(new Date()) + ".xls";//设置下载时客户端Excel的名称
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + filename);

        OutputStream ouputStream = response.getOutputStream();
        hssfWorkbook.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();

    }

    public void setExcelHeader(HSSFSheet excelSheet) {
        HSSFRow excelHeader = excelSheet.createRow(0);
        excelHeader.createCell(0).setCellValue("订单号");
        excelHeader.createCell(1).setCellValue("交易门店");
        excelHeader.createCell(2).setCellValue("消费者");
        excelHeader.createCell(3).setCellValue("支付方式");
        excelHeader.createCell(4).setCellValue("确认码");
        excelHeader.createCell(5).setCellValue("交易完成时间");
        excelHeader.createCell(6).setCellValue("订单金额");
        excelHeader.createCell(7).setCellValue("实际入账");
        excelHeader.createCell(8).setCellValue("手续费率");
        excelHeader.createCell(9).setCellValue("订单类型");
        excelHeader.createCell(10).setCellValue("订单状态");
    }

    public void setExcelRows(HSSFSheet excelSheet, List<OffLineOrder> orderList) {
        int record = 1;
        for (OffLineOrder order : orderList) {
            HSSFRow excelRow = excelSheet.createRow(record++);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            excelRow.createCell(0).setCellValue(order.getOrderSid());
            excelRow.createCell(1).setCellValue(order.getMerchant().getName());
            if (order.getLeJiaUser() != null && order.getLeJiaUser().getUserName() != null) {
                excelRow.createCell(2).setCellValue(order.getLeJiaUser().getUserName());
            } else {
                excelRow.createCell(2).setCellValue("非会员");
            }
            excelRow.createCell(3).setCellValue(order.getPayWay().getPayWay());
            excelRow.createCell(4).setCellValue(order.getLepayCode());
            excelRow.createCell(5).setCellValue(order.getCompleteDate() == null ? "未完成的订单"
                : sdf
                .format(order.getCompleteDate()));
            excelRow.createCell(6)
                .setCellValue(order.getTotalPrice() / 100.0);
            excelRow.createCell(7).setCellValue(order.getTransferMoney()/100.0);
            if (order.getRebateWay() == 1 || order.getRebateWay() == 3) {
                double result = (100-order.getMerchant().getLjCommission().intValue())/10;
                excelRow.createCell(8).setCellValue((result)+"折");
            } else {
                excelRow.createCell(8).setCellValue(order.getWxCommission()/100.0);
            }
            if (order.getRebateWay() == 1 || order.getRebateWay() == 3) {
                excelRow.createCell(9).setCellValue("乐加订单");
            } else {
                excelRow.createCell(9).setCellValue("普通订单");
            }
            if (order.getState() == 1) {
                excelRow.createCell(10).setCellValue("已完成");
            } else if (order.getState() == 0) {
                excelRow.createCell(10).setCellValue("未完成");
            } else {
                excelRow.createCell(10).setCellValue("已退回");
            }
        }
    }
}
