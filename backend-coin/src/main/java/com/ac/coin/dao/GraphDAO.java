package com.ac.coin.dao;

import com.ac.coin.po.Graph;
import com.ac.coin.po.Node;
import com.ac.coin.po.User;

import java.util.List;
import java.util.Optional;

public interface GraphDAO {
    Long addGraph(Graph graph, Long userId);
    void deleteGraphById(Long graphId);
    void deleteAllById(Long graphId);
    void deleteAllGraphs();
    Graph editGraph(Graph graph);
    Optional<Graph> findGraphById(Long graphId);
    Long findGraphByName(String name);
    List<Graph> findAllGraphs();
}
