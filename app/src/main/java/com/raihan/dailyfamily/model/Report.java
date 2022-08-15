package com.raihan.dailyfamily.model;

public class Report {

    String name;
    String depositAmount;
    String dueAmount;
    String dueMonths;
    String email;
    String nick;
     double alltotalMeal = 0.0;
     double perMealAmount = 0.0;
     double alltotalCostAmount = 0.0;


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

    public Report(String name, String depositAmount, String dueAmount, String dueMonths, String email, String nick, double alltotalMeal, double perMealAmount, double alltotalCostAmount) {
        this.name = name;
        this.depositAmount = depositAmount;
        this.dueAmount = dueAmount;
        this.dueMonths = dueMonths;
        this.email = email;
        this.nick = nick;
        this.alltotalMeal = alltotalMeal;
        this.perMealAmount = perMealAmount;
        this.alltotalCostAmount = alltotalCostAmount;
    }

    public double getAlltotalMeal() {
        return alltotalMeal;
    }

    public void setAlltotalMeal(double alltotalMeal) {
        this.alltotalMeal = alltotalMeal;
    }

    public double getPerMealAmount() {
        return perMealAmount;
    }

    public void setPerMealAmount(double perMealAmount) {
        this.perMealAmount = perMealAmount;
    }

    public double getAlltotalCostAmount() {
        return alltotalCostAmount;
    }

    public void setAlltotalCostAmount(double alltotalCostAmount) {
        this.alltotalCostAmount = alltotalCostAmount;
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