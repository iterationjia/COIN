package com.ac.coin.dao.impl;

import com.ac.coin.dao.RelationDAO;
import com.ac.coin.dao.repository.RelationRepository;
import com.ac.coin.po.Relation;
import com.ac.coin.util.Transform;
import org.neo4j.driver.internal.value.RelationshipValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class RelationDAOImpl implements RelationDAO {
    @Autowired
    private RelationRepository relationRepository;

    @Override
    public Long addRelation(Relation relation) {
        return relationRepository.addRelation(relation.getFromId(),relation.getToId(), Transform.relationToMapValue(relation));
    }

    @Override
    public void deleteRelationById(Long relationId) {
        relationRepository.deleteRelationById(relationId);
    }

    @Override
    public Relation editRelation(Relation relation) {
        Optional<?> optionalO =  relationRepository.editRelation(relation.getId(),Transform.relationToMapValue(relation));
        if(optionalO.isPresent()){
            RelationshipValue relationshipValue = (RelationshipValue) optionalO.get();
            return Transform.relationshipValueToRelation(relationshipValue);
        }
        return null;
    }

    @Override
    public Optional<Relation> findRelationById(Long relationId) {
        Optional<?> optionalO =  relationRepository.findRelationById(relationId);
        if(optionalO.isPresent()){
            RelationshipValue relationshipValue = (RelationshipValue) optionalO.get();
            Relation relation = Transform.relationshipValueToRelation(relationshipValue);
            return Optional.of(relation);
        }
        return Optional.empty();
    }

    @Override
    public List<Relation> findAllRelationsByGraphId(Long graphId) {
        List<RelationshipValue> relationshipValueList = relationRepository.findAllRelationsByGraphId(graphId);
        List<Relation> relationList = new ArrayList<>();
        for(RelationshipValue relationshipValue:relationshipValueList){
            relationList.add(Transform.relationshipValueToRelation(relationshipValue));
        }
        return relationList;
    }
}
