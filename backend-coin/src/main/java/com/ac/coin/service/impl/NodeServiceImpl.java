package com.ac.coin.service.impl;

import com.ac.coin.dao.NodeDAO;
import com.ac.coin.po.Node;
import com.ac.coin.service.NodeService;
import com.ac.coin.util.Transform;
import com.ac.coin.vo.NodeVO;
import com.ac.coin.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NodeServiceImpl implements NodeService {

    @Autowired
    private NodeDAO nodeDAO;

    @Override
    public ResponseVO add(NodeVO nodeVO) {
        Node node = Transform.nodePO(nodeVO);
        return ResponseVO.buildSuccess(nodeDAO.addNode(node));
    }

    @Override
    public ResponseVO delete(Long nodeId) {
        nodeDAO.deleteNodeById(nodeId);
        return ResponseVO.buildSuccess(true);
    }

    @Override
    public ResponseVO edit(NodeVO nodeVO) {
        Node node = Transform.nodePO(nodeVO);
        return ResponseVO.buildSuccess(Transform.nodeVO(nodeDAO.editNode(node)));
    }

    @Override
    public Optional<NodeVO> find(Long nodeId) {
        Optional<Node> optionalNode = nodeDAO.findNodeById(nodeId);
        return optionalNode.map(Transform::nodeVO);
    }

    @Override
    public ResponseVO findSubNodes(Long nodeId) {
        List<Node> nodeList = nodeDAO.findToByFromId(nodeId);
        List<NodeVO> nodeVOList = new ArrayList<>();
        for(Node node:nodeList){
            nodeVOList.add(Transform.nodeVO(node));
        }
        return ResponseVO.buildSuccess(nodeVOList);
    }
}
