package com.aim.questionnaire.common.utils;

import org.apache.poi.hssf.usermodel.*;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * Created by Administrator on 2018\9\12 0012.
 */
public class ExcelUtil {
    @Test
    public void test(){
        String[] title = new String[2];
        title[0] = "username";
        title[1] = "password";
        String[][] values = new String[2][2];
        values[0][0] = "1111";
        values[0][1] = "2222";
        values[1][0] = "3333";
        values[1][1] = "4444";
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        Date date = new Date();
        map.put("username",date);
        map.put("password","sdsad");
        list.add(map);
        ExcelWrite.createExcelXSSF("/Users/wuyong/Desktop/未命名3.xlsx","test",title);
        ExcelWrite.writeToExcelXSSF("/Users/wuyong/Desktop/未命名3.xlsx","test",list,title);
        //HSSFWorkbook hssfWorkbook= new HSSFWorkbook();
       // HSSFWorkbook test = getHSSFWorkbook("test",title,values,hssfWorkbook);
    }

    /**
     * 导出Excel
     * @param sheetName sheet名称
     * @param title 标题
     * @param values 内容
     * @param wb HSSFWorkbook对象
     * @return
     */
    public static HSSFWorkbook getHSSFWorkbook(String sheetName, String []title, String [][]values, HSSFWorkbook wb){

        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if(wb == null){
            wb = new HSSFWorkbook();
        }

        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);

        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(0);

        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

        //声明列对象
        HSSFCell cell = null;

        //创建标题
        for(int i=0;i<title.length;i++){
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }

        //创建内容
        for(int i=0;i<values.length;i++){
            row = sheet.createRow(i + 1);
            for(int j=0;j<values[i].length;j++){
                //将内容按顺序赋给对应的列对象
                row.createCell(j).setCellValue(values[i][j]);
            }
        }
        return wb;
    }
    public static HSSFWorkbook getHSSFWorkbook2(String sheetName, String []title, List<Map<String, Object>> list, HSSFWorkbook wb){

        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if(wb == null){
            wb = new HSSFWorkbook();
        }

        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);

        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(0);

        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

        //声明列对象
        HSSFCell cell = null;

        //创建标题
        for(int i=0;i<title.length;i++){
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }

        //创建内容
        for(int i=0;i<list.size();i++){
            row = sheet.createRow(i + 1);
            for(int j=0;j<title.length;j++){
                //将内容按顺序赋给对应的列对象
                row.createCell(j).setCellValue(list.get(i).get(title[j]).toString());
            }
        }
        return wb;
    }
}
