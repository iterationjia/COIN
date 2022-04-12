package com.ac.coin.controller;

import org.junit.jupiter.api.Test;
import com.ac.coin.enums.NodeShape;
import com.ac.coin.po.Node;
import com.ac.coin.service.GraphService;
import com.ac.coin.service.NodeService;
import com.ac.coin.service.RelationService;
import com.ac.coin.service.UserService;
import com.ac.coin.vo.GraphVO;
import com.ac.coin.vo.NodeVO;
import com.ac.coin.vo.RelationVO;
import com.ac.coin.vo.UserVO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
class GraphControllerTest {
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

    private static MockMvc mockMvc;

    String path="/api/graph";

    @Autowired
    public GraphControllerTest(GraphController graphController){
        mockMvc = MockMvcBuilders.standaloneSetup(graphController).build();
    }


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
    void add() throws Exception {
        GraphVO graphVO2 = new GraphVO();
        graphVO2.setName("graphVO2");
        ResultActions resultActions = mockMvc.perform(
                post(path+"/add/"+ userVOId)
                        .content(JSON.toJSONString(graphVO2))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        Long graphVOId = ((Integer) NodeControllerTest.toContent(resultActions)).longValue();
        assertNotNull(graphVOId,"图创建失败");
        Optional<GraphVO> optionalGraphVO = graphService.find(graphVOId);
        assertTrue(optionalGraphVO.isPresent());
        GraphVO queryGraphVO = optionalGraphVO.get();
        assertEquals("graphVO2",queryGraphVO.getName());
    }

    @Test
    void deleteAll() throws Exception{
        Optional<GraphVO> optionalGraphVO = graphService.find(graphVOIdList.get(0));
        assertTrue(optionalGraphVO.isPresent());
        assertEquals(graphService.findAllNodes(graphVOIdList.get(0)).size(),2);
        assertEquals(graphService.findAllRelations(graphVOIdList.get(0)).size(),1);

        ResultActions resultActions = mockMvc.perform(
                get(path+"/deleteAll/"+ graphVOIdList.get(0))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        assertTrue((Boolean) NodeControllerTest.toContent(resultActions));

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
    void delete() throws Exception{
        Optional<GraphVO> optionalGraphVO = graphService.find(graphVOIdList.get(0));
        assertTrue(optionalGraphVO.isPresent());
        ResultActions resultActions = mockMvc.perform(
                get(path+"/delete/"+ graphVOIdList.get(0))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        assertTrue((Boolean) NodeControllerTest.toContent(resultActions));
        optionalGraphVO = graphService.find(graphVOIdList.get(0));
        assertFalse(optionalGraphVO.isPresent());
    }

    @Test
    void edit() throws Exception{
        GraphVO graphVO = new GraphVO();
        graphVO.setName("graphVO0_edit");
        graphVO.setId(graphVOIdList.get(0));

        ResultActions resultActions = mockMvc.perform(
                post(path+"/edit")
                        .content(JSON.toJSONString(graphVO))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        Optional<GraphVO> optionalGraphVO = graphService.find(graphVOIdList.get(0));
        assertTrue(optionalGraphVO.isPresent());
        GraphVO queryGraphVO = optionalGraphVO.get();
        assertEquals("graphVO0_edit",queryGraphVO.getName());
    }

    @Test
    void findAllNodes() throws Exception{
        ResultActions resultActions = mockMvc.perform(
                get(path+"/findAllNodes/"+ graphVOIdList.get(0))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        List<NodeVO> nodeVOList = ((JSONArray) NodeControllerTest.toContent(resultActions)).toJavaList(NodeVO.class);
        Set<Long> querySet = nodeVOList.stream().map(NodeVO::getId).collect(Collectors.toSet());
        Set<Long> nodeIdSet = new HashSet<>();
        nodeIdSet.add(nodeVOIdList.get(0));
        nodeIdSet.add(nodeVOIdList.get(1));
        assertEquals(nodeIdSet,querySet);
    }

    @Test
    void findAllRelations() throws Exception{
        ResultActions resultActions = mockMvc.perform(
                get(path+"/findAllRelations/"+ graphVOIdList.get(0))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        List<RelationVO> relationVOList = ((JSONArray) NodeControllerTest.toContent(resultActions)).toJavaList(RelationVO.class);
        Set<Long> querySet = relationVOList.stream().map(RelationVO::getId).collect(Collectors.toSet());
        assertEquals(1,querySet.size());
    }

    @Test
    void findAllGraphs() throws Exception{
        ResultActions resultActions = mockMvc.perform(
                get(path+"/findAllGraphs/")
                        .contentType(MediaType.APPLICATION_JSON)
        );
        List<GraphVO> graphVOList = ((JSONArray) NodeControllerTest.toContent(resultActions)).toJavaList(GraphVO.class);
        Set<Long> queryGraphVOIdSet= graphVOList.stream().map(GraphVO::getId).collect(Collectors.toSet());
        assertEquals(new HashSet<>(graphVOIdList),queryGraphVOIdSet);
    }

    @Test
    void search() throws Exception {
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

        ResultActions resultActions = mockMvc.perform(
                post(path+"/searchNode")
                        .content(JSON.toJSONString(nodeVO))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        assertNotNull(NodeControllerTest.toContent(resultActions));
        List<NodeVO> nodeVOList = ((JSONArray) NodeControllerTest.toContent(resultActions)).toJavaList(NodeVO.class);
        assertEquals(nodeVOList.get(0).getName(),"Thorns");

        graphService.delete(1L);
    }

    @Test
    void loadGraph() throws Exception {
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
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", jsonString.getBytes());

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.fileUpload(path+"/loadGraph/"+graphId)
                        .file(file)
        );

        List<NodeVO> nodeVOList = ((JSONArray) NodeControllerTest.toContent(resultActions)).toJavaList(NodeVO.class);
        assertEquals(nodeVOList.size(),3);
        graphService.delete(1L);
    }

    @Test
    void fuzzyMatching() throws Exception {
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

        ResultActions resultActions = mockMvc.perform(
                get(path+"/fuzzyMatching/1/Ska")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        assertNotNull(NodeControllerTest.toContent(resultActions));
        List<NodeVO> nodeVOList = ((JSONArray) NodeControllerTest.toContent(resultActions)).toJavaList(NodeVO.class);
        assertEquals(nodeVOList.get(0).getName(),queryList.get(0).getName());


//        List<NodeVO> respond=graphService.fuzzyMatching("Ska",nodeVO.getGraphId());
//        assertEquals(respond.get(0).getName(),queryList.get(0).getName());
        graphService.delete(1L);
    }

    @Test
    void typesetting() throws Exception {
        NodeVO nodeVO=new NodeVO();
        nodeVO.setName("Skadi");
        nodeVO.setGraphId(1L);
        nodeService.add(nodeVO);

        ResultActions resultActions = mockMvc.perform(
                get(path+"/typesetting/1")
        );

        assertNotNull(NodeControllerTest.toContent(resultActions));
        List<NodeVO> nodeVOList = ((JSONArray) NodeControllerTest.toContent(resultActions)).toJavaList(NodeVO.class);
        assertEquals(nodeVOList.get(0).getTypesetting_x(),10);
        assertEquals(nodeVOList.get(0).getTypesetting_y(),10);


        graphService.delete(1L);
    }

    @Test
    void chatBot() throws Exception{
        String question = "通策医疗股份有限公司的ceo是谁";
        ResultActions resultActions = mockMvc.perform(
                post(path+"/chatBot/admin"+"?question="+question)
        );
        String result = (String) NodeControllerTest.toContent(resultActions);
        assertEquals("王毅",result);
        String question1 = "史今的公司有那些";
        ResultActions resultActions1 = mockMvc.perform(
                post(path+"/chatBot/admin"+"?question="+question1)
        );
        String result1 = (String) NodeControllerTest.toContent(resultActions1);
        assertEquals("西安国际医学投资股份有限公司",result);
        String question2 = "西安国际医学投资股份有限公司的股票名是什么";
        ResultActions resultActions2 = mockMvc.perform(
                post(path+"/chatBot/admin"+"?question="+question2)
        );
        String result2 = (String) NodeControllerTest.toContent(resultActions2);
        assertEquals("国际医学",result);
    };

    @Test
    void recommand30() throws Exception{
        ResultActions resultActions = mockMvc.perform(
                post(path+"/recommand30/admin")
        );
        List<String> RecomendVOList = ((JSONArray) NodeControllerTest.toContent(resultActions)).toJavaList(String.class);
        assertNotEquals(0,RecomendVOList.size());
    };

}