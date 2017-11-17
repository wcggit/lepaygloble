package com.jifenke.lepluslive.partner.controller.view;

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
 * Created by xf on 17-11-16.
 */
@Configuration
public class PartnerWalletLogExcel extends AbstractExcelView {
    @Override
    protected void buildExcelDocument(Map<String, Object> map, HSSFWorkbook hssfWorkbook,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
        HSSFSheet sheet = hssfWorkbook.createSheet("list");
        setExcelHeader(sheet);
        List<Map> data = (List<Map>) map.get("data");
        setExcelRows(sheet, data);
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
        excelHeader.createCell(0).setCellValue("服务流水号");
        excelHeader.createCell(1).setCellValue("变更时间");
        excelHeader.createCell(2).setCellValue("变更值");
        excelHeader.createCell(3).setCellValue("变更来源");
        excelHeader.createCell(4).setCellValue("关联业务单号");
        excelHeader.createCell(5).setCellValue("变更的余额");
        excelHeader.createCell(6).setCellValue("变更后余额");
    }

    public void setExcelRows(HSSFSheet excelSheet, List<Map> merDataList) {
        int record = 1;
        for (Map map : merDataList) {
            HSSFRow excelRow = excelSheet.createRow(record++);
            excelRow.createCell(0).setCellValue(String.valueOf(map.get("logId")));
            excelRow.createCell(1).setCellValue(String.valueOf(map.get("changeDate")));
            excelRow.createCell(2).setCellValue(Long.valueOf(String.valueOf(map.get("changeMoney")==null?0:map.get("changeMoney")))/100.0);
            excelRow.createCell(3).setCellValue(String.valueOf(map.get("changeOrigin")==null?"锁定会员消费":map.get("changeOrigin")));
            excelRow.createCell(4).setCellValue(String.valueOf(map.get("orderSid")));
            excelRow.createCell(5).setCellValue(Long.valueOf(String.valueOf(map.get("beforeChange")==null?0:map.get("beforeChange")))/100.0);
            excelRow.createCell(6).setCellValue(Long.valueOf(String.valueOf(map.get("afterChange")==null?0:map.get("afterChange")))/100.0);
        }
    }
}
