package com.ac.coin.dao.repository;

import com.ac.coin.po.Relation;
import org.neo4j.driver.internal.value.MapValue;
import org.neo4j.driver.internal.value.RelationshipValue;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RelationRepository extends Neo4jRepository<Relation,Long> {
    @Query("match (n1) where id(n1)=$fromId optional match (n2) where id(n2)=$toId"
            +" create (n1)-[r:Relation $properties]->(n2) return id(r)")
    Long addRelation(@Param("fromId") Long fromId, @Param("toId") Long toId, @Param("properties") MapValue properties);

    @Query("match (n1)-[r]->(n2) where id(r)=$relationId delete r")
    void deleteRelationById(@Param("relationId") Long relationId);

    @Query("match (n1)-[r]->(n2) where id(r) = $relationId"
            +" set r += $properties"
            +" return r")
    Optional<?> editRelation(@Param("relationId") Long relationId, @Param("properties") MapValue properties);

    @Query("match (n1)-[r:Relation]->(n2) where id(r)=$relationId return r")
    Optional<?> findRelationById(@Param("relationId") Long relationId);

    @Query("match (n1)-[r:Relation]->(n2) where r.graphId=$graphId return r")
    List<RelationshipValue> findAllRelationsByGraphId(@Param("graphId") Long graphId);
}
