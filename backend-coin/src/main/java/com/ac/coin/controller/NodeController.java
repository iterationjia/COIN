package com.ac.coin.controller;

import com.ac.coin.service.NodeService;
import com.ac.coin.vo.NodeVO;
import com.ac.coin.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/node")
@CrossOrigin
public class NodeController {
    @Autowired
    private NodeService nodeService;

    @PostMapping("/add")
    public ResponseVO add(@RequestBody NodeVO nodeVO){
        return nodeService.add(nodeVO);
    }

    @GetMapping("/delete/{nodeId}")
    public ResponseVO delete(@PathVariable("nodeId") Long nodeId){
        return nodeService.delete(nodeId);
    }

    @PostMapping("/edit")
    public ResponseVO edit(@RequestBody NodeVO nodeVO){
        return nodeService.edit(nodeVO);
    }

    @GetMapping("/find/{nodeId}")
    public ResponseVO find(@PathVariable("nodeId") Long nodeId){
        Optional<NodeVO> optionalNode = nodeService.find(nodeId);
        return optionalNode.map(ResponseVO::buildSuccess).orElseGet(() -> ResponseVO.buildFailure("不存在该节点"));
    }

    @GetMapping("/findSubNodes/{nodeId}")
    public ResponseVO findSubNodes(@PathVariable("nodeId") Long nodeId){
        return nodeService.findSubNodes(nodeId);
    }
}
