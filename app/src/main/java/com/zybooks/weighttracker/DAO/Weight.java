package com.zybooks.weighttracker.DAO;

import java.util.Date;

public class Weight {
    private long id;
    private String username;
    private Date date;
    private int weight;

    public Weight(long id, String username, String date, int weight) {
        this.id = id;
        this.username = username;
        Date newDate = new Date(date);
        this.date = newDate;
        this.weight = weight;
    }

    public Weight() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(String date) {
        Date newDate = new Date(date);
        this.date = newDate;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
