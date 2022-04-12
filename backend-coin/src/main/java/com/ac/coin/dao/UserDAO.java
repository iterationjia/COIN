package com.ac.coin.dao;

import com.ac.coin.po.Graph;
import com.ac.coin.po.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    Long addUser(User user);
    void deleteUserById(Long userId);
    void deleteAllGraphsByUserId(Long userId);
    User editUser(User user);
    Optional<User> findUserById(Long userId);
    Optional<User> findUserByName(String name);
    List<Graph> findGraphsById(Long userId);
}