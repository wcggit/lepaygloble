package com.jifenke.lepluslive.groupon.controller.view;

import com.jifenke.lepluslive.groupon.domain.entities.GrouponCode;
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
 * GrouponCodeExcel
 * 团购券码 Excel
 * @author XF
 * @date 2017/6/19
 */
@Configuration
public class GrouponCodeExcel extends AbstractExcelView {
    @Override
    protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HSSFSheet sheet = workbook.createSheet("list");
        setExcelHeader(sheet);
        List<GrouponCode> codeList = (List<GrouponCode>) model.get("codeList");
        setExcelRows(sheet, codeList);
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
        excelHeader.createCell(0).setCellValue("券码");
        excelHeader.createCell(1).setCellValue("订单编号");
        excelHeader.createCell(2).setCellValue("团购ID");
        excelHeader.createCell(3).setCellValue("团购名称");
        excelHeader.createCell(4).setCellValue("订单类型");
        excelHeader.createCell(5).setCellValue("团购价格");
        excelHeader.createCell(6).setCellValue("佣金");
        excelHeader.createCell(7).setCellValue("应到账金额");
        excelHeader.createCell(8).setCellValue("层级分润金额");
        excelHeader.createCell(9).setCellValue("积分客分润金额");
        excelHeader.createCell(10).setCellValue("核销门店");
        excelHeader.createCell(11).setCellValue("核销员");
        excelHeader.createCell(12).setCellValue("状态");
        excelHeader.createCell(13).setCellValue("下单时间");
        excelHeader.createCell(14).setCellValue("核销时间");
    }

    public void setExcelRows(HSSFSheet excelSheet, List<GrouponCode> orderList) {
        int record = 1;
        for (GrouponCode code : orderList) {
            HSSFRow excelRow = excelSheet.createRow(record++);
            excelRow.createCell(0).setCellValue(code.getSid().substring(4) + "****" + code.getSid().substring(code.getSid().length() - 3, code.getSid().length() - 1));
            if (code.getGrouponOrder() != null) {
                excelRow.createCell(1).setCellValue(code.getGrouponOrder().getOrderSid());
            }
            if (code.getGrouponProduct() != null) {
                excelRow.createCell(2).setCellValue(code.getGrouponProduct().getSid());
                excelRow.createCell(3).setCellValue(code.getGrouponProduct().getName());
            }
            //  0 普通订单 1 乐加订单
            if (code.getGrouponOrder().getOrderType() == 0) {
                excelRow.createCell(4).setCellValue("普通订单");
            } else if (code.getGrouponOrder().getOrderType() == 1) {
                excelRow.createCell(4).setCellValue("乐加订单");
            }
            //  团购价格
            if (code.getGrouponOrder().getOrderType() == 0) {
                excelRow.createCell(5).setCellValue(code.getGrouponProduct().getNormalPrice() / 100.0);
            } else if (code.getGrouponOrder().getOrderType() == 1) {
                excelRow.createCell(5).setCellValue(code.getGrouponProduct().getLjPrice() / 100.0);
            }
            //  佣金
            excelRow.createCell(6).setCellValue(code.getCommission() / 100.0);
            //  应到账金额
            excelRow.createCell(7).setCellValue(code.getTrasnferMoney() / 100.0);
            // 层级分润金额
            excelRow.createCell(8).setCellValue((code.getShareToLockMerchant() + code.getShareToLockPartner() + code.getShareToTradePartner() + code.getShareToLockPartnerManager() + code.getShareToTradePartnerManager()) / 100.0);
            // 积分客分润金额
            excelRow.createCell(9).setCellValue((code.getCommission() - (code.getShareToLockMerchant() + code.getShareToLockPartner() + code.getShareToTradePartner() + code.getShareToLockPartnerManager() + code.getShareToTradePartnerManager()+(code.getGrouponOrder().getRebateScorea()+code.getGrouponOrder().getRebateScorec())*0.5)) / 100.0);
            //  核销门店
            if (code.getState() == 1) {
                excelRow.createCell(10).setCellValue(code.getMerchant().getName());
                excelRow.createCell(11).setCellValue(code.getMerchantUser());
            } else {
                excelRow.createCell(10).setCellValue("-");
                excelRow.createCell(11).setCellValue("-");
            }
            //  状态  //-1 未付款 0 未使用  1 已使用 2 退款中 3 已退款  4过期
            if (code.getState() == -1) {
                excelRow.createCell(12).setCellValue("未付款");
            } else if (code.getState() == 0) {
                excelRow.createCell(12).setCellValue("未使用");
            } else if (code.getState() == 1) {
                excelRow.createCell(12).setCellValue("已使用");
            } else if (code.getState() == 2) {
                excelRow.createCell(12).setCellValue("退款中");
            } else if (code.getState() == 3) {
                excelRow.createCell(12).setCellValue("已退款");
            } else if (code.getState() == 4) {
                excelRow.createCell(12).setCellValue("过期");
            }
            // 下单时间
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            excelRow.createCell(13).setCellValue(sdf.format(code.getCreateDate()));
            //  核销时间
            if (code.getCheckDate() != null) {
                excelRow.createCell(14).setCellValue(sdf.format(code.getCheckDate()));
            } else {
                excelRow.createCell(14).setCellValue("-");
            }

        }

    }

}
