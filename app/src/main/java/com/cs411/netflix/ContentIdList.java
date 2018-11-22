package com.cs411.netflix;

import java.util.ArrayList;

public class ContentIdList {
    private ArrayList<Top3Content> content;
    private int statusCode;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }


    public ArrayList<Top3Content> getContentList() {
        return content;
    }

    public void setContentList(ArrayList<Top3Content> content) {
        this.content = content;
    }
}
