package com.example.utils;

import com.example.bean.WordBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.joda.time.DateTime;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by akun on 2018/5/30.
 */
@Slf4j
public class ExcelUtil {

    public static void createExcel(List<WordBean> list) {
        // 创建一个Excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 创建一个工作表
        HSSFSheet sheet = workbook.createSheet("单词统计");
        // 添加表头行
        HSSFRow hssfRow = sheet.createRow(0);
        // 设置单元格格式居中
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        // 添加表头内容
        HSSFCell headCell = hssfRow.createCell(0);
        headCell.setCellValue("单词");
        headCell.setCellStyle(cellStyle);

        headCell = hssfRow.createCell(1);
        headCell.setCellValue("频次");
        headCell.setCellStyle(cellStyle);


        // 添加数据内容
        for (int i = 0; i < list.size(); i++) {
            hssfRow = sheet.createRow(i + 1);
            WordBean wordBean = list.get(i);

            // 创建单元格，并设置值
            HSSFCell cell = hssfRow.createCell(0);
            cell.setCellValue(wordBean.getWorld());
            cell.setCellStyle(cellStyle);

            cell = hssfRow.createCell(1);
            cell.setCellValue(wordBean.getCount());
            cell.setCellStyle(cellStyle);
        }

        // 保存Excel文件
        try {
            String dateTime = new DateTime().toString("yyyy-MM-dd");
            String fileName = "/tmp/words_" + dateTime + ".xls";
            OutputStream outputStream = new FileOutputStream(fileName);
            workbook.write(outputStream);
            outputStream.close();
            log.info("excel文件写入成功 fileName:{}", fileName);
        } catch (Exception e) {
            log.error("", e);
        }
        log.info("excel文件写入完成");
    }

    public static void main(String[] args) {
        List<WordBean> list = new ArrayList<>(1);
        WordBean wordBean = new WordBean();
        wordBean.setWorld("guikun");
        wordBean.setCount(1);
        list.add(wordBean);

        ExcelUtil.createExcel(list);
    }

}
