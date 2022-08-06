package com.raihan.dailyfamily.model;

public class Message {
    private String textid;
    private String text;
    private String martext;

    public Message() {
    }

    public Message(String textid, String text, String martext) {
        this.textid = textid;
        this.text = text;
        this.martext = martext;
    }

    public String getTextid() {
        return textid;
    }

    public void setTextid(String textid) {
        this.textid = textid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMartext() {
        return martext;
    }

    public void setMartext(String martext) {
        this.text = martext;
    }
}
