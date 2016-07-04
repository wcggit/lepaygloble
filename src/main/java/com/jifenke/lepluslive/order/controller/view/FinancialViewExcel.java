package com.jifenke.lepluslive.order.controller.view;

import com.jifenke.lepluslive.order.domain.entities.FinancialStatistic;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    excelHeader.createCell(0).setCellValue("结算单号");
    excelHeader.createCell(1).setCellValue("结算日期");
    excelHeader.createCell(2).setCellValue("转账日期");
    excelHeader.createCell(3).setCellValue("商户信息");
    excelHeader.createCell(4).setCellValue("绑定银行卡");
    excelHeader.createCell(5).setCellValue("开户行");
    excelHeader.createCell(6).setCellValue("收款人");
    excelHeader.createCell(7).setCellValue("待转账金额");
  }

  public void setExcelRows(HSSFSheet excelSheet, List<FinancialStatistic> financialList) {
    int record = 1;
    for (FinancialStatistic financialStatistic : financialList) {
      HSSFRow excelRow = excelSheet.createRow(record++);
      excelRow.createCell(0).setCellValue(financialStatistic.getStatisticId());
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      excelRow.createCell(1).setCellValue(sdf.format(financialStatistic.getBalanceDate()));
      excelRow.createCell(2).setCellValue(financialStatistic.getTransferDate()==null?"结算未完成":sdf.format(financialStatistic.getTransferDate()));
      excelRow.createCell(3).setCellValue(
          financialStatistic.getMerchant().getName() + "(" + financialStatistic.getMerchant()
              .getMerchantSid() + ")");
      excelRow.createCell(4).setCellValue(
          financialStatistic.getMerchant().getMerchantBank().getBankNumber());
      excelRow.createCell(5)
          .setCellValue(financialStatistic.getMerchant().getMerchantBank().getBankName());
      excelRow.createCell(6).setCellValue(financialStatistic.getMerchant().getPayee());
      excelRow.createCell(7).setCellValue(financialStatistic.getTransferPrice() / 100.0);

    }
  }
}
