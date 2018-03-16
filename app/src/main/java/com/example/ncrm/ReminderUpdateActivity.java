package com.example.ncrm;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by shameer on 2018-03-16.
 */

public class ReminderUpdateActivity extends MainActivity {
    private Reminder selectedReminder;

    private EditText mItemEditText;
    private EditText mDateEditText;
    private EditText mTimeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_reminder_add, frameLayout);

        Intent intent = getIntent();
        selectedReminder = (Reminder) intent.getSerializableExtra("object");

        setValuesFromDatabase(selectedReminder);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        final MenuItem menuItem = menu.add(Menu.NONE, 1000, Menu.NONE, R.string.update_caps);
        menuItem.setShowAsAction(1);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        updateReminder();
        navigateScene();
        return true;
    }

    private DatabaseReference getDatabaseReference() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String reminderId = selectedReminder.getId();
        DatabaseReference reminderDatabaseReference = firebaseDatabase.getReference().child("reminders").child(uid).child(reminderId);
        return reminderDatabaseReference;
    }

    private void updateReminder() {
        Reminder updatedReminder = getInputValues();
        DatabaseReference databaseReference = getDatabaseReference();
        databaseReference.setValue(updatedReminder);
    }

    private void setValuesFromDatabase(Reminder selectedReminder) {
        mItemEditText = findViewById(R.id.reminderItemEditText);
        mDateEditText = findViewById(R.id.reminderDateEditText);
        mTimeEditText = findViewById(R.id.reminderTimeEditText);

        mItemEditText.setText(selectedReminder.getReminderItem());
        mDateEditText.setText(selectedReminder.getReminderDate());
        mTimeEditText.setText(selectedReminder.getReminderTime());
    }

    private Reminder getInputValues() {
        String item = Utility.getStringFromEditText(mItemEditText);
        String date = Utility.getStringFromEditText(mDateEditText);
        String time = Utility.getStringFromEditText(mTimeEditText);

        Reminder updatedReminder = new Reminder(
                item,
                date,
                time
        );

        return updatedReminder;
    }

    private void navigateScene() {
        Intent intent = new Intent(ReminderUpdateActivity.this, ReminderListActivity.class);
        startActivity(intent);
    }
}