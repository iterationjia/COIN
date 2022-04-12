package com.ac.coin.service;

import com.ac.coin.po.Node;
import com.ac.coin.po.Relation;
import com.ac.coin.vo.NodeVO;
import com.ac.coin.vo.RelationVO;
import com.ac.coin.vo.ResponseVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface RelationService {
    ResponseVO add(RelationVO relationVO);

    ResponseVO delete(Long relationId);

    ResponseVO edit(RelationVO relationVO);

    Optional<RelationVO> find(Long relationId);
}
