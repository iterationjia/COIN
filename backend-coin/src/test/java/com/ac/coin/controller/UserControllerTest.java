package com.ac.coin.controller;

import org.junit.jupiter.api.Test;
import com.ac.coin.dao.UserDAO;
import com.ac.coin.po.User;
import com.ac.coin.service.GraphService;
import com.ac.coin.service.NodeService;
import com.ac.coin.service.RelationService;
import com.ac.coin.service.UserService;
import com.ac.coin.vo.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
class UserControllerTest {

    @Autowired
    NodeService nodeService;
    @Autowired
    RelationService relationService;
    @Autowired
    GraphService graphService;
    @Autowired
    UserService userService;

    private List<Long> userVOIdList;

    //gzj的内容
    private Long g_gvid_0;
    private Long g_nvid_0;
    private Long g_nvid_1;
    private Long g_rvid_0;

    private Long g_gvid_1;
    private Long g_nvid_2;
    private Long g_nvid_3;
    private Long g_rvid_1;

    //zzy的内容
    private Long z_gvid;
    private Long z_nvid_0;
    private Long z_nvid_1;
    private Long z_rvid;

    private static MockMvc mockMvc;

    String path="/api/user";

    @Autowired
    public UserControllerTest(UserController userController){
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @BeforeEach
    public void setUp(){
        userVOIdList = new ArrayList<>();
        UserVO userVO0 = new UserVO();
        userVO0.setName("gzj");
        userVO0.setPassword("g123456");
        userVOIdList.add((Long) userService.add(userVO0).getContent());

        UserVO userVO1 = new UserVO();
        userVO1.setName("zzy");
        userVO1.setPassword("z123456");
        userVOIdList.add((Long) userService.add(userVO1).getContent());

        //gzj的内容

        //gzj第一张图的内容
        GraphVO graphVO0 = new GraphVO();
        graphVO0.setName("g_graph0");
        g_gvid_0 = (Long) graphService.add(graphVO0,userVOIdList.get(0)).getContent();

        NodeVO g_nodeVO0 = new NodeVO();
        g_nodeVO0.setGraphId(g_gvid_0);
        g_nvid_0 = (Long) nodeService.add(g_nodeVO0).getContent();

        NodeVO g_nodeVO1 = new NodeVO();
        g_nodeVO1.setGraphId(g_gvid_0);
        g_nvid_1 = (Long) nodeService.add(g_nodeVO1).getContent();

        RelationVO g_relationVO0 = new RelationVO();
        g_relationVO0.setGraphId(g_gvid_0);
        g_relationVO0.setSource(g_nvid_0);
        g_relationVO0.setTarget(g_nvid_1);
        g_relationVO0.setName("g_name");
        g_relationVO0.setLabel("g_label");
        g_rvid_0 = (Long) relationService.add(g_relationVO0).getContent();

        //gzj第二张图的内容
        GraphVO graphVO1 = new GraphVO();
        graphVO1.setName("g_graph1");
        g_gvid_1 = (Long) graphService.add(graphVO1,userVOIdList.get(0)).getContent();


        NodeVO g_nodeVO2 = new NodeVO();
        g_nodeVO2.setGraphId(g_gvid_1);
        g_nvid_2 = (Long) nodeService.add(g_nodeVO2).getContent();

        NodeVO g_nodeVO3 = new NodeVO();
        g_nodeVO3.setGraphId(g_gvid_1);
        g_nvid_3 = (Long) nodeService.add(g_nodeVO3).getContent();

        RelationVO g_relationVO1 = new RelationVO();
        g_relationVO1.setGraphId(g_gvid_1);
        g_relationVO1.setSource(g_nvid_2);
        g_relationVO1.setTarget(g_nvid_3);
        g_relationVO1.setName("g_name");
        g_relationVO1.setLabel("g_label");
        g_rvid_1 = (Long) relationService.add(g_relationVO1).getContent();

        //zzy图的内容
        GraphVO graphVO2 = new GraphVO();
        graphVO2.setName("z_graph");
        z_gvid = (Long) graphService.add(graphVO2,userVOIdList.get(1)).getContent();

        NodeVO z_nodeVO0 = new NodeVO();
        z_nodeVO0.setGraphId(z_gvid);
        z_nvid_0 = (Long) nodeService.add(z_nodeVO0).getContent();

        NodeVO z_nodeVO1 = new NodeVO();
        z_nodeVO1.setGraphId(z_gvid);
        z_nvid_1 = (Long) nodeService.add(z_nodeVO1).getContent();

        RelationVO z_relationVO = new RelationVO();
        z_relationVO.setGraphId(z_gvid);
        z_relationVO.setSource(z_nvid_0);
        z_relationVO.setTarget(z_nvid_1);
        z_relationVO.setName("z_name");
        z_relationVO.setLabel("z_label");
        z_rvid = (Long) relationService.add(z_relationVO).getContent();
    }

    @AfterEach
    public void tearDown(){
        for(Long userId:userVOIdList){
            userService.delete(userId);
        }
    }

    @Test
    void register() throws Exception {
        UserVO userVO = new UserVO();
        userVO.setName("lyx");
        userVO.setPassword("l123456");
        ResultActions resultActions = mockMvc.perform(
                post(path+"/register")
                        .content(JSON.toJSONString(userVO))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        Long userVOId = ((Integer) NodeControllerTest.toContent(resultActions)).longValue();
        assertNotNull(userVOId,"用户创建失败");
        userVOIdList.add(userVOId);
        Optional<UserVO> optionalUserVO = userService.find(userVOId);
        assertTrue(optionalUserVO.isPresent());
        UserVO queryUserVO = optionalUserVO.get();
        assertEquals("lyx",queryUserVO.getName());

        userVO = new UserVO();
        userVO.setName("lyx");
        userVO.setPassword("l123456");
        resultActions = mockMvc.perform(
                post(path+"/register")
                        .content(JSON.toJSONString(userVO))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        MockHttpServletResponse response = resultActions.andReturn().getResponse();
        assertEquals(2,response.getStatus()/100);
        String res = response.getContentAsString(StandardCharsets.UTF_8);
        ResponseVO responseVO = JSON.toJavaObject(JSON.parseObject(res),ResponseVO.class);

        assertFalse(responseVO.getSuccess());
        assertEquals("用户名已存在",responseVO.getMessage());
    }

    @Test
    @SuppressWarnings("unchecked")
    void deleteAllGraphs() throws Exception{
        Optional<UserVO> optionalUserVO = userService.find(userVOIdList.get(0));
        assertTrue(optionalUserVO.isPresent());
        UserVO queryUserVO = optionalUserVO.get();
        List<GraphVO> g_graphVOList = (List<GraphVO>) userService.findGraphs(queryUserVO.getId()).getContent();
        assertEquals(2,g_graphVOList.size());
        assertEquals(2,graphService.findAllNodes(g_graphVOList.get(0).getId()).size());
        assertEquals(1,graphService.findAllRelations(g_graphVOList.get(0).getId()).size());

        Long gvid0 = g_graphVOList.get(0).getId();
        ResultActions resultActions = mockMvc.perform(
                get(path+"/deleteAllGraphs/"+ userVOIdList.get(0))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        assertTrue((Boolean) NodeControllerTest.toContent(resultActions));

        optionalUserVO = userService.find(userVOIdList.get(0));
        assertTrue(optionalUserVO.isPresent());
        queryUserVO = optionalUserVO.get();
        g_graphVOList = (List<GraphVO>) userService.findGraphs(queryUserVO.getId()).getContent();
        assertEquals(0,g_graphVOList.size());
        assertEquals(0,graphService.findAllNodes(gvid0).size());
        assertEquals(0,graphService.findAllRelations(gvid0).size());
    }

    @Test
    void delete() throws Exception {
        Optional<UserVO> optionalUserVO = userService.find(userVOIdList.get(0));
        assertTrue(optionalUserVO.isPresent());
        ResultActions resultActions = mockMvc.perform(
                get(path+"/delete/"+ userVOIdList.get(0))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        optionalUserVO = userService.find(userVOIdList.get(0));
        assertFalse(optionalUserVO.isPresent());
    }

    @Test
    void edit() throws Exception {
        UserVO userVO = new UserVO();
        userVO.setName("GZJ");
        userVO.setPassword("G123456");
        userVO.setId(userVOIdList.get(0));
        ResultActions resultActions = mockMvc.perform(
                post(path+"/edit")
                        .content(JSON.toJSONString(userVO))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        Optional<UserVO> optionalUserVO = userService.find(userVOIdList.get(0));
        assertTrue(optionalUserVO.isPresent());
        UserVO queryUserVO = optionalUserVO.get();
        assertEquals("GZJ",queryUserVO.getName());
        assertEquals("G123456",queryUserVO.getPassword());
    }


    @Test
    void login() throws Exception {
        UserVO userVO = new UserVO();
        userVO.setName("dsy");
        userVO.setPassword("d123456");
        ResultActions resultActions = mockMvc.perform(
                post(path+"/login")
                        .content(JSON.toJSONString(userVO))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        MockHttpServletResponse response = resultActions.andReturn().getResponse();
        assertEquals(2,response.getStatus()/100);
        String res = response.getContentAsString(StandardCharsets.UTF_8);
        ResponseVO responseVO = JSON.toJavaObject(JSON.parseObject(res),ResponseVO.class);

        assertFalse(responseVO.getSuccess());
        assertEquals("用户名不存在",responseVO.getMessage());

        userVO = new UserVO();
        userVO.setName("zzy");
        userVO.setPassword("z1234567");
        resultActions = mockMvc.perform(
                post(path+"/login")
                        .content(JSON.toJSONString(userVO))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        response = resultActions.andReturn().getResponse();
        assertEquals(2,response.getStatus()/100);
        res = response.getContentAsString(StandardCharsets.UTF_8);
        responseVO = JSON.toJavaObject(JSON.parseObject(res),ResponseVO.class);

        assertFalse(responseVO.getSuccess());
        assertEquals("密码错误",responseVO.getMessage());

        userVO = new UserVO();
        userVO.setName("gzj");
        userVO.setPassword("g123456");
        resultActions = mockMvc.perform(
                post(path+"/login")
                        .content(JSON.toJSONString(userVO))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        response = resultActions.andReturn().getResponse();
        assertEquals(2,response.getStatus()/100);
        res = response.getContentAsString(StandardCharsets.UTF_8);
        responseVO = JSON.toJavaObject(JSON.parseObject(res),ResponseVO.class);
        assertTrue(responseVO.getSuccess());
    }

    @Test
    void findGraphs() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get(path+"/findGraphs/"+ userVOIdList.get(0))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        Set<Long> graphVOIdSet = ((JSONArray) NodeControllerTest.toContent(resultActions)).toJavaList(GraphVO.class).stream().map(GraphVO::getId).collect(Collectors.toSet());
        Set<Long> testGraphVOIdSet = new HashSet<>();
        testGraphVOIdSet.add(g_gvid_0);
        testGraphVOIdSet.add(g_gvid_1);
        assertEquals(testGraphVOIdSet,graphVOIdSet);
    }
}