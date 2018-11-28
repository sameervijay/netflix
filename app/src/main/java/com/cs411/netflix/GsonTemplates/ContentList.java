package com.cs411.netflix.GsonTemplates;

import java.util.ArrayList;

public class ContentList {
    private ArrayList<Content> content;
    private int statusCode;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }


    public ArrayList<Content> getContentList() {
        return content;
    }

    public void setContentList(ArrayList<Content> content) {
        this.content = content;
    }

}
