package com.raihan.dailyfamily.model;

public class Report {

    String name;
    String depositAmount;
    String dueAmount;
    String dueMonths;
    String email;
    String nick;


    public Report() {
    }

    public Report(String name, String depositAmount, String dueAmount, String dueMonths, String email,String nick) {
        this.name = name;
        this.depositAmount = depositAmount;
        this.dueAmount = dueAmount;
        this.dueMonths = dueMonths;
        this.email = email;
        this.nick = nick;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(String depositAmount) {
        this.depositAmount = depositAmount;
    }

    public String getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(String dueAmount) {
        this.dueAmount = dueAmount;
    }

    public String getDueMonths() {
        return dueMonths;
    }

    public void setDueMonths(String dueMonths) {
        this.dueMonths = dueMonths;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}