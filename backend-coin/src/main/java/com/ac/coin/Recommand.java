package com.ac.coin;

import com.ac.coin.dao.GraphDAO;
import com.ac.coin.dao.NodeDAO;
import com.ac.coin.dao.RelationDAO;
import com.ac.coin.dao.UserDAO;
import com.ac.coin.po.PriceStatistic;
import com.ac.coin.po.Stock;
import com.ac.coin.po.User;
import com.ac.coin.vo.ResponseVO;
import com.alibaba.fastjson.JSON;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Recommand {
    @Autowired
    private GraphDAO graphDAO;
    @Autowired
    private NodeDAO nodeDAO;
    @Autowired
    private RelationDAO relationDAO;
    @Autowired
    private UserDAO userDAO;

    public ResponseVO recommand30(String userName, String graphName) {
        Optional<User> optionalUser = userDAO.findUserByName(userName);
        if (!optionalUser.isPresent()) {
            System.out.println("没有这个用户名");
            return null;
        }
        User user = optionalUser.get();
        Long graphId = graphDAO.findGraphByName(graphName);
        List<PriceStatistic> priceStatisticList = nodeDAO.findAllPriceStatisticByGraphId(graphId);
        List<List<String>> res = new ArrayList<>();
        Map<String,Integer> nameMap = new HashMap<>();

        for(PriceStatistic priceStatistic:priceStatisticList){
            double initialCapital_7 = priceStatistic.get_7_minus_price()+ priceStatistic.get_7_risk_price();
            double initialCapital_15 = priceStatistic.get_15_minus_price()+ priceStatistic.get_15_risk_price();
            double initialCapital_30 = priceStatistic.get_30_minus_price()+ priceStatistic.get_30_risk_price();

            double balanceRate_7 = (priceStatistic.get_7_expected_price()-initialCapital_7)/initialCapital_7;   //单位本金收益率
            double balanceRate_15 = (priceStatistic.get_15_expected_price()-initialCapital_15)/initialCapital_15;
            double balanceRate_30 = (priceStatistic.get_30_expected_price()-initialCapital_30)/initialCapital_30;

            double riskRate_7 = priceStatistic.get_7_minus_price()/initialCapital_7;   //单位本金风险率
            double riskRate_15 = priceStatistic.get_15_minus_price()/initialCapital_15;
            double riskRate_30 = priceStatistic.get_7_minus_price()/initialCapital_30;

            double reciprocal_7 = 1.0/7;
            double reciprocal_15 = 1.0/15;
            double reciprocal_30 = 1.0/30;
            double reciprocal_sum  = reciprocal_7+reciprocal_15+reciprocal_30;

            double weight_7 = reciprocal_7/reciprocal_sum;
            double weight_15 = reciprocal_15/reciprocal_sum;
            double weight_30 = reciprocal_30/reciprocal_sum;

            double balance = balanceRate_7 * weight_7 + balanceRate_15 * weight_15 + balanceRate_30 * weight_30;

            Stock stock = nodeDAO.findStockToPriceStatistic(priceStatistic.getId());
            double st_rate = stock.getSt_rate();
            if(!user.isAcceptSt()) continue;
            double risk = (riskRate_7 * weight_7 + riskRate_15 * weight_15 + riskRate_30 * weight_30) * (1+st_rate);
            if(risk>1) risk = 1;

            double userBalance = user.getBalance()/100;
            double userRisk = user.getRisk()/100;

            if(balance>= userBalance && risk <= userRisk){
                List<String> ans = new ArrayList<>();
                String stock_code = stock.getStock_code();
                String stock_name = stock.getName();
                String balanceRate = String.valueOf((balance-userBalance)/ userBalance);
                String riskRate = String.valueOf((userRisk-risk)/userRisk);
                String favorRate = balanceRate;

                ans.add(stock_code);
                ans.add(stock_name);
                ans.add(balanceRate);
                ans.add(riskRate);
                ans.add("");
                ans.add(favorRate);

                nameMap.put(stock_name,res.size());
                res.add(ans);
            }
        }

        Map<String,Double> stockFavors = JSONObject.fromObject(user.getStocks());
        for(Map.Entry<String, Double> entry:stockFavors.entrySet()){
            String stock_name = entry.getKey();
            if(nameMap.containsKey(stock_name)){
                double originalFavorRate = Double.parseDouble(res.get(nameMap.get(stock_name)).get(5));
                String favorRate = String.valueOf(originalFavorRate * (1+entry.getValue()));
                res.get(nameMap.get(stock_name)).set(5,favorRate);
                res.get(nameMap.get(stock_name)).set(4,"感兴趣过的股票或公司");
            }
        }
        String stocks = JSON.toJSONString(stockFavors);
        user.setStocks(stocks);

        Map<String,Double> industryFavors = JSONObject.fromObject(user.getIndustries());
        for(Map.Entry<String, Double> entry:industryFavors.entrySet()){
            String industry_name = entry.getKey();
            List<Stock> stockList = nodeDAO.findStockBySecondIndustry(industry_name);
            for(Stock stock:stockList){
                String stock_name = stock.getName();
                if(nameMap.containsKey(stock_name)){
                    if(res.get(nameMap.get(stock_name)).get(4).equals("")){
                        double originalFavorRate = Double.parseDouble(res.get(nameMap.get(stock_name)).get(5));
                        String favorRate = String.valueOf(originalFavorRate * (entry.getValue()+1));
                        res.get(nameMap.get(stock_name)).set(5,favorRate);
                        res.get(nameMap.get(stock_name)).set(4,"感兴趣过的行业中股票");
                    }
                }
            }
        }
        String industries = JSON.toJSONString(industryFavors);
        user.setIndustries(industries);

        res = res.stream().sorted(Comparator.comparing(o -> Double.parseDouble(o.get(5)))).collect(Collectors.toList());
        Collections.reverse(res);

        System.out.println(res.size());
        if(res.size()>30) res = res.subList(0,30);
        System.out.println(res);
        return ResponseVO.buildSuccess(res);
    }
}
