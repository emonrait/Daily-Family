package com.raihan.dailyfamily.model;

import android.app.Application;

import java.util.ArrayList;

public class GlobalVariable extends Application {
    private String timeCount = "0";
    private String totalAmount = "0";
    private String requireAmount = "0";
    private String myAmount = "0";
    private String url = "";
    private String version = "0";
    private String versionName = "0";
    private String newversioncode = "0";
    private String useremail = "";
    private String role = "0";
    private String deviceid = "0";
    private String model = "0";
    private ArrayList<String> emailList;
    private boolean amountFlag=true;
    private boolean memberFlag=true;

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getTimeCount() {
        return timeCount;
    }

    public void setTimeCount(String timeCount) {
        this.timeCount = timeCount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getMyAmount() {
        return myAmount;
    }

    public void setMyAmount(String myAmount) {
        this.myAmount = myAmount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getNewversioncode() {
        return newversioncode;
    }

    public void setNewversioncode(String newversioncode) {
        this.newversioncode = newversioncode;
    }

    public ArrayList<String> getEmailList() {
        return emailList;
    }

    public void setEmailList(ArrayList<String> emailList) {
        this.emailList = emailList;
    }

    public boolean isAmountFlag() {
        return amountFlag;
    }

    public void setAmountFlag(boolean amountFlag) {
        this.amountFlag = amountFlag;
    }

    public boolean isMemberFlag() {
        return memberFlag;
    }

    public void setMemberFlag(boolean memberFlag) {
        this.memberFlag = memberFlag;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRequireAmount() {
        return requireAmount;
    }

    public void setRequireAmount(String requireAmount) {
        this.requireAmount = requireAmount;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
