package com.ac.coin.vo;

public class GraphVO {
    private Long id;

    private boolean isRelationLabelVisible;

    private boolean isNodeLabelVisible;

    //必填,其余不开放初始化时自定义
    private String name;

    private String url;

    private String time;

    private Boolean isFavored;

    public GraphVO() {
        id = -1L;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
