package com.raihan.dailyfamily.model;

public class MonthCost {
    private String id;
    private String date;
    private String houseRent;
    private String electricyBill;
    private String buaBill;
    private String otherBill;
    private String member;
    private String cityBill;
    private String flag;
    private String updateBy;

    public MonthCost(String id, String date, String houseRent, String electricyBill, String buaBill, String otherBill, String member, String cityBill, String flag, String updateBy) {
        this.id = id;
        this.date = date;
        this.houseRent = houseRent;
        this.electricyBill = electricyBill;
        this.buaBill = buaBill;
        this.otherBill = otherBill;
        this.member = member;
        this.cityBill = cityBill;
        this.flag = flag;
        this.updateBy = updateBy;
    }

    public MonthCost() {
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

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
}
