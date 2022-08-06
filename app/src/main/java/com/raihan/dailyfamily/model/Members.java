package com.raihan.dailyfamily.model;

public class Members {

    String member_name;
    String father_name;
    String mobile;
    String email;
    String nid;
    String address;
    String password;
    String balance;
    String notify;
    String occupation;
    String prolink;
    String nick;
    String version;
    String role;


    public Members() {
    }

    public Members(String member_name, String mobile, String email, String prolink,String nick) {
        this.member_name = member_name;
        this.mobile = mobile;
        this.email = email;
        this.prolink = prolink;
        this.nick = nick;
    }

    public Members(String member_name, String father_name, String mobile, String email, String nid, String address, String password, String balance, String notify, String occupation, String prolink, String nick,String version, String role) {
        this.member_name = member_name;
        this.father_name = father_name;
        this.mobile = mobile;
        this.email = email;
        this.nid = nid;
        this.address = address;
        this.password = password;
        this.balance = balance;
        this.notify = notify;
        this.occupation = occupation;
        this.prolink = prolink;
        this.nick = nick;
        this.version = version;
        this.role = role;
    }


    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getFather_name() {
        return father_name;
    }

    public void setFather_name(String father_name) {
        this.father_name = father_name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getNotify() {
        return notify;
    }

    public void setNotify(String notify) {
        this.notify = notify;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getProlink() {
        return prolink;
    }

    public void setProlink(String prolink) {
        this.prolink = prolink;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}