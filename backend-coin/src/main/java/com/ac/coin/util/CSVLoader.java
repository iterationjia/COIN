package com.ac.coin.util;

import com.ac.coin.enums.Path;

import java.io.*;
import java.util.*;

public class CSVLoader {
    public Map<Long,Map<String,Object>> companyMap;
    public Map<String,Map<String,Object>> personMap;
    public Map<String,Map<String,Object>> industryMap;
    public Map<String,Map<String,Object>> firstIndustryMap;

    public Map<String,Map<String,Object>> stockMap;
    public Map<String,Map<String,Map<String,Object>>> priceMap;
    public Map<String,Map<String,Object>> priceStatisticMap;

    public Map<Long,String> ceoRel;
    public Map<Long,String> legal_representativeRel;
    public Map<Long, Set<String>> managerRel;
    public Map<Long,String> industryRel;
    public Map<String, String> firstIndustryRel;

    public Map<String,Long> stockCompanyRel;
    public Map<String,String> stockPriceStatisticRel;
    public Map<String,String> pricePriceStatisticRel;

    public CSVLoader(){
        companyMap = new HashMap<>();
        personMap = new HashMap<>();
        industryMap = new HashMap<>();
        firstIndustryMap = new HashMap<>();
        stockMap = new HashMap<>();
        priceMap = new HashMap<>();
        priceStatisticMap = new HashMap<>();

        ceoRel = new HashMap<>();
        legal_representativeRel = new HashMap<>();
        industryRel = new HashMap<>();
        firstIndustryRel = new HashMap<>();
        managerRel = new HashMap<>();
        stockCompanyRel = new HashMap<>();
        stockPriceStatisticRel = new HashMap<>();
        pricePriceStatisticRel = new HashMap<>();

        String pathPrefix = Path.JoinQuaintPath.getValue();
        company_basic_loader(pathPrefix+"company_basic.csv");
        company_employees_loader(pathPrefix+"company_employees.csv");
        company_managers_loader(pathPrefix+"company_managers.csv");
        stock_basic_loader(pathPrefix+"stock_basic.csv");
        stock_st_loader(pathPrefix+"stock_st.csv");
        stock_price_loader(pathPrefix+"stock_price");
    }

