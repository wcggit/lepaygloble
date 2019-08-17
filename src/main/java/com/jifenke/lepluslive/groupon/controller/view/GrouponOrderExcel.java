package com.jifenke.lepluslive.groupon.controller.view;

import com.jifenke.lepluslive.groupon.domain.entities.GrouponOrder;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * GrouponOrderExcel
 * 团购订单 Excel
 *
 * @author XF
 * @date 2017/6/19
 */
@Configuration
public class GrouponOrderExcel extends AbstractExcelView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HSSFSheet sheet = workbook.createSheet("list");
        setExcelHeader(sheet);
        List<GrouponOrder> orderList = (List<GrouponOrder>) model.get("orderList");
        setExcelRows(sheet, orderList);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String filename = sdf.format(new Date()) + ".xls";//设置下载时客户端Excel的名称
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + filename);

        OutputStream ouputStream = response.getOutputStream();
        workbook.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }

    public void setExcelHeader(HSSFSheet excelSheet) {
        HSSFRow excelHeader = excelSheet.createRow(0);
        excelHeader.createCell(0).setCellValue("订单编号");
        excelHeader.createCell(1).setCellValue("团购ID");
        excelHeader.createCell(2).setCellValue("团购名称");
        excelHeader.createCell(3).setCellValue("订单类型");
        excelHeader.createCell(4).setCellValue("数量");
        excelHeader.createCell(5).setCellValue("订单金额");
        excelHeader.createCell(6).setCellValue("鼓励金使用");
        excelHeader.createCell(7).setCellValue("实付金额");
        excelHeader.createCell(8).setCellValue("手续费");
        excelHeader.createCell(9).setCellValue("支付方式");
        excelHeader.createCell(10).setCellValue("送鼓励金");
        excelHeader.createCell(11).setCellValue("送金币");
        excelHeader.createCell(12).setCellValue("状态");
        excelHeader.createCell(13).setCellValue("下单时间");
        excelHeader.createCell(14).setCellValue("支付完成时间");
    }

    public void setExcelRows(HSSFSheet excelSheet, List<GrouponOrder> orderList) {
        int record = 1;
        for (GrouponOrder order : orderList) {
            HSSFRow excelRow = excelSheet.createRow(record++);
            excelRow.createCell(0).setCellValue(order.getOrderSid());
            if (order.getGrouponProduct() != null) {
                excelRow.createCell(1).setCellValue(order.getGrouponProduct().getSid());
                excelRow.createCell(2).setCellValue(order.getGrouponProduct().getName());
            }
            //  0 普通订单 1 乐加订单
            if (order.getOrderType() == 0) {
                excelRow.createCell(3).setCellValue("普通订单");
            } else if (order.getOrderType() == 1) {
                excelRow.createCell(3).setCellValue("乐加订单");
            }
            // 数量
            excelRow.createCell(4).setCellValue(order.getBuyNum());
            excelRow.createCell(5).setCellValue(order.getTotalPrice() / 100.0);
            excelRow.createCell(6).setCellValue(order.getScorea() / 100.0);
            excelRow.createCell(7).setCellValue(order.getTruePay() / 100.0);
            excelRow.createCell(8).setCellValue(new BigDecimal((order.getTruePay()/100.0) * 0.006).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
            // 支付方式 ： 0 公众号 1 app
            if (order.getPayOrigin() == 0) {
                excelRow.createCell(9).setCellValue("公众号");
            } else if (order.getPayOrigin() == 1) {
                excelRow.createCell(9).setCellValue("app");
            }
            //  送鼓励金
            excelRow.createCell(10).setCellValue(order.getRebateScorea() / 100.0);
            //  送金币
            excelRow.createCell(11).setCellValue(order.getRebateScorec() / 100.0);
            //  状态 orderState 0=待使用  1=已使用  2=退款
            if (order.getOrderState() == 0) {
                excelRow.createCell(12).setCellValue("待使用");
            } else if (order.getOrderState() == 1) {
                excelRow.createCell(12).setCellValue("已使用");
            } else {
                excelRow.createCell(12).setCellValue("退款");
            }
            // 下单时间
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            excelRow.createCell(13).setCellValue(sdf.format(order.getCreateDate()));
            //  支付完成时间
            if (order.getCompleteDate() != null) {
                excelRow.createCell(14).setCellValue(sdf.format(order.getCompleteDate()));
            } else {
                excelRow.createCell(14).setCellValue("-");
            }

        }

    }
}
