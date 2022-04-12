package com.ac.coin.service;

import com.ac.coin.po.User;
import com.ac.coin.vo.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public interface GraphService {
    ResponseVO add(GraphVO graphVO, Long userId);

    ResponseVO delete(Long graphId);

    ResponseVO deleteAll(Long graphId);

    ResponseVO deleteAllGraphs();

    ResponseVO edit(GraphVO graphVO);

    List<NodeVO> findAllNodes(Long graphId);

    List<RelationVO> findAllRelations(Long graphId);

    List<GraphVO> findAllGraphs();

    Optional<GraphVO> find(Long graphId);

    List<NodeVO> searchNodeByOptions(NodeVO nodeVO);

    List<NodeVO> loadGraph(String jsonString,Long graphId) throws IOException;

    List<NodeVO> fuzzyMatching(String jsonString,Long graphId);

    List<NodeVO> typesetting(Long graphId);

    String[] chatBot(String question) throws IOException, InterruptedException;

    ResponseVO recommand30(String userName,String graphName);

    ResponseVO favorUpdate(String userName,String industryName,String stockName);
}
