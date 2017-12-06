package com.jifenke.lepluslive.groupon.controller.view;

import com.jifenke.lepluslive.groupon.domain.entities.GrouponRefund;
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
 * GrouponRefundExcel
 * 退款单 Excel
 * @author XF
 * @date 2017/6/22
 */
@Configuration
public class GrouponRefundExcel extends AbstractExcelView {
    @Override
    protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HSSFSheet sheet = workbook.createSheet("list");
        setExcelHeader(sheet);
        List<GrouponRefund> refundList = (List<GrouponRefund>) model.get("refundList");
        setExcelRows(sheet, refundList);
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
        excelHeader.createCell(8).setCellValue("支付方式");
        excelHeader.createCell(9).setCellValue("送鼓励金");
        excelHeader.createCell(10).setCellValue("送金币");
        excelHeader.createCell(11).setCellValue("退款状态");
        excelHeader.createCell(12).setCellValue("下单时间");
        excelHeader.createCell(13).setCellValue("退款时间");
        excelHeader.createCell(14).setCellValue("退款完成时间");
    }

    public void setExcelRows(HSSFSheet excelSheet, List<GrouponRefund> orderList) {
        int record = 1;
        for (GrouponRefund refund : orderList) {
            HSSFRow excelRow = excelSheet.createRow(record++);
            excelRow.createCell(0).setCellValue(refund.getGrouponOrder().getOrderSid());
            if (refund.getGrouponOrder() != null) {
                excelRow.createCell(1).setCellValue(refund.getGrouponOrder().getGrouponProduct().getSid());
                excelRow.createCell(2).setCellValue(refund.getGrouponOrder().getGrouponProduct().getName());
            }
            //  0 普通订单 1 乐加订单
            if (refund.getGrouponOrder().getOrderType() == 0) {
                excelRow.createCell(3).setCellValue("普通订单");
            } else if (refund.getGrouponOrder().getOrderType() == 1) {
                excelRow.createCell(3).setCellValue("乐加订单");
            }
            // 数量
            excelRow.createCell(4).setCellValue(refund.getGrouponOrder().getBuyNum());
            excelRow.createCell(5).setCellValue(refund.getGrouponOrder().getTotalPrice() / 100.0);
            excelRow.createCell(6).setCellValue(refund.getGrouponOrder().getScorea() / 100.0);
            excelRow.createCell(7).setCellValue(refund.getGrouponOrder().getTruePay() / 100.0);
            // 支付方式 ： 0 公众号 1 app
            if (refund.getGrouponOrder().getPayOrigin() == 0) {
                excelRow.createCell(8).setCellValue("公众号");
            } else if (refund.getGrouponOrder().getPayOrigin() == 1) {
                excelRow.createCell(8).setCellValue("app");
            }
            //  送鼓励金
            excelRow.createCell(9).setCellValue(refund.getGrouponOrder().getRebateScorea() / 100.0);
            //  送金币
            excelRow.createCell(10).setCellValue(refund.getGrouponOrder().getRebateScorec() / 100.0);
            //  状态 o 退款中 1 退款完成 2 退款驳回
            if (refund.getState() == 0) {
                excelRow.createCell(11).setCellValue("退款中");
            } else if (refund.getState() == 1) {
                excelRow.createCell(11).setCellValue("退款完成");
            } else {
                excelRow.createCell(11).setCellValue("退款驳回");
            }
            // 下单时间
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            excelRow.createCell(12).setCellValue(sdf.format(refund.getGrouponOrder().getCreateDate()));
            excelRow.createCell(13).setCellValue(sdf.format(refund.getCreateDate()));
            //  支付完成时间
            if (refund.getCompleteDate() != null) {
                excelRow.createCell(14).setCellValue(sdf.format(refund.getCompleteDate()));
            } else {
                excelRow.createCell(14).setCellValue("-");
            }
        }

    }
}
