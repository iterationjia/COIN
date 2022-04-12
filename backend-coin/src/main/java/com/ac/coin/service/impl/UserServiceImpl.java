package com.ac.coin.service.impl;

import com.ac.coin.DataLoader;
import com.ac.coin.dao.UserDAO;
import com.ac.coin.po.Graph;
import com.ac.coin.po.User;
import com.ac.coin.service.UserService;
import com.ac.coin.util.Transform;
import com.ac.coin.vo.GraphVO;
import com.ac.coin.vo.ResponseVO;
import com.ac.coin.vo.UserVO;
import com.alibaba.fastjson.JSON;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private DataLoader dataLoader;

    @Override
    public ResponseVO add(UserVO userVO) {
        if(userVO.getName().equals("admin")) {
            return ResponseVO.buildFailure("你不能注册管理员账户");
        }

        userVO.setRisk(31);
        userVO.setBalance(20);
        userVO.setStocks("{}");
        userVO.setIndustries("{}");

        User user = Transform.userPO(userVO);
        if(userDAO.findUserByName(userVO.getName()).isPresent()) {
            return ResponseVO.buildFailure("用户名已存在");
        } else return ResponseVO.buildSuccess(userDAO.addUser(user));
    }

    @Override
    public ResponseVO delete(Long userId) {
        userDAO.deleteUserById(userId);
        return ResponseVO.buildSuccess(true);
    }

    @Override
    public ResponseVO deleteAllGraphs(Long userId) {
        userDAO.deleteAllGraphsByUserId(userId);
        return ResponseVO.buildSuccess(true);
    }

    @Override
    public ResponseVO edit(UserVO userVO) {
        User user = Transform.userPO(userVO);
        return ResponseVO.buildSuccess(Transform.userVO(userDAO.editUser(user)));
    }

    @Override
    public Optional<UserVO> find(Long userId) {
        Optional<User> optionalUser = userDAO.findUserById(userId);
        return optionalUser.map(Transform::userVO);
    }

    @Override
    public ResponseVO verify(String name, String password) {
        Optional<User> optionalUser = userDAO.findUserByName(name);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            if(user.getPassword().equals(password)) return ResponseVO.buildSuccess(user.getId());
            else return ResponseVO.buildFailure("密码错误");
        }else return ResponseVO.buildFailure("用户名不存在");
    }

    @Override
    public ResponseVO findGraphs(Long userId) {
        List<Graph> graphList = userDAO.findGraphsById(userId);
        List<GraphVO> graphVOList = new ArrayList<>();
        for(Graph graph:graphList) graphVOList.add(Transform.graphVO(graph));
        return ResponseVO.buildSuccess(graphVOList);
    }

    @Override
    public ResponseVO listFavors(String userName, String stockName) {
        Optional<User> optionalUser = userDAO.findUserByName(userName);
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            Map<String,Double> originStocks = JSONObject.fromObject(user.getStocks());
            if(originStocks.containsKey(stockName)){
                originStocks.put(stockName,originStocks.get(stockName)+1);
            }else {
                originStocks.put(stockName,1.0);
            }
            user.setStocks(JSON.toJSONString(originStocks));
            userDAO.editUser(user);
            System.out.println(user.getStocks());
        }
        return ResponseVO.buildSuccess();
    }

    @Override
    public ResponseVO adminLoad() {
        dataLoader.dataLoader();
        return ResponseVO.buildSuccess();
    }
}
