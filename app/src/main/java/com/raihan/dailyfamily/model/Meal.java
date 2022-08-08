package com.raihan.dailyfamily.model;

public class Meal {
    private String date = "";
    private String email = "";
    private String breakfast = "";
    private String launch = "";
    private String dinner = "";
    private String flag = "";
    private String id = "";
    private String name = "";
    private String nick = "";


    public Meal() {

    }

    public Meal(String id, String date, String email, String breakfast, String launch, String dinner, String name,String nick) {
        this.id = id;
        this.date = date;
        this.email = email;
        this.breakfast = breakfast;
        this.launch = launch;
        this.dinner = dinner;
        this.nick = nick;
        this.name = name;
    }

    public Meal(String id, String date, String email, String breakfast, String launch, String dinner, String flag) {
        this.id = id;
        this.date = date;
        this.email = email;
        this.breakfast = breakfast;
        this.launch = launch;
        this.dinner = dinner;
        this.flag = flag;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(String breakfast) {
        this.breakfast = breakfast;
    }

    public String getLaunch() {
        return launch;
    }

    public void setLaunch(String launch) {
        this.launch = launch;
    }

    public String getDinner() {
        return dinner;
    }

    public void setDinner(String dinner) {
        this.dinner = dinner;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
