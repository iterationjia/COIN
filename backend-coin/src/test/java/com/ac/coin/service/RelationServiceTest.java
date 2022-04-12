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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RelationServiceTest {
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
    void add() {
        assertNotNull(relationVOIdList.get(0),"创建关系失败");
        Optional<RelationVO> optionalRelationVO = relationService.find(relationVOIdList.get(0));
        assertTrue(optionalRelationVO.isPresent());
        RelationVO relationVO = optionalRelationVO.get();
        assertEquals(graphVOId,relationVO.getGraphId());
        assertEquals("relabel",relationVO.getLabel());
        assertEquals("rename",relationVO.getName());
        assertTrue(relationVO.isSolid());
    }

    @Test
    void delete() {
        Optional<RelationVO> optionalRelationVO = relationService.find(relationVOIdList.get(0));
        assertTrue(optionalRelationVO.isPresent());
        relationService.delete(relationVOIdList.get(0));
        optionalRelationVO = relationService.find(relationVOIdList.get(0));
        assertFalse(optionalRelationVO.isPresent());
    }

    @Test
    void edit() {
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

        relationService.edit(relationVO);
        optionalRelationVO = relationService.find(relationVOIdList.get(0));
        assertTrue(optionalRelationVO.isPresent());
        RelationVO editRelationVO = optionalRelationVO.get();
        assertFalse(editRelationVO.isSolid());
        assertEquals("rename_edit",editRelationVO.getName());
        assertEquals("relabel_edit",editRelationVO.getLabel());
    }
}