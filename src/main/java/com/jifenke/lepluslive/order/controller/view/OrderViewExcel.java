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
 * Created by wcg on 16/5/9.
 */
@Configuration
public class OrderViewExcel extends AbstractExcelView {

    @Override
    protected void buildExcelDocument(Map<String, Object> map, HSSFWorkbook hssfWorkbook,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
        HSSFSheet sheet = hssfWorkbook.createSheet("list");
        setExcelHeader(sheet);

        List<OffLineOrder> orderList = (List<OffLineOrder>) map.get("orderList");
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
        excelHeader.createCell(0).setCellValue("交易完成时间");
        excelHeader.createCell(1).setCellValue("交易单号");
        excelHeader.createCell(2).setCellValue("订单类型");
        excelHeader.createCell(3).setCellValue("金额");
        excelHeader.createCell(4).setCellValue("支付方式");
        excelHeader.createCell(5).setCellValue("手续费(会员折扣费)");
        excelHeader.createCell(6).setCellValue("到账收入");
    }

    public void setExcelRows(HSSFSheet excelSheet, List<OffLineOrder> orderList) {
        int record = 1;
        for (OffLineOrder order : orderList) {
            HSSFRow excelRow = excelSheet.createRow(record++);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            excelRow.createCell(0).setCellValue(order.getCompleteDate() == null ? "未完成的订单"
                                                                                : sdf
                                                    .format(order.getCompleteDate()));
            excelRow.createCell(1).setCellValue(order.getOrderSid());
            if (order.getBasicType() == 0) {
                excelRow.createCell(2).setCellValue("普通订单");
            } else if (order.getBasicType() == 1) {
                excelRow.createCell(2).setCellValue("乐加订单");
            }
            excelRow.createCell(3).setCellValue(order.getTotalPrice() / 100.0);
            excelRow.createCell(4).setCellValue(order.getPayWay().getPayWay());
            excelRow.createCell(5).setCellValue(order.getLjCommission() / 100.0);
            excelRow.createCell(6)
                .setCellValue(order.getTransferMoney() / 100.0);
        }
    }
}
