package com.ac.coin.po;

@org.springframework.data.neo4j.core.schema.Node
public class Company extends Node{
    private Long company_id;
    private String register_location;
    private double register_capital;
    private String establish_date;

    private int employee_num;
    private double retire_rate;
    private String average_education;

    public Company(Long company_id) {
        this.company_id = company_id;
    }

    public Long getCompany_id() {
        return company_id;
    }

    public void setCompany_id(Long company_id) {
        this.company_id = company_id;
    }

    public String getRegister_location() {
        return register_location;
    }

    public void setRegister_location(String register_location) {
        this.register_location = register_location;
    }

    public double getRegister_capital() {
        return register_capital;
    }

    public void setRegister_capital(double register_capital) {
        this.register_capital = register_capital;
    }

    public String getEstablish_date() {
        return establish_date;
    }

    public void setEstablish_date(String establish_date) {
        this.establish_date = establish_date;
    }

    public int getEmployee_num() {
        return employee_num;
    }

    public void setEmployee_num(int employee_num) {
        this.employee_num = employee_num;
    }

    public double getRetire_rate() {
        return retire_rate;
    }

    public void setRetire_rate(double retire_rate) {
        this.retire_rate = retire_rate;
    }

    public String getAverage_education() {
        return average_education;
    }

    public void setAverage_education(String average_education) {
        this.average_education = average_education;
    }
}
