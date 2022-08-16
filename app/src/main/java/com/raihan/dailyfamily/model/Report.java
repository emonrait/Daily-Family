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
    String id = "";
    String date = "";
    String houseRent = "";
    String electricyBill = "";
    String buaBill = "";
    String otherBill = "";
    String member = "";
    String cityBill = "";
    String mobile = "";


    public Report() {
    }

    public Report(String name, String depositAmount, String dueAmount, String dueMonths, String email, String nick) {
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

    public Report(String name, String depositAmount, String dueAmount, String dueMonths, String email, String nick, double alltotalMeal, double perMealAmount, double alltotalCostAmount, String id, String date, String houseRent, String electricyBill, String buaBill, String otherBill, String member, String cityBill, String mobile) {
        this.name = name;
        this.depositAmount = depositAmount;
        this.dueAmount = dueAmount;
        this.dueMonths = dueMonths;
        this.email = email;
        this.nick = nick;
        this.alltotalMeal = alltotalMeal;
        this.perMealAmount = perMealAmount;
        this.alltotalCostAmount = alltotalCostAmount;
        this.id = id;
        this.date = date;
        this.houseRent = houseRent;
        this.electricyBill = electricyBill;
        this.buaBill = buaBill;
        this.otherBill = otherBill;
        this.member = member;
        this.cityBill = cityBill;
        this.mobile = mobile;
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

    public String getHouseRent() {
        return houseRent;
    }

    public void setHouseRent(String houseRent) {
        this.houseRent = houseRent;
    }

    public String getElectricyBill() {
        return electricyBill;
    }

    public void setElectricyBill(String electricyBill) {
        this.electricyBill = electricyBill;
    }

    public String getBuaBill() {
        return buaBill;
    }

    public void setBuaBill(String buaBill) {
        this.buaBill = buaBill;
    }

    public String getOtherBill() {
        return otherBill;
    }

    public void setOtherBill(String otherBill) {
        this.otherBill = otherBill;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getCityBill() {
        return cityBill;
    }

    public void setCityBill(String cityBill) {
        this.cityBill = cityBill;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}