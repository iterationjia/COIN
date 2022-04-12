package com.ac.coin.dao.repository;

import com.ac.coin.po.Graph;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GraphRepository extends Neo4jRepository<Graph,Long> {
    @Query("match (n) detach delete n")
    void deleteAllGraphs();

    @Query("match (u:User) where id(u)=$userId optional match (g:Graph) where id(g)=$graphId"
            +" create (u)-[r:Relation]->(g) return id(r)")
    Long linkGraphToUser(@Param("userId") Long userId, @Param("graphId") Long graphId);

    @Query("match (u:User)-[r]->(g:Graph) where id(u)=$userId return g")
    List<Graph> findGraphsById(@Param("userId") Long userId);

    @Query("match (g:Graph) where g.name = $name return id(g)")
    Long findGraphByName(@Param("name") String name);
}
