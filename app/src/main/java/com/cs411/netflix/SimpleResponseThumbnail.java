package com.cs411.netflix;

public class SimpleResponseThumbnail {
    private TNItem response;
    private int statusCode;

    public TNItem getResponse() {
        return response;
    }

    public void setResponse(TNItem response) {
        this.response = response;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
