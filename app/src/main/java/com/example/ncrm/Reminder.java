package com.example.ncrm;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

/**
 * Created by shameer on 2018-03-09.
 */

public class Reminder implements Serializable {
    private String reminderItem;
    private String reminderDate;
    private String reminderTime;

    @Exclude
    private String id;

    public String getReminderItem() {
        return reminderItem;
    }

    public void setReminderItem(String reminderItem) {
        this.reminderItem = reminderItem;
    }

    public String getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(String reminderTime) {
        this.reminderTime = reminderTime;
    }

    public String getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(String reminderDate) {
        this.reminderDate = reminderDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}


