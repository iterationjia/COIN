package com.ac.coin.po;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
public class Graph {
    @Id
    @GeneratedValue
    private Long id;

    //必填,其余不开放初始化时自定义
    private String name;

    private boolean isRelationLabelVisible;

    private boolean isNodeLabelVisible;

    private String url;

    private String time;

    private Boolean isFavored;

    public Graph(String name) {
        this.name = name;
        //不开放初始化时自定义
        isRelationLabelVisible = true;
        isNodeLabelVisible = true;
        isFavored = false;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getFavored() {
        return isFavored;
    }

    public void setFavored(Boolean favored) {
        isFavored = favored;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRelationLabelVisible() {
        return isRelationLabelVisible;
    }

    public void setRelationLabelVisible(boolean relationLabelVisible) {
        isRelationLabelVisible = relationLabelVisible;
    }

    public boolean isNodeLabelVisible() {
        return isNodeLabelVisible;
    }

    public void setNodeLabelVisible(boolean nodeLabelVisible) {
        isNodeLabelVisible = nodeLabelVisible;
    }
}