    public int getLineNumber(String path) {
        try {
            FileReader fileReader = new FileReader(new File(path));
            LineNumberReader lineNumberReader = new LineNumberReader(fileReader);
            lineNumberReader.skip(Long.MAX_VALUE);
            int lines = lineNumberReader.getLineNumber();
            fileReader.close();
            lineNumberReader.close();
            return lines;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void company_basic_loader(String path){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));//换成你的文件名
            reader.readLine();//第一行信息，为标题信息，不用，如果需要，注释掉
            String line;
            while((line=reader.readLine())!=null){
                line=line+",eof";
                String[] item = line.split(",");//CSV格式文件为逗号分隔符文件，这里根据逗号切分

                boolean isEmpty = false;
                StringBuilder zero = new StringBuilder();
                for(int i=0;i<6-item[2].length();i++) zero.append("0");
                item[2] = zero+item[2];
                for(int i=0;i<=12;i++){
                    if(item[i].length()==0 && i!=3 && i!=4){
                        isEmpty = true;
                        break;
                    }
                }
                if(isEmpty) continue;
                if(item[2].charAt(0)<'0'||item[2].charAt(0)>'9') continue;

                Map<String,Object> company = new HashMap<>();
                company.put("company_id",Long.parseLong(item[0]));
                company.put("name",item[1]);
                company.put("register_location",item[6]);
                if(item[7].charAt(0)<'0'||item[7].charAt(0)>'9') continue;
                company.put("register_capital",Double.parseDouble(item[7]));
                company.put("establish_date",item[8]);

                companyMap.put((Long) company.get("company_id"),company);

                Map<String,Object> legal_representative = new HashMap<>();
                legal_representative.put("name",item[5]);
                personMap.put((String) legal_representative.get("name"),legal_representative);
                legal_representativeRel.put((Long) company.get("company_id"),(String) legal_representative.get("name"));

                Map<String,Object> ceo = new HashMap<>();
                ceo.put("name",item[12]);
                personMap.put((String) ceo.get("name"),ceo);
                ceoRel.put((Long) company.get("company_id"),(String) ceo.get("name"));

                Map<String,Object> industry = new HashMap<>();
                industry.put("name",item[11]);
                industryMap.put((String) industry.get("name"),industry);
                industryRel.put((Long) company.get("company_id"),(String) industry.get("name"));

                Map<String,Object> firstIndustry = new HashMap<>();
                firstIndustry.put("name",item[10]);
                firstIndustryMap.put((String) firstIndustry.get("name"),firstIndustry);
                firstIndustryRel.put((String) firstIndustry.get("name"),(String) industry.get("name"));

                String stock_code = item[2];
                if(!stockCompanyRel.containsKey(stock_code)){
                    stockCompanyRel.put(stock_code,(Long) company.get("company_id"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void company_employees_loader(String path){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            reader.readLine();
            String line;
            while((line=reader.readLine())!=null){
                line=line+",eof";
                String[] item = line.split(",");
                Long company_id = Long.parseLong(item[1]);
                if(companyMap.containsKey(company_id)) {
                    if (item[6].length() != 0) {
                        int employee_num = (int) Float.parseFloat(item[6]);
                        int retire_num = (item[7].length() != 0) ? (int) Float.parseFloat(item[6]) : 0;
                        double retire_rate = ((double) retire_num) / ((double) employee_num);

                        float graduate_rate = (item[8].length() != 0) ? Float.parseFloat(item[8]) : 0;
                        float college_rate = (item[9].length() != 0) ? Float.parseFloat(item[9]) : 0;
                        float middle_rate = (item[10].length() != 0) ? Float.parseFloat(item[10]) : 0;

                        String avg_education;
                        float avg=(graduate_rate*3+college_rate*2+middle_rate)/100;
                        if (avg>=2.4) avg_education ="graduate";
                        else if(avg<2.4&&avg>1.5) avg_education ="college";
                        else avg_education ="middle";

                        Map<String, Object> company = companyMap.get(company_id);
                        company.put("employee_num", employee_num);
                        company.put("retire_rate", retire_rate);
                        company.put("average_education", avg_education);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void company_managers_loader(String path){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            reader.readLine();
            String line;
            while((line=reader.readLine())!=null){
                line=line+",eof";
                String[] item = line.split(",");
                Long company_id = Long.parseLong(item[1]);

                if(companyMap.containsKey(company_id)){
                    Map<String,Object> manager = new HashMap<>();
                    manager.put("name",item[6]);
                    manager.put("title_class",item[8]);
                    manager.put("title_level",item[19]);
                    manager.put("title",item[9]);
                    if(item[15].length()!=0) manager.put("birth_year",Integer.parseInt(item[15]));
                    else manager.put("birth_year",1970);
                    if(item[14].equals("m")) manager.put("gender","男");
                    else manager.put("gender","女");
                    manager.put("degree",item[17]);
                    manager.put("resume",item[25]);
                    personMap.put((String) manager.get("name"),manager);
                    Set<String> nameSet;
                    if(!managerRel.containsKey(company_id)) nameSet = new HashSet<>();
                    else nameSet = managerRel.get(company_id);
                    nameSet.add((String) manager.get("name"));
                    managerRel.put(company_id,nameSet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stock_basic_loader(String path){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            reader.readLine();
            String line;
            while((line=reader.readLine())!=null){
                line=line+",eof";
                String[] item = line.split(",");

                String stock_code = item[0];
                if(stockCompanyRel.containsKey(stock_code.substring(0,6))){
                    Map<String,Object> stock = new HashMap<>();
                    stock.put("stock_code",stock_code);
                    stock.put("name",item[1]);
                    stock.put("start_date",item[3]);

                    double st_rate;
                    if(item[1].startsWith("*ST")) st_rate = 2.0;
                    else if(item[1].startsWith("ST")) st_rate = 1.0;
                    else st_rate = 0.0;
                    stock.put("st_rate",st_rate);
                    stockMap.put((String)stock.get("stock_code"),stock);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stock_st_loader(String path){
        int line_num = 0;
        List<String> stock_codeList;
        Map<String,Integer> trueCnt = new HashMap<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();
            String[] item = line.split(",");
            stock_codeList = Arrays.asList(item);
            line_num++;
            while((line=reader.readLine())!=null){
                line_num++;
                line=line+",eof";
                item = line.split(",");

                for(int i=1;i<stock_codeList.size();i++){
                    if(stockMap.containsKey(stock_codeList.get(i))){
                        double st_rate = Double.parseDouble(String.valueOf(stockMap.get(stock_codeList.get(i)).get("st_rate")));
                        if(st_rate >= 1) continue;
                        if(item[i].equals("True")){
                            if(!trueCnt.containsKey(stock_codeList.get(i))) trueCnt.put(stock_codeList.get(i),1);
                            else trueCnt.put(stock_codeList.get(i),trueCnt.get(stock_codeList.get(i))+1);
                        }
                    }
                }
            }

            for (Map.Entry<String,Integer> entry:trueCnt.entrySet()){
                stockMap.get(entry.getKey()).put("st_rate",((double)entry.getValue())/(line_num-1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stock_price_loader(String path){
        File dir = new File(path);
        File[] files = dir.listFiles();
        String filePath;
        ExcelLoader excelLoader = null;
        try{
            excelLoader = new ExcelLoader();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (File file : files) {
            filePath = file.toString();
            int line_num = getLineNumber(filePath) - 1;
            try {
                BufferedReader reader = new BufferedReader(new FileReader(filePath));
                reader.readLine();
                String line;
                String stock_code = filePath.substring(filePath.length()-15,filePath.length()-4);

                if(!stockMap.containsKey(stock_code)) continue;

                Map<String,Object> priceStatistic = new HashMap<>();
                priceStatistic.put("name",stock_code+"的价格指数");

                assert excelLoader != null;
                if(excelLoader.priceStatisticMap.containsKey(stock_code)){
                    priceStatistic.putAll(excelLoader.priceStatisticMap.get(stock_code));
                }
                priceStatisticMap.put((String) priceStatistic.get("name"),priceStatistic);
                stockPriceStatisticRel.put(stock_code,(String) priceStatistic.get("name"));

                Map<String,Map<String,Object>> oneStockMap = new HashMap<>();
                int line_cnt = 0;
                while((line=reader.readLine())!=null){
                    line_cnt++;
                    if(line_num>=30 && line_cnt<=line_num-30) continue;

                    line=line+",eof";
                    String[] item = line.split(",");

                    String date = item[0];
                    Map<String,Object> dateMap = new HashMap<>();

                    dateMap.put("name",stock_code+"在"+date+"的价格");
                    dateMap.put("date",date);
                    dateMap.put("open",Double.parseDouble(item[1]));
                    dateMap.put("close",Double.parseDouble(item[2]));
                    dateMap.put("high",Double.parseDouble(item[3]));
                    dateMap.put("low",Double.parseDouble(item[4]));
                    dateMap.put("volume",Double.parseDouble(item[5]));
                    dateMap.put("money",Double.parseDouble(item[6]));

                    oneStockMap.put(date,dateMap);
                    pricePriceStatisticRel.put((String) dateMap.get("name"),(String) priceStatistic.get("name"));
                }
                priceMap.put(stock_code,oneStockMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}