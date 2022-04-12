package com.ac.coin.vo;

public class StockVO extends NodeVO{
    private String stock_code;
    private String start_date;
    private double st_rate;

    public String getStock_code() {
        return stock_code;
    }

    public void setStock_code(String stock_code) {
        this.stock_code = stock_code;
    }

    public double getSt_rate() {
        return st_rate;
    }

    public void setSt_rate(double st_rate) {
        this.st_rate = st_rate;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }
}
