package com.ac.coin.controller;

import com.ac.coin.service.UserService;
import com.ac.coin.vo.ResponseVO;
import com.ac.coin.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseVO register(@RequestBody UserVO userVO){
        System.out.println(userVO.getId());
        return userService.add(userVO);
    }

    @GetMapping("/delete/{userId}")
    public ResponseVO delete(@PathVariable("userId") Long userId){
        return userService.delete(userId);
    }

    @GetMapping("/deleteAllGraphs/{userId}")
    public ResponseVO deleteAllGraphs(@PathVariable("userId") Long userId){
        return userService.deleteAllGraphs(userId);
    }

    @PostMapping("/edit")
    public ResponseVO edit(@RequestBody UserVO userVO){
        return userService.edit(userVO);
    }

    @GetMapping("/find/{userId}")
    public ResponseVO find(@PathVariable("userId") Long userId){
        Optional<UserVO> optionalUserVO = userService.find(userId);
        return optionalUserVO.map(ResponseVO::buildSuccess).orElseGet(()->ResponseVO.buildFailure("不存在该用户"));
    }

    @PostMapping("/login")
    public ResponseVO login(@RequestBody UserVO userVO){
        return userService.verify(userVO.getName(),userVO.getPassword());
    }

    @GetMapping("/findGraphs/{userId}")
    public ResponseVO findGraphs(@PathVariable("userId") Long userId){
        return userService.findGraphs(userId);
    }

    @GetMapping("/listFavors/{userName}/{stockName}")
    public ResponseVO listFavors(@PathVariable("userName") String userName, @PathVariable("stockName") String stockName){
        return userService.listFavors(userName, stockName);
    }

    @GetMapping("/adminLoad/{userName}")
    public ResponseVO adminLoad(@PathVariable("userName") String userName){
        if (userName.equals("admin")){
            return userService.adminLoad();
        }else {
            return ResponseVO.buildFailure("无权限");
        }
    }
}
