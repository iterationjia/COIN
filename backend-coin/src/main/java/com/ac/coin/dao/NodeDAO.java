package com.ac.coin.dao;

import com.ac.coin.po.Node;
import com.ac.coin.po.PriceStatistic;
import com.ac.coin.po.Stock;
import com.ac.coin.vo.PriceStatisticVO;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NodeDAO {
    Long addNode(Node node);
    void deleteNodeById(Long nodeId);
    Node editNode(Node node);
    Optional<Node> findNodeById(Long nodeId);
    List<Node> findAllNodesByGraphId(Long graphId);
    List<Node> findToByFromId(Long fromId);
    List<PriceStatistic> findAllPriceStatisticByGraphId(Long graphId);
    Stock findStockToPriceStatistic(Long priceStatisticId);
    List<Stock> findStockBySecondIndustry(String industryName);
}
