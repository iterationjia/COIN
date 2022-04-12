package com.ac.coin.dao;

import org.junit.jupiter.api.Test;
import com.ac.coin.po.Graph;
import com.ac.coin.po.Node;
import com.ac.coin.po.Relation;
import com.ac.coin.po.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NodeDAOTest {
    @Autowired
    private NodeDAO nodeDAO;
    @Autowired
    private RelationDAO relationDAO;
    @Autowired
    private GraphDAO graphDAO;
    @Autowired
    private UserDAO userDAO;

    private Long graphId;
    private Long nodeId;

    private List<Long> nodeIdList;

    private Long relationId1_2;
    private Long relationId2_3;
    private Long relationId3_1;

    private Long userId;

    @BeforeEach
    public void setUp(){
        User user = new User("gzj","123456");
        userId = userDAO.addUser(user);

        Graph graph = new Graph("nodeDAOTestGraph");
        graphId = graphDAO.addGraph(graph,user.getId());
        Node node = new Node();
        node.setGraphId(graphId);
        node.setColor("red");
        node.setName("alpha");
        node.setHighlighted(true);
        nodeId = nodeDAO.addNode(node);

        nodeIdList = new ArrayList<>();
        nodeIdList.add(nodeId);
        Node node1 = new Node();
        node1.setGraphId(graphId);
        nodeIdList.add(nodeDAO.addNode(node1));
        Node node2 = new Node();
        node2.setGraphId(graphId);
        nodeIdList.add(nodeDAO.addNode(node2));
        Node node3 = new Node();
        node3.setGraphId(graphId);
        nodeIdList.add(nodeDAO.addNode(node3));
        Node node4 = new Node();
        node4.setGraphId(graphId);
        nodeIdList.add(nodeDAO.addNode(node4));
        Node node5 = new Node();
        node5.setGraphId(graphId);
        nodeIdList.add(nodeDAO.addNode(node5));

        Relation relation = new Relation();
        relation.setGraphId(graphId);
        relation.setFromId(nodeIdList.get(0));
        relation.setToId(nodeIdList.get(1));
        relation.setName("rename");
        relation.setLabel("relabel");
        relation.setHighlighted(true);
        relation.setSolid(true);
        relation.setShown(true);

        relationDAO.addRelation(relation);
        relation.setToId(nodeIdList.get(2));
        relationDAO.addRelation(relation);
        relation.setToId(nodeIdList.get(3));
        relationDAO.addRelation(relation);
        relation.setToId(nodeIdList.get(4));
        relationDAO.addRelation(relation);
        relation.setToId(nodeIdList.get(5));
        relationDAO.addRelation(relation);

        relation.setFromId(nodeIdList.get(1));
        relation.setToId(nodeIdList.get(2));
        relationId1_2 = relationDAO.addRelation(relation);
        relation.setFromId(nodeIdList.get(2));
        relation.setToId(nodeIdList.get(3));
        relationId2_3 = relationDAO.addRelation(relation);
        relation.setFromId(nodeIdList.get(3));
        relation.setToId(nodeIdList.get(1));
        relationId3_1 = relationDAO.addRelation(relation);
    }

    @AfterEach
    public void tearDown(){
        userDAO.deleteUserById(userId);
    }

    @Test
    void addNode() {
        assertNotNull(nodeId,"节点创建失败");
        Optional<Node> optionalNode = nodeDAO.findNodeById(nodeId);
        assertTrue(optionalNode.isPresent());
        Node node = optionalNode.get();
        assertEquals(graphId,node.getGraphId());
        assertEquals("red",node.getColor());
        assertEquals("alpha",node.getName());
        assertTrue(node.isHighlighted());
    }

    @Test
    void deleteNodeById() {
        Optional<Node> optionalNode = nodeDAO.findNodeById(nodeIdList.get(1));
        assertTrue(optionalNode.isPresent());
        assertTrue(relationDAO.findRelationById(relationId1_2).isPresent());
        assertTrue(relationDAO.findRelationById(relationId3_1).isPresent());
        nodeDAO.deleteNodeById(nodeIdList.get(1));
        optionalNode = nodeDAO.findNodeById(nodeIdList.get(1));
        assertFalse(optionalNode.isPresent());
        assertFalse(relationDAO.findRelationById(relationId1_2).isPresent());
        assertFalse(relationDAO.findRelationById(relationId3_1).isPresent());
    }

    @Test
    void editNode() {
        Node node = new Node();
        node.setGraphId(graphId);
        node.setId(nodeId);
        node.setColor("blue");
        node.setName("alpha_edit");
        node.setHighlighted(false);

        Node returnNode = nodeDAO.editNode(node);
        assertEquals(nodeId,returnNode.getId());
        assertEquals("blue",returnNode.getColor());
        assertEquals("alpha_edit",returnNode.getName());

        Optional<Node> optionalFindNode = nodeDAO.findNodeById(nodeId);
        assertTrue(optionalFindNode.isPresent());
        Node findNode = optionalFindNode.get();
        assertEquals(nodeId,findNode.getId());
        assertEquals("blue",findNode.getColor());
        assertEquals("alpha_edit",findNode.getName());
    }

    @Test
    void findAllNodesByGraphId() {
        Set<Long> querySet = nodeDAO.findAllNodesByGraphId(graphId).stream().map(Node::getId).collect(Collectors.toSet());
        assertEquals(new HashSet<>(nodeIdList),querySet);
    }

    @Test
    void findToByFromId() {
        Set<Long> toIdSet = nodeDAO.findToByFromId(nodeId).stream().map(Node::getId).collect(Collectors.toSet());
        nodeIdList.remove(0);
        assertEquals(new HashSet<>(nodeIdList),toIdSet);
    }
}