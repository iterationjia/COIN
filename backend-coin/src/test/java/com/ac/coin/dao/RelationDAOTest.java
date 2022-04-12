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
class RelationDAOTest {
    @Autowired
    private NodeDAO nodeDAO;
    @Autowired
    private RelationDAO relationDAO;
    @Autowired
    private GraphDAO graphDAO;
    @Autowired
    private UserDAO userDAO;

    private Long graphId;

    private List<Long> relationIdList;

    private Long userId;

    @BeforeEach
    public void setUp(){
        User user = new User("gzj","123456");
        userId = userDAO.addUser(user);

        Graph graph = new Graph("relationDAOTestGraph");
        graphId = graphDAO.addGraph(graph,user.getId());

        Node node1 = new Node();
        node1.setGraphId(graphId);
        nodeDAO.addNode(node1);
        Node node2 = new Node();
        node2.setGraphId(graphId);
        nodeDAO.addNode(node2);
        Node node3 = new Node();
        node3.setGraphId(graphId);
        nodeDAO.addNode(node3);
        Node node4 = new Node();
        node4.setGraphId(graphId);
        nodeDAO.addNode(node4);
        Node node5 = new Node();
        node5.setGraphId(graphId);
        nodeDAO.addNode(node5);

        Relation relation = new Relation();
        relation.setGraphId(graphId);
        relation.setFromId(node1.getId());
        relation.setToId(node2.getId());
        relation.setName("rename");
        relation.setLabel("relabel");
        relation.setHighlighted(true);
        relation.setSolid(true);
        relation.setShown(true);

        relationIdList = new ArrayList<>();
        relationIdList.add(relationDAO.addRelation(relation));
        relation.setToId(node3.getId());
        relationIdList.add(relationDAO.addRelation(relation));
        relation.setToId(node4.getId());
        relationIdList.add(relationDAO.addRelation(relation));
        relation.setToId(node5.getId());
        relationIdList.add(relationDAO.addRelation(relation));

        relation.setFromId(node2.getId());
        relation.setToId(node3.getId());
        relationIdList.add(relationDAO.addRelation(relation));
        relation.setFromId(node3.getId());
        relation.setToId(node4.getId());
        relationIdList.add(relationDAO.addRelation(relation));
        relation.setFromId(node4.getId());
        relation.setToId(node2.getId());
        relationIdList.add(relationDAO.addRelation(relation));
    }

    @AfterEach
    public void tearDown(){
        userDAO.deleteUserById(userId);
    }

    @Test
    void addRelation() {
        assertNotNull(relationIdList.get(0),"创建关系失败");
        Optional<Relation> optionalRelation = relationDAO.findRelationById(relationIdList.get(0));
        assertTrue(optionalRelation.isPresent());
        Relation relation = optionalRelation.get();
        assertEquals(graphId,relation.getGraphId());
        assertEquals("relabel",relation.getLabel());
        assertEquals("rename",relation.getName());
        assertTrue(relation.isHighlighted());
    }

    @Test
    void deleteRelationById() {
        Optional<Relation> optionalRelation = relationDAO.findRelationById(relationIdList.get(0));
        assertTrue(optionalRelation.isPresent());
        relationDAO.deleteRelationById(relationIdList.get(0));
        optionalRelation = relationDAO.findRelationById(relationIdList.get(0));
        assertFalse(optionalRelation.isPresent());
    }

    @Test
    void editRelation() {
        Relation relation = new Relation();
        relation.setGraphId(graphId);
        relation.setId(relationIdList.get(0));

        Optional<Relation> optionalRelation = relationDAO.findRelationById(relationIdList.get(0));
        assertTrue(optionalRelation.isPresent());

        Relation originRelation = optionalRelation.get();
        relation.setFromId(originRelation.getFromId());
        relation.setToId(originRelation.getToId());
        relation.setShown(false);
        relation.setSolid(false);
        relation.setHighlighted(false);
        relation.setName("rename_edit");
        relation.setLabel("relabel_edit");

        relationDAO.editRelation(relation);
        optionalRelation = relationDAO.findRelationById(relationIdList.get(0));
        assertTrue(optionalRelation.isPresent());
        Relation editRelation = optionalRelation.get();
        assertFalse(editRelation.isShown());
        assertFalse(editRelation.isSolid());
        assertFalse(editRelation.isHighlighted());
        assertEquals("rename_edit",editRelation.getName());
        assertEquals("relabel_edit",editRelation.getLabel());
    }

    @Test
    void findAllRelationsByGraphId() {
        Set<Long> querySet = relationDAO.findAllRelationsByGraphId(graphId).stream().map(Relation::getId).collect(Collectors.toSet());
        assertEquals(new HashSet<>(relationIdList),querySet);
    }
}

