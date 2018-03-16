package com.example.ncrm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

/**
 * Created by shameer on 2018-03-09.
 */

public class ReminderListActivity extends MainActivity {
    RecyclerView mReminderListRecyclerView;
    ReminderAdapter mReminderAdapter;
    ArrayList<Reminder> mReminderList;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRemindersDatabaseReference;
    ChildEventListener mChildEventListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_reminder_list, frameLayout);

        mReminderListRecyclerView = (RecyclerView) findViewById(R.id.reminderListRecyclerView);
        mReminderListRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mReminderListRecyclerView.setLayoutManager(layoutManager);
        mReminderListRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mReminderListRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        mReminderList = new ArrayList<>();
        mReminderAdapter = new ReminderAdapter(mReminderList, this);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        getDataFromFirebase();

        FloatingActionButton createReminderFAB = (FloatingActionButton) findViewById(R.id.addReminderFAB);
        createReminderFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReminderListActivity.this, ReminderAddActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getDataFromFirebase() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mRemindersDatabaseReference = mFirebaseDatabase.getReference().child("reminders").child(uid);
        attachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Reminder reminder = dataSnapshot.getValue(Reminder.class);
                    String id = dataSnapshot.getKey();
                    reminder.setId(id);
                    mReminderList.add(reminder);
                    sortArray(mReminderList);
                    mReminderListRecyclerView.setAdapter(mReminderAdapter);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mRemindersDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }

    private void sortArray(ArrayList<Reminder> arrayList) {
        Collections.sort(arrayList, new Comparator<Reminder>() {
            @Override
            public int compare(Reminder reminder1, Reminder reminder2) {
                Date date1 = convertStringToDate(reminder1.getReminderDate());
                Date date2 = convertStringToDate(reminder2.getReminderDate());

                if (date1.compareTo(date2) == 0) {
                    Date time1 = convertStringToTime(reminder1.getReminderTime());
                    Date time2 = convertStringToTime(reminder2.getReminderTime());
                    return (time1.compareTo(time2));
                }
                return date1.compareTo(date2);
            }
        });
    }

    private Date convertStringToDate(String dateString) {
        DateFormat format = new SimpleDateFormat("EE, dd MMMM yyyy", Locale.ENGLISH);
        try {
            Date date = format.parse(dateString);
            return date;
        } catch (ParseException e) {
            Toast.makeText(getApplicationContext(), "Could not convert string to date", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    private Date convertStringToTime(String timeString) {
        DateFormat format = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
        try {
            Date time = format.parse(timeString);
            return time;
        } catch (ParseException e) {
            Toast.makeText(getApplicationContext(), "Could not convert string to date", Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}
