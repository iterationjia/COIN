package com.ac.coin.po;

@org.springframework.data.neo4j.core.schema.Node
public class Stock extends Node{
    private String stock_code;
    private String start_date;
    private double st_rate;

    public String getStock_code() {
        return stock_code;
    }

    public void setStock_code(String stock_code) {
        this.stock_code = stock_code;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public void setSt_rate(double st_rate) {
        this.st_rate = st_rate;
    }

    public double getSt_rate() {
        return st_rate;
    }
}
