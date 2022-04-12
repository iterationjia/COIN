package com.ac.coin.service;

import com.ac.coin.vo.ResponseVO;
import com.ac.coin.vo.UserVO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
    ResponseVO add(UserVO userVO);

    ResponseVO delete(Long userId);

    ResponseVO deleteAllGraphs(Long userId);

    ResponseVO edit(UserVO userVO);

    Optional<UserVO> find(Long userId);

    ResponseVO verify(String name,String password);

    ResponseVO findGraphs(Long userId);

    ResponseVO listFavors(String userName, String stockName);

    ResponseVO adminLoad();
}
