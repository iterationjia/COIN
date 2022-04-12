package com.ac.coin.controller;

import org.junit.jupiter.api.Test;
import com.ac.coin.service.GraphService;
import com.ac.coin.service.NodeService;
import com.ac.coin.service.RelationService;
import com.ac.coin.service.UserService;
import com.ac.coin.vo.GraphVO;
import com.ac.coin.vo.NodeVO;
import com.ac.coin.vo.RelationVO;
import com.ac.coin.vo.UserVO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
class RelationControllerTest {
    @Autowired
    NodeService nodeService;
    @Autowired
    RelationService relationService;
    @Autowired
    GraphService graphService;
    @Autowired
    UserService userService;

    private Long graphVOId;

    private List<Long> relationVOIdList;

    private List<Long> nodeVOIdList;

    private Long userVOId;

    private static MockMvc mockMvc;

    String path="/api/relation";

    @Autowired
    public RelationControllerTest(RelationController relationController){
        mockMvc = MockMvcBuilders.standaloneSetup(relationController).build();
    }

    @BeforeEach
    public void setUp(){
        UserVO userVO = new UserVO();
        userVO.setName("gzj");
        userVO.setPassword("123456");
        userVOId = (Long) userService.add(userVO).getContent();
        userVO.setId(userVOId);

        GraphVO graphVO = new GraphVO();
        graphVO.setName("relationServiceTestGraph");
        graphVOId = (Long)graphService.add(graphVO,userVOId).getContent();

        nodeVOIdList = new ArrayList<>();
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

        relationVOIdList = new ArrayList<>();
        relationVOIdList.add((Long) relationService.add(relationVO).getContent());
        relationVO.setTarget(nodeVOIdList.get(2));
        relationVOIdList.add((Long) relationService.add(relationVO).getContent());
        relationVO.setTarget(nodeVOIdList.get(3));
        relationVOIdList.add((Long) relationService.add(relationVO).getContent());
        relationVO.setTarget(nodeVOIdList.get(4));
        relationVOIdList.add((Long) relationService.add(relationVO).getContent());

        relationVO.setSource(nodeVOIdList.get(1));
        relationVO.setTarget(nodeVOIdList.get(2));
        relationVOIdList.add((Long) relationService.add(relationVO).getContent());
        relationVO.setSource(nodeVOIdList.get(2));
        relationVO.setTarget(nodeVOIdList.get(3));
        relationVOIdList.add((Long) relationService.add(relationVO).getContent());
        relationVO.setSource(nodeVOIdList.get(3));
        relationVO.setTarget(nodeVOIdList.get(1));
        relationVOIdList.add((Long) relationService.add(relationVO).getContent());
    }

    @AfterEach
    public void tearDown(){
        userService.delete(userVOId);
    }

    @Test
    void add() throws Exception {
        RelationVO relationVO = new RelationVO();
        relationVO.setGraphId(graphVOId);
        relationVO.setSource(nodeVOIdList.get(3));
        relationVO.setTarget(nodeVOIdList.get(4));
        relationVO.setName("rename_add");
        relationVO.setLabel("relabel_add");
        relationVO.setSolid(true);

        ResultActions resultActions = mockMvc.perform(
                post(path+"/add")
                        .content(JSON.toJSONString(relationVO))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        Long relationVOId = ((Integer) NodeControllerTest.toContent(resultActions)).longValue();
        assertNotNull(relationVOId,"关系创建失败");

        Optional<RelationVO> optionalRelationVO = relationService.find(relationVOId);
        assertTrue(optionalRelationVO.isPresent());
        relationVO = optionalRelationVO.get();

        assertEquals(graphVOId,relationVO.getGraphId());
        assertEquals("relabel_add",relationVO.getLabel());
        assertEquals("rename_add",relationVO.getName());
        assertTrue(relationVO.isSolid());
    }

    @Test
    void delete() throws Exception {
        Optional<RelationVO> optionalRelationVO = relationService.find(relationVOIdList.get(0));
        assertTrue(optionalRelationVO.isPresent());

        ResultActions resultActions = mockMvc.perform(
                get(path+"/delete/"+ relationVOIdList.get(0))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        assertTrue((Boolean) NodeControllerTest.toContent(resultActions));

        optionalRelationVO = relationService.find(relationVOIdList.get(0));
        assertFalse(optionalRelationVO.isPresent());
    }

    @Test
    void edit() throws Exception {
        RelationVO relationVO = new RelationVO();
        relationVO.setGraphId(graphVOId);
        relationVO.setId(relationVOIdList.get(0));

        Optional<RelationVO> optionalRelationVO = relationService.find(relationVOIdList.get(0));
        assertTrue(optionalRelationVO.isPresent());

        RelationVO originRelationVO = optionalRelationVO.get();
        relationVO.setSource(originRelationVO.getSource());
        relationVO.setTarget(originRelationVO.getTarget());
        relationVO.setSolid(false);
        relationVO.setName("rename_edit");
        relationVO.setLabel("relabel_edit");

        ResultActions resultActions = mockMvc.perform(
                post(path+"/edit")
                        .content(JSON.toJSONString(relationVO))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        optionalRelationVO = relationService.find(relationVOIdList.get(0));
        assertTrue(optionalRelationVO.isPresent());
        RelationVO editRelationVO = optionalRelationVO.get();
        assertFalse(editRelationVO.isSolid());
        assertEquals("rename_edit",editRelationVO.getName());
        assertEquals("relabel_edit",editRelationVO.getLabel());
    }

    @Test
    void find() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get(path+"/find/"+ relationVOIdList.get(0))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        RelationVO relationVO = ((JSONObject) NodeControllerTest.toContent(resultActions)).toJavaObject(RelationVO.class);
        assertEquals(graphVOId,relationVO.getGraphId());
        assertEquals("relabel",relationVO.getLabel());
        assertEquals("rename",relationVO.getName());
        assertTrue(relationVO.isSolid());
    }
}