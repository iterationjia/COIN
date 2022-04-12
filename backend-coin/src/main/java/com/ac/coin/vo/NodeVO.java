package com.ac.coin.vo;

import java.util.HashMap;
import java.util.Map;

public class NodeVO {
    //不可自定义
    private Long id;

    //必填
    private Long graphId;

    private String name;

    private String label;

    private float x;

    private float y;

    //可选
    private String shape;

    private String color;

    private int node_size;

    private int font_size;

    private float typesetting_x;

    private float typesetting_y;

    private boolean isShown;


    //不可自定义
    private boolean isHighlighted;

    public NodeVO(){
        id = -1L;
        node_size = -1;
        font_size = -1;
        isShown = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGraphId() {
        return graphId;
    }

    public void setGraphId(Long graphId) {
        this.graphId = graphId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getNode_size() {
        return node_size;
    }

    public void setNode_size(int node_size) {
        this.node_size = node_size;
    }

    public int getFont_size() {
        return font_size;
    }

    public void setFont_size(int font_size) {
        this.font_size = font_size;
    }

    public float getTypesetting_x() {
        return typesetting_x;
    }

    public void setTypesetting_x(float typesetting_x) {
        this.typesetting_x = typesetting_x;
    }

    public float getTypesetting_y() {
        return typesetting_y;
    }

    public void setTypesetting_y(float typesetting_y) {
        this.typesetting_y = typesetting_y;
    }

    public boolean isHighlighted() {
        return isHighlighted;
    }

    public void setHighlighted(boolean highlighted) {
        isHighlighted = highlighted;
    }

    public boolean isShown() {
        return isShown;
    }

    public void setShown(boolean shown) {
        isShown = shown;
    }
}
