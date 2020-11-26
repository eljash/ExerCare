package com.example.sovellus;

import java.util.Date;

public class User {
    private String name;
    private double startWeight;
    private double weightGoal;
    private int timeGoal;
    private int year;
    private int month;
    private int day;
    private Date creationDate;

    public User(String name) {
        this.name = name;
        this.timeGoal = 60;
        this.creationDate = new Date();
    }

    public int getTimeGoal() {
        return this.timeGoal;
    }
}
