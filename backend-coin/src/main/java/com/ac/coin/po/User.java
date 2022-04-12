package com.ac.coin.po;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.List;
import java.util.Map;

@Node
public class User {
    @Id
    @GeneratedValue
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

    public User(String name, String password) {
        this.name = name;
        this.password = password;
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

    public void setRisk(double risk) {
        this.risk = risk;
    }

    public double getRisk() {
        return risk;
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