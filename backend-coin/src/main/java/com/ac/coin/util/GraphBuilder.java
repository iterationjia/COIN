package com.ac.coin.util;

import com.ac.coin.service.NodeService;
import com.ac.coin.service.RelationService;
import com.ac.coin.util.CSVLoader;
import com.ac.coin.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class GraphBuilder {
    @Autowired
    private NodeService nodeService;
    @Autowired
    private RelationService relationService;

    private CSVLoader csvLoader;

    private Long graphId;

    public Long getGraphId() {
        return graphId;
    }

    public void setGraphId(Long graphId) {
        this.graphId = graphId;
        csvLoader = new CSVLoader();
    }

    public void buildCompany(){
        for(Map<String,Object> map:csvLoader.companyMap. values()){
            CompanyVO companyVO = new CompanyVO();

            companyVO.setGraphId(graphId);
            companyVO.setName((String) map.get("name"));

            companyVO.setCompany_id((Long) map.get("company_id"));
            companyVO.setRegister_location((String) map.get("register_location"));
            companyVO.setRegister_capital((double) map.get("register_capital"));
            companyVO.setEstablish_date((String) map.get("establish_date"));

            if(map.containsKey("employee_num")){
                companyVO.setEmployee_num((int) map.get("employee_num"));
                companyVO.setRetire_rate((double) map.get("retire_rate"));
                companyVO.setAverage_education((String) map.get("average_education"));

                Long id = (Long) nodeService.add(companyVO).getContent();
                map.put("id",id);
            }
        }
    }

    public void buildPerson(){
        for(Map<String,Object> map:csvLoader.personMap.values()){
            PersonVO personVO = new PersonVO();
            personVO.setGraphId(graphId);
            personVO.setName((String) map.get("name"));
            if(map.containsKey("title_class")){
                personVO.setTitle_class((String) map.get("title_class"));
                personVO.setTitle_level((String) map.get("title_level"));
                personVO.setTitle((String) map.get("title"));
                personVO.setGender((String) map.get("gender"));
                personVO.setBirth_year((Integer) map.get("birth_year"));
                personVO.setDegree((String) map.get("degree"));
                personVO.setResume((String) map.get("resume"));
            }

            Long id = (Long) nodeService.add(personVO).getContent();
            map.put("id",id);
        }
    }

    public void buildSecondIndustry(){
        for(Map<String,Object> map:csvLoader.industryMap.values()){
            SecondIndustryVO secondIndustryVO = new SecondIndustryVO();
            secondIndustryVO.setGraphId(graphId);
            secondIndustryVO.setName((String) map.get("name"));

            Long id = (Long) nodeService.add(secondIndustryVO).getContent();
            map.put("id",id);
        }
    }

    public void buildFirstIndustry(){
        for(Map<String,Object> map:csvLoader.firstIndustryMap.values()){
            FirstIndustryVO firstIndustryVO = new FirstIndustryVO();
            firstIndustryVO.setGraphId(graphId);
            firstIndustryVO.setName((String) map.get("name"));

            Long id = (Long) nodeService.add(firstIndustryVO).getContent();
            map.put("id",id);
        }
    }

    public void buildStock(){
        for(Map<String,Object> map:csvLoader.stockMap.values()){
            StockVO stockVO = new StockVO();
            stockVO.setGraphId(graphId);
            stockVO.setName((String) map.get("name"));

            stockVO.setStock_code((String) map.get("stock_code"));
            stockVO.setStart_date((String) map.get("start_date"));
            stockVO.setSt_rate((double) map.get("st_rate"));

            Long id = (Long) nodeService.add(stockVO).getContent();
            map.put("id",id);
        }
    }

    public void buildPrice(){
        for(Map<String,Map<String,Object>> oneStockMap:csvLoader.priceMap.values()){
            for(Map<String,Object> dateMap:oneStockMap.values()){
                PriceVO priceVO = new PriceVO();
                priceVO.setGraphId(graphId);
                priceVO.setName((String) dateMap.get("name"));

                priceVO.setDate((String) dateMap.get("date"));
                priceVO.setOpen((double) dateMap.get("open"));
                priceVO.setClose((double) dateMap.get("close"));
                priceVO.setHigh((double) dateMap.get("high"));
                priceVO.setLow((double) dateMap.get("low"));
                priceVO.setVolume((double) dateMap.get("volume"));
                priceVO.setMoney((double) dateMap.get("money"));

                Long id = (Long) nodeService.add(priceVO).getContent();
                dateMap.put("id",id);
            }
        }
    }

    public void buildPriceStatistic(){
        for(Map<String,Object> map:csvLoader.priceStatisticMap.values()){
            PriceStatisticVO priceStatisticVO = new PriceStatisticVO();
            priceStatisticVO.setGraphId(graphId);
            priceStatisticVO.setName((String) map.get("name"));

            if(map.containsKey("7_expected_price")){
                priceStatisticVO.set_7_expected_price((double) map.get("7_expected_price"));
                priceStatisticVO.set_7_minus_price((double) map.get("7_minus_price"));
                priceStatisticVO.set_7_risk_price((double) map.get("7_risk_price"));
                priceStatisticVO.set_7_pic_url((String) map.get("7_pic_url"));
                priceStatisticVO.set_15_expected_price((double) map.get("14_expected_price"));
                priceStatisticVO.set_15_minus_price((double) map.get("14_minus_price"));
                priceStatisticVO.set_15_risk_price((double) map.get("14_risk_price"));
                priceStatisticVO.set_15_pic_url((String) map.get("14_pic_url"));
                priceStatisticVO.set_30_expected_price((double) map.get("30_expected_price"));
                priceStatisticVO.set_30_minus_price((double) map.get("30_minus_price"));
                priceStatisticVO.set_30_risk_price((double) map.get("30_risk_price"));
                priceStatisticVO.set_30_pic_url((String) map.get("30_pic_url"));
            }

            Long id = (Long) nodeService.add(priceStatisticVO).getContent();
            map.put("id",id);
        }
    }

    public void buildCeoRel(){
        for (Map.Entry<Long,String> entry : csvLoader.ceoRel.entrySet()) {
            Long fromId = (Long) csvLoader.companyMap.get(entry.getKey()).get("id");
            Long toId = (Long) csvLoader.personMap.get(entry.getValue()).get("id");

            if(fromId!=null && toId!=null){
                RelationVO relationVO = new RelationVO();
                relationVO.setGraphId(graphId);
                relationVO.setName("ceo");
                relationVO.setSource(fromId);
                relationVO.setTarget(toId);
                relationService.add(relationVO);
            }
        }
    }

    public void buildLegal_representativeRel(){
        for (Map.Entry<Long,String> entry : csvLoader.legal_representativeRel.entrySet()) {
            Long fromId = (Long) csvLoader.companyMap.get(entry.getKey()).get("id");
            Long toId = (Long) csvLoader.personMap.get(entry.getValue()).get("id");

            if(fromId!=null && toId!=null){
                RelationVO relationVO = new RelationVO();
                relationVO.setGraphId(graphId);
                relationVO.setName("法人");
                relationVO.setSource(fromId);
                relationVO.setTarget(toId);
                relationService.add(relationVO);
            }
        }
    }

    public void buildManagerRel(){
        for (Map.Entry<Long, Set<String>> entry : csvLoader.managerRel.entrySet()) {
            Long fromId = (Long) csvLoader.companyMap.get(entry.getKey()).get("id");
            for(String manager:entry.getValue()){
                Long toId = (Long) csvLoader.personMap.get(manager).get("id");

                if(fromId!=null && toId!=null){
                    RelationVO relationVO = new RelationVO();
                    relationVO.setGraphId(graphId);
                    relationVO.setName("高管");
                    relationVO.setSource(fromId);
                    relationVO.setTarget(toId);
                    relationService.add(relationVO);
                }
            }
        }
    }

    public void buildIndustryRel(){
        for (Map.Entry<Long,String> entry : csvLoader.industryRel.entrySet()) {
            Long fromId = (Long) csvLoader.industryMap.get(entry.getValue()).get("id");
            Long toId = (Long) csvLoader.companyMap.get(entry.getKey()).get("id");

            if(fromId!=null && toId!=null){
                RelationVO relationVO = new RelationVO();
                relationVO.setGraphId(graphId);
                relationVO.setName("行业二级分类");
                relationVO.setSource(fromId);
                relationVO.setTarget(toId);
                relationService.add(relationVO);
            }
        }
    }

    public void buildFirstIndustryRel(){
        for (Map.Entry<String,String> entry : csvLoader.firstIndustryRel.entrySet()) {
            Long fromId = (Long) csvLoader.firstIndustryMap.get(entry.getKey()).get("id");
            Long toId = (Long) csvLoader.industryMap.get(entry.getValue()).get("id");

            if(fromId!=null && toId!=null){
                RelationVO relationVO = new RelationVO();
                relationVO.setGraphId(graphId);
                relationVO.setName("行业一级分类");
                relationVO.setSource(fromId);
                relationVO.setTarget(toId);
                relationService.add(relationVO);
            }
        }
    }

    public void buildStockCompanyRel(){
        for (Map.Entry<String,Long> entry : csvLoader.stockCompanyRel.entrySet()) {
            String code;
            if(csvLoader.stockMap.containsKey(entry.getKey()+".XSHE")) code = entry.getKey()+".XSHE";
            else if(csvLoader.stockMap.containsKey(entry.getKey()+".XSHG")) code = entry.getKey()+".XSHG";
            else continue;  //股票基本信息里无，但公司列表有对应股票
            Long fromId = (Long) csvLoader.stockMap.get(code).get("id");
            Long toId = (Long) csvLoader.companyMap.get(entry.getValue()).get("id");

            if(fromId!=null && toId!=null){
                RelationVO relationVO = new RelationVO();
                relationVO.setGraphId(graphId);
                relationVO.setName("所属公司");
                relationVO.setSource(fromId);
                relationVO.setTarget(toId);
                relationService.add(relationVO);
            }
        }
    }

    public void buildStockPriceStatisticRel(){
        for (Map.Entry<String,String> entry : csvLoader.stockPriceStatisticRel.entrySet()) {
            Long fromId = (Long) csvLoader.stockMap.get(entry.getKey()).get("id");
            Long toId = (Long) csvLoader.priceStatisticMap.get(entry.getValue()).get("id");

            if(fromId!=null && toId!=null){
                RelationVO relationVO = new RelationVO();
                relationVO.setGraphId(graphId);
                relationVO.setName("价格指数");
                relationVO.setSource(fromId);
                relationVO.setTarget(toId);
                relationService.add(relationVO);
            }
        }
    }

    public void buildPricePriceStatisticRel(){
        for (Map.Entry<String,String> entry : csvLoader.pricePriceStatisticRel.entrySet()) {
            String name = entry.getKey();
            Long fromId = (Long) csvLoader.priceStatisticMap.get(entry.getValue()).get("id");
            Long toId = (Long) csvLoader.priceMap.get(name.substring(0,name.indexOf("在"))).get(name.substring(name.indexOf("在")+1,name.indexOf("的"))).get("id");

            if(fromId!=null && toId!=null){
                RelationVO relationVO = new RelationVO();
                relationVO.setGraphId(graphId);
                relationVO.setName("日期价格");
                relationVO.setSource(fromId);
                relationVO.setTarget(toId);
                relationService.add(relationVO);
            }
        }
    }
}