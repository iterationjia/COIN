package com.ac.coin.po;

import com.ac.coin.enums.NodeShape;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;

@org.springframework.data.neo4j.core.schema.Node
public class Node {
    @Id
    @GeneratedValue
    private Long id;

    private Long graphId;

    private String name;

    private NodeShape shape;

    private String color;

    private int node_size;

    private int font_size;

    private String label;

    private boolean isHighlighted;

    private float x;

    private float y;

    //排版模式坐标
    private float typesetting_x;

    private float typesetting_y;

    private boolean isShown;

    public Node() {
        this.isHighlighted = false;
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

    public void setShape(NodeShape shape) {
        this.shape = shape;
    }

    public NodeShape getShape() {
        return shape;
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isHighlighted() {
        return isHighlighted;
    }

    public void setHighlighted(boolean highlighted) {
        isHighlighted = highlighted;
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

    public boolean isShown() {
        return isShown;
    }

    public void setShown(boolean shown) {
        isShown = shown;
    }
}

