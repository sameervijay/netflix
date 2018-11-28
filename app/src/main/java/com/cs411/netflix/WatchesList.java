package com.cs411.netflix;

import java.util.ArrayList;

public class WatchesList {
    private ArrayList<Watches> watches;
    private int statusCode;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public ArrayList<Watches> getWatches() {
        return watches;
    }

    public void setWatches(ArrayList<Watches> watches) {
        this.watches = watches;
    }
}
