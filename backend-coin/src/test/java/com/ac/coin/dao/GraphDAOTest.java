package com.ac.coin.dao;

import org.junit.jupiter.api.Test;
import com.ac.coin.po.Graph;
import com.ac.coin.po.Node;
import com.ac.coin.po.Relation;
import com.ac.coin.po.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GraphDAOTest {
    @Autowired
    private NodeDAO nodeDAO;
    @Autowired
    private RelationDAO relationDAO;
    @Autowired
    private GraphDAO graphDAO;
    @Autowired
    private UserDAO userDAO;

    private List<Long> graphIdList;

    private Long userId;

    @BeforeEach
    public void setUp(){
        User user = new User("gzj","123456");
        userId = userDAO.addUser(user);

        graphIdList = new ArrayList<>();
        Graph graph0 = new Graph("graph0");
        graphIdList.add(graphDAO.addGraph(graph0,user.getId()));
        Graph graph1 = new Graph("graph1");
        graphIdList.add(graphDAO.addGraph(graph1,user.getId()));

        Node node1 = new Node();
        node1.setGraphId(graphIdList.get(0));
        nodeDAO.addNode(node1);
        Node node2 = new Node();
        node2.setGraphId(graphIdList.get(0));
        nodeDAO.addNode(node2);
        Node node3 = new Node();
        node3.setGraphId(graphIdList.get(1));
        nodeDAO.addNode(node3);
        Node node4 = new Node();
        node4.setGraphId(graphIdList.get(1));
        nodeDAO.addNode(node4);

        Relation relation = new Relation();
        relation.setGraphId(graphIdList.get(0));
        relation.setFromId(node1.getId());
        relation.setToId(node2.getId());
        relation.setName("rename");
        relation.setLabel("relabel");
        relation.setHighlighted(true);
        relation.setSolid(true);
        relation.setShown(true);

        relationDAO.addRelation(relation);
        relation.setGraphId(graphIdList.get(1));
        relation.setFromId(node3.getId());
        relation.setToId(node4.getId());
        relationDAO.addRelation(relation);
    }

    @AfterEach
    public void tearDown(){
        userDAO.deleteUserById(userId);
    }


    @Test
    void addGraph() {
        assertNotNull(graphIdList.get(0),"图创建失败");
        Optional<Graph> optionalGraph = graphDAO.findGraphById(graphIdList.get(0));
        assertTrue(optionalGraph.isPresent());
        Graph graph = optionalGraph.get();
        assertEquals("graph0",graph.getName());
    }

    @Test
    void deleteAllById() {
        Optional<Graph> optionalGraph = graphDAO.findGraphById(graphIdList.get(0));
        assertTrue(optionalGraph.isPresent());
        assertEquals(nodeDAO.findAllNodesByGraphId(graphIdList.get(0)).size(),2);
        assertEquals(relationDAO.findAllRelationsByGraphId(graphIdList.get(0)).size(),1);
        graphDAO.deleteAllById(graphIdList.get(0));
        optionalGraph = graphDAO.findGraphById(graphIdList.get(0));
        assertTrue(optionalGraph.isPresent());
        Graph queryGraph = optionalGraph.get();
        assertEquals(nodeDAO.findAllNodesByGraphId(queryGraph.getId()).size(),0);
        assertEquals(relationDAO.findAllRelationsByGraphId(queryGraph.getId()).size(),0);

        optionalGraph = graphDAO.findGraphById(graphIdList.get(1));
        assertTrue(optionalGraph.isPresent());
        queryGraph = optionalGraph.get();
        assertEquals(nodeDAO.findAllNodesByGraphId(queryGraph.getId()).size(),2);
        assertEquals(relationDAO.findAllRelationsByGraphId(queryGraph.getId()).size(),1);
    }

    @Test
    void deleteGraphById() {
        Optional<Graph> optionalGraph = graphDAO.findGraphById(graphIdList.get(0));
        assertTrue(optionalGraph.isPresent());
        graphDAO.deleteGraphById(graphIdList.get(0));
        optionalGraph = graphDAO.findGraphById(graphIdList.get(0));
        assertFalse(optionalGraph.isPresent());
    }

    @Test
    void editGraph() {
        Graph graph = new Graph("graph0_edit");
        graph.setNodeLabelVisible(false);
        graph.setRelationLabelVisible(false);
        graph.setId(graphIdList.get(0));
        graphDAO.editGraph(graph);

        Optional<Graph> optionalGraph = graphDAO.findGraphById(graphIdList.get(0));
        assertTrue(optionalGraph.isPresent());
        Graph queryGraph = optionalGraph.get();
        assertEquals("graph0_edit",queryGraph.getName());
        assertFalse(queryGraph.isNodeLabelVisible());
        assertFalse(queryGraph.isRelationLabelVisible());
    }

    @Test
    void findAllGraphs() {
        Set<Long> queryGraphIdSet= graphDAO.findAllGraphs().stream().map(Graph::getId).collect(Collectors.toSet());
        assertEquals(new HashSet<>(graphIdList),queryGraphIdSet);
    }
}