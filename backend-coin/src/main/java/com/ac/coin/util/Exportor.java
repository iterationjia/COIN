package com.ac.coin.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Exportor {
    String pathPrefix = "src\\main\\resources\\dict\\";

    public void export(){
        CSVLoader csvLoader = new CSVLoader();
        //company
        Set<String> contentSet = new HashSet<>();
        for(Map<String,Object> map:csvLoader.companyMap.values()) contentSet.add((String) map.get("name"));
        writer("company.txt",contentSet);
        contentSet.clear();
        //stock
        for(Map<String,Object> map:csvLoader.stockMap.values()) contentSet.add((String) map.get("name"));
        writer("stock.txt",contentSet);
        contentSet.clear();
        //ceo
        for (Map.Entry<Long,String> entry : csvLoader.ceoRel.entrySet()) contentSet.add(entry.getValue());
        writer("ceo.txt",contentSet);
        contentSet.clear();
        //legal_representative
        for (Map.Entry<Long,String> entry : csvLoader.legal_representativeRel.entrySet()) contentSet.add(entry.getValue());
        writer("legal_representative.txt",contentSet);
        contentSet.clear();
        //manager
        for (Map.Entry<Long, Set<String>> entry : csvLoader.managerRel.entrySet()){
            for(String name:entry.getValue()) contentSet.add(name);
        }
        writer("manager.txt",contentSet);
        contentSet.clear();
        //second_industry
        for(Map<String,Object> map:csvLoader.industryMap.values()) contentSet.add((String) map.get("name"));
        writer("second_industry.txt",contentSet);
        contentSet.clear();
        //first_industry
        for(Map<String,Object> map:csvLoader.firstIndustryMap.values()) contentSet.add((String) map.get("name"));
        writer("first_industry.txt",contentSet);
        contentSet.clear();
        //register_location
        for(Map<String,Object> map:csvLoader.companyMap.values()) contentSet.add((String) map.get("register_location"));
        writer("register_location.txt",contentSet);
        contentSet.clear();
        //average_education
        for(Map<String,Object> map:csvLoader.companyMap.values()) contentSet.add((String) map.get("average_education"));
        writer("average_education.txt",contentSet);
        contentSet.clear();
        //date
        for(Map<String,Map<String,Object>> oneStockMap:csvLoader.priceMap.values()){
            contentSet.addAll(oneStockMap.keySet());
        }
        writer("date.txt",contentSet);
        contentSet.clear();
    }

    public void writer(String name, Set<String> contentSet){
        try {
            contentSet.remove(null);
            String fullPath = pathPrefix+name;
            File file = new File(fullPath);
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();//创建文件夹 如：在f盘创建/TXT文件夹/testTXT/两个文件夹。
            }
            if(!file.exists()){
                file.createNewFile();//创建txt文件 如：testData.txt文件
            }
            //写入txt文件
            FileWriter fileWriter = new FileWriter(fullPath);
            BufferedWriter bw = new BufferedWriter(fileWriter);
            System.out.println(name);
            System.out.println(contentSet.size());
            boolean firstLine = true;
            for(String content:contentSet){
                if(firstLine) firstLine=false;
                else bw.newLine();
                bw.write(content);
            }
            fileWriter.flush();
            bw.close();
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
