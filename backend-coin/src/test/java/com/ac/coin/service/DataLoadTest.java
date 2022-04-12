package com.ac.coin.service;

import com.ac.coin.dao.GraphDAO;
import com.ac.coin.dao.NodeDAO;
import com.ac.coin.dao.RelationDAO;
import com.ac.coin.dao.UserDAO;
import com.ac.coin.util.GraphBuilder;
import com.ac.coin.po.*;
import com.ac.coin.util.Exportor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
class DataLoadTest {
    @Autowired
    private NodeDAO nodeDAO;

    @Autowired
    private RelationDAO relationDAO;
    @Autowired
    private GraphDAO graphDAO;
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private NodeService nodeService;

    @Autowired
    private GraphService graphService;

    @Autowired
    private GraphBuilder graphBuilder;

    @Autowired
    private RelationService relationService;

    private Long graphId;
    private Long nodeId;
    private List<Long> nodeIdList;

    private Long relationId1_2;
    private Long relationId2_3;
    private Long relationId3_1;

    private Long userId;

    @BeforeEach
    public void setUp() {

    }

    @AfterEach
    public void tearDown() {
        //userDAO.deleteUserById(userId);
    }

    @Test
    void addNode() {
        User user = new User("admin", "123456");
        user.setRisk(31);
        user.setBalance(20);
        user.setStocks("{}");
        user.setIndustries("{}");
        userId = userDAO.addUser(user);

        Graph graph = new Graph("JoinQuaint");
        graphId = graphDAO.addGraph(graph, user.getId());

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

    @Test
    void export(){
        Exportor exportor = new Exportor();
        exportor.export();
    }

    @Test
    void test(){
        graphService.recommand30("admin","joinQuaint");
    }

    @Test
    void test1(){

    }
}
