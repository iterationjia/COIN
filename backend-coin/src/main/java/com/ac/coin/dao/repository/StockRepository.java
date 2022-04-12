package com.ac.coin.dao.repository;

import com.ac.coin.po.Stock;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface StockRepository extends Neo4jRepository<Stock,Long> {
    @Query("match (n1:Stock)-[r]->(n2:PriceStatistic) where id(n2)=$toId return n1")
    Stock findStockToPriceStatistic(@Param("toId") Long toId);

    @Query("MATCH (n1:Stock)-->(n2:Company)<--(n3:SecondIndustry) where n3.name = $industryName return n1")
    List<Stock> findStockBySecondIndustry(@Param("industryName") String industryName);
}
