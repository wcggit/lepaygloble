package com.jifenke.lepluslive.cmbc.domain.criteria.view;

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
 * Created by xf on 17-10-11.
 */
@Configuration
public class CmbcSettlementExcel extends AbstractExcelView {
    @Override
    protected void buildExcelDocument(Map<String, Object> map, HSSFWorkbook hssfWorkbook,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
        HSSFSheet sheet = hssfWorkbook.createSheet("list");
        setExcelHeader(sheet);
        List<Map<String, Object>> list= (List<Map<String, Object>>) map.get("settlementList");
        setExcelRows(sheet, list);
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
        excelHeader.createCell(0).setCellValue("交易日期");
        excelHeader.createCell(1).setCellValue("状态");
        excelHeader.createCell(2).setCellValue("微信到账金额");
        excelHeader.createCell(3).setCellValue("支付宝到账金额");
        excelHeader.createCell(4).setCellValue("乐加到账金额");
        excelHeader.createCell(5).setCellValue("总入账");
        excelHeader.createCell(6).setCellValue("结算账户");
    }

    public void setExcelRows(HSSFSheet excelSheet, List<Map<String, Object>> settlementList) {
        int record = 1;
        for(Map<String,Object> map : settlementList) {
            HSSFRow excelRow = excelSheet.createRow(record++);
            excelRow.createCell(0).setCellValue(String.valueOf(map.get("settleDate")));
            Integer state = Integer.valueOf(String.valueOf(map.get("state")));
            if(state==0) {
                excelRow.createCell(1).setCellValue("待结算");
            }else if(state==1) {
                excelRow.createCell(1).setCellValue("已结算");
            }else{
                excelRow.createCell(1).setCellValue("已挂起");
            }
            Long wxActual = Long.valueOf(String.valueOf(map.get("wxActual")));
            Long aliActual = Long.valueOf(String.valueOf(map.get("aliActual")));
            Long leActual = Long.valueOf(String.valueOf(map.get("leActual")));
            Long totalActual = Long.valueOf(String.valueOf(map.get("totalActual")));
            excelRow.createCell(2).setCellValue(wxActual/100.0);
            excelRow.createCell(3).setCellValue(aliActual/100.0);
            excelRow.createCell(4).setCellValue(leActual/100.0);
            excelRow.createCell(5).setCellValue(totalActual/100.0);
            excelRow.createCell(6).setCellValue(String.valueOf(map.get("accNo"))+" "+String.valueOf(map.get("accName")));
        }
    }
}
