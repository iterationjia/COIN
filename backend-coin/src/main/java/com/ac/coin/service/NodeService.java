package com.ac.coin.service;

import com.ac.coin.po.Node;
import com.ac.coin.vo.NodeVO;
import com.ac.coin.vo.ResponseVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface NodeService {
    ResponseVO add(NodeVO nodeVO);

    ResponseVO delete(Long nodeId);

    ResponseVO edit(NodeVO nodeVO);

    Optional<NodeVO> find(Long nodeId);

    ResponseVO findSubNodes(Long nodeId);
}
