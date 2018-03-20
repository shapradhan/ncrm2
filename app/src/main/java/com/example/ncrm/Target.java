package com.example.ncrm;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

/**
 * Created by KN on 2018-03-15.
 */

public class Target implements Serializable {
    private String targetItem;
    private String targetDate;
    private String targetTime;

    @Exclude
    private String id;

    public Target() {

    }

    public Target(String targetItem, String targetDate, String targetTime) {
        this.targetItem = targetItem;
        this.targetDate = targetDate;
        this.targetTime = targetTime;
    }

    public String getTargetItem() {
        return targetItem;
    }

    public void setTargetItem(String targetItem) {
        this.targetItem = targetItem;
    }

    public String getTargetTime() {
        return targetTime;
    }

    public void setTargetTime(String targetTime) {
        this.targetTime = targetTime;
    }

    public String getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(String targetDate) {
        this.targetDate = targetDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}


