package com.ac.coin.service;

import org.junit.jupiter.api.Test;
import com.ac.coin.vo.GraphVO;
import com.ac.coin.vo.NodeVO;
import com.ac.coin.vo.RelationVO;
import com.ac.coin.vo.UserVO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NodeServiceTest {
    @Autowired
    NodeService nodeService;
    @Autowired
    RelationService relationService;
    @Autowired
    GraphService graphService;
    @Autowired
    UserService userService;

    private Long graphVOId;
    private Long nodeVOId;

    private List<Long> nodeVOIdList;

    private Long relationVOId1_2;
    private Long relationVOId2_3;
    private Long relationVOId3_1;

    private Long userVOId;

    @BeforeEach
    public void setUp(){
        UserVO userVO = new UserVO();
        userVO.setName("gzj");
        userVO.setPassword("123456");
        userVOId = (Long) userService.add(userVO).getContent();
        userVO.setId(userVOId);

        GraphVO graphVO = new GraphVO();
        graphVO.setName("nodeServiceTestGraph");
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
    void add() {
        assertNotNull(nodeVOId,"节点创建失败");
        Optional<NodeVO> optionalNodeVO = nodeService.find(nodeVOId);
        assertTrue(optionalNodeVO.isPresent());
        NodeVO nodeVO = optionalNodeVO.get();
        assertEquals(graphVOId,nodeVO.getGraphId());
        assertEquals("red",nodeVO.getColor());
        assertEquals("alpha",nodeVO.getName());
        assertTrue(nodeVO.isShown());
    }

    @Test
    void delete() {
        Optional<NodeVO> optionalNodeVO = nodeService.find(nodeVOIdList.get(1));
        assertTrue(optionalNodeVO.isPresent());
        assertTrue(relationService.find(relationVOId1_2).isPresent());
        assertTrue(relationService.find(relationVOId3_1).isPresent());
        nodeService.delete(nodeVOIdList.get(1));
        optionalNodeVO = nodeService.find(nodeVOIdList.get(1));
        assertFalse(optionalNodeVO.isPresent());
        assertFalse(relationService.find(relationVOId1_2).isPresent());
        assertFalse(relationService.find(relationVOId3_1).isPresent());
    }

    @Test
    void edit() {
        NodeVO nodeVO = new NodeVO();
        nodeVO.setGraphId(graphVOId);
        nodeVO.setId(nodeVOId);
        nodeVO.setColor("blue");
        nodeVO.setName("alpha_edit");

        NodeVO returnNodeVO = (NodeVO) nodeService.edit(nodeVO).getContent();
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
    @SuppressWarnings("unchecked")
    void findSubNodes() {
        Set<Long> toIdSet = ((List<NodeVO>) nodeService.findSubNodes(nodeVOId).getContent()).stream().map(NodeVO::getId).collect(Collectors.toSet());
        nodeVOIdList.remove(0);
        assertEquals(new HashSet<>(nodeVOIdList),toIdSet);
    }
}