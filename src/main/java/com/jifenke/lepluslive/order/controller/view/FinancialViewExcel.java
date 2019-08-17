package com.jifenke.lepluslive.order.controller.view;

import com.jifenke.lepluslive.order.domain.entities.FinancialStatistic;
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
public class FinancialViewExcel extends AbstractExcelView {

    @Override
    protected void buildExcelDocument(Map<String, Object> map, HSSFWorkbook hssfWorkbook,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
        HSSFSheet sheet = hssfWorkbook.createSheet("list");
        setExcelHeader(sheet);

        List<FinancialStatistic> financialList = (List<FinancialStatistic>) map.get("financialList");
        setExcelRows(sheet, financialList);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String filename =sdf.format(new Date())+".xls";//设置下载时客户端Excel的名称
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + filename);

        OutputStream ouputStream = response.getOutputStream();
        hssfWorkbook.write(ouputStream);
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

    public void setExcelRows(HSSFSheet excelSheet, List<FinancialStatistic> financialList) {
        int record = 1;
        for (FinancialStatistic financialStatistic : financialList) {
            HSSFRow excelRow = excelSheet.createRow(record++);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            excelRow.createCell(0).setCellValue(sdf.format(financialStatistic.getBalanceDate()));
            excelRow.createCell(1).setCellValue(financialStatistic.getState()==0?"转账中":"已到帐");
            excelRow.createCell(2).setCellValue(financialStatistic.getTransferPrice() / 100.0);
            excelRow.createCell(3)
                .setCellValue(
                    financialStatistic.getMerchant().getMerchantBank().getBankNumber()+" "+financialStatistic.getMerchant().getMerchantBank().getBankName());
        }
    }
}
