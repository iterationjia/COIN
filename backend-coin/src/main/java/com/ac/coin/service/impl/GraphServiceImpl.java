package com.ac.coin.service.impl;

import com.ac.coin.Recommand;
import com.ac.coin.dao.GraphDAO;
import com.ac.coin.dao.NodeDAO;
import com.ac.coin.dao.RelationDAO;
import com.ac.coin.dao.UserDAO;
import com.ac.coin.enums.NodeShape;
import com.ac.coin.po.*;
import com.ac.coin.service.GraphService;
import com.ac.coin.ChatBot;
import com.ac.coin.util.Transform;

import com.alibaba.fastjson.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import com.ac.coin.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class GraphServiceImpl implements GraphService {
    @Autowired
    private GraphDAO graphDAO;
    @Autowired
    private NodeDAO nodeDAO;
    @Autowired
    private RelationDAO relationDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private Recommand recommand;


    @Override
    public ResponseVO add(GraphVO graphVO, Long userId) {
        Graph graph = Transform.graphPO(graphVO);
        return ResponseVO.buildSuccess(graphDAO.addGraph(graph,userId));
    }

    @Override
    public ResponseVO delete(Long graphId) {
        graphDAO.deleteGraphById(graphId);
        return ResponseVO.buildSuccess(true);
    }

    @Override
    public ResponseVO deleteAll(Long graphId) {
        graphDAO.deleteAllById(graphId);
        return ResponseVO.buildSuccess(true);
    }

    @Override
    public ResponseVO deleteAllGraphs() {
        graphDAO.deleteAllGraphs();
        return ResponseVO.buildSuccess(true);
    }

    @Override
    public ResponseVO edit(GraphVO graphVO) {
        Graph graph = Transform.graphPO(graphVO);
        return ResponseVO.buildSuccess(Transform.graphVO(graphDAO.editGraph(graph)));
    }

    @Override
    public List<NodeVO> findAllNodes(Long graphId) {
        List<Node> nodeList = nodeDAO.findAllNodesByGraphId(graphId);
        List<NodeVO> nodeVOList = new ArrayList<>();
        for(Node node:nodeList){
            nodeVOList.add(Transform.nodeVO(node));
        }
        return nodeVOList;
    }

    @Override
    public List<RelationVO> findAllRelations(Long graphId) {
        List<Relation> relationList = relationDAO.findAllRelationsByGraphId(graphId);
        List<RelationVO> relationVOList = new ArrayList<>();
        for(Relation relation:relationList){
            relationVOList.add(Transform.relationVO(relation));
        }
        return relationVOList;
    }

    @Override
    public List<GraphVO> findAllGraphs() {
        List<Graph> graphList = graphDAO.findAllGraphs();
        List<GraphVO> graphVOList = new ArrayList<>();
        for(Graph graph:graphList){
            graphVOList.add(Transform.graphVO(graph));
        }
        return graphVOList;
    }

    @Override
    public Optional<GraphVO> find(Long graphId) {
        Optional<Graph> optionalGraph = graphDAO.findGraphById(graphId);
        return optionalGraph.map(Transform::graphVO);
    }

    @Override
    public List<NodeVO> searchNodeByOptions(NodeVO nodeVO) {
        int nameF,colorF,shapeF;
        nameF=0;colorF=0;shapeF=0;

        String name=nodeVO.getName();
        if (name.length()!=0) nameF=1;
        Long id=nodeVO.getGraphId();

        HashSet<String> colorSet = new HashSet<>();
        if (nodeVO.getColor().length()!=0) {
            colorF = 1;
            String[] colors = nodeVO.getColor().split(" ");
            colorSet = new HashSet<>(Arrays.asList(colors));
        }

        HashSet<String> shapeSet=new HashSet<>();
        if (nodeVO.getShape().length()!=0) {
            shapeF = 1;
            String[] shapes = nodeVO.getShape().split(" ");
            shapeSet = new HashSet<>(Arrays.asList(shapes));
        }



        List<Node> nodes=nodeDAO.findAllNodesByGraphId(id);
        List<Node> nodesAfterChoose=new ArrayList<>();

        if(nameF==1){
            for (Node node:nodes){
                if (node.getName().equals(name)) nodesAfterChoose.add(node);
            }
        }
        else nodesAfterChoose=nodes;

        if (colorF==1){
            List<Node> nodesAfterColor=new ArrayList<>();
            for (Node node:nodesAfterChoose){
                if (colorSet.contains(node.getColor())){
                    nodesAfterColor.add(node);
                }
            }
            nodesAfterChoose=nodesAfterColor;
        }
        if (shapeF==1){
            List<Node> nodesAfterShape=new ArrayList<>();
            for (Node node:nodesAfterChoose){
                if (shapeSet.contains(node.getShape().toString())){
                    nodesAfterShape.add(node);
                }
            }
            nodesAfterChoose=nodesAfterShape;
        }
        List<NodeVO> nodeVOS=new ArrayList<>();
        for (Node node:nodesAfterChoose){
            nodeVOS.add(Transform.nodeVO(node));
        }
        return nodeVOS;
    }

    @Override
    public List<NodeVO> loadGraph(String jsonString,Long graphId) throws IOException {
        List<Node> nodes = nodeDAO.findAllNodesByGraphId(graphId);
        HashMap<String, Long> map = new HashMap<String, Long>();
        for (Node node : nodes) {
            map.put(node.getName(), node.getId());
        }
        JSONObject rootObject = JSONObject.fromObject(jsonString);
        JSONArray nodeArray = rootObject.getJSONArray("nodes");

        Long _graphId = null;
        for (int i = 0; i < nodeArray.size(); i++) {
            _graphId = nodeArray.getJSONObject(i).getLong("graphId");
            String name = nodeArray.getJSONObject(i).getString("name");
            String label = nodeArray.getJSONObject(i).getString("label");
            float x = (float) nodeArray.getJSONObject(i).getDouble("x");
            float y = (float) nodeArray.getJSONObject(i).getDouble("y");

            if (!map.containsKey(name)) {
                Node node = new Node();
                node.setGraphId(_graphId);
                node.setName(name);
                node.setLabel(label);
                nodeDAO.addNode(node);
                map.put(name, node.getId());
            }
        }
        JSONArray relationArray = rootObject.getJSONArray("relations");
        for (int i = 0; i < relationArray.size(); i++) {
            String fromName = relationArray.getJSONObject(i).getString("fromName");
            String toName = relationArray.getJSONObject(i).getString("toName");
            Long _graphId_ = relationArray.getJSONObject(i).getLong("graphId");
            String name = relationArray.getJSONObject(i).getString("name");
            String label = relationArray.getJSONObject(i).getString("label");

            Relation relation = new Relation();
            relation.setGraphId(_graphId_);
            relation.setFromId(map.get(fromName));
            relation.setToId(map.get(toName));
            relation.setName(name);
            relation.setLabel(label);
            relationDAO.addRelation(relation);
        }
        return findAllNodes(_graphId);
    }

    @Override
    public List<NodeVO> fuzzyMatching(String str,Long graphId) {
        List<Node> nodeList=nodeDAO.findAllNodesByGraphId(graphId);
        List<NodeVO> nodeVOS =new ArrayList<>();
        StringBuilder pattern = new StringBuilder(".*");
        for (char c:str.toCharArray()){
            pattern.append(c);
            pattern.append(".*");
        }

        Pattern r = Pattern.compile(pattern.toString());
        for (Node node:nodeList) {
            String name=node.getName();
            Matcher m = r.matcher(name);
            if (m.find()){
                nodeVOS.add(Transform.nodeVO(node));
            }
        }
        return nodeVOS;
    }

    @Override
    public List<NodeVO> typesetting(Long graphId) {
        //第一个节点位置
        int firstPosition_x=40;
        int firstPosition_y=40;

        //节点左右上下间隔
        int space_x=100;
        int space_y=100;


        List<Node> nodeList = nodeDAO.findAllNodesByGraphId(graphId);

        HashMap<NodeShape, Integer> shapeMap=new HashMap<>();
        for (Node node:nodeList){
            if (!shapeMap.containsKey(node.getShape())){
                shapeMap.put(node.getShape(),shapeMap.size());
            }
        }

        int[] shapeM=new int[100];
        for (Node node:nodeList){
            int shape=shapeMap.get(node.getShape());
            int before=0;
            before=shapeM[shape];
            node.setTypesetting_x(firstPosition_x+before*space_x);
            node.setTypesetting_y(firstPosition_y+shape*space_y);
            nodeDAO.editNode(node);
            shapeM[shape]++;
        }

        return findAllNodes(graphId);
    }

    @Override
    public String[] chatBot(String question) throws IOException, InterruptedException {
        return (ChatBot.question_analysis(question));
    }

    @Override
    public ResponseVO recommand30(String userName,String graphName) {
        return recommand.recommand30(userName, graphName);
    }

    @Override
    public ResponseVO favorUpdate(String userName, String industryName, String stockName) {
        Optional<User> optionalUser = userDAO.findUserByName(userName);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            if(!industryName.equals("unknown")){
                Map<String,Double> originIndustries = JSONObject.fromObject(user.getIndustries());
                if(originIndustries.containsKey(industryName)){
                    originIndustries.put(industryName,originIndustries.get(industryName)+0.3);
                }else {
                    originIndustries.put(industryName,0.3);
                }
                user.setIndustries(JSON.toJSONString(originIndustries));
            }
            if(!stockName.equals("unknown")){
                Map<String,Double> originStocks = JSONObject.fromObject(user.getStocks());
                if(originStocks.containsKey(stockName)){
                    originStocks.put(stockName,originStocks.get(stockName)+1);
                }else {
                    originStocks.put(stockName,1.0);
                }
                user.setStocks(JSON.toJSONString(originStocks));
            }
            userDAO.editUser(user);
            System.out.println(user.getIndustries());
            System.out.println(user.getStocks());
        }

        return ResponseVO.buildSuccess();
    }

    public static String getPercentValue(double similarity){
        NumberFormat fmt = NumberFormat.getPercentInstance();
        fmt.setMaximumFractionDigits(2);//最多两位百分小数，如25.23%
        return fmt.format(similarity);
    }
}
