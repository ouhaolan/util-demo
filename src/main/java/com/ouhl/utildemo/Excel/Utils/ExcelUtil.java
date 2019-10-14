package com.ouhl.utildemo.Excel.Utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * excel 表格导出
 */
public class ExcelUtil {

    /**
     * 导出 Excel
     *
     * @param sheetName sheet名称
     * @param title     标题
     * @param values    内容
     * @param wb        HSSFWorkbook对象
     * @return
     */
    public static HSSFWorkbook getHSSFWorkbook(String sheetName, String[] title, String[][] values, HSSFWorkbook wb) {

        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if (wb == null) {
            wb = new HSSFWorkbook();
        }

        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);

        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(0);

        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);     //创建一个居中格式
        //声明列对象
        HSSFCell cell = null;

        //创建标题
        for (int i = 0; i < title.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }

        //创建内容
        for (int i = 0; i < values.length; i++) {
            row = sheet.createRow(i + 1);
            for (int j = 0; j < values[i].length; j++) {
                //将内容按顺序赋给对应的列对象
                row.createCell(j).setCellValue(values[i][j]);
            }
        }
        return wb;
    }

    /**
     * 导入 Excel
     *
     * @param file
     * @return
     */
    public static List<Object> getReadExport(MultipartFile file) {
        List<Object> rows = Collections.emptyList();    //读取的excel数据
        try {
            // 1.创建workbook对象，读取整个文档
            Workbook wb = null;                                 //excel 文件
            String fileName = file.getName();                   //excel 名称
            InputStream inputStream = file.getInputStream();    //excel 字节流

            // 2.判断 excel 文件类型，如果是xls，使用 HSSFWorkbook；如果是 xlsx，使用 XSSFWorkbook
            if(fileName.substring(fileName.indexOf("."),fileName.length()).equals(".xls")){
                wb = new HSSFWorkbook(inputStream);
            }else{
                wb = new XSSFWorkbook(inputStream);
            }

            // 3.读取页脚sheet
            Sheet sheetAt = wb.getSheetAt(0);

            // 4.循环读取行
            for (Row row : sheetAt) {
                List<Object> column = Collections.emptyList();    //读取的excel数据
                // 5.循环读取列
                for (int rowIndex = 0; rowIndex < row.getLastCellNum(); rowIndex++) {
                    rows.add(row.getCell(rowIndex).getStringCellValue());
                }
                rows.add(column);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            return rows;
        }
    }
}
