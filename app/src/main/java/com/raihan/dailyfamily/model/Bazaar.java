package com.raihan.dailyfamily.model;

public class Bazaar {
    private String date = "";
    private String email = "";
    private String amount = "";
    private String productDetails = "";
    private String flag = "";
    private String id = "";


    public Bazaar() {

    }


    public Bazaar(String id, String date, String email, String amount, String productDetails, String flag) {
        this.date = date;
        this.email = email;
        this.amount = amount;
        this.productDetails = productDetails;
        this.flag = flag;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(String productDetails) {
        this.productDetails = productDetails;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
