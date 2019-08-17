package com.jifenke.lepluslive.merchant.controller.view;

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
 * Created by xf on 17-11-15.
 */
@Configuration
public class MerchantUserView extends AbstractExcelView {
    @Override
    protected void buildExcelDocument(Map<String, Object> map, HSSFWorkbook hssfWorkbook,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
        HSSFSheet sheet = hssfWorkbook.createSheet("list");
        setExcelHeader(sheet);

        List<Map> merDataList = (List<Map>) map.get("merList");
        setExcelRows(sheet, merDataList);
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
        excelHeader.createCell(0).setCellValue("商户编号");
        excelHeader.createCell(1).setCellValue("商户名称");
        excelHeader.createCell(2).setCellValue("所属城市");
        excelHeader.createCell(3).setCellValue("名下门店");
        excelHeader.createCell(4).setCellValue("锁定会员");
        excelHeader.createCell(5).setCellValue("负责人名称");
        excelHeader.createCell(6).setCellValue("负责人手机号");
        excelHeader.createCell(7).setCellValue("累计佣金收入");
        excelHeader.createCell(8).setCellValue("创建日期");
    }

    public void setExcelRows(HSSFSheet excelSheet, List<Map>  merDataList) {
        int record = 1;
        for (Map map : merDataList) {
            HSSFRow excelRow = excelSheet.createRow(record++);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            excelRow.createCell(0).setCellValue(String.valueOf(map.get("merchantNo")));
            excelRow.createCell(1).setCellValue(String.valueOf(map.get("merchantName")));
            excelRow.createCell(2).setCellValue(String.valueOf(map.get("cityName")));
            excelRow.createCell(3).setCellValue(String.valueOf(map.get("merchantCount")));
            excelRow.createCell(4).setCellValue(String.valueOf(map.get("totalBind"))+"/"+String.valueOf(map.get("totalLimit")));
            excelRow.createCell(5).setCellValue(String.valueOf(map.get("linkMan")));
            excelRow.createCell(6).setCellValue(String.valueOf(map.get("phoneNum")));
            excelRow.createCell(7).setCellValue(Long.valueOf(String.valueOf(map.get("totalCommission")))/100.0);
            excelRow.createCell(8).setCellValue(String.valueOf(map.get("createdDate")));
        }
    }
}
