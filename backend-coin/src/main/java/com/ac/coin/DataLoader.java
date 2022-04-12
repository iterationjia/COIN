package com.ac.coin;

import com.ac.coin.dao.GraphDAO;
import com.ac.coin.dao.UserDAO;
import com.ac.coin.po.Graph;
import com.ac.coin.po.User;
import com.ac.coin.util.GraphBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    @Autowired
    private GraphDAO graphDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private GraphBuilder graphBuilder;

    public void dataLoader() {
        User user = new User("admin", "123456");
        user.setRisk(31);
        user.setBalance(20);
        user.setStocks("{}");
        user.setIndustries("{}");
        Long userId = userDAO.addUser(user);

        Graph graph = new Graph("JoinQuaint");
        Long graphId = graphDAO.addGraph(graph, user.getId());

        graphBuilder.setGraphId(graphId);
        graphBuilder.buildCompany();
        graphBuilder.buildPerson();
        graphBuilder.buildSecondIndustry();
        graphBuilder.buildFirstIndustry();
        graphBuilder.buildStock();
        graphBuilder.buildPriceStatistic();
        graphBuilder.buildPrice();

        graphBuilder.buildCeoRel();
        graphBuilder.buildLegal_representativeRel();
        graphBuilder.buildManagerRel();
        graphBuilder.buildIndustryRel();
        graphBuilder.buildFirstIndustryRel();
        graphBuilder.buildStockCompanyRel();
        graphBuilder.buildStockPriceStatisticRel();
        graphBuilder.buildPricePriceStatisticRel();
    }
}
