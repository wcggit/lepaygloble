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
public class MerchantDataView extends AbstractExcelView {
    @Override
    protected void buildExcelDocument(Map<String, Object> map, HSSFWorkbook hssfWorkbook,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
        HSSFSheet sheet = hssfWorkbook.createSheet("list");
        setExcelHeader(sheet);

        List<Map> merDataList = (List<Map>) map.get("merDataList");
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
        excelHeader.createCell(0).setCellValue("门店编号");
        excelHeader.createCell(1).setCellValue("门店名称");
        excelHeader.createCell(2).setCellValue("所属城市");
        excelHeader.createCell(3).setCellValue("所属商户");
        excelHeader.createCell(4).setCellValue("协议类型");
        excelHeader.createCell(5).setCellValue("扫码费率");
        excelHeader.createCell(6).setCellValue("总流水");
        excelHeader.createCell(7).setCellValue("普通订单");
        excelHeader.createCell(8).setCellValue("乐加订单");
        excelHeader.createCell(9).setCellValue("时间内锁定会员");
        excelHeader.createCell(10).setCellValue("当前锁定情况");
    }

    public void setExcelRows(HSSFSheet excelSheet, List<Map>  merDataList) {
        int record = 1;
        for (Map map : merDataList) {
            HSSFRow excelRow = excelSheet.createRow(record++);
            excelRow.createCell(0).setCellValue(String.valueOf(map.get("mid")));
            excelRow.createCell(1).setCellValue(String.valueOf(map.get("merchantName")));
            excelRow.createCell(2).setCellValue(String.valueOf(map.get("cityName")));
            excelRow.createCell(3).setCellValue(String.valueOf(map.get("ownerMerchant")));
            Integer partnership = Integer.valueOf(String.valueOf(map.get("linkMan")));
            if(partnership==0) {
                excelRow.createCell(4).setCellValue("普通");
                excelRow.createCell(5).setCellValue("普通: "+String.valueOf(map.get("ljBrokerage"))+"% 联盟："+String.valueOf(map.get("ljCommission"))+"%");
            }else {
                excelRow.createCell(4).setCellValue("联盟");
                excelRow.createCell(5).setCellValue("普通: "+String.valueOf(map.get("ljBrokerage"))+"% 联盟："+String.valueOf(map.get("ljCommission"))+"%");
            }
            excelRow.createCell(6).setCellValue(String.valueOf(map.get("orderNum"))+"笔"+" ￥"+(Long.valueOf(String.valueOf(map.get("orderAmount")))/100.0));
            excelRow.createCell(7).setCellValue(String.valueOf(map.get("leOrderNum"))+"笔"+" ￥"+(Long.valueOf(String.valueOf(map.get("leOrderAmount")))/100.0));
            excelRow.createCell(8).setCellValue(String.valueOf(map.get("comOrderNum"))+"笔"+" ￥"+(Long.valueOf(String.valueOf(map.get("comOrderAmount")))/100.0));
            excelRow.createCell(9).setCellValue(String.valueOf(map.get("lockNum")));
            excelRow.createCell(10).setCellValue(String.valueOf(map.get("bindCount"))+"/"+String.valueOf(map.get("bindLimit")));
        }
    }
}
