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
public class UserDAOTest {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private GraphDAO graphDAO;
    @Autowired
    private NodeDAO nodeDAO;
    @Autowired
    private RelationDAO relationDAO;

    private List<Long> userIdList;

    //gzj的内容
    private Long g_gid_0;
    private Long g_nid_0;
    private Long g_nid_1;
    private Long g_rid_0;

    private Long g_gid_1;
    private Long g_nid_2;
    private Long g_nid_3;
    private Long g_rid_1;

    //zzy的内容
    private Long z_gid;
    private Long z_nid_0;
    private Long z_nid_1;
    private Long z_rid;

    @BeforeEach
    public void setUp(){
        userIdList = new ArrayList<>();
        User user0 = new User("gzj","g123456");
        userIdList.add(userDAO.addUser(user0));
        User user1 = new User("zzy","z123456");
        userIdList.add(userDAO.addUser(user1));

        //gzj的内容

        //gzj第一张图的内容
        g_gid_0 = graphDAO.addGraph(new Graph("g_graph0"),user0.getId());

        Node g_node0 = new Node();
        g_node0.setGraphId(g_gid_0);
        g_nid_0 = nodeDAO.addNode(g_node0);

        Node g_node1 = new Node();
        g_node1.setGraphId(g_gid_0);
        g_nid_1 = nodeDAO.addNode(g_node1);

        Relation g_relation0 = new Relation();
        g_relation0.setGraphId(g_gid_0);
        g_relation0.setFromId(g_nid_0);
        g_relation0.setToId(g_nid_1);
        g_relation0.setName("g_name");
        g_relation0.setLabel("g_label");
        g_rid_0 = relationDAO.addRelation(g_relation0);

        //gzj第二张图的内容
        g_gid_1 = graphDAO.addGraph(new Graph("g_graph1"),user0.getId());

        Node g_node2 = new Node();
        g_node2.setGraphId(g_gid_1);
        g_nid_2 = nodeDAO.addNode(g_node2);

        Node g_node3 = new Node();
        g_node3.setGraphId(g_gid_1);
        g_nid_3 = nodeDAO.addNode(g_node3);

        Relation g_relation1 = new Relation();
        g_relation1.setGraphId(g_gid_1);
        g_relation1.setFromId(g_nid_2);
        g_relation1.setToId(g_nid_3);
        g_relation1.setName("g_name");
        g_relation1.setLabel("g_label");
        g_rid_1 = relationDAO.addRelation(g_relation1);

        //zzy图的内容
        z_gid = graphDAO.addGraph(new Graph("z_graph"),user1.getId());

        Node z_node0 = new Node();
        z_node0.setGraphId(z_gid);
        z_nid_0 = nodeDAO.addNode(z_node0);

        Node z_node1 = new Node();
        z_node1.setGraphId(z_gid);
        z_nid_1 = nodeDAO.addNode(z_node1);

        Relation z_relation = new Relation();
        z_relation.setGraphId(z_gid);
        z_relation.setFromId(z_nid_0);
        z_relation.setToId(z_nid_1);
        z_relation.setName("z_name");
        z_relation.setLabel("z_label");
        z_rid = relationDAO.addRelation(z_relation);
    }

    @AfterEach
    public void tearDown(){
        for(Long userId:userIdList){
            userDAO.deleteUserById(userId);
        }
    }

    @Test
    void addUser() {
        assertNotNull(userIdList.get(0),"用户创建失败");
        Optional<User> optionalUser = userDAO.findUserById(userIdList.get(0));
        assertTrue(optionalUser.isPresent());
        User user = optionalUser.get();
        assertEquals("gzj",user.getName());
    }

    @Test
    void deleteAllGraphsByUserId() {
        Optional<User> optionalUser = userDAO.findUserById(userIdList.get(0));
        assertTrue(optionalUser.isPresent());
        User queryUser = optionalUser.get();
        List<Graph> g_graphList = userDAO.findGraphsById(queryUser.getId());
        assertEquals(2,g_graphList.size());
        assertEquals(2,nodeDAO.findAllNodesByGraphId(g_graphList.get(0).getId()).size());
        assertEquals(1,relationDAO.findAllRelationsByGraphId(g_graphList.get(0).getId()).size());

        Long gid0 = g_graphList.get(0).getId();
        userDAO.deleteAllGraphsByUserId(userIdList.get(0));

        optionalUser = userDAO.findUserById(userIdList.get(0));
        assertTrue(optionalUser.isPresent());
        queryUser = optionalUser.get();
        g_graphList = userDAO.findGraphsById(queryUser.getId());
        assertEquals(0,g_graphList.size());
        assertEquals(0,nodeDAO.findAllNodesByGraphId(gid0).size());
        assertEquals(0,relationDAO.findAllRelationsByGraphId(gid0).size());
    }

    @Test
    void deleteUserById() {
        Optional<User> optionalUser = userDAO.findUserById(userIdList.get(0));
        assertTrue(optionalUser.isPresent());
        userDAO.deleteUserById(userIdList.get(0));
        optionalUser = userDAO.findUserById(userIdList.get(0));
        assertFalse(optionalUser.isPresent());
    }

    @Test
    void editUser() {
        User user = new User("GZJ","G123456");
        user.setId(userIdList.get(0));
        userDAO.editUser(user);
        Optional<User> optionalUser = userDAO.findUserById(userIdList.get(0));
        assertTrue(optionalUser.isPresent());
        User queryUser = optionalUser.get();
        assertEquals("GZJ",queryUser.getName());
        assertEquals("G123456",queryUser.getPassword());
    }

    @Test
    void findUserByName() {
        Optional<User> optionalUser = userDAO.findUserByName("gzj");
        assertTrue(optionalUser.isPresent());
        User user = optionalUser.get();
        assertEquals("gzj",user.getName());
        assertEquals("g123456",user.getPassword());
        assertEquals(userIdList.get(0),user.getId());
    }

    @Test
    void findGraphsById() {
        Set<Long> graphIdSet = userDAO.findGraphsById(userIdList.get(0)).stream().map(Graph::getId).collect(Collectors.toSet());
        Set<Long> testGraphIdSet = new HashSet<>();
        testGraphIdSet.add(g_gid_0);
        testGraphIdSet.add(g_gid_1);
        assertEquals(testGraphIdSet,graphIdSet);
    }
}