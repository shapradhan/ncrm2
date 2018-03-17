package com.example.ncrm;

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
            Toast.makeText(getApplicationContext(), "Please provide meeting title", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private boolean addInDatabase(DatabaseReference databaseReference, Reminder reminder) {
        if (databaseReference != null & reminder != null) {
            if (validateReminder(reminder)) {
                databaseReference.push().setValue(reminder);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean validateReminder(Reminder reminder) {
        if (reminder.getReminderItem() != null && !reminder.getReminderItem().equals("")) {
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
}