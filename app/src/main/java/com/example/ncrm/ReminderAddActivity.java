package com.example.ncrm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by shameer on 2018-03-09.
 */

public class ReminderAddActivity extends MainActivity {
    private String mUid;
    private EditText mReminderEditText;
    private EditText mDateEditTExt;
    private EditText mTimeEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_reminder_add, frameLayout);

        mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mReminderEditText = (EditText) findViewById(R.id.reminderItemEditText);

        mDateEditTExt = (EditText) findViewById(R.id.reminderDateEditText);
        SetDate date = new SetDate(this, mDateEditTExt);

        mTimeEditText = (EditText) findViewById(R.id.reminderTimeEditText);
        SetTime time = new SetTime(this, mTimeEditText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        final MenuItem menuItem = menu.add(Menu.NONE, 1000, Menu.NONE, R.string.add_caps);
        menuItem.setShowAsAction(1);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        Reminder reminder = getInputValues();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = Utility.getDatabaseReference(firebaseDatabase, "reminders", mUid);

        boolean addedInDatabase = addInDatabase(databaseReference, reminder);
        if (addedInDatabase) {
            navigateScene();
        } else {
            Toast.makeText(getApplicationContext(), "Please provide reminder, date, and time", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private boolean addInDatabase(DatabaseReference databaseReference, Reminder reminder) {
        if (databaseReference != null & reminder != null) {
            if (validateReminder(reminder)) {
                databaseReference.push().setValue(reminder);
                String dateTimeString = reminder.getReminderDate() + " " + reminder.getReminderTime();
                long epoch = convertDateTimeStringToEpoch(dateTimeString);
                createAlarm(epoch, reminder);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean validateReminder(Reminder reminder) {
        if (reminder.getReminderItem() != null && !reminder.getReminderItem().equals("") &&
                reminder.getReminderDate() != null && !reminder.getReminderDate().equals("") &&
                reminder.getReminderTime() != null && !reminder.getReminderTime().equals("")) {
            return true;
        } else {
            return false;
        }
    }

    private Reminder getInputValues() {
        String reminderItem = Utility.getStringFromEditText(mReminderEditText);
        String date = Utility.getStringFromEditText(mDateEditTExt);
        String time = Utility.getStringFromEditText(mTimeEditText);

        Reminder reminder = new Reminder(reminderItem, date, time);

        return reminder;
    }

    private void navigateScene() {
        Intent intent = new Intent(ReminderAddActivity.this, ReminderListActivity.class);
        startActivity(intent);
    }

    private void createAlarm(long epoch, Reminder reminder) {
        Intent intent = new Intent(ReminderAddActivity.this, AlarmBroadcastReceiver.class);
        intent.putExtra("reminderItem", reminder.getReminderItem());
        intent.putExtra("date", reminder.getReminderDate());
        intent.putExtra("time", reminder.getReminderTime());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), (int) System.currentTimeMillis(), intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, epoch, pendingIntent);
    }

    private long convertDateTimeStringToEpoch(String dateTimeString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EE, dd MMMM yyyy hh:mm aa");
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long epoch = date.getTime();
        return epoch;
    }
}