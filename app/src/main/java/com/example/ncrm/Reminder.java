package com.example.ncrm;

import java.io.Serializable;

/**
 * Created by shameer on 2018-03-09.
 */

public class Reminder implements Serializable {
    private String reminderItem;
    private String reminderTime;

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
}


