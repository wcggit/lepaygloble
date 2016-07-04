package com.jifenke.lepluslive.order.controller.view;

import com.jifenke.lepluslive.order.domain.entities.OffLineOrder;

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
    excelHeader.createCell(0).setCellValue("订单编号");
    excelHeader.createCell(1).setCellValue("交易完成时间");
    excelHeader.createCell(2).setCellValue("商户名称");
    excelHeader.createCell(3).setCellValue("消费者信息");
    excelHeader.createCell(4).setCellValue("订单金额");
    excelHeader.createCell(5).setCellValue("红包使用");
    excelHeader.createCell(6).setCellValue("支付方式");
    excelHeader.createCell(7).setCellValue("实际支付");
    excelHeader.createCell(8).setCellValue("佣金");
    excelHeader.createCell(9).setCellValue("商户应入账");
    excelHeader.createCell(10).setCellValue("手续费");
    excelHeader.createCell(11).setCellValue("发放红包");
    excelHeader.createCell(12).setCellValue("分润金额");
    excelHeader.createCell(13).setCellValue("发放积分");
    excelHeader.createCell(14).setCellValue("状态");
  }

  public void setExcelRows(HSSFSheet excelSheet, List<OffLineOrder> orderList) {
    int record = 1;
    for (OffLineOrder order : orderList) {
      HSSFRow excelRow = excelSheet.createRow(record++);
      excelRow.createCell(0).setCellValue(order.getOrderSid());
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      excelRow.createCell(1).setCellValue(order.getCompleteDate() == null ? "未完成的订单"
                                                                          : sdf
                                              .format(order.getCompleteDate()));
      excelRow.createCell(2).setCellValue(
          order.getMerchant().getName() + "(" + order.getMerchant().getMerchantSid() + ")");
      if (order.getLeJiaUser().getPhoneNumber() == null) {
        excelRow.createCell(3).setCellValue("未绑定手机号(" + order.getLeJiaUser().getUserSid() + ")");
      } else {
        excelRow.createCell(3).setCellValue(
            order.getLeJiaUser().getPhoneNumber() + "(" + order.getLeJiaUser().getUserSid() + ")");
      }
      excelRow.createCell(4).setCellValue(order.getTotalPrice() / 100.0);
      excelRow.createCell(5).setCellValue(order.getTrueScore() / 100.0);
      excelRow.createCell(6).setCellValue(order.getPayWay().getPayWay());
      excelRow.createCell(7).setCellValue(order.getTruePay() / 100.0);
      excelRow.createCell(8).setCellValue(order.getLjCommission() / 100.0);
      excelRow.createCell(9)
          .setCellValue(order.getTransferMoney()/100.0);
      excelRow.createCell(10).setCellValue(order.getWxCommission() / 100.0);
      excelRow.createCell(11).setCellValue(order.getRebate() / 100.0);
      if(order.getRebateWay()!=1){
        excelRow.createCell(12).setCellValue(0);
      }else {
        excelRow.createCell(12).setCellValue(
            (order.getLjCommission() - order.getWxCommission() - order.getRebate()) / 100.0);
      }
      excelRow.createCell(13).setCellValue(order.getScoreB());
      if (order.getState() == 0) {
        excelRow.createCell(14).setCellValue("未支付");
      } else {
        excelRow.createCell(14).setCellValue("已支付");
      }

    }
  }
}
