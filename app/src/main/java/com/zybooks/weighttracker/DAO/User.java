package com.zybooks.weighttracker.DAO;

public class User {
    private long id;
    private String username;
    private String password;
    private int goal;
    private int sendSMS;
    private String phone;

    public User(long id, String username, String password, int goal, int sendSMS, String phone) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.goal = goal;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public int getSendSMS() {
        return sendSMS;
    }

    public void setSendSMS(int sendSMS) {
        this.sendSMS = sendSMS;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
