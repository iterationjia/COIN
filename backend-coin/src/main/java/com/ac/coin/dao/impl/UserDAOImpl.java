package com.ac.coin.dao.impl;

import com.ac.coin.dao.GraphDAO;
import com.ac.coin.dao.UserDAO;
import com.ac.coin.dao.repository.GraphRepository;
import com.ac.coin.dao.repository.UserRepository;
import com.ac.coin.po.Graph;
import com.ac.coin.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDAOImpl implements UserDAO {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GraphRepository graphRepository;
    @Autowired
    private GraphDAO graphDAO;

    @Override
    public Long addUser(User user) {
        userRepository.save(user);
        return user.getId();
    }

    @Override
    public void deleteUserById(Long userId) {
        deleteAllGraphsByUserId(userId);
        userRepository.deleteById(userId);
    }

    @Override
    public void deleteAllGraphsByUserId(Long userId) {
        List<Graph> graphList = findGraphsById(userId);
        for(Graph graph:graphList) {
            graphDAO.deleteGraphById(graph.getId());
        }
    }

    @Override
    public User editUser(User user) {
        userRepository.save(user);
        return user;
    }

    @Override
    public Optional<User> findUserById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> findUserByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    public List<Graph> findGraphsById(Long userId) {
        return graphRepository.findGraphsById(userId);
    }
}
