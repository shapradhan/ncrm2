package com.example.ncrm;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

/**
 * Created by KN on 2018-03-15.
 */

public class Interaction implements Serializable {
    private String interactionItem;
    private String interactionDate;
    private String interactionTime;

    @Exclude
    private String id;

    public Interaction() {

    }

    public Interaction(String interactionItem, String interactionDate, String interactionTime) {
        this.interactionItem = interactionItem;
        this.interactionDate = interactionDate;
        this.interactionTime = interactionTime;
    }

    public String getInteractionItem() {
        return interactionItem;
    }

    public void setInteractionItem(String interactionItem) {
        this.interactionItem = interactionItem;
    }

    public String getInteractionTime() {
        return interactionTime;
    }

    public void setInteractionTime(String interactionTime) {
        this.interactionTime = interactionTime;
    }

    public String getInteractionDate() {
        return interactionDate;
    }

    public void setInteractionDate(String interactionDate) {
        this.interactionDate = interactionDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}


