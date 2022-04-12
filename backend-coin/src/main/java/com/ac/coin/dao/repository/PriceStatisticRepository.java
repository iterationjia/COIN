package com.ac.coin.dao.repository;

import com.ac.coin.po.PriceStatistic;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PriceStatisticRepository extends Neo4jRepository<PriceStatistic,Long> {
    @Query("match (n:PriceStatistic) where n.graphId=$graphId return n")
    List<PriceStatistic> findAllPriceStatisticByGraphId(@Param("graphId") Long graphId);
}
