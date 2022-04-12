package com.ac.coin.enums;

public enum Size {
    DEFAULT_NODE_SIZE(3),
    DEFAULT_FONT_SIZE(1);

    private int value;

    Size(int value) {this.value = value;}

    public int getValue() {
        return value;
    }
}
