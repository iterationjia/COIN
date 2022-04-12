package com.ac.coin.service.impl;

import com.ac.coin.dao.RelationDAO;
import com.ac.coin.po.Relation;
import com.ac.coin.service.RelationService;
import com.ac.coin.util.Transform;
import com.ac.coin.vo.RelationVO;
import com.ac.coin.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RelationServiceImpl implements RelationService {
    @Autowired
    RelationDAO relationDAO;

    @Override
    public ResponseVO add(RelationVO relationVO) {
        Long relationId = relationDAO.addRelation(Transform.relationPO(relationVO));
        return ResponseVO.buildSuccess(relationId);
    }

    @Override
    public ResponseVO delete(Long relationId) {
        relationDAO.deleteRelationById(relationId);
        return ResponseVO.buildSuccess(true);
    }

    @Override
    public ResponseVO edit(RelationVO relationVO) {
        Relation relation = relationDAO.editRelation(Transform.relationPO(relationVO));
        return ResponseVO.buildSuccess(Transform.relationVO(relation));
    }

    @Override
    public Optional<RelationVO> find(Long relationId) {
        Optional<Relation> optionalRelation = relationDAO.findRelationById(relationId);
        return optionalRelation.map(Transform::relationVO);
    }
}
