package com.example.ncrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by shameer on 2018-03-09.
 */

public class ReminderAddActivity extends MainActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_reminder_add, frameLayout);

        EditText reminderItemEditText = (EditText) findViewById(R.id.reminderItemEditText);

        EditText reminderDateEditText = (EditText) findViewById(R.id.reminderDateEditText);
        SetDate reminderDate = new SetDate(this, reminderDateEditText);

        EditText reminderTimeEditText = (EditText) findViewById(R.id.reminderTimeEditText);
        SetTime reminderTime = new SetTime(this, reminderTimeEditText);

        Button addReminderBtn = (Button) findViewById(R.id.addReminderBtn);
        addReminderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText reminderItemEditText = (EditText) findViewById(R.id.reminderItemEditText);
                EditText reminderDateEditText = (EditText) findViewById(R.id.reminderDateEditText);
                EditText reminderTimeEditText = (EditText) findViewById(R.id.reminderTimeEditText);

                String reminderItem = Utility.getStringFromEditText(reminderItemEditText);
                String reminderDate = Utility.getStringFromEditText(reminderDateEditText);
                String reminderTime = Utility.getStringFromEditText(reminderTimeEditText);

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference remindersDatabaseReference = firebaseDatabase.getReference().child("reminders").child(uid);

                Reminder reminder = new Reminder(reminderItem, reminderDate, reminderTime);

                remindersDatabaseReference.push().setValue(reminder);

                Utility.cleanUpEditText(reminderItemEditText, reminderDateEditText, reminderTimeEditText);

                Intent intent = new Intent(ReminderAddActivity.this, ReminderListActivity.class);
                startActivity(intent);
            }
        });
    }
}
