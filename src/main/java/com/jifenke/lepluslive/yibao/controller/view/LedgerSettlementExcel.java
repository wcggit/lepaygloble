package com.jifenke.lepluslive.yibao.controller.view;

import com.jifenke.lepluslive.yibao.domain.entities.LedgerSettlement;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 通道结算单报表
 * Created by xf on 17-7-14.
 */
@Configuration
public class LedgerSettlementExcel extends AbstractExcelView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook,
                                      HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        HSSFSheet sheet = workbook.createSheet("list");
        setExcelHeader(sheet);
        List<LedgerSettlement>
            settlementList =
            (List<LedgerSettlement>) model.get("ledgerSettlementList");
        setExcelRows(sheet, settlementList);
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
        excelHeader.createCell(0).setCellValue("交易日期");
        excelHeader.createCell(1).setCellValue("状态");
        excelHeader.createCell(2).setCellValue("到账金额");
        excelHeader.createCell(3).setCellValue("结算账户");
    }

    public void setExcelRows(HSSFSheet excelSheet, List<LedgerSettlement> settlementList) {
        int record = 1;
        for (LedgerSettlement settlement : settlementList) {
            HSSFRow excelRow = excelSheet.createRow(record++);
            excelRow.createCell(0).setCellValue(getDateStrBefore(settlement.getTradeDate()));
            if (settlement.getState() == 0) {
                excelRow.createCell(1).setCellValue("划款中");
            } else if (settlement.getState() == 1) {
                excelRow.createCell(1).setCellValue("划款成功");
            } else if (settlement.getState() == 2) {
                excelRow.createCell(1).setCellValue("划款失败");
            } else if (settlement.getState() == 3) {
                excelRow.createCell(1).setCellValue("划款中");
            } else if (settlement.getState() == 4) {
                excelRow.createCell(1).setCellValue("划款中");
            } else if (settlement.getState() == -1) {
                excelRow.createCell(1).setCellValue("划款失败");
            } else if (settlement.getState() == -2) {
                excelRow.createCell(1).setCellValue("划款失败");
            } else {
                excelRow.createCell(1).setCellValue("待查询");
            }
            excelRow.createCell(2).setCellValue(settlement.getTotalTransfer() / 100.0);
            excelRow.createCell(3).setCellValue(settlement.getBankAccountNumber() + " " + settlement.getAccountName()); // 实际转账金额
        }
    }

    public String getDateStrBefore(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(dateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, -1);
            date = calendar.getTime();
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
