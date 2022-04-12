package com.ac.coin.controller;

import org.junit.jupiter.api.Test;
import com.ac.coin.service.GraphService;
import com.ac.coin.service.NodeService;
import com.ac.coin.service.RelationService;
import com.ac.coin.service.UserService;
import com.ac.coin.vo.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.alibaba.fastjson.JSON;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
class NodeControllerTest {
    @Autowired
    private NodeService nodeService;
    @Autowired
    private RelationService relationService;
    @Autowired
    private GraphService graphService;
    @Autowired
    UserService userService;

    private Long graphVOId;
    private Long nodeVOId;

    private List<Long> nodeVOIdList;

    private Long relationVOId1_2;
    private Long relationVOId2_3;
    private Long relationVOId3_1;

    private Long userVOId;

    private static MockMvc mockMvc;

    String path="/api/node";

    @Autowired
    public NodeControllerTest(NodeController nodeController){
        mockMvc = MockMvcBuilders.standaloneSetup(nodeController).build();
    }

    @BeforeEach
    public void setUp(){
        UserVO userVO = new UserVO();
        userVO.setName("gzj");
        userVO.setPassword("123456");
        userVOId = (Long) userService.add(userVO).getContent();
        userVO.setId(userVOId);

        GraphVO graphVO = new GraphVO();
        graphVO.setName("nodeControllerTestGraph");
        graphVOId = (Long)graphService.add(graphVO,userVOId).getContent();

        NodeVO nodeVO = new NodeVO();
        nodeVO.setGraphId(graphVOId);
        nodeVO.setColor("red");
        nodeVO.setName("alpha");
        nodeVO.setShown(true);
        nodeVOId = (Long)nodeService.add(nodeVO).getContent();

        nodeVOIdList = new ArrayList<>();
        nodeVOIdList.add(nodeVOId);
        NodeVO nodeVO1 = new NodeVO();
        nodeVO1.setGraphId(graphVOId);
        nodeVOIdList.add((Long)nodeService.add(nodeVO1).getContent());
        NodeVO nodeVO2 = new NodeVO();
        nodeVO2.setGraphId(graphVOId);
        nodeVOIdList.add((Long)nodeService.add(nodeVO2).getContent());
        NodeVO nodeVO3 = new NodeVO();
        nodeVO3.setGraphId(graphVOId);
        nodeVOIdList.add((Long)nodeService.add(nodeVO3).getContent());
        NodeVO nodeVO4 = new NodeVO();
        nodeVO4.setGraphId(graphVOId);
        nodeVOIdList.add((Long)nodeService.add(nodeVO4).getContent());
        NodeVO nodeVO5 = new NodeVO();
        nodeVO5.setGraphId(graphVOId);
        nodeVOIdList.add((Long)nodeService.add(nodeVO5).getContent());

        RelationVO relationVO = new RelationVO();
        relationVO.setGraphId(graphVOId);
        relationVO.setSource(nodeVOIdList.get(0));
        relationVO.setTarget(nodeVOIdList.get(1));
        relationVO.setName("rename");
        relationVO.setLabel("relabel");
        relationVO.setSolid(true);
        relationVO.setShown(true);

        relationService.add(relationVO);
        relationVO.setTarget(nodeVOIdList.get(2));
        relationService.add(relationVO);
        relationVO.setTarget(nodeVOIdList.get(3));
        relationService.add(relationVO);
        relationVO.setTarget(nodeVOIdList.get(4));
        relationService.add(relationVO);
        relationVO.setTarget(nodeVOIdList.get(5));
        relationService.add(relationVO);

        relationVO.setSource(nodeVOIdList.get(1));
        relationVO.setTarget(nodeVOIdList.get(2));
        relationVOId1_2 = (Long)relationService.add(relationVO).getContent();
        relationVO.setSource(nodeVOIdList.get(2));
        relationVO.setTarget(nodeVOIdList.get(3));
        relationVOId2_3 = (Long)relationService.add(relationVO).getContent();
        relationVO.setSource(nodeVOIdList.get(3));
        relationVO.setTarget(nodeVOIdList.get(1));
        relationVOId3_1 = (Long)relationService.add(relationVO).getContent();
    }

    @AfterEach
    public void tearDown(){
        userService.delete(userVOId);
    }

