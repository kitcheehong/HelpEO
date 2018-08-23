package com.kitchee.app.helpeo.bean;

import java.util.Date;

/**
 * Created by kitchee on 2018/8/24.
 *
 */

public class ChatMessage {

    private String name;
    private String message;
    private int type;
    private Date date;

    public ChatMessage() {
    }

    public ChatMessage(String name, String message, int type, Date date) {
        this.name = name;
        this.message = message;
        this.type = type;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
