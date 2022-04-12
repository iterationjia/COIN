package com.ac.coin.dao;

import com.ac.coin.po.Node;
import com.ac.coin.po.Relation;

import java.util.List;
import java.util.Optional;

public interface RelationDAO {
    Long addRelation(Relation relation);
    void deleteRelationById(Long relationId);
    Relation editRelation(Relation relation);
    Optional<Relation> findRelationById(Long relationId);
    List<Relation> findAllRelationsByGraphId(Long graphId);
}
