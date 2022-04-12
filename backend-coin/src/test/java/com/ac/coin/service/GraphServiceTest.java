package com.ac.coin.service;

import org.junit.jupiter.api.Test;
import com.ac.coin.enums.NodeShape;
import com.ac.coin.vo.GraphVO;
import com.ac.coin.vo.NodeVO;
import com.ac.coin.vo.RelationVO;
import com.ac.coin.vo.UserVO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GraphServiceTest {
    @Autowired
    NodeService nodeService;
    @Autowired
    RelationService relationService;
    @Autowired
    GraphService graphService;
    @Autowired
    UserService userService;

    private List<Long> graphVOIdList;
    private List<Long> nodeVOIdList;

    private Long userVOId;

    @BeforeEach
    public void setUp(){
        UserVO userVO = new UserVO();
        userVO.setName("gzj");
        userVO.setPassword("123456");
        userVOId = (Long) userService.add(userVO).getContent();
        userVO.setId(userVOId);

        graphVOIdList = new ArrayList<>();
        GraphVO graphVO0 = new GraphVO();
        graphVO0.setName("graphVO0");
        graphVOIdList.add((Long)graphService.add(graphVO0,userVOId).getContent());

        GraphVO graphVO1 = new GraphVO();
        graphVO1.setName("graphVO1");
        graphVOIdList.add((Long)graphService.add(graphVO1,userVOId).getContent());

        nodeVOIdList = new ArrayList<>();
        NodeVO nodeVO1 = new NodeVO();
        nodeVO1.setGraphId(graphVOIdList.get(0));
        nodeVO1.setName("A");
        nodeVOIdList.add((Long)nodeService.add(nodeVO1).getContent());
        NodeVO nodeVO2 = new NodeVO();
        nodeVO2.setGraphId(graphVOIdList.get(0));
        nodeVO2.setName("B");
        nodeVOIdList.add((Long)nodeService.add(nodeVO2).getContent());
        NodeVO nodeVO3 = new NodeVO();
        nodeVO3.setGraphId(graphVOIdList.get(1));
        nodeVO3.setName("C");
        nodeVOIdList.add((Long)nodeService.add(nodeVO3).getContent());
        NodeVO nodeVO4 = new NodeVO();
        nodeVO4.setGraphId(graphVOIdList.get(1));
        nodeVO4.setName("D");
        nodeVOIdList.add((Long)nodeService.add(nodeVO4).getContent());

        RelationVO relationVO = new RelationVO();
        relationVO.setGraphId(graphVOIdList.get(0));
        relationVO.setSource(nodeVOIdList.get(0));
        relationVO.setTarget(nodeVOIdList.get(1));
        relationVO.setName("rename");
        relationVO.setLabel("relabel");
        relationVO.setSolid(true);
        relationVO.setShown(true);

        relationService.add(relationVO);
        relationVO.setGraphId(graphVOIdList.get(1));
        relationVO.setSource(nodeVOIdList.get(2));
        relationVO.setTarget(nodeVOIdList.get(3));
        relationService.add(relationVO);
    }

    @AfterEach
    public void tearDown(){
        userService.delete(userVOId);
    }
    @Test
    void add() {
        assertNotNull(graphVOIdList.get(0),"图创建失败");
        Optional<GraphVO> optionalGraphVO = graphService.find(graphVOIdList.get(0));
        assertTrue(optionalGraphVO.isPresent());
        GraphVO graphVO = optionalGraphVO.get();
        assertEquals("graphVO0",graphVO.getName());
    }

    @Test
    void deleteAll() {
        Optional<GraphVO> optionalGraphVO = graphService.find(graphVOIdList.get(0));
        assertTrue(optionalGraphVO.isPresent());
        assertEquals(graphService.findAllNodes(graphVOIdList.get(0)).size(),2);
        assertEquals(graphService.findAllRelations(graphVOIdList.get(0)).size(),1);
        graphService.deleteAll(graphVOIdList.get(0));
        optionalGraphVO = graphService.find(graphVOIdList.get(0));
        assertTrue(optionalGraphVO.isPresent());
        GraphVO queryGraphVO = optionalGraphVO.get();
        assertEquals(graphService.findAllNodes(queryGraphVO.getId()).size(),0);
        assertEquals(graphService.findAllRelations(queryGraphVO.getId()).size(),0);

        optionalGraphVO = graphService.find(graphVOIdList.get(1));
        assertTrue(optionalGraphVO.isPresent());
        queryGraphVO = optionalGraphVO.get();
        assertEquals(graphService.findAllNodes(queryGraphVO.getId()).size(),2);
        assertEquals(graphService.findAllRelations(queryGraphVO.getId()).size(),1);
    }

    @Test
    void delete() {
        Optional<GraphVO> optionalGraphVO = graphService.find(graphVOIdList.get(0));
        assertTrue(optionalGraphVO.isPresent());
        graphService.delete(graphVOIdList.get(0));
        optionalGraphVO = graphService.find(graphVOIdList.get(0));
        assertFalse(optionalGraphVO.isPresent());
    }

    @Test
    void edit() {
        GraphVO graphVO = new GraphVO();
        graphVO.setName("graphVO0_edit");
        graphVO.setId(graphVOIdList.get(0));
        graphService.edit(graphVO);

        Optional<GraphVO> optionalGraphVO = graphService.find(graphVOIdList.get(0));
        assertTrue(optionalGraphVO.isPresent());
        GraphVO queryGraphVO = optionalGraphVO.get();
        assertEquals("graphVO0_edit",queryGraphVO.getName());
    }

    @Test
    void findAllNodes() {
        Set<Long> querySet = graphService.findAllNodes(graphVOIdList.get(0)).stream().map(NodeVO::getId).collect(Collectors.toSet());
        Set<Long> nodeIdSet = new HashSet<>();
        nodeIdSet.add(nodeVOIdList.get(0));
        nodeIdSet.add(nodeVOIdList.get(1));
        assertEquals(nodeIdSet,querySet);
    }

    @Test
    void findAllRelations() {
        Set<Long> querySet = graphService.findAllRelations(graphVOIdList.get(0)).stream().map(RelationVO::getId).collect(Collectors.toSet());
        assertEquals(1,querySet.size());
    }

    @Test
    void findAllGraphs() {
        Set<Long> queryGraphVOIdSet= graphService.findAllGraphs().stream().map(GraphVO::getId).collect(Collectors.toSet());
        assertEquals(new HashSet<>(graphVOIdList),queryGraphVOIdSet);
    }
    @Test
    void search(){
        GraphVO graphV0=new GraphVO();
        graphService.add(graphV0,userVOId);
        NodeVO nodeVO=new NodeVO();
        nodeVO.setName("Thorns");
        nodeVO.setColor("");
        //不能将shape设为“”字符串？？？
        nodeVO.setShape(NodeShape.CIRCLE.toString());
        nodeVO.setGraphId(graphService.findAllGraphs().get(2).getId());
        nodeService.add(nodeVO);
        nodeVO.setShape(NodeShape.NoRequirement.toString());
        assertEquals(graphService.searchNodeByOptions(nodeVO).get(0).getName(),"Thorns");
        graphService.delete(1L);
    }

    @Test
    void loadGraph() throws IOException {
        GraphVO graphVO0 = new GraphVO();
        graphVO0.setName("graphVO0");
        graphVO0.setId(1L);
        graphService.add(graphVO0,userVOId);
        Long graphId=1L;
        String jsonString="{\"nodes\":[\n" +
                "{\"graphId\":1,\n" +
                "\"name\":\"Alice\",\n" +
                "\"label\":\"girl\",\n" +
                "\"x\":1.23,\n" +
                "\"y\":33.1\n" +
                "},\n" +
                "{\n" +
                "\"graphId\":1,\n" +
                "\"name\":\"Blob\",\n" +
                "\"label\":\"boy\",\n" +
                "\"x\":3.23,\n" +
                "\"y\":12.1\n" +
                "},\n" +
                "{\"graphId\":1,\n" +
                "\"name\":\"John\",\n" +
                "\"label\":\"boy\",\n" +
                "\"x\":5.23,\n" +
                "\"y\":22.1\n" +
                "}\n" +
                "],\n" +
                "\"relations\":[\n" +
                "{\n" +
                "\"fromName\":\"Alice\",\n" +
                "\"toName\":\"Blob\",\n" +
                "\"graphId\":1,\n" +
                "\"name\":\"freindly\",\n" +
                "\"label\":\"friend\"},\n" +
                "{\n" +
                "\"fromName\":\"Alice\",\n" +
                "\"toName\":\"John\",\n" +
                "\"graphId\":1,\n" +
                "\"name\":\"strange\",\n" +
                "\"label\":\"stranger\"}\n" +
                "]\n" +
                "}";
        graphService.loadGraph(jsonString,graphId);
        assertEquals(graphService.findAllNodes(graphId).size(),3);
        assertEquals(graphService.findAllRelations(graphId).size(),2);
        graphService.delete(1L);
    }

    @Test
    void fuzzyMatching() {
        List<NodeVO> queryList = new ArrayList<>();

        GraphVO graphVO0 = new GraphVO();
        graphVO0.setName("graphVO0");
        graphVO0.setId(1L);
        NodeVO nodeVO=new NodeVO();
        nodeVO.setName("Skadi");
        nodeVO.setGraphId(1L);
        nodeService.add(nodeVO);
        queryList.add(nodeVO);
        nodeService.find(nodeVO.getId());

        graphService.add(graphVO0,userVOId);
        List<NodeVO> respond=graphService.fuzzyMatching("Ska",nodeVO.getGraphId());
        assertEquals(respond.get(0).getName(),queryList.get(0).getName());
        graphService.delete(1L);
    }

    @Test
    void typesetting(){
        NodeVO nodeVO=new NodeVO();
        nodeVO.setName("Skadi");
        nodeVO.setGraphId(1L);
        nodeService.add(nodeVO);

        NodeVO nodeVO1=graphService.typesetting(1L).get(0);
        assertEquals(nodeVO1.getTypesetting_x(),40);
        assertEquals(nodeVO1.getTypesetting_y(),40);

        graphService.delete(1L);
    }
}