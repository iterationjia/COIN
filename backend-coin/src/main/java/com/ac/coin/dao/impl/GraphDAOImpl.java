package com.ac.coin.dao.impl;

import com.ac.coin.dao.GraphDAO;
import com.ac.coin.dao.repository.GraphRepository;
import com.ac.coin.dao.repository.NodeRepository;
import com.ac.coin.po.Graph;
import com.ac.coin.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class GraphDAOImpl implements GraphDAO {

    @Autowired
    GraphRepository graphRepository;

    @Autowired
    NodeRepository nodeRepository;

    @Override
    public Long addGraph(Graph graph, Long userId) {
        graphRepository.save(graph);
        graphRepository.linkGraphToUser(userId,graph.getId());
        return graph.getId();
    }

    @Override
    public void deleteGraphById(Long graphId) {
        nodeRepository.deleteAllNodesByGraphId(graphId);
        graphRepository.deleteById(graphId);
    }

    @Override
    public void deleteAllById(Long graphId) {
        nodeRepository.deleteAllNodesByGraphId(graphId);
    }

    @Override
    public void deleteAllGraphs() {
        graphRepository.deleteAllGraphs();
    }

    @Override
    public Graph editGraph(Graph graph) {
        graphRepository.save(graph);
        return graph;
    }

    @Override
    public Optional<Graph> findGraphById(Long graphId) {
        return graphRepository.findById(graphId);
    }

    @Override
    public Long findGraphByName(String name) {
        return graphRepository.findGraphByName(name);
    }

    @Override
    public List<Graph> findAllGraphs() {
        return graphRepository.findAll();
    }
}
