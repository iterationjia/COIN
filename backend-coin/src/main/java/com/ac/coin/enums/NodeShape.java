package com.ac.coin.enums;

public enum NodeShape {
    NoRequirement(""),
    CIRCLE("circle"),
    TRIANGLE_UP("triangle_up"),
    RECT("rect"),
    DIAMOND("diamond");

    private String value;

    NodeShape(String value) {this.value = value;}

    @Override
    public String toString(){return value;}
}