    @Test
    void add() throws Exception {
        NodeVO nodeVO = new NodeVO();
        nodeVO.setGraphId(graphVOId);
        nodeVO.setColor("blue");
        nodeVO.setName("beta");
        nodeVO.setShown(true);
        ResultActions resultActions = mockMvc.perform(
                post(path+"/add")
                        .content(JSON.toJSONString(nodeVO))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        Long nodeVOId = ((Integer) toContent(resultActions)).longValue();
        assertNotNull(nodeVOId,"节点创建失败");
        Optional<NodeVO> optionalNodeVO = nodeService.find(nodeVOId);
        assertTrue(optionalNodeVO.isPresent());
        NodeVO queryNodeVO = optionalNodeVO.get();
        assertEquals(graphVOId,queryNodeVO.getGraphId());
        assertEquals("blue",queryNodeVO.getColor());
        assertEquals("beta",queryNodeVO.getName());
        assertTrue(queryNodeVO.isShown());
    }

    @Test
    void delete() throws Exception {
        Optional<NodeVO> optionalNodeVO = nodeService.find(nodeVOIdList.get(1));
        assertTrue(optionalNodeVO.isPresent());
        assertTrue(relationService.find(relationVOId1_2).isPresent());
        assertTrue(relationService.find(relationVOId3_1).isPresent());

        ResultActions resultActions = mockMvc.perform(
                get(path+"/delete/"+ nodeVOIdList.get(1))
                .contentType(MediaType.APPLICATION_JSON)
        );

        assertTrue((Boolean) toContent(resultActions));

        optionalNodeVO = nodeService.find(nodeVOIdList.get(1));
        assertFalse(optionalNodeVO.isPresent());
        assertFalse(relationService.find(relationVOId1_2).isPresent());
        assertFalse(relationService.find(relationVOId3_1).isPresent());
    }

    @Test
    void edit() throws Exception {
        NodeVO nodeVO = new NodeVO();
        nodeVO.setGraphId(graphVOId);
        nodeVO.setId(nodeVOId);
        nodeVO.setColor("blue");
        nodeVO.setName("alpha_edit");

        ResultActions resultActions = mockMvc.perform(
                post(path+"/edit")
                        .content(JSON.toJSONString(nodeVO))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        NodeVO returnNodeVO = ((JSONObject) toContent(resultActions)).toJavaObject(NodeVO.class);

        assertEquals(nodeVOId,returnNodeVO.getId());
        assertEquals("blue",returnNodeVO.getColor());
        assertEquals("alpha_edit",returnNodeVO.getName());

        Optional<NodeVO> optionalFindNodeVO = nodeService.find(nodeVOId);
        assertTrue(optionalFindNodeVO.isPresent());
        NodeVO findNodeVO = optionalFindNodeVO.get();
        assertEquals(nodeVOId,findNodeVO.getId());
        assertEquals("blue",findNodeVO.getColor());
        assertEquals("alpha_edit",findNodeVO.getName());
    }

    @Test
    void find() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get(path+"/find/"+ nodeVOId)
                        .contentType(MediaType.APPLICATION_JSON)
        );
        NodeVO returnNodeVO = ((JSONObject) toContent(resultActions)).toJavaObject(NodeVO.class);
        assertEquals(nodeVOId,returnNodeVO.getId());
        assertEquals("red",returnNodeVO.getColor());
        assertEquals("alpha",returnNodeVO.getName());
        assertEquals(graphVOId,returnNodeVO.getGraphId());
    }

    @Test
    void findSubNodes() throws Exception{
        ResultActions resultActions = mockMvc.perform(
                get(path+"/findSubNodes/"+ nodeVOId)
                        .contentType(MediaType.APPLICATION_JSON)
        );
        List<NodeVO> nodeVOList = ((JSONArray) toContent(resultActions)).toJavaList(NodeVO.class);
        Set<Long> toIdSet = nodeVOList.stream().map(NodeVO::getId).collect(Collectors.toSet());
        nodeVOIdList.remove(0);
        assertEquals(new HashSet<>(nodeVOIdList),toIdSet);
    }

    public static Object toContent(ResultActions resultActions) throws UnsupportedEncodingException {
        MockHttpServletResponse response = resultActions.andReturn().getResponse();
        assertEquals(2,response.getStatus()/100);
        String res = response.getContentAsString(StandardCharsets.UTF_8);
        ResponseVO responseVO = JSON.toJavaObject(JSON.parseObject(res),ResponseVO.class);
        return responseVO.getContent();
    }
}