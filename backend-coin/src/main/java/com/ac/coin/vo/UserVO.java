package com.ac.coin.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserVO {
    //不可自定义
    private Long id;

    //必填
    private String name;
    //必填
    private String password;

    private double risk;

    private double balance;

    private boolean acceptSt;

    private String stocks;

    private String industries;

    public UserVO(){
        this.id = -1L;
        this.risk = -1;
        this.balance = -1.0;
        this.acceptSt = true;
    }

    public boolean isAcceptSt() {
        return acceptSt;
    }

    public void setAcceptSt(boolean acceptSt) {
        this.acceptSt = acceptSt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getRisk() {
        return risk;
    }

    public void setRisk(double risk) {
        this.risk = risk;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getStocks() {
        return stocks;
    }

    public void setStocks(String stocks) {
        this.stocks = stocks;
    }

    public String getIndustries() {
        return industries;
    }

    public void setIndustries(String industries) {
        this.industries = industries;
    }
}
