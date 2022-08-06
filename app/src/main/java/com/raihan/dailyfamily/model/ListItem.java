package com.raihan.dailyfamily.model;

public class ListItem {
    private String id;
    private String txnid;
    private String date;
    private String amount;
    private String email;
    private String invoiceno;
    private String updateBy;

    public ListItem(String id, String txnid, String date, String amount, String email, String invoiceno) {
        this.id = id;
        this.txnid = txnid;
        this.date = date;
        this.amount = amount;
        this.email = email;
        this.invoiceno = invoiceno;
    }
    public ListItem(String id, String txnid, String date, String amount, String email, String invoiceno,String updateBy) {
        this.id = id;
        this.txnid = txnid;
        this.date = date;
        this.amount = amount;
        this.email = email;
        this.invoiceno = invoiceno;
        this.updateBy = updateBy;
    }

    public ListItem(String id, String date, String amount, String invoiceno, String email) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.invoiceno = invoiceno;
        this.email = email;
    }

    public String getInvoiceno() {
        return invoiceno;
    }

    public void setInvoiceno(String invoiceno) {
        this.invoiceno = invoiceno;
    }


    public ListItem(String id, String date, String amount) {
        this.id = id;
        this.date = date;
        this.amount = amount;
    }

    public ListItem(String id, String date, String amount, String invoiceno) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.invoiceno = invoiceno;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTxnid() {
        return txnid;
    }

    public void setTxnid(String txnid) {
        this.txnid = txnid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Override
    public String toString() {
        return this.txnid + "---" + date + "---" + amount + "---";
    }
}