package com.ac.coin.util;
import com.ac.coin.enums.Path;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExcelLoader {
    public Map<String, Map<String,Object>> priceStatisticMap;

    public ExcelLoader() throws IOException {
        priceStatisticMap = new HashMap<>();
        String path = Path.StockPath.getValue();
        File dir = new File(path);
        File[] files = dir.listFiles();
        String filePath;
        for (File file : files){
            filePath = file.toString();
            stockLoader(filePath);
        }
    }

    public void stockLoader(String path)throws IOException {
        //excel文件路径
        Workbook wb = null;
        try {
            File excel = new File(path);
            if (excel.isFile() && excel.exists()) {   //判断文件是否存在
                String[] split = excel.getName().split("\\.");  //.是特殊字符，需要转义！！！！！

                //根据文件后缀（xls/xlsx）进行判断
                if ( "xls".equals(split[1])){
                    FileInputStream fis = new FileInputStream(excel);   //文件流对象
                    wb = new HSSFWorkbook(fis);
                }else if ("xlsx".equals(split[1])){
                    wb = new XSSFWorkbook(excel);
                }else {
                    System.out.println("文件类型错误!");
                    return;
                }

                //开始解析
                Sheet sheet = wb.getSheetAt(0);     //读取sheet 0

                int firstRowIndex = sheet.getFirstRowNum();
                int lastRowIndex = sheet.getLastRowNum();

                for(int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {   //遍历行
                    Row row = sheet.getRow(rIndex);
                    if (row != null) {
                        Map<String,Object> map = new HashMap<>();
                        map.put("7_expected_price",Double.parseDouble(row.getCell(1).toString()));
                        map.put("7_minus_price",Double.parseDouble(row.getCell(2).toString()));
                        map.put("7_risk_price",Double.parseDouble(row.getCell(3).toString()));
                        map.put("7_pic_url",row.getCell(4).toString());
                        map.put("14_expected_price",Double.parseDouble(row.getCell(5).toString()));
                        map.put("14_minus_price",Double.parseDouble(row.getCell(6).toString()));
                        map.put("14_risk_price",Double.parseDouble(row.getCell(7).toString()));
                        map.put("14_pic_url",row.getCell(8).toString());
                        map.put("30_expected_price",Double.parseDouble(row.getCell(9).toString()));
                        map.put("30_minus_price",Double.parseDouble(row.getCell(10).toString()));
                        map.put("30_risk_price",Double.parseDouble(row.getCell(11).toString()));
                        map.put("30_pic_url",row.getCell(12).toString());
                        priceStatisticMap.put(row.getCell(0).toString(),map);
                    }
                }
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            assert wb != null;
            wb.close();
        }
    }

}
