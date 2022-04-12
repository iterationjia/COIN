package com.ac.coin.controller;

import com.ac.coin.service.RelationService;
import com.ac.coin.vo.NodeVO;
import com.ac.coin.vo.RelationVO;
import com.ac.coin.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/relation")
@CrossOrigin
public class RelationController {
    @Autowired
    RelationService relationService;

    @PostMapping("/add")
    public ResponseVO add(@RequestBody RelationVO relationVO){
        return relationService.add(relationVO);
    }

    @GetMapping("/delete/{relationId}")
    public ResponseVO delete(@PathVariable("relationId") long relationId){
        return relationService.delete(relationId);
    }


    @PostMapping("/edit")
    public ResponseVO edit(@RequestBody RelationVO relationVO){
        return  relationService.edit(relationVO);
    }

    @GetMapping("/find/{relationId}")
    public ResponseVO find(@PathVariable("relationId") long relationId){
        Optional<RelationVO> optionalRelation = relationService.find(relationId);
        return optionalRelation.map(ResponseVO::buildSuccess).orElseGet(() -> ResponseVO.buildFailure("不存在该关系"));
    }
}
