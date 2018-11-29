package com.cs411.netflix;

import java.util.ArrayList;

public class UserIdList {
    private ArrayList<User> content;
    private int statusCode;

    public ArrayList<User> getContent() {
        return content;
    }

    public void setContent(ArrayList<User> content) {
        this.content = content;
    }


    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }


}
