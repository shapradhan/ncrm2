package com.example.ncrm;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by shameer on 2018-02-12.
 */

public class MeetingAddActivity extends MainActivity {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mContactNameDatabaseReference;
    private DatabaseReference mContactOrganizationDatabaseReference;
    private DatabaseReference mContactDatabaseReference;
    private FirebaseAuth mFirebaseAuth;

    private AutoCompleteTextView meetingParticipantAutoCompleteTextView;

    private final Calendar calendar = Calendar.getInstance();
    private EditText meetingDateEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_meeting_add, frameLayout);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mContactNameDatabaseReference = mFirebaseDatabase.getReference().child("contacts").child("name");
        mContactOrganizationDatabaseReference = mFirebaseDatabase.getReference().child("contacts").child("organization");

        mFirebaseAuth = FirebaseAuth.getInstance();

        final ArrayAdapter<String> names = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        FirebaseUser user = mFirebaseAuth.getCurrentUser();

        mContactDatabaseReference = mFirebaseDatabase.getReference().child("contacts").child(user.getUid());

        mContactDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.child("name").getValue(String.class);
                    String organization = ds.child("organization").getValue(String.class);
                    names.add(name + " - " + organization);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        meetingParticipantAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.addParticipantAutoCompleteTextView);
        meetingParticipantAutoCompleteTextView.setAdapter(names);

        EditText meetingDateEditText = (EditText) findViewById(R.id.meetingDateEditText);
        SetDate meetingDate = new SetDate(this, meetingDateEditText);

        EditText meetingTimeEditText = (EditText) findViewById(R.id.meetingTimeEditText);
        SetTime meetingTime = new SetTime(this, meetingTimeEditText);
    }
}
