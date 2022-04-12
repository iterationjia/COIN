package com.ac.coin.controller;

import com.ac.coin.service.GraphService;
import com.ac.coin.vo.GraphVO;
import com.ac.coin.vo.NodeVO;
import com.ac.coin.vo.ResponseVO;
import com.ac.coin.vo.UserVO;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/graph")
@CrossOrigin
public class GraphController {
    @Autowired
    private GraphService graphService;

    @PostMapping("/add/{userId}")
    public ResponseVO add(@RequestBody GraphVO graphVO, @PathVariable("userId") Long userId){
        return graphService.add(graphVO,userId);
    }

    @GetMapping("/delete/{graphId}")
    public ResponseVO delete(@PathVariable("graphId") Long graphId){
        return graphService.delete(graphId);
    }

    @GetMapping("/deleteAll/{graphId}")
    public ResponseVO deleteAll(@PathVariable("graphId") Long graphId){
        return graphService.deleteAll(graphId);
    }

    @PostMapping("/edit")
    public ResponseVO edit(@RequestBody GraphVO graphVO){
        return graphService.edit(graphVO);
    }

    @GetMapping("/findAllNodes/{graphId}")
    public ResponseVO findAllNode(@PathVariable("graphId") Long graphId){
        return ResponseVO.buildSuccess(graphService.findAllNodes(graphId));
    }

    @GetMapping("/findAllRelations/{graphId}")
    public ResponseVO findAllRelation(@PathVariable("graphId") Long graphId){
        return ResponseVO.buildSuccess(graphService.findAllRelations(graphId));
    }

    @GetMapping("/findAllGraphs")
    public ResponseVO findAllGraph(){
        return ResponseVO.buildSuccess(graphService.findAllGraphs());
    }


    @PostMapping("/searchNode")
    public ResponseVO searchNodeByOptions(@RequestBody NodeVO nodeVO){
        return ResponseVO.buildSuccess(graphService.searchNodeByOptions(nodeVO));}

    @PostMapping(value = "/loadGraph/{graphId}")
    public ResponseVO loadGraph(@RequestParam(value = "file") MultipartFile multipartFile, @PathVariable("graphId")Long graphId)throws Exception {
        String fileName = multipartFile.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));

        String filePath = "E:\\test\\";

        File dest = new File(filePath + fileName);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        multipartFile.transferTo(dest);
        String jsonString = FileUtils.readFileToString(dest);
        return ResponseVO.buildSuccess(graphService.loadGraph(jsonString, graphId));
    }

    @GetMapping(value = "/fuzzyMatching/{graphId}/{string}")
    public ResponseVO fuzzyMatching(@PathVariable("string") String str,@PathVariable("graphId")Long graphId){
        System.out.println(str);
        return ResponseVO.buildSuccess(graphService.fuzzyMatching(str,graphId));
    }

    @GetMapping("/typesetting/{id}")
    public ResponseVO typesetting(@PathVariable("id") Long id){
        return ResponseVO.buildSuccess(graphService.typesetting(id));
    }

    @PostMapping(value = "/chatBot/{userName}")
    public ResponseVO chatBot(@RequestParam(value = "question") String question,@PathVariable("userName") String userName) throws IOException, InterruptedException {
        System.out.println(question);
        String[] answers=graphService.chatBot(question);
        String secondIndustry=answers[1];
        String stock=answers[2];
        graphService.favorUpdate(userName,secondIndustry,stock);
        return ResponseVO.buildSuccess(answers[0]);
    }

    @GetMapping("/recommand/{userName}")
    public ResponseVO recommand30(@PathVariable("userName") String userName){
        return graphService.recommand30(userName,"JoinQuaint");
    }
}

