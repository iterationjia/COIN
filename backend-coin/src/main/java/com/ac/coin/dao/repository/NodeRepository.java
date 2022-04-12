package com.ac.coin.dao.repository;

import com.ac.coin.po.Node;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NodeRepository extends Neo4jRepository<Node,Long> {
    @Query("match (n) where n.graphId=$graphId return n")
    List<Node> findAllNodesByGraphId(@Param("graphId") Long graphId);

    @Query("match (n) where n.graphId=$graphId detach delete n")
    void deleteAllNodesByGraphId(@Param("graphId") Long graphId);

    @Query("match (n1)-[r]->(n2) where id(n1)=$fromId return n2")
    List<Node> findToByFromId(@Param("fromId") Long fromId);
}
