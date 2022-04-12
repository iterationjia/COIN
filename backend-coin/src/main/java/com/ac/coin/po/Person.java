package com.ac.coin.po;

@org.springframework.data.neo4j.core.schema.Node
public class Person extends Node{
    //name在父类
    private String title_class;
    private String title_level;
    private String title;
    private int birth_year;
    private String gender;
    private String degree;
    private String resume;

    public String getTitle_class() {
        return title_class;
    }

    public void setTitle_class(String title_class) {
        this.title_class = title_class;
    }

    public String getTitle_level() {
        return title_level;
    }

    public void setTitle_level(String title_level) {
        this.title_level = title_level;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getBirth_year() {
        return birth_year;
    }

    public void setBirth_year(int birth_year) {
        this.birth_year = birth_year;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }
}